package uk.gov.dwp.jms.manager.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class BrokerManager {
    private final BrokerServiceSetup brokerServiceSetup;

    public BrokerManager(BrokerServiceSetup brokerServiceSetup) {
        this.brokerServiceSetup = brokerServiceSetup;
    }


    public void sendAndDeadLetterMessage(String queue, String message, Supplier<RuntimeException> exceptionSupplier) {
        ActiveMQConnectionFactory connectionFactory = brokerServiceSetup.broker().createConnectionFactory();
        sendMessage(connectionFactory, queue, message);
        handleMessage(connectionFactory, queue, exceptionSupplier);
    }

    private void handleMessage (ActiveMQConnectionFactory connectionFactory, String queue, Supplier<RuntimeException> exceptionSupplier) {
        try {
            Connection mqConnection = connectionFactory.createConnection();
            long size = Optional.ofNullable(brokerServiceSetup.messagesOnQueues().get(queue)).orElse(0L).longValue();
            tryResource(mqConnection, Connection::close, connection -> tryResource(connection.createSession(false, Session.AUTO_ACKNOWLEDGE), Session::close, session -> {
                ActiveMQQueue destination = new ActiveMQQueue(queue);
                MessageConsumer sessionConsumer = session.createConsumer(destination);
                sessionConsumer.setMessageListener(new MessageHandler(exceptionSupplier));

                connection.start();

                while (size == Optional.ofNullable(brokerServiceSetup.messagesOnQueues().get(queue)).orElse(0L).longValue()) {
                    Thread.sleep(500);
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(ActiveMQConnectionFactory connectionFactory, String queue, String message) {
        try {
            Connection mqConnection = connectionFactory.createConnection();
            tryResource(mqConnection, Connection::close, connection -> {
                connection.start();
                tryResource(connection.createSession(false, Session.AUTO_ACKNOWLEDGE), Session::close, session -> {
                    ActiveMQQueue destination = new ActiveMQQueue(queue);
                    tryResource(session.createProducer(destination), MessageProducer::close, producer -> {
                        System.out.println("Send message to queue "+queue);
                        TextMessage textMessage = session.createTextMessage(message);
                        textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
                        producer.send(textMessage);
                    });
                });
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void tryResource(T object, ConsumerWithException<T> close, ConsumerWithException<T> handle) {
        try {
            handle.consume(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                close.consume(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Long> countMessages() {
        return brokerServiceSetup.messagesOnQueues();
    }

    interface ConsumerWithException<T> {
        void consume(T item) throws Exception;
    }

    static class MessageHandler implements MessageListener {
        private final Supplier<RuntimeException> exceptionSupplier;

        public MessageHandler(Supplier<RuntimeException> exceptionSupplier) {
            this.exceptionSupplier = exceptionSupplier;
        }

        @Override
        public void onMessage(Message message) {
            throw exceptionSupplier.get();
        }
    }
}

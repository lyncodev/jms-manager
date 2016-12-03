package uk.gov.dwp.jms.manager.core.configuration;

import com.novemberain.quartz.mongodb.MongoDBJobStore;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import uk.gov.dwp.jms.manager.core.service.schedule.RunActionJob;
import uk.gov.dwp.jms.manager.core.service.schedule.SendMessageJob;

import java.util.Properties;

@Configuration
public class QuartzConfiguration {
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${jms.manager.db.dbName}")
    private String dbName;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setApplicationContextSchedulerContextKey("applicationContext");
        scheduler.setWaitForJobsToCompleteOnShutdown(true);

        Properties quartzProperties = new Properties();
        quartzProperties.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, MongoDBJobStore.class.getName());
        quartzProperties.put(StdSchedulerFactory.PROP_JOB_STORE_PREFIX + ".mongoUri", mongoUri);
        quartzProperties.put(StdSchedulerFactory.PROP_JOB_STORE_PREFIX + ".dbName", dbName);
        quartzProperties.put(StdSchedulerFactory.PROP_JOB_STORE_PREFIX + ".collectionPrefix", "quartz");
        quartzProperties.put(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, true);

        scheduler.setQuartzProperties(quartzProperties);
        scheduler.setJobFactory(new SimpleJobFactory() {
            @Override
            public Job newJob(TriggerFiredBundle bundle, Scheduler Scheduler) throws SchedulerException {
                Job newJob = super.newJob(bundle, Scheduler);
                beanFactory.autowireBean(newJob);
                return newJob;
            }
        });
        scheduler.setJobDetails(
                JobBuilder.newJob(RunActionJob.class).withIdentity(RunActionJob.JOB_NAME, RunActionJob.JOB_GROUP).storeDurably().build(),
                JobBuilder.newJob(SendMessageJob.class).withIdentity(SendMessageJob.JOB_NAME, SendMessageJob.JOB_GROUP).storeDurably().build()
        );
        return scheduler;
    }
}

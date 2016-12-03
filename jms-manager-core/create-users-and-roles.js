use jms-manager

db.dropUser("jmsManagerUser")
db.dropRole("jmsManagerReadWrite")
db.dropRole("jmsManagerReadOnly")


db.createRole(
  {
    role: "jmsManagerReadOnly",
    roles: [],
    privileges: [
      { resource: { db: "jms-manager", collection: "failedMessage" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "failedMessageLabel" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "destinationStatistics" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "quartz_calendars" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "quartz_jobs" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "quartz_locks" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "quartz_schedulers" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "quartz_triggers" }, actions: [ "find" ] }
    , { resource: { db: "jms-manager", collection: "system.namespaces" }, actions: [ "find" ] }
    ]
  }
)


db.createRole(
  {
    role: "jmsManagerReadWrite",
    roles: [
      { role: "jmsManagerReadOnly", db: "jms-manager" }
    ],
    privileges: [
      { resource: { db: "jms-manager", collection: "failedMessage" }, actions: [ "update", "insert", "remove" ] }
      , { resource: { db: "jms-manager", collection: "failedMessageLabel" }, actions: [ "update", "insert", "remove" ] }
      , { resource: { db: "jms-manager", collection: "destinationStatistics" }, actions: [ "update", "insert", "remove" ] }
      , { resource: { db: "jms-manager", collection: "quartz_calendars" }, actions: [ "update", "insert", "remove", "createIndex" ] }
      , { resource: { db: "jms-manager", collection: "quartz_jobs" }, actions: [ "update", "insert", "remove", "createIndex" ] }
      , { resource: { db: "jms-manager", collection: "quartz_locks" }, actions: [ "update", "insert", "remove", "createIndex" ] }
      , { resource: { db: "jms-manager", collection: "quartz_schedulers" }, actions: [ "update", "insert", "remove", "createIndex" ] }
      , { resource: { db: "jms-manager", collection: "quartz_triggers" }, actions: [ "update", "insert", "remove", "createIndex" ] }
    ]
  }
)

db.createUser(
  {
    user: "jmsManagerUser",
    pwd:  "Passw0rd",
    roles: [
      { role: "jmsManagerReadWrite", db: "jms-manager" }
    ]
  }
)
# scheduler

This service is used to schedule jobs for our customers. 
JobController exposes 2 APIs, 
1. Schedule Trigger: Schedules a job using jobrunr where scheduleId is sent by the user and then save the mapping of scheduleId to jobId in documentDb
2. Cancel Trigger: Receives scheduleId, get the jobId from documentDb based on scheduleId and delete the schedule from the jobrunr using the jobId

## Usage

This starter app is a simple Java SpringMVC service

[//]: # (local development) 

## Getting Started with Local Development

### Prerequisites
* [Java 17 OR later](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

* [Maven 3.8.5 OR later](https://maven.apache.org/download.cgi)

### The issue

We had a documentDb cluster setup with following details:
1. Version: 4.0.0
2. Storage configuration: Amazon DocumentDB Standard
3. Instance type: db.r5.large
4. No. of instances: 3

Phase 1: Multiple jobs scheduled to run were getting concurrently updated:

```
Storage state: ENQUEUED (at 2024-03-09T18:42:44.175209622Z) ← SCHEDULED (at 2024-03-08T18:52:15.605886650Z)
at org.jobrunr.server.concurrent.DefaultConcurrentJobModificationResolver.resolve(DefaultConcurrentJobModificationResolver.java:50)
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:79)
... 14 common frames omitted
Caused by: org.jobrunr.storage.ConcurrentJobModificationException: The following jobs where concurrently updated: 9d9f3f76-c852-451f-95b9-baf5bf0cff3f, d16936a3-98f9-4969-9af7-d0e63d93c777
at org.jobrunr.storage.nosql.mongo.MongoDBStorageProvider.save(MongoDBStorageProvider.java:291)
at org.jobrunr.storage.ThreadSafeStorageProvider.save(ThreadSafeStorageProvider.java:112)
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:75)
... 14 common frames omitted
```

Phase 2 : Multiple warnings that two jobs are getting updated concurrently
```
[2024-03-09T18:42:44,239+0000]-[WARN ]-["backgroundjob-zookeeper-pool-8-thread-1"  cid=, clu=]-[o.j.server.JobZooKeeper]-[69]-JobRunr encountered a problematic exception. Please create a bug report (if possible, provide the code to reproduce this and the stacktrace) - Processing will continue. org.jobrunr.SevereJobRunrException: Could not resolve ConcurrentJobModificationException
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:81)
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:67)
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:59)
at org.jobrunr.server.zookeeper.tasks.ProcessScheduledJobsTask.runTask(ProcessScheduledJobsTask.java:26)
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.run(ZooKeeperTask.java:47)
at org.jobrunr.server.JobZooKeeper.lambda$runMasterTasksIfCurrentServerIsMaster$0(JobZooKeeper.java:80)
at java.base/java.util.Arrays$ArrayList.forEach(Arrays.java:4204)
at org.jobrunr.server.JobZooKeeper.runMasterTasksIfCurrentServerIsMaster(JobZooKeeper.java:80)
at org.jobrunr.server.JobZooKeeper.run(JobZooKeeper.java:56)
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
at java.base/java.lang.Thread.run(Thread.java:833)
Caused by: org.jobrunr.server.concurrent.UnresolvableConcurrentJobModificationException: The following jobs where concurrently updated: 9d9f3f76-c852-451f-95b9-baf5bf0cff3f, d16936a3-98f9-4969-9af7-d0e63d93c777
Concurrent modified jobs:
Job id: 9d9f3f76-c852-451f-95b9-baf5bf0cff3f
Job Name: com.intuit.scheduler.library.services.ScheduleJobRequestHandler.run(ScheduleTriggerRequest())
Job Signature: com.intuit.scheduler.library.services.ScheduleJobRequestHandler.run(com.intuit.scheduler.library.entities.ScheduleTriggerRequest)
Local version: 1; Storage version: 2
```
Phase 3: After several such warnings for multiple jobs, the jobrunr stopped working and all kubernetes pods came down
```
[2024-03-09T19:12:44,317+0000]-[ERROR]-["backgroundjob-zookeeper-pool-8-thread-1"  cid=, clu=]-[o.j.server.JobZooKeeper]-[65]-FATAL - JobRunr encountered too many processing exceptions. Shutting down.
org.jobrunr.SevereJobRunrException: Could not resolve ConcurrentJobModificationException 	
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:81) 	
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:67) 	
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.processJobList(ZooKeeperTask.java:59) 	
at org.jobrunr.server.zookeeper.tasks.ProcessScheduledJobsTask.runTask(ProcessScheduledJobsTask.java:26) 	
at org.jobrunr.server.zookeeper.tasks.ZooKeeperTask.run(ZooKeeperTask.java:47) 	
at org.jobrunr.server.JobZooKeeper.lambda$runMasterTasksIfCurrentServerIsMaster$0(JobZooKeeper.java:80) 	
at java.base/java.util.Arrays$ArrayList.forEach(Arrays.java:4204) 	
at org.jobrunr.server.JobZooKeeper.runMasterTasksIfCurrentServerIsMaster(JobZooKeeper.java:80) 	
at org.jobrunr.server.JobZooKeeper.run(JobZooKeeper.java:56) 	
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539) 	
at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305) 	
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305) 	
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136) 	
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635) 	
at java.base/java.lang.Thread.run(Thread.java:833)
Caused by: org.jobrunr.server.concurrent.UnresolvableConcurrentJobModificationException: The following jobs where concurrently updated: ba00057e-abd9-4f84-955f-afbced9a477e
## Concurrent modified jobs: Job id: ba00057e-abd9-4f84-955f-afbced9a477e
Job Name: com.intuit.scheduler.library.services.ScheduleJobRequestHandler.run(ScheduleTriggerRequest()) 	
Job Signature: com.intuit.scheduler.library.services.ScheduleJobRequestHandler.run(com.intuit.scheduler.library.entities.ScheduleTriggerRequest) 	
Local version: 1;
Storage version: 1
```
Phase 4: On restarting the pods are not coming up
```
2:07:34.187 PM

[2024-03-11T08:37:34,187+0000]-[ERROR]-["backgroundjob-zookeeper-pool-8-thread-1"  cid=, clu=]-[o.j.s.ServerZooKeeper]-[58]-An unrecoverable error occurred. Shutting server down... java.lang.NullPointerException: Cannot invoke "java.util.UUID.equals(Object)" because "this.masterId" is null
at org.jobrunr.server.ServerZooKeeper.determineIfCurrentBackgroundJobServerIsMaster(ServerZooKeeper.java:123)
at org.jobrunr.server.ServerZooKeeper.announceBackgroundJobServer(ServerZooKeeper.java:77)
at org.jobrunr.server.ServerZooKeeper.run(ServerZooKeeper.java:53)
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
at java.base/java.lang.Thread.run(Thread.java:833)
```
Phase 5: On reducing the number of documentDb instances/nodes to 1, the pods came up and the service started working as expected

Phase 6: Increased the number of documentDb instances/nodes to 2, the pods started coming down again with the same error as before
```
[ERROR]-["backgroundjob-zookeeper-pool-8-thread-1"  cid=, clu=]-[o.j.s.ServerZooKeeper]-[58]-An unrecoverable error occurred. Shutting server down... java.lang.NullPointerException: Cannot invoke "java.util.UUID.equals(Object)" because "this.masterId" is null
at org.jobrunr.server.ServerZooKeeper.determineIfCurrentBackgroundJobServerIsMaster(ServerZooKeeper.java:123)
at org.jobrunr.server.ServerZooKeeper.announceBackgroundJobServer(ServerZooKeeper.java:77)
at org.jobrunr.server.ServerZooKeeper.run(ServerZooKeeper.java:53)
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
at java.base/java.lang.Thread.run(Thread.java:833)
```


#### How to replicate the issue 

1. Create a DocumentDb cluster with more than 1 instance and add its details in the properties file
2. Run the service with these details in a kubernetes cluster



#### How to use Swagger Documentation
From your web browser navigate here


#### Note about included certificate

The packaged certificate is OK for local development. However, we encourage you to use Intuit CA-signed or generate your own self-signed certificate for **TLS** termination.


## Technologies Used
- Spring Boot MVC

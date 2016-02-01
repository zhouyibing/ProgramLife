package framework.quartz.core.impl;

import framework.quartz.core.IExecutor;
import framework.quartz.core.IJobManager;
import framework.quartz.exception.JobException;
import framework.quartz.job.IJob;
import framework.quartz.model.JobMetaData;
import framework.quartz.trigger.ITrigger;
import org.quartz.*;

import java.util.Map;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class JobExecutor implements IExecutor{
    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;
    private static JobExecutor jobExecutor;

    private JobExecutor(){};
    public Scheduler getScheduler() throws SchedulerException {
        if(null==scheduler)
            scheduler = schedulerFactory.getScheduler();
        return scheduler;
    };

    public SchedulerFactory getSchedulerFactory() {
        return schedulerFactory;
    }

    public void setSchedulerFactory(SchedulerFactory schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    public static JobExecutor getInstance(){
        if(null==jobExecutor) {
            synchronized (JobExecutor.class) {
               if(null==jobExecutor)
                   jobExecutor = new JobExecutor();
            }
        }
        return jobExecutor;
    }

    public void executeAll() throws SchedulerException {
        scheduler = getScheduler();
        IJobManager jobManager = JobManager.getInstance();
        Map<String,ITrigger> triggers = jobManager.getTriggers();
        for(Map.Entry<String,ITrigger> entry:triggers.entrySet()){
            ITrigger trigger = entry.getValue();
            if(null==trigger) continue;
            IJob job = trigger.getTriggerMetaData().getJob();
            scheduler.scheduleJob(job.getJobDetail(),trigger.getTrigger());
        }
    }

    public void start() throws SchedulerException {
        scheduler = getScheduler();
        scheduler.start();
    }

    public void stop() throws SchedulerException {
        scheduler = getScheduler();
        scheduler.shutdown();
    }

    public void executeByTrigger(String triggerId) throws JobException,SchedulerException {
        scheduler = getScheduler();
        IJobManager jobManager = JobManager.getInstance();
        ITrigger trigger = jobManager.getTriggers().get(triggerId);
        if(null==triggerId||null==trigger)
            throw new JobException("can not find the trigger!");
        scheduler.scheduleJob(trigger.getTriggerMetaData().getJob().getJobDetail(),trigger.getTrigger());
    }

    public void executeByJob(String jobId) throws SchedulerException {
        scheduler = getScheduler();
        IJobManager jobManager = JobManager.getInstance();
        ITrigger trigger = jobManager.getTriggerByJob(jobId);
        if(null==trigger)
            throw new JobException("can not find the trigger!");
        scheduler.scheduleJob(trigger.getTriggerMetaData().getJob().getJobDetail(),trigger.getTrigger());
    }

    public void reExecute(String triggerId) throws SchedulerException {
        scheduler = getScheduler();
        JobManager jobManager = JobManager.getInstance();
        ITrigger trigger = jobManager.getTriggers().get(triggerId);
        if(null==trigger)
            throw new JobException("can not find the trigger!");
        Trigger t = trigger.getTrigger();
        scheduler.rescheduleJob(t.getKey(),t);
    }

    public void changeExecute(String jobId,JobMetaData.JobStatus status) throws SchedulerException {
        scheduler = getScheduler();
        JobManager jobManager = JobManager.getInstance();
        IJob job = jobManager.getJobs().get(jobId);
        if(null==job)
            throw new JobException("can not find the job!");
        JobDetail jd = job.getJobDetail();
        JobKey jobKey = jd.getKey();
        if(null!= jobKey){
            if(status == JobMetaData.JobStatus.PAUSE)
                scheduler.pauseJob(jobKey);
            else if(status== JobMetaData.JobStatus.DELETE)
                scheduler.deleteJob(jobKey);
            else if(status == JobMetaData.JobStatus.ON) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                if(null==jobDetail)
                   executeByJob(jobId);
                else
                    scheduler.resumeJob(jobKey);
            }
        }
    }
}

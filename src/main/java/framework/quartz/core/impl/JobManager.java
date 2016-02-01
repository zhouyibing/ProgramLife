package framework.quartz.core.impl;

import framework.quartz.core.IJobManager;
import framework.quartz.exception.JobException;
import framework.quartz.job.IJob;
import framework.quartz.job.impl.QuartzJob;
import framework.quartz.model.JobMetaData;
import framework.quartz.model.TriggerMetaData;
import framework.quartz.trigger.ITrigger;
import framework.quartz.util.UUID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class JobManager implements IJobManager{
    private static JobManager jobManager;
    private Map<String,IJob> jobMap = new ConcurrentHashMap<>();
    private Map<String,ITrigger> triggerMap = new ConcurrentHashMap<>();

    private JobManager(){}
    public static JobManager getInstance(){
        if(jobManager == null){
            synchronized (JobManager.class){
                if(jobManager == null){
                    jobManager = new JobManager();
                }
            }
        }
        return jobManager;
    }

    @Override
   public Map<String,IJob> getJobs(){
       return jobMap;
   }

    @Override
    public IJob getJobByTrigger(String triggerId){
        for(Map.Entry<String,ITrigger> entry:triggerMap.entrySet()){
            ITrigger trigger = entry.getValue();
            TriggerMetaData triggerMetaData = trigger.getTriggerMetaData();
            if(triggerMetaData.getTriggerId().equals(triggerId)){
              return triggerMetaData.getJob();
            }
        }
        return null;
    }

    @Override
    public ITrigger getTriggerByJob(String jobId) {
        for(Map.Entry<String,ITrigger> entry:triggerMap.entrySet()){
            ITrigger trigger = entry.getValue();
            TriggerMetaData triggerMetaData = trigger.getTriggerMetaData();
            JobMetaData jobMetaData = triggerMetaData.getJob().getJobMetaData();
            if(jobMetaData.getJobId().equals(jobId))
                return trigger;
        }
        return null;
    }

    @Override
    public Map<String,ITrigger> getTriggers(){
        return triggerMap;
    }

    @Override
    public IJob createJob(JobMetaData jobMetaData){
        Class jobClass = jobMetaData.getJobClass();
        if(null==jobClass)
            throw new JobException("the jobClass must be not null,when you create a job.");
        if(!hasImplInterface(jobClass,IJob.class))
            throw new JobException("the jobClass not implements IJob interface.");
        for(Map.Entry<String,IJob> entry:jobMap.entrySet()){
            IJob job = entry.getValue();
            JobMetaData jobData = job.getJobMetaData();
            if(jobData.getJobClass()==jobMetaData.getJobClass())
                throw new JobException("the job has created before.");
        }
        try {
            jobMetaData.setJobId(UUID.uuid());
            IJob job = (IJob)jobClass.newInstance();
            job.createJobDetail(jobMetaData);
            jobMap.put(jobMetaData.getJobId(),job);
            return job;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ITrigger createTrigger(TriggerMetaData triggerMetaData){
         Class triggerClass = triggerMetaData.getTriggerClass();
        if(null==triggerMetaData)
            throw new JobException("the triggerClass must be not null,when you create a job.");
        if(!hasImplInterface(triggerClass,ITrigger.class))
            throw new JobException("the triggerClass not implements ITrigger interface.");
        try {
            triggerMetaData.setTriggerId(UUID.uuid());
            ITrigger trigger = (ITrigger)triggerClass.newInstance();
            trigger.createTrigger(triggerMetaData);
            triggerMap.put(triggerMetaData.getTriggerId(), trigger);
            return trigger;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasImplInterface(Class a,Class inter){
        Class[] interfaces  = a.getInterfaces();
        for(Class f:interfaces){
            if(f==inter)
                return  true;
        }
        return false;
    }
    public static void main(String[] args){
        Class clazz = QuartzJob.class;
        Class[] interfaces  = clazz.getInterfaces();
        for(Class f:interfaces){
            if(f==IJob.class)
                System.out.println("aa");
        }
    }
}

package framework.quartz.job.impl;

import framework.quartz.core.IJobManager;
import framework.quartz.exception.JobException;
import framework.quartz.job.IJob;
import framework.quartz.model.JobMetaData;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class QuartzJob implements IJob{

    private JobMetaData jobMetaData;
    private JobDetail jobDetail;

    public QuartzJob() {
    }

    public QuartzJob(JobMetaData jobMetaData) {
        this.jobMetaData = jobMetaData;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobMetaData jobMetaData = (JobMetaData) jobDetail.getJobDataMap().get("jobMetaData");
        JobMetaData.JobStatus jobStatus = jobMetaData.getJobstatus();
        if(jobStatus!= JobMetaData.JobStatus.ON){throw new IllegalStateException("the job is not in a active status!");};
        doAction();
        if(null==jobDetail)
            throw new JobException("you must invoke createJobDetail before execute the job.");
        System.out.println(Thread.currentThread().getName()+" has execute the job at "+new Date());
        Integer  count  = jobMetaData.getExecuteCount();
        System.out.println("the job has execute "+count+" times.");
        if(count==null||count==0)
            count = 1;
        else
            count++;
        jobMetaData.setExecuteCount(count);
    }

    public void doAction(){
        System.out.println("do your business");
    }
    public void changeJobStatus(JobMetaData.JobStatus jobStatus) {
         jobMetaData.setJobstatus(jobStatus);
    }

    @Override
    public JobDetail createJobDetail(JobMetaData jobMetaData) {
        JobDetail jobDetail = JobBuilder.newJob(this.getClass()).withIdentity(jobMetaData.getJobName(),jobMetaData.getJobGroup()).build();
        jobDetail.getJobDataMap().put("jobMetaData",jobMetaData);
        this.jobMetaData = jobMetaData;
        this.jobDetail = jobDetail;
        return jobDetail;
    }

    @Override
    public JobDetail getJobDetail(){
        return jobDetail;
    }

    @Override
    public JobMetaData getJobMetaData() {
        return jobMetaData;
    }
}

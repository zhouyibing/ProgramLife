package framework.quartz.job.impl;

import framework.quartz.core.IJobManager;
import framework.quartz.exception.JobException;
import framework.quartz.job.IJob;
import framework.quartz.model.JobMetaData;
import org.quartz.JobExecutionContext;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class QuartzJob implements IJob{

    private IJobManager jobManager;
    private JobMetaData jobMetaData;

    public QuartzJob(IJobManager jobManager, JobMetaData jobMetaData) {
        this.jobManager = jobManager;
        this.jobMetaData = jobMetaData;
    }

    public QuartzJob(JobMetaData jobMetaData) {
        this.jobMetaData = jobMetaData;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobException {
        JobMetaData.JobStatus jobStatus = jobMetaData.getJobstatus();
    }

    public void setManager(IJobManager manager) {
           this.jobManager = manager;
    }

    public void changeJobStatus(JobMetaData.JobStatus jobStatus) {
         jobMetaData.setJobstatus(jobStatus);
    }
}

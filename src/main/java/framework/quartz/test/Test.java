package framework.quartz.test;

import framework.quartz.core.impl.JobExecutor;
import framework.quartz.core.impl.JobManager;
import framework.quartz.job.IJob;
import framework.quartz.job.impl.QuartzJob;
import framework.quartz.model.JobMetaData;
import framework.quartz.model.TriggerMetaData;
import framework.quartz.trigger.ITrigger;
import framework.quartz.trigger.impl.QuartzTrigger;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Zhou Yibing on 2016/2/1.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        JobManager jobManager = JobManager.getInstance();
        JobMetaData jobMetaData = new JobMetaData();
        jobMetaData.setJobClass(QuartzJob.class);
        jobMetaData.setDesc("≤‚ ‘job");
        jobMetaData.setJobName("QuartzJob");
        jobMetaData.setJobGroup("test");
        jobMetaData.setJobstatus(JobMetaData.JobStatus.ON);
        IJob job =  jobManager.createJob(jobMetaData);

        TriggerMetaData triggerMetaData = new TriggerMetaData();
        triggerMetaData.setJob(job);
        triggerMetaData.setCronExpression("0/5 * * * * ? *");
        triggerMetaData.setTriggerClass(QuartzTrigger.class);
        ITrigger trigger = jobManager.createTrigger(triggerMetaData);

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        JobExecutor jobExecutor = JobExecutor.getInstance();
        jobExecutor.setSchedulerFactory(schedulerFactory);
        try {
            jobExecutor.executeAll();
            jobExecutor.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

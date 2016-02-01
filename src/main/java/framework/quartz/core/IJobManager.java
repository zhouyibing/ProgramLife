package framework.quartz.core;

import framework.quartz.job.IJob;
import framework.quartz.model.JobMetaData;
import framework.quartz.model.TriggerMetaData;
import framework.quartz.trigger.ITrigger;

import java.util.Map;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public interface IJobManager {

    Map<String,IJob> getJobs();

    IJob getJobByTrigger(String triggerId);

    ITrigger getTriggerByJob(String jobId);

    Map<String,ITrigger> getTriggers();

    IJob createJob(JobMetaData jobMetaData);

    ITrigger createTrigger(TriggerMetaData triggerMetaData);
}

package framework.quartz.job;

import framework.quartz.model.JobMetaData;
import org.quartz.Job;
import org.quartz.JobDetail;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public interface IJob extends Job{

    void changeJobStatus(JobMetaData.JobStatus jobStatus);
    JobDetail createJobDetail(JobMetaData jobMetaData);

    JobDetail getJobDetail();

    JobMetaData getJobMetaData();
}

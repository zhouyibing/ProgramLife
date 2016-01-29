package framework.quartz.job;

import framework.quartz.core.IJobManager;
import framework.quartz.model.JobMetaData;
import org.quartz.Job;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public interface IJob extends Job{

    void setManager(IJobManager manager);
    void changeJobStatus(JobMetaData.JobStatus jobStatus);
}

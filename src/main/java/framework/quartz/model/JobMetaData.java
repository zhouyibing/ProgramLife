package framework.quartz.model;

import java.io.Serializable;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class JobMetaData implements Serializable{
    private String jobName;
    private boolean concurrent;
    private String jobGroup;
    private String jobId;
    private Class jobClass;
    private String desc;
    private Integer executeCount;
    private JobStatus jobstatus = JobStatus.OFF;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Class getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class jobClass) {
        this.jobClass = jobClass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public JobStatus getJobstatus() {
        return jobstatus;
    }

    public void setJobstatus(JobStatus jobstatus) {
        this.jobstatus = jobstatus;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public enum JobStatus{
        OFF(0,"½ûÓÃ"),ON(1,"ÆôÓÃ"),PAUSE(2,"ÔÝÍ£"),DELETE(3,"É¾³ý");
        private int statusCode;
        private String desc;

        JobStatus(int statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }
    }
}

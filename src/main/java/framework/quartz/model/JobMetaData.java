package framework.quartz.model;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class JobMetaData {
    private String jobName;
    private boolean concurrent;
    private String jobGroup;
    private String jobId;
    private String cronExpression;
    private String desc;
    private JobStatus jobstatus;

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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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

package framework.quartz.model;

import framework.quartz.job.IJob;

/**
 * Created by Zhou Yibing on 2016/2/1.
 */
public class TriggerMetaData {
    private String triggerId;
    private String cronExpression;
    private String expressionDesc;
    private Class triggerClass;
    private IJob job;

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getExpressionDesc() {
        return expressionDesc;
    }

    public void setExpressionDesc(String expressionDesc) {
        this.expressionDesc = expressionDesc;
    }

    public IJob getJob() {
        return job;
    }

    public void setJob(IJob job) {
        this.job = job;
    }

    public Class getTriggerClass() {
        return triggerClass;
    }

    public void setTriggerClass(Class triggerClass) {
        this.triggerClass = triggerClass;
    }
}

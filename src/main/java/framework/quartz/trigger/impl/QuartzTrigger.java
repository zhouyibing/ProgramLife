package framework.quartz.trigger.impl;

import framework.quartz.model.JobMetaData;
import framework.quartz.model.TriggerMetaData;
import framework.quartz.trigger.ITrigger;
import org.quartz.*;

import java.text.ParseException;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public class QuartzTrigger implements ITrigger{
    private CronTrigger trigger;
    private TriggerMetaData triggerMetaData;

    public QuartzTrigger() {
    }

    @Override
    public Trigger createTrigger(TriggerMetaData triggerMetaData) {
        try {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(triggerMetaData.getCronExpression());
            JobMetaData jobMetaData = triggerMetaData.getJob().getJobMetaData();
            CronTrigger trigger = TriggerBuilder.newTrigger().forJob(triggerMetaData.getJob().getJobDetail()).withIdentity(jobMetaData.getJobName(), jobMetaData.getJobGroup()).withSchedule(scheduleBuilder).build();
            this.trigger = trigger;
            this.triggerMetaData = triggerMetaData;
            return trigger;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeCronExpression(String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey("","");
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            this.trigger = this.trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            triggerMetaData.setCronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TriggerMetaData getTriggerMetaData() {
        return triggerMetaData;
    }

    @Override
    public Trigger getTrigger(){
        return trigger;
    }
}

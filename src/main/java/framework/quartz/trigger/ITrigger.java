package framework.quartz.trigger;

import framework.quartz.model.TriggerMetaData;
import org.quartz.Trigger;

/**
 * Created by Zhou Yibing on 2016/1/29.
 */
public interface ITrigger {

    Trigger createTrigger(TriggerMetaData triggerMetaData);
    void changeCronExpression(String cronExpression);
    TriggerMetaData getTriggerMetaData();

    Trigger getTrigger();
}

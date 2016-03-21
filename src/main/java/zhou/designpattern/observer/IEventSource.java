package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 事件源
 */
public interface IEventSource {
    void setEventBus(IEventBus bus);
    void attachListener(IEventListener listener);

    void removeListener(IEventListener listener);

    void action(EventType eventType);
}

package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 事件监听器(观察者)
 */
public interface IEventListener {
    void actionPerformed(Event event);
    void setEventType(EventType eventType);
    EventType getEventType();
}

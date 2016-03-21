package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 事件
 */
public class Event {
    public IEventSource source;
    public EventType eventType;

    public IEventSource getSource() {
        return source;
    }

    public void setSource(IEventSource source) {
        this.source = source;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}

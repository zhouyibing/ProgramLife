package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class Actor implements IEventSource{
    private IEventBus eventBus;

    public Actor() {
        eventBus = new DefaultEventBus();
    }

    @Override
    public void setEventBus(IEventBus bus) {
        this.eventBus = bus;
    }

    @Override
    public void attachListener(IEventListener listener){
        eventBus.attachListener(listener);
    }

    @Override
    public void removeListener(IEventListener listener){
        eventBus.detachListener(listener);
    }

    @Override
    public void action(EventType eventType){
        Event event = new Event();
        event.setEventType(eventType);
        event.setSource(this);
        eventBus.fire(event);
    }
}

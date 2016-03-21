package zhou.designpattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class DefaultEventBus implements IEventBus{
    List<ListenerCollection> eventCollection = new ArrayList<>();

    @Override
    public void attachListener(IEventListener listener) {
        EventType eventType = listener.getEventType();
        if(eventType==null) throw new IllegalArgumentException("实现事件监听器必须实现getEventType方法");
        boolean find = false;
        for(ListenerCollection listenerCollection:eventCollection){
            if(listenerCollection.getEventType()==eventType){
                listenerCollection.getEventListenerList().add(listener);
                find = true;
            }
        }
        if(!find) {
            ListenerCollection collection = new ListenerCollection();
            collection.setEventType(eventType);
            collection.getEventListenerList().add(listener);
            eventCollection.add(collection);
        }
    }

    @Override
    public void detachListener(IEventListener listener) {
        EventType eventType = listener.getEventType();
        if(eventType==null) throw new IllegalArgumentException("实现事件监听器必须实现getEventType方法");
        for(ListenerCollection listenerCollection:eventCollection){
            if(listenerCollection.getEventType()==eventType){
                listenerCollection.getEventListenerList().remove(listener);
            }
        }
    }

    @Override
    public void fire(Event event) {
        EventType eventType = event.getEventType();
        if(eventType==null) throw new IllegalArgumentException("实现事件监听器必须实现getEventType方法");
        for(ListenerCollection listenerCollection:eventCollection){
            if(null==listenerCollection.getEventType()||listenerCollection.getEventType()==eventType){
                List<IEventListener> listeners = listenerCollection.getEventListenerList();
                for(IEventListener listener :listeners)
                    listener.actionPerformed(event);
            }
        }
    }

    class ListenerCollection{
        EventType eventType;
        List<IEventListener> eventListenerList = new ArrayList<>();

        public EventType getEventType() {
            return eventType;
        }

        public void setEventType(EventType eventType) {
            this.eventType = eventType;
        }

        public List<IEventListener> getEventListenerList() {
            return eventListenerList;
        }

        public void setEventListenerList(List<IEventListener> eventListenerList) {
            this.eventListenerList = eventListenerList;
        }
    }
}

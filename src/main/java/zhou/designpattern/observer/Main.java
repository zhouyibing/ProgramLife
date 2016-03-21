package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class Main {

    public static void main(String[] args){
        Actor actor = new Actor();
        IEventListener draglistener = new IEventListener() {
            @Override
            public void actionPerformed(Event event) {
                System.out.println(event.getSource()+" 触发了一个 "+event.getEventType().getEventDesc()+" 事件");
            }

            @Override
            public void setEventType(EventType eventType) {
            }

            @Override
            public EventType getEventType() {
                return EventType.DRAG;
            }
        };
        actor.attachListener(draglistener);

        IEventListener clicklistener = new IEventListener() {
            @Override
            public void actionPerformed(Event event) {
                System.out.println(event.getSource()+" 触发了一个 "+event.getEventType().getEventDesc()+" 事件");
            }

            @Override
            public void setEventType(EventType eventType) {
            }

            @Override
            public EventType getEventType() {
                return EventType.CLICK;
            }
        };

        actor.attachListener(clicklistener);

        actor.action(EventType.CLICK);
        actor.action(EventType.DRAG);
        actor.removeListener(clicklistener);
        actor.action(EventType.CLICK);
    }
}

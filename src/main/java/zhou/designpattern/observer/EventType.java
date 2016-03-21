package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 事件类型
 */
public enum EventType {
    CLICK("点击"),MOVE("移动"),DRAG("拖拽"),EDIT("编辑");
    public String eventDesc;

    EventType(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventDesc(){
        return eventDesc;
    }
}

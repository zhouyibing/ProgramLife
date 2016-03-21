package zhou.designpattern.observer;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 事件总线,主题对象
 */
public interface IEventBus{

    void attachListener(IEventListener listener);
    void detachListener(IEventListener listener);
    void fire(Event event);
}

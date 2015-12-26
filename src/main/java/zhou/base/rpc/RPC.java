package zhou.base.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import zhou.base.rpc.protocol.Invocation;
import zhou.base.rpc.support.Client;
import zhou.base.rpc.support.Listener;
import zhou.base.rpc.support.Server;

public class RPC {

    /**
     * 使用RPC.getProxy生成接口Echo的代理实现类。然后就可以像使用本地的程序一样来调用Echo中的echo方法。
     * 使用RPC的好处是简化了远程服务访问。提高了开发效率。在分发代码时，
     * 只需要将接口分发给客户端使用，在客户端看来只有接口，没有具体类实现。这样保证了代码的可扩展性和安全性。
     * @param clazz
     * @param host
     * @param port
     * @return
     */
    public static <T> T getProxy(final Class<T> clazz,String host,int port){
        final Client client = new Client(host,port);
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invo = new Invocation();
                invo.setInterfaces(clazz);
                invo.setMethod(new zhou.base.rpc.protocol.Method(method.getName(),method.getParameterTypes()));
                invo.setParams(args);
                client.invoke(invo);
                return invo.getResult();
            }
        };
        T t = (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[] {clazz}, handler);
        return t;
    }

    static class RPCServer implements Server{

        private int port = 9999;//the server's port
        private Listener listener;
        private boolean isRunning = true;//the server's running status

        // this variable contains the services that server provide to the client
        // the key is service name,the object is the handler
        private Map<String ,Object> serviceEngine = new HashMap<String,Object>();

        @Override
        public void stop() {
            this.isRunning = false;
        }

        @Override
        public void start() {
            System.out.println("starting server...");
            listener = new Listener(this);
            this.isRunning = true;
            listener.start();
        }

        @Override
        public void register(Class interfaceDefiner, Class impl) {
            try {
                this.serviceEngine.put(interfaceDefiner.getName(), impl.newInstance());
                System.out.println(serviceEngine);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void call(Invocation invo) {
            System.out.println(invo);
            //get the service by invocation information
            Object obj = serviceEngine.get(invo.getInterfaces().getName());
            if(null!=obj){
                try {
                    Method m = obj.getClass().getMethod(invo.getMethod().getMethodName(),invo.getMethod().getParams());
                    Object result = m.invoke(obj, invo.getParams());
                    invo.setResult(result);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else
                throw new IllegalArgumentException("has no matched class");
        }

        @Override
        public boolean isRunning() {
            return this.isRunning;
        }

        @Override
        public int getPort() {
            return this.port;
        }

    }
}

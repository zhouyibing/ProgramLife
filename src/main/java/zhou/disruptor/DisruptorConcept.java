package zhou.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
/**
 * Ring Buffer
 * 如其名，环形的缓冲区。曾经 RingBuffer 是 Disruptor 中的最主要的对象，但从3.0版本开始，其职责被简化为仅仅负责对通过 Disruptor 进行交换的数据（事件）进行存储和更新。在一些更高级的应用场景中，Ring Buffer 可以由用户的自定义实现来完全替代。
 * Sequence  Disruptor
 * 通过顺序递增的序号来编号管理通过其进行交换的数据（事件），对数据(事件)的处理过程总是沿着序号逐个递增处理。一个 Sequence 用于跟踪标识某个特定的事件处理者( RingBuffer/Consumer )的处理进度。虽然一个 AtomicLong 也可以用于标识进度，但定义 Sequence 来负责该问题还有另一个目的，那就是防止不同的 Sequence 之间的CPU缓存伪共享(Flase Sharing)问题。
 * （注：这是 Disruptor 实现高性能的关键点之一，网上关于伪共享问题的介绍已经汗牛充栋，在此不再赘述）。
 * Sequencer 
 * Sequencer 是 Disruptor 的真正核心。此接口有两个实现类 SingleProducerSequencer、MultiProducerSequencer ，它们定义在生产者和消费者之间快速、正确地传递数据的并发算法。
 * Sequence Barrier
 * 用于保持对RingBuffer的 main published Sequence 和Consumer依赖的其它Consumer的 Sequence 的引用。 Sequence Barrier 还定义了决定 Consumer 是否还有可处理的事件的逻辑。
 * Wait Strategy
 * 定义 Consumer 如何进行等待下一个事件的策略。 （注：Disruptor 定义了多种不同的策略，针对不同的场景，提供了不一样的性能表现）
 * Event
 * 在 Disruptor 的语义中，生产者和消费者之间进行交换的数据被称为事件(Event)。它不是一个被 Disruptor 定义的特定类型，而是由 Disruptor 的使用者定义并指定。
 * EventProcessor
 * EventProcessor 持有特定消费者(Consumer)的 Sequence，并提供用于调用事件处理实现的事件循环(Event Loop)。
 * EventHandler
 * Disruptor 定义的事件处理接口，由用户实现，用于处理事件，是 Consumer 的真正实现。
 * Producer
 * 即生产者，只是泛指调用 Disruptor 发布事件的用户代码，Disruptor 没有定义特定接口或类型。
 *
 *
 * Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待新事件，这是策略模式的应用。
 * Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
 * 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
 * BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
 * SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
 * YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
 ***
 */
public class DisruptorConcept {

	public static void main(String[] args) {
        //4.定义用于事件处理的线程池  Disruptor 通过 java.util.concurrent.ExecutorService 提供的线程来触发 Consumer 的事件处理。
		ExecutorService executor = Executors.newCachedThreadPool();
		//5.指定等待策略
		WaitStrategy waitStrategy = new YieldingWaitStrategy();
		
		EventFactory<LongEvent> eventFactory = new LongEventFactory();
		int ringBufferSize = 1024*1024;// RingBuffer 大小，必须是 2 的 N 次方；
		
		//create disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,ringBufferSize,executor,ProducerType.SINGLE,waitStrategy);
		
		//create eventhandler 
		WorkHandler<LongEvent>[] handlers = new LongEventHandler[10];
	    for(int i=0;i<handlers.length;i++)
	    	handlers[i]=new LongEventHandler();
	    //set the eventhandler to the disruptor
	    //disruptor.handleEventsWith(handlers);
	    disruptor.handleEventsWithWorkerPool(handlers);
	    
	    //start
	    disruptor.start();
	    long start = System.currentTimeMillis();
	    for(int i=0;i<100;i++){
	    	//publishEvent(disruptor);
	    	publishEvent(disruptor,start+i);//the second way to publish a event
	    }
	    try {
			Thread.sleep(10000);
			//destory(disruptor, executor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Disruptor 的事件发布过程是一个两阶段提交的过程：
　　       * 第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
　　       * 第二步：获取对应的事件对象，将数据写入事件对象；
　　       * 第三部：将事件提交到 RingBuffer;
     * 事件只有在提交之后才会通知 EventProcessor 进行处理；
	 * @param disruptor
	 */
	static void publishEvent(Disruptor<LongEvent> disruptor,long value){
		// 发布事件；
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();//请求下一个事件序号；
		    
		try {
		    LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
		    event.set(value);
		} finally{
		    ringBuffer.publish(sequence);//发布事件；Disruptor 要求 RingBuffer.publish 必须得到调用
		}
	}
	
	static class Translator implements EventTranslatorOneArg<LongEvent, Long>{
		
	    @Override
	    public void translateTo(LongEvent event, long sequence, Long data) {
	        event.set(data);
	    }    
	}
	
	public static Translator TRANSLATOR = new Translator();
    
	public static void publishEvent2(Disruptor<LongEvent> disruptor) {
	    // 发布事件；
	    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
	    long data = System.currentTimeMillis();//获取要通过事件传递的业务数据；
	    ringBuffer.publishEvent(TRANSLATOR, data);
	}
	
	static void destory(Disruptor<LongEvent> disruptor,ExecutorService executor){
		disruptor.shutdown();
		if(executor!=null)
			executor.shutdown();
		System.out.println("the disruptor has been destoried!");
	}
	
	//1.定义事件--事件(Event)就是通过 Disruptor 进行交换的数据类型。
	static class LongEvent{
		private long value;
		public void set(long value){
	        this.value = value;
	    }
	}
	
	/**
	 *2.事件工厂(Event Factory)定义了如何实例化前面第1步中定义的事件(Event)，需要实现接口 com.lmax.disruptor.EventFactory<T>。
     *Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
     *一个 Event 实例实际上被用作一个“数据槽”，发布者发布前，先从 RingBuffer 获得一个 Event 的实例，然后往 Event 实例中填充数据，
     *之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据。
	 ***
	 */
	static class LongEventFactory implements EventFactory<LongEvent>{

		@Override
		public LongEvent newInstance() {
			return new LongEvent();
		}
		
	}
	
	/**
	 * 3.定义事件处理的具体实现
     * 通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
	 ***
	 */
	static class LongEventHandler implements WorkHandler<LongEvent>{

		private long start;
		@Override
		public void onEvent(LongEvent event) throws Exception {
			System.out.println(Thread.currentThread().getName()+" event's data "+event.value);
		}
	}
}

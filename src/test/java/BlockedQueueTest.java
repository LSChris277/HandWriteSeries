import java.util.concurrent.CountDownLatch;

/**
 * 阻塞队列实现生产者消费者模式
 * @author Shengqian
 */
class BlockedQueueTest {
    BlockedQueue<String> queue = new BlockedQueue<String>(20);

    Thread thread0 = new Thread(new Runnable() {
        public void run() {
            int id = 0;
            try {
                while (true) {
                    Thread.sleep(1000);
                    queue.enq("生成苹果" + id++);
                    queue.enq("生成苹果" + id++);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    Thread thread1 = new Thread(new Runnable() {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(500);
                    queue.deq();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    public static void main(String[] args) throws InterruptedException {
        BlockedQueueTest test = new BlockedQueueTest();
        test.thread0.start();
        test.thread1.start();

    }
}
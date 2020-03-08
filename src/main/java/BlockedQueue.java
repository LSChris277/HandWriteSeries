import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Shengqian
 */
public class BlockedQueue<T> {
    final Lock lock = new ReentrantLock();
    // 条件变量：队列不满
    final Condition notFull = lock.newCondition();
    // 条件变量：队列不空
    final Condition notEmpty = lock.newCondition();
    // 阻塞单列最大长度
    int capacity = 0;
    // 当前已经存在下标：入队
    int putIndex = 0;
    // 当前已经存在下标：出队
    int takeIndex = 0;
    // 元素总数
    int elementsSize = 0;
    // 元素数组
    Object[] items;

    public BlockedQueue(int capacity) {
        this.capacity = capacity;
        items = new Object[capacity];
        System.out.println("capacity=" + capacity + ",items.size=" + items.length);
    }

    /**
     * 入队逻辑
     * @param t
     */
    public void enq(T t) {
        lock.lock();
        try {
            // 如果队满，等待
            while (elementsSize == capacity) {
                notFull.await();
            }
            // 入队操作
            items[putIndex] = t;
            if (++putIndex == items.length) {
                putIndex = 0;
            }
            elementsSize++;
            // 入队后，通知可出队
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(t.toString() + "--入队完成");
        }
    }

    /**
     * 出队逻辑
     * @return
     */
    public T deq() {
        T t = null;
        lock.lock();
        try {
            // 队列为空，等待
            while (elementsSize == 0) {
                notEmpty.await();
            }
            // 出队逻辑
            t = (T)items[takeIndex];
            if (++takeIndex == items.length) {
                takeIndex = 0;
            }
            elementsSize--;
            // 出对后，通知可入队
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(t.toString() + "--出队完成");
        }
        return t;
    }
}

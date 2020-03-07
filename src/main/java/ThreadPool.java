import java.util.LinkedList;
import java.util.List;


/**
 * @author LiuShengqian
 */
public class ThreadPool {
    //线程池中允许的最大线程数
    private static int MAXTHREDNUM = Integer.MAX_VALUE;
    //当用户没有指定时默认的线程数
    private int threadNum = 6;
    //线程队列，存放线程任务
    private List<Runnable> queue;

    private WorkerThread[] workerThreads;

    private class WorkerThread extends Thread {
        private volatile boolean on = true;

        @Override
        public void run() {
            Runnable task = null;
            // 判断是否可以取任务
            try {
                while (on && !isInterrupted()) {
                    synchronized (queue) {
                        while (on && !isInterrupted() && queue.isEmpty()) {
                            // 这里要着重测试
                            queue.wait(500);
                        }
                        if (on && !isInterrupted() && !queue.isEmpty()) {
                            task = queue.remove(0);
                        }
                        if (task != null) {
                            System.out.println(currentThread().getName() + "开始执行");
                            task.run();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task = null;
        }

        public void cancel() {
            on = false;
            interrupt();
        }
    }

    public ThreadPool(int threadNum) {
        this.threadNum = threadNum;
        if (threadNum > MAXTHREDNUM)
            threadNum = MAXTHREDNUM;
        this.queue = new LinkedList<Runnable>();
        workerThreads = new WorkerThread[threadNum];
        init();
    }
    public ThreadPool() {
        this(6);
    }

    private void init() {
        for (int i = 0; i < threadNum; i++) {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start();
        }
    }

    //提交任务
    public void execute(Runnable task){
        synchronized (queue){
            queue.add(task);
            //提交任务后唤醒等待在队列的线程
            queue.notifyAll();
        }
    }

    //销毁线程池
    public void shutdown(){
        for(int i=0;i<threadNum;i++){
            workerThreads[i].cancel();
            workerThreads[i] = null;
        }
        queue.clear();
    }
}

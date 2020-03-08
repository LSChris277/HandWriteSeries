import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(3);
        CountDownLatch latch = new CountDownLatch(10);
        threadPool.execute(new Task(latch, "A"));
        threadPool.execute(new Task(latch, "B"));
        threadPool.execute(new Task(latch, "C"));
        threadPool.execute(new Task(latch, "D"));
        threadPool.execute(new Task(latch, "E"));
        threadPool.execute(new Task(latch, "F"));
        threadPool.execute(new Task(latch, "G"));
        threadPool.execute(new Task(latch, "H"));
        threadPool.execute(new Task(latch, "A"));
        threadPool.execute(new Task(latch, "B"));
        threadPool.execute(new Task(latch, "C"));
        threadPool.execute(new Task(latch, "D"));
        threadPool.execute(new Task(latch, "E"));
        threadPool.execute(new Task(latch, "F"));
        threadPool.execute(new Task(latch, "G"));
        threadPool.execute(new Task(latch, "H"));

        latch.await();
        Thread.sleep(1000);
        threadPool.shutdown();
        System.out.println("finished...");
    }

    static class Task implements Runnable {
        private String name;
        private CountDownLatch latch;
        public Task(CountDownLatch latch, String name) {
            this.latch = latch;
            this.name = name;
        }
        public void run() {
            try {
                latch.countDown();
                Thread.sleep(1000);
                System.out.println(" mission: " + name + " complete");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
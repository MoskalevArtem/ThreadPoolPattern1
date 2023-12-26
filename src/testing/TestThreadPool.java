package testing;

import threadpool.PoolThreadManager;
import threadpool.Task;

public class TestThreadPool {
    public static long testThread(int nTask) {
        //Метод возвращает время выполнения задач через обычные потоки
        int n = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < nTask; i++) {
            new Thread(() -> {//создаём поток для выполнения задач
                Task task = new Task() {
                    @Override
                    public void execute() {
                        for (int j = 0; j < n; j++);//cчётчик от 0 до n
                    }
                    @Override
                    public void exit(){}
                };
                task.execute();
                task.exit();
            }).start();
        }
        long end = System.currentTimeMillis();
        long threadTime = end - start;
        return threadTime;
    }

    public static long testPoolThread(int nTask, int nThreads) {
        //Метод возвращает время выполнения задач через пул потоков
        int n = 1000000000;
        long start = System.currentTimeMillis();
        PoolThreadManager manager = new PoolThreadManager(nThreads);
        for (int i = 0; i < nTask; i++) {
            int finalI = i;
            manager.addTask(new Task() {//добавляем новую задачу в очередь задач
                @Override
                public void execute() {

                    for (int i = 0; i < n; i++);//cчётчик от 0 до n
                }
                @Override
                public void exit(){}
            });
        }
        long end = System.currentTimeMillis();
        long threadPoolTime = end - start;
        manager.shutdownManager();
        return threadPoolTime;
    }

    public static void printTime(int nTask, int nThreads) {
        //вывод в консоль результа тестирования
        long threadTime = testThread(nTask);
        long threadPoolTime = testPoolThread(nTask, nThreads);
        System.out.format("%10d %20d %20d %20d\n",
                nTask, nThreads, threadTime, threadPoolTime);
    }
}

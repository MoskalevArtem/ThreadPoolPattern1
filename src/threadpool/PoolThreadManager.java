package threadpool;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PoolThreadManager {

    private  Vector<PoolThread> poolThreads = new Vector<>(); //вектор потоков
    private  Thread managerThread = new Thread();//менеджер потока для разпределения задач по потокам
    private  BlockingQueue<PoolThread> freeThreadQueue = new LinkedBlockingQueue<>(); //блокирующая очередь свободных потоков
    private  BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(); //блокирующая очередь из задач
    private boolean shutdown = false;//состояние менеджера потока
    private  Object obj = new Object();  //объект для синхронизации
    public PoolThreadManager(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            poolThreads.add(new PoolThread(this));//добавляем в вектор потоков nThreads потоков
        }
        freeThreadQueue.addAll(poolThreads);//добавляем все потоки в блокирующую очередь свободных потоков

        managerThread = new Thread(() -> {//распределение задач между пулами потоков
            while (!shutdown) {//пока менеджер потока запущен
                try {
                    PoolThread thread = freeThreadQueue.take();//находим свободный поток
                    Task task = taskQueue.take();//находим свободную задачу
                    thread.executeTask(task);//отправляем эту задачу в поток
                } catch (InterruptedException ignored) {
                }
            }
        });
        managerThread.start();
    }


    public synchronized void addTask(Task task) {
        //добавление записи в блокирующую очередь задач
        synchronized (obj) {
            try {
                taskQueue.put(task);
            } catch (Exception exception) {
            }
        }
    }

    public synchronized void shutdownManager() {// остановка работы менеджера
        shutdown = true;
        synchronized (obj) {
            managerThread.interrupt();//прерываем поток менеджера
            poolThreads.forEach(PoolThread::shutdownThread);//останавливаем все потоки
        }
    }

    public void freeThread(PoolThread thread) throws InterruptedException {//освобождение потока
        freeThreadQueue.put(thread);//добавляем поток в блокирующую очередь свободных потоков
    }


}

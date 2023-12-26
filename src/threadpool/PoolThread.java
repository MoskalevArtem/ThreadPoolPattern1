package threadpool;

public class PoolThread {
    private Runnable runnable;//интерфейс
    private PoolThreadManager manager;//менеджер потока
    private Task task;//задача
    private Object obj = new Object();//объект для синхронизации
    private boolean shutdown;//состояние потока

    public PoolThread(PoolThreadManager manager) {
        this.manager = manager;
        runnable = () -> {
            while (!shutdown) {
                try {
                    synchronized (obj) {
                        if (task == null)
                            obj.wait();//поток засыпает
                    }
                    if (task == null) continue;//ждём появления задачи
                    try {
                        task.execute();//выполнение задачи
                        task.exit();//
                    } catch (Exception exception) {
                    }
                    task = null;
                    manager.freeThread(this); //переводим поток в очередь свободных потоков
                } catch (InterruptedException ignored) {
                }
            }
        };
        new Thread(runnable).start();//запускаем поток
    }

    public void executeTask(Task task) {//сопоставляет свободный поток и задачу, затем пробуждает поток
        synchronized (obj) {
            this.task = task;
            obj.notify();
        }
    }

    public void shutdownThread() {// остановка работы потока
        shutdown = true;
        synchronized (obj) {
            obj.notify();
        }
    }
}
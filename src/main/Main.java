package main;
import testing.TestThreadPool;

public class Main {
    public static void main(String[] args) {
        System.out.format("\n%10s %20s %20s %20s\n","nTask","nThreads",
                "thread time","thread pool time");

        for(int i = 100;i<10000;i*=2)
            TestThreadPool.printTime(i,10);
        for(int i = 10000;i<=50000;i+=10000)
            TestThreadPool.printTime(i,10);

        System.out.println("---------------------------------------------------------------------------");
       for( int i = 1; i<10; i += 1)
            TestThreadPool.printTime(1000,i);
        for( int i = 10;i<100;i += 10)
            TestThreadPool.printTime(1000,i);
        for( int i = 100;i<500;i += 50)
            TestThreadPool.printTime(1000,i);

    }
}

package com.wz.wagemanager.tools;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

//线程的优雅停机问题
//在某些情况下，线程池在等待优雅停机过程中，spring其他bean已经被销毁，线程会报错运行。
//解决的办法就是利用ApplicationListener<ContextClosedEvent> 来监听spring上下文关闭事件，该事件发生在上下文和spring bean销毁之前，此时手动执行线程池优雅停机
@Component
public class ThreadPoolClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static int WAIT_TIME = 30;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        shutdownAndAwaitTermination(threadPoolTaskExecutor.getThreadPoolExecutor());
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(30, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}

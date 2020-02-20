package com.sssj.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 
 * 	通过实现AsyncConfigurer自定义异常线程池，包含异常处理
 * 
 * @author Senyer
 *
 */
@Configuration
@EnableAsync
@Slf4j
public class SpringAsyncConfig extends AsyncConfigurerSupport {
    
    @Override
	public Executor getAsyncExecutor() {
    	ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(1);  
        threadPool.setMaxPoolSize(10);  
        threadPool.setWaitForTasksToCompleteOnShutdown(true);  
        threadPool.setAwaitTerminationSeconds(60 * 15);  
        threadPool.setThreadNamePrefix("Zygk-Async-");
        threadPool.initialize();
        return threadPool;  
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new MyAsyncExceptionHandler();
	}
	
	 /**
     * 	自定义异常处理类
     * @author 
     *
     */
    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override  
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {  
            log.info("Exception message - " + throwable.getMessage());  
            log.info("Method name - " + method.getName());  
            for (Object param : obj) {  
                log.info("Parameter value - " + param);  
            }  
        }  

    } 
}
/*
Spring 已经实现的异常线程池： 
1. SimpleAsyncTaskExecutor：不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。 
2. SyncTaskExecutor：这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地方 
3. ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类 
4. SimpleThreadPoolTaskExecutor：是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类 
5. ThreadPoolTaskExecutor ：最常使用，推荐。 其实质是对java.util.concurrent.ThreadPoolExecutor的包装
*/
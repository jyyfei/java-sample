package com.spring.demo.controller;

import com.spring.demo.dto.Result;
import com.spring.demo.aop.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static com.spring.demo.controller.ThreadPoolTaskExecutorConfig.ASYNC_TASK_BEAN_NAME;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestUtil testUtil;

    private Map<String, List<DeferredResult>> confDeferredResultMap = new ConcurrentHashMap<>();

    @GetMapping("/send/{topic}")
    @ResponseBody
    public Result<String> find(@PathVariable String topic) {
        List<DeferredResult> deferredResultList = confDeferredResultMap.get(topic);
        if (deferredResultList != null) {
            confDeferredResultMap.remove(topic);
            for (DeferredResult deferredResult : deferredResultList) {
                deferredResult.setResult(Result.success("消息来了"));
            }
        }
        return Result.success("发送成功");
    }

    @GetMapping("/monitor/{topic}")
    @ResponseBody
    public DeferredResult<Result<String>> monitor(@PathVariable String topic) {
        DeferredResult deferredResult = new DeferredResult(30 * 1000L, Result.success("无新消息"));
        List<DeferredResult> deferredResultList = confDeferredResultMap.computeIfAbsent(topic, k -> new ArrayList<>());
        deferredResultList.add(deferredResult);
        return deferredResult;
    }

    @GetMapping("/monitor2/{topic}")
    @ResponseBody
    public WebAsyncTask<Result<String>> monitor2(@PathVariable String topic) {
        log.info("接收到请求");
        WebAsyncTask<Result<String>> webAsyncTask = new WebAsyncTask<>(30 * 1000L, ASYNC_TASK_BEAN_NAME, () -> {
            log.info("webAsyncTask 业务处理开始");
            Thread.sleep(10 * 1000);
            log.info("webAsyncTask 业务处理完成");
            return Result.success("完毕");
        });
        webAsyncTask.onCompletion(() -> log.info("onCompletion"));
        webAsyncTask.onError(() -> {
            log.error("onError");
            return Result.success("onError");
        });
        webAsyncTask.onTimeout(() -> {
            log.info("onCompletion");
            return Result.success("onTimeout");
        });
        log.info("接收到请求处理完");
        return webAsyncTask;
    }

    @GetMapping("/monitor2sync/{topic}")
    @ResponseBody
    public Result<String> monitor2sync(@PathVariable String topic) {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("业务处理完成");
        return Result.success("完毕");
    }

    @GetMapping("/testCallable")
    @ResponseBody
    public Callable<String> testCallable() {
        StopWatch stopWatch = new StopWatch(" Callable test 容器线程");
        stopWatch.start("容器线程");
        log.info("返回值Callable 异步请求开始");

        Callable<String> callable = () -> {
            StopWatch stopWatch1 = new StopWatch(" Callable test 工作线程");
            stopWatch1.start("工作线程");
            log.info("返回值Callable 业务处理开始");
            //模拟结果延时返回
            Thread.sleep(10000);
            log.info("返回值Callable 业务处理结束");
            stopWatch1.stop();
            log.info("{} ms", stopWatch1.getTotalTimeMillis());
            return "OK";
        };

        log.info("返回值Callable 异步请求结束");
        stopWatch.stop();
        log.info("{} ms", stopWatch.getTotalTimeMillis());
        return callable;
    }

    @GetMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<String> getUser() {
        testUtil.test2();
        return Result.success("发送成功");
    }
}

package com.gaokao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.advise.AutoGenerateFormParams;
import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.advise.FilterParams;
import com.gaokao.common.meta.vo.volunteer.UserFormDetailVO;
import com.gaokao.common.service.AdviseService;
import com.gaokao.common.service.VolunteerService;

import lombok.extern.slf4j.Slf4j;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;

/**
 * @author MaeYon-Z
 * date 2021-08-09
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/advise")
public class AdviseController {

    private static final String TOPIC = "advise";
    private static final String DEFAULT_CONSUMER_GROUP = "default-consumer";
    private static final String DEFAULT_TAG = ":gaokao";
    @Autowired
    private AdviseService adviseService;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping("/getrank/{score}")
    public AjaxResult<Integer> getRank(@PathVariable Integer score){
        return AjaxResult.SUCCESS(adviseService.getUserRank(score));
    }

    @PostMapping("/listAll")
    public AjaxResult<Page<AdviseVO>> advise(@RequestBody FilterParams filterParams){
        return AjaxResult.SUCCESS(adviseService.list(filterParams));
    }

    private AutoGenerateFormParams deserializeObject(String serializedObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(serializedObject, AutoGenerateFormParams.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }
    private String serializeObject(AutoGenerateFormParams object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }
    @PostMapping("/autoAdvise")
    public AjaxResult<String> sendMessage(@RequestBody AutoGenerateFormParams autoGenerateFormParams) {
        rocketMQTemplate.convertAndSend(TOPIC + DEFAULT_TAG, serializeObject(autoGenerateFormParams));
        return AjaxResult.SUCCESS("加入MQ成功");
    }
    @RocketMQMessageListener(topic = TOPIC, consumerGroup = DEFAULT_CONSUMER_GROUP)
    @Component
    public class MyRocketMQListener implements RocketMQListener<String> {
        int corePoolSize = 9;
        int maximumPoolSize = 16;
        long keepAliveTime = 2L;
        TimeUnit unit = TimeUnit.MINUTES; //存活一分钟
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1000);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                handler
        );
        @Override
        public void onMessage(String message) {
            FutureTask<Boolean> task = new FutureTask<>(new SumCalculator(message));
            executor.submit(task);
            Boolean result = false;
            try {
                result = task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(result == false) {
                System.out.println("获取结果失败");
            }
        }
    }

    //轮询rocketmq中的结果
    class SumCalculator implements Callable<Boolean> {
        private String message;

        public SumCalculator(String message) {
            this.message = message;
        }
        public Boolean call() {
            Boolean success = true;
            try {
                AutoGenerateFormParams autoGenerateFormParams = deserializeObject(message);
                UserFormDetailVO userFormDetailVO = adviseService.generateVoluntForm(autoGenerateFormParams);
                volunteerService.changeCurrentForm(userFormDetailVO.getUserId(), userFormDetailVO.getId());
                System.out.println("ThreadId " + Thread.currentThread().getId() +  " New form id" + userFormDetailVO.getId() + " Received message: " + serializeObject(autoGenerateFormParams));
                String id = autoGenerateFormParams.getUserId().toString();
                System.out.println("form" + id);
                redisTemplate.delete("form" +id);
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }
    }

    @PostMapping("/autoGenerateForm")
    public AjaxResult<UserFormDetailVO> autoGenerateVolunteerForm(@RequestBody AutoGenerateFormParams autoGenerateFormParams){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("cachemiss");
        bloomFilter.tryInit(1000000, 0.01);
        bloomFilter.add(autoGenerateFormParams.getUserId().toString());
        UserFormDetailVO userFormDetailVO = adviseService.generateVoluntForm(autoGenerateFormParams);
        volunteerService.changeCurrentForm(userFormDetailVO.getUserId(), userFormDetailVO.getId());
        return AjaxResult.SUCCESS(userFormDetailVO);
    }

}

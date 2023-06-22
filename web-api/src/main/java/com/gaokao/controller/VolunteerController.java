package com.gaokao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaokao.common.constants.VolunteerConstant;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.po.FormVolunteer;
import com.gaokao.common.meta.vo.volunteer.*;
import com.gaokao.common.service.VolunteerService;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java.util.List;


/**
 * @author attack204
 * date:  2021/8/7
 * email: 757394026@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/volunteer")
public class VolunteerController {

    private static final String TOPIC = "redis";
    private static final String REDIS_CONSUMER_GROUP = "redis-consumer";
    private static final String DEFAULT_TAG = ":redis";
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Bean(name = "bytesRedisTemplate")
    public RedisTemplate<String, byte[]> bytesRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 设置key和value的序列化规则
        redisTemplate.setValueSerializer(RedisSerializer.byteArray());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;
    /**
     * 查询某一用户当前的志愿表
     * 这个类型不是null，后期会改
     */
    public void saveCompressedValueToRedis(String key, String value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(value.getBytes());
        gzipOutputStream.close();
        byte[] compressedValue = outputStream.toByteArray();
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, compressedValue);
        Random random = new Random();
        int randomSeconds = random.nextInt(60);
        long expireTime = TimeUnit.MINUTES.toSeconds(20) + randomSeconds; // 设置过期时间为 20 分钟加上一个随机的秒数
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }
    public String getDecompressedValueFromRedis(String key) throws IOException {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] compressedValue = valueOps.get(key);
        if (compressedValue == null) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedValue);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length;
        while ((length = gzipInputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        gzipInputStream.close();
        outputStream.close();
        return new String(outputStream.toByteArray());
    }

    private UserFormDetailVO deserializeObject(String serializedObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(serializedObject, UserFormDetailVO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }
    private String serializeObject(UserFormDetailVO object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }




    @GetMapping("/getCurrent")
    public AjaxResult<UserFormDetailVO> listCurrent() {

        Long id = UserUtils.getUserId();
        String redis_key = "form" + id.toString();
        try {
            //查redis，看能不能找到cache数据
            String value = getDecompressedValueFromRedis(redis_key);
            if(value == null) {
                System.out.println("从redis未查询到, 用户id为" + id.toString());
                RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("cachemiss");
                bloomFilter.tryInit(1000000, 0.01);

                //查redis BF，检查是否是没有创建过志愿表
                if(bloomFilter.contains(id.toString())) {
                    //创建了志愿表
                    UserFormDetailVO userFormDetailVO = volunteerService.listCurrentForm(id);
                    if(userFormDetailVO == null) {
                        return AjaxResult.FAIL("无法获取用户当前志愿表或用户未创建志愿表");
                    } else {
                        saveCompressedValueToRedis(redis_key, serializeObject(userFormDetailVO));
                        return AjaxResult.SUCCESS(userFormDetailVO);
                    }
                } else {
                    //没有创建志愿表
                    return AjaxResult.FAIL("从BF中查询到该用户未创建志愿表");
                }

            } else {
                System.out.println("success:从redis中查询到");
                UserFormDetailVO userFormDetailVO = deserializeObject(value);
                return AjaxResult.SUCCESS(userFormDetailVO);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.FAIL("无法执行到此处");
    }

    /**
     * 查询某一用户所有的志愿表
     *
     * @return
     */
    @GetMapping("/getAll")
    public AjaxResult<List<UserFormAllVO>> listAllForm() {
        Long id = UserUtils.getUserId();
        return AjaxResult.SUCCESS(volunteerService.listAll(id));
    }

    @GetMapping("/delete/{id}")
    public AjaxResult<Long> deleteForm(@PathVariable Long id) {
        return AjaxResult.SUCCESS(volunteerService.deleteForm(id));
    }

    @PostMapping("/changeCurrentForm")
    public AjaxResult<Long> changeCurrentForm(@RequestBody ChangeCurrentFormParams changeCurrentFormParams) {
        return AjaxResult.SUCCESS(volunteerService.changeCurrentForm(UserUtils.getUserId(),
                changeCurrentFormParams.getNewFormId()));
    }

    /**
     * 创建志愿表
     *
     * @return 返回新创建的志愿表id
     */
    @PostMapping("/create")
    public AjaxResult<Long> create(@RequestBody VolunteerCreateParams volunteerCreateParams) {

        Long result = volunteerService.create(UserUtils.getUserId(),
                volunteerCreateParams.getSubject(),
                volunteerCreateParams.getScore(),
                volunteerCreateParams.getGeneratedType(),
                volunteerCreateParams.getName());

        if (result == -1L)
            return AjaxResult.FAIL("创建失败");
        else
            return AjaxResult.SUCCESS(result);

    }

    @GetMapping("/queryExist")
    public AjaxResult<Boolean> queryExist(@RequestBody VolunteerExistParams volunteerExistParams) {

        Boolean result = volunteerService.queryExist(volunteerExistParams.getFormId(),
                volunteerExistParams.getVolunteerSection(),
                volunteerExistParams.getVolunteerPosition());
        return AjaxResult.SUCCESS(result);

    }

    /**
     * 修改志愿表名称
     */
    @PostMapping("/updateName")
    public AjaxResult<Long> updateName(@RequestBody VolunteerUpdateParams volunteerUpdateParams) {

        Long result = volunteerService.updateUserFormName(UserUtils.getUserId(),
                volunteerUpdateParams.getFormId(),
                volunteerUpdateParams.getName());

        if (result == -1L)
            return AjaxResult.FAIL("更新失败");
        else
            return AjaxResult.SUCCESS(result);

    }

    /**
     * 添加志愿
     */
    @PostMapping("/addVolunteer")
    public AjaxResult<Long> addVolunteer(@RequestBody AddVolunteerParams addVolunteerParams) {
        Long result = volunteerService.setVolunteer(UserUtils.getUserId(),
                addVolunteerParams.getFormId(),
                addVolunteerParams.getSection(),
                addVolunteerParams.getVolunteerPosition(),
                addVolunteerParams.getVolunteerId());

        if (result == -1L)
            return AjaxResult.FAIL("添加志愿失败或该志愿表下已有相同志愿");
        else
            return AjaxResult.SUCCESS(result);
    }

    /**
     * 删除志愿
     */
    @PostMapping("/deleteVolunteer")
    public AjaxResult<Long> deleteVolunteer(@RequestBody DeleteVolunteerParams deleteVolunteerParams) {
        Long result = volunteerService.setVolunteer(UserUtils.getUserId(),
                deleteVolunteerParams.getFormId(),
                deleteVolunteerParams.getSection(),
                deleteVolunteerParams.getVolunteerPosition(),
                VolunteerConstant.EMPTY_VOLUNTEER);
        String id =  UserUtils.getUserId().toString();
        System.out.println(result == 0 ? "删除志愿失败" : "删除志愿成功");
        if(result != 0) { //只有能删除数据库才删除缓存
            String redis_key = "form" + id;
            System.out.println("redis_key=" + redis_key);

            Boolean ret = redisTemplate.delete(redis_key);
            if(!ret) {
                System.out.println("删除失败, 加入MQ=" + redis_key);
                rocketMQTemplate.convertAndSend(TOPIC + DEFAULT_TAG, redis_key);
            }
        }
        if (result == 0)
            return AjaxResult.FAIL("操作失败");
        else
            return AjaxResult.SUCCESS(result);
    }


    @RocketMQMessageListener(topic = TOPIC, consumerGroup = REDIS_CONSUMER_GROUP)
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
            FutureTask<Boolean> task = new FutureTask<>(new RedisQuery(message));
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
    class RedisQuery implements Callable<Boolean> {
        private String message;

        public RedisQuery(String message) {
            this.message = message;
        }
        public Boolean call() {
            Boolean success = true;
            try {
                Boolean ret = false;
                final int cnt = 5;
                for(int i = 0; i < cnt; i++) {
                    ret = redisTemplate.delete(message);
                    if(ret) break;
                }
                if(!ret) {
                    System.out.println("MQ重试仍然失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }
    }


    /**
     * 志愿换位
     * 如果第二个id是0的话，说明和一个空的志愿进行交换
     *
     */
    @PostMapping("/swapVolunteer")
    public AjaxResult<Long> swapVolunteer(@RequestBody SwapVolunteerParams swapVolunteerParams) {

        if(swapVolunteerParams.getFirstVolunteerPosition() < 1 || swapVolunteerParams.getFirstVolunteerPosition() > 96
                ||swapVolunteerParams.getSecondVolunteerPosition() < 1 || swapVolunteerParams.getSecondVolunteerPosition() > 96) {
            return AjaxResult.FAIL("需要交换志愿的边界出现异常");
        }


        volunteerService.setVolunteer(UserUtils.getUserId(),
                swapVolunteerParams.getFormId(),
                swapVolunteerParams.getSection(),
                swapVolunteerParams.getFirstVolunteerPosition(),
                VolunteerConstant.EMPTY_VOLUNTEER);
        volunteerService.setVolunteer(UserUtils.getUserId(),
                swapVolunteerParams.getFormId(),
                swapVolunteerParams.getSection(),
                swapVolunteerParams.getSecondVolunteerPosition(),
                VolunteerConstant.EMPTY_VOLUNTEER);

        volunteerService.setVolunteer(UserUtils.getUserId(),
                swapVolunteerParams.getFormId(),
                swapVolunteerParams.getSection(),
                swapVolunteerParams.getFirstVolunteerPosition(),
                swapVolunteerParams.getSecondVolunteerId());
        Long result = volunteerService.setVolunteer(UserUtils.getUserId(),
                swapVolunteerParams.getFormId(),
                swapVolunteerParams.getSection(),
                swapVolunteerParams.getSecondVolunteerPosition(),
                swapVolunteerParams.getFirstVolunteerId());

        if (result == -1L)
            return AjaxResult.FAIL("操作失败");
        else
            return AjaxResult.SUCCESS(result);

    }

    /**
     * 上调志愿
     * 本质是和上一个交换位置
     * //TODO 如果上一个已经有了，则变为交换
     */
    @PostMapping("/upVolunteer")
    public AjaxResult<Long> upVolunteer(@RequestBody UpVolunteerParams upVolunteerParams) {

        if(upVolunteerParams.getVolunteerPosition() <= 1 || upVolunteerParams.getVolunteerPosition() > 96) {
            return AjaxResult.FAIL("待上移志愿的边界出现问题");
        }

        FormVolunteer formVolunteer = volunteerService.findByFormIdAndSectionAndVolunteerPosition(upVolunteerParams.getFormId(), upVolunteerParams.getSection(), upVolunteerParams.getVolunteerPosition() - 1);

        volunteerService.setVolunteer(UserUtils.getUserId(),
                upVolunteerParams.getFormId(),
                upVolunteerParams.getSection(),
                upVolunteerParams.getVolunteerPosition(),
                VolunteerConstant.EMPTY_VOLUNTEER);
        Long result = volunteerService.setVolunteer(UserUtils.getUserId(),
                upVolunteerParams.getFormId(),
                upVolunteerParams.getSection(),
                upVolunteerParams.getVolunteerPosition() - 1,
                upVolunteerParams.getVolunteerId());

        if(formVolunteer != null) {
            volunteerService.setVolunteer(UserUtils.getUserId(), formVolunteer.getFormId(), formVolunteer.getVolunteerSection(), formVolunteer.getVolunteerPosition() + 1, formVolunteer.getVolunteerId());
        }


        if (result == -1L)
            return AjaxResult.FAIL("操作失败");
        else
            return AjaxResult.SUCCESS(result);

    }

    /**
     * 下调志愿
     * 本质是和下一个交换位置
     * //TODO 参考上面的todo
     */
    @PostMapping("/downVolunteer")
    public AjaxResult<Long> downVolunteer(@RequestBody DownVolunteerParams downVolunteerParams) {

        if(downVolunteerParams.getVolunteerPosition() >= 96 || downVolunteerParams.getVolunteerPosition() < 1) {
            return AjaxResult.FAIL("待下移志愿的边界出现异常");
        }

        FormVolunteer formVolunteer = volunteerService.findByFormIdAndSectionAndVolunteerPosition(downVolunteerParams.getFormId(), downVolunteerParams.getSection(), downVolunteerParams.getVolunteerPosition() + 1);

        volunteerService.setVolunteer(UserUtils.getUserId(),
                downVolunteerParams.getFormId(),
                downVolunteerParams.getSection(),
                downVolunteerParams.getVolunteerPosition(),
                VolunteerConstant.EMPTY_VOLUNTEER);
        Long result = volunteerService.setVolunteer(UserUtils.getUserId(),
                downVolunteerParams.getFormId(),
                downVolunteerParams.getSection(),
                downVolunteerParams.getVolunteerPosition() + 1,
                downVolunteerParams.getVolunteerId());

        if(formVolunteer != null) {
            volunteerService.setVolunteer(UserUtils.getUserId(), formVolunteer.getFormId(), formVolunteer.getVolunteerSection(), formVolunteer.getVolunteerPosition() - 1, formVolunteer.getVolunteerId());
        }

        if (result == -1L)
            return AjaxResult.FAIL("操作失败");
        else
            return AjaxResult.SUCCESS(result);

    }

}

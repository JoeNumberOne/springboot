package com.joe.springboot.mqlistenner;

import com.joe.springboot.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author maqiao
 * @create create on 2019-03-28 14:41
 * @desc
 **/
@Component
@Slf4j
public class MsgProducer implements RabbitTemplate.ConfirmCallback {


    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
     */
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMsg(String content, Integer queue) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        if (queue==1) {
            rabbitTemplate.convertAndSend(RabbitConfig.ITM_GPS_MQ_EXCHANGE, RabbitConfig.ITM_GPS_IN_TRANSFER, content, correlationId);
        }
        if (queue==2) {
            rabbitTemplate.convertAndSend(RabbitConfig.ITM_GPS_MQ_EXCHANGE, RabbitConfig.ITM_GPS_OVER_TRANSFER, content, correlationId);

        }
    }
    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + cause);
        }
    }
}

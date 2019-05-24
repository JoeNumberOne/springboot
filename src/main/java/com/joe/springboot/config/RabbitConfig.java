package com.joe.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author maqiao
 * @create create on 2019-03-28 14:26
 * @desc rabbitMQ配置
 **/
@Configuration
@Slf4j
public class RabbitConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String ITM_GPS_MQ_EXCHANGE = "itm_gps_mq_exchange";


    public static final String ITM_GPS_IN_TRANSFER_QUEUE = "itm_gps_in_transfer_queue";
    public static final String ITM_GPS_OVER_TRANSFER_QUEUE = "itm_gps_over_transfer_queue";

    public static final String ITM_GPS_IN_TRANSFER = "itm_gps_in_transfer";
    public static final String ITM_GPS_OVER_TRANSFER = "itm_gps_over_transfer";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 必须是prototype类型
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型:TopicExchange
     */
    @Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange(ITM_GPS_MQ_EXCHANGE);
    }

    /**
     * 获取队列 ITM_GPS_IN_TRANSFER_QUEUE
     * @return
     */
    @Bean
    public Queue itmGpsInTransferQueue() {
        //队列持久
        return new Queue(ITM_GPS_IN_TRANSFER_QUEUE, true);
    }
    /**
     * 获取队列 ITM_GPS_OVER_TRANSFER_QUEUE
     * @return
     */
    @Bean
    public Queue itmGpsOverTransferQueue() {
        //队列持久
        return new Queue(ITM_GPS_OVER_TRANSFER_QUEUE, true);
    }

    /**
     * 队列绑定Exchange 和 routing key
     * @return
     */
    @Bean
    public Binding bindingInTransferQueue() {
        return BindingBuilder.bind(itmGpsInTransferQueue()).to(defaultExchange()).with(RabbitConfig.ITM_GPS_IN_TRANSFER);
    }
    /**
     * 队列绑定Exchange 和 routing key
     * @return
     */
    @Bean
    public Binding bindingOverTransferQueue() {
        return BindingBuilder.bind(itmGpsOverTransferQueue()).to(defaultExchange()).with(RabbitConfig.ITM_GPS_OVER_TRANSFER);
    }

}

package com.changgou.order.mq.queue;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 延时队列配置
 */
@Configuration
public class QueueConfig {

    /**
     * 创建queue1
     * 会过期，将数据发送到Q2
     */
    @Bean
    public Queue orderDelayQueue(){
        return QueueBuilder.durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange","orderListenerExchange")//死信队列
                .withArgument("x-dead-letter-routing-key", "orderListenerQueue")
                .build();

    }

    /**
     * 创建queue2
     */
    @Bean
    public Queue orderListenerQueue(){

        return new Queue("orderListenerQueue", true);
    }

    /**
     * 创建交换机
     */
    @Bean
    public Exchange orderListenerExchange(){
        return new DirectExchange("orderListenerExchange");
    }

    /**
     * Q2进行绑定
     *
     * 监听的是     Q2
     *
     */
    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue, Exchange orderListenerExchange){
        return BindingBuilder
                .bind(orderListenerQueue)
                .to(orderListenerExchange)
                .with("orderListenerQueue").noargs();
    }


}

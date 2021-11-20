package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.IRabbitSenderCallback;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回调-失败重试
 *
 * @author wangz
 * @date 2021/11/14 0014 22:57
 */
@RabbitParameter(prefix = MyConstant.PREFIX_MY)
public class FailedRetryRabbitSenderCallback implements IRabbitSenderCallback {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FailedRetryRabbitSenderCallback.class);

    /**
     * 发送确认
     *
     * @param correlationData 关联数据
     * @param ack             是否ack
     * @param cause           失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            logger.error(String.format("message not arrive exchange, cause: %s", cause));
            if (correlationData != null) {
                logger.error(String.format("return message: %s", correlationData.getReturnedMessage() == null ? "" :
                        correlationData.getReturnedMessage().toString()));
            }
        }
    }

    /**
     * 发送失败
     *
     * @param returnedMessage 消息
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        if (returnedMessage.getReplyCode() != 200) {
            logger.error(String.format("message not arrive queue, return message: %s", returnedMessage.toString()));
        }
    }

    /**
     * doInRabbit
     *
     * @param channel 信道
     * @return null
     * @throws IOException 异常
     */
    @Override
    public Void doInRabbit(Channel channel) throws IOException {

        // 死信参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "exchange.retry");
        arguments.put("x-dead-letter-routing-key", "routing.key.retry");

        // 声明死信交换机
        String dlxExchange = "dlx.exchange.delay";
        channel.exchangeDeclare(dlxExchange, BuiltinExchangeType.DIRECT, true, false, null);

        // 延时时间集合，根据时间绑定多个延时队列
        List<Long> delayTimes = new ArrayList<>();
        delayTimes.add(10000L);
        delayTimes.add(20000L);
        delayTimes.forEach(t -> {
            String queueName = "dlx.queue.delay." + t;
            arguments.put("x-message-ttl", t);
            try {
                channel.queueDeclare(queueName, true, false, false, arguments);
                channel.queueBind(queueName, dlxExchange, "dlx.routing.key.delay." + t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return null;
    }
}

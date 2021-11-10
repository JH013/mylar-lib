package com.mylar.lib.rabbitmq.component.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * 发送失败策略-默认
 *
 * @author wangz
 * @date 2021/11/10 0010 22:39
 */
public class DefaultRabbitSendFailedStrategy implements IRabbitSendFailedStrategy {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultRabbitSendFailedStrategy.class);

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
}

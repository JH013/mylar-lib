package com.mylar.lib.rabbitmq.component.data;

import org.springframework.amqp.core.MessageProperties;

/**
 * RabbitMQ消息
 *
 * @author wangz
 * @date 2021/11/14 0014 23:30
 */
public class RabbitMessage {

    /**
     * 构造方法
     *
     * @param messageBody 消息体
     */
    public RabbitMessage(String messageBody) {
        this.messageBody = messageBody;
        this.messageProperties = new MessageProperties();
    }

    /**
     * 消息体
     */
    private String messageBody;

    /**
     * 消息属性
     */
    private MessageProperties messageProperties;

    /**
     * 设置消息头
     *
     * @param headerKey   键
     * @param headerValue 值
     */
    public void setHeader(String headerKey, Object headerValue) {
        this.messageProperties.setHeader(headerKey, headerValue);
    }

    /**
     * 获取消息头
     *
     * @param headerKey 键
     * @return 值
     */
    public Object getHeader(String headerKey) {
        return this.messageProperties.getHeader(headerKey);
    }

    // region getter & setter

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    // endregion
}

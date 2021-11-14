package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;

/**
 * @author wangz
 * @date 2021/11/14 0014 23:18
 */
@RabbitParameter(prefix = "my")
public class FailedRetryRabbitSender extends AbstractRabbitSender {
}

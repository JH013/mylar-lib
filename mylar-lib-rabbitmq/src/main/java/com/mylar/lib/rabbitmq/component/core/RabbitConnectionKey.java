package com.mylar.lib.rabbitmq.component.core;

import java.util.Objects;

/**
 * @author wangz
 * @date 2021/11/10 0010 21:20
 */
public class RabbitConnectionKey {

    public RabbitConnectionKey(String prefix, String scope) {
        this.prefix = prefix;
        this.scope = scope;
    }

    private final String prefix;

    private final String scope;

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.scope);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RabbitConnectionKey key = (RabbitConnectionKey) obj;
        return Objects.equals(this.prefix, key.prefix) && Objects.equals(this.scope, key.scope);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getScope() {
        return scope;
    }
}

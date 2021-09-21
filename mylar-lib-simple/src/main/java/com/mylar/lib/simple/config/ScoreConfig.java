package com.mylar.lib.simple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 配置-分数
 *
 * @author wangz
 * @date 2021/9/21 0021 19:42
 */
@Component
@PropertySource(value = {"custom/custom-config.properties"})
public class ScoreConfig {

    @Value("${score.math}")
    private BigDecimal math;

    @Value("${score.chemistry}")
    private BigDecimal chemistry;

    @Value("${score.sports}")
    private BigDecimal sports;

    public BigDecimal getMath() {
        return math;
    }

    public void setMath(BigDecimal math) {
        this.math = math;
    }

    public BigDecimal getChemistry() {
        return chemistry;
    }

    public void setChemistry(BigDecimal chemistry) {
        this.chemistry = chemistry;
    }

    public BigDecimal getSports() {
        return sports;
    }

    public void setSports(BigDecimal sports) {
        this.sports = sports;
    }

    @Override
    public String toString() {
        return "ScoreConfig{" +
                "math=" + math +
                ", chemistry=" + chemistry +
                ", sports=" + sports +
                '}';
    }
}

package com.mylar.lib.quartz.biz;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 商品信息实体类
 *
 * @author wangz
 * @date 2021/9/9 0009 1:26
 */
@Entity
@Table(name = "basic_goods_info")
@Data
public class GoodsInfoEntity {

    /**
     * 商品编号
     */
    @Id
    @GeneratedValue
    @Column(name = "bgi_id")
    private Long id;

    /**
     * 商品名称
     */
    @Column(name = "bgi_name")
    private String name;

    /**
     * 商品单位
     */
    @Column(name = "bgi_unit")
    private String unit;

    /**
     * 商品单价
     */
    @Column(name = "bgi_price")
    private BigDecimal price;
}

package com.mylar.lib.quartz.biz;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 商品信息仓储
 *
 * @author wangz
 * @date 2021/9/9 0009 1:26
 */
public interface GoodsInfoRepository extends JpaRepository<GoodsInfoEntity, Long> {
}

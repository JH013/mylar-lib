package com.mylar.lib.quartz.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品控制器
 *
 * @author wangz
 * @date 2021/9/9 0009 1:27
 */
@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    /**
     * 商品信息服务
     */
    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 添加商品
     *
     * @return 主键
     */
    @RequestMapping(value = "/save")
    public Long save(GoodsInfoEntity goods) throws Exception {
        return this.goodsInfoService.saveGoods(goods);
    }
}

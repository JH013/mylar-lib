package com.mylar.lib.quartz.biz;

import com.mylar.lib.quartz.biz.job.GoodsAddAfterTask;
import com.mylar.lib.quartz.biz.job.GoodsStockCheckTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * 商品信息服务
 *
 * @author wangz
 * @date 2021/9/9 0009 1:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsInfoService {

    /**
     * 注入任务调度器
     */
    @Autowired
    private Scheduler scheduler;

    /**
     * 商品数据接口
     */
    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    /**
     * 保存商品基本信息
     *
     * @param goods 商品实例
     * @return 主键
     */
    public Long saveGoods(GoodsInfoEntity goods) throws Exception {

        // 添加商品信息到数据库
        this.goodsInfoRepository.save(goods);

        // 创建商品添加后置任务，1分钟后执行，执行1次后销毁
        this.buildGoodsAddAfterTask();

        // 创建商品库存检查任务，定时执行，频率30秒
        this.buildGoodsStockCheckTask(goods.getId());
        return goods.getId();
    }

    /**
     * 创建商品添加后置任务
     *
     * @throws Exception 异常
     */
    public void buildGoodsAddAfterTask() throws Exception {

        // 设置开始时间为1分钟后
        long startAtTime = System.currentTimeMillis() + 1000 * 60;

        // 任务名称
        String name = UUID.randomUUID().toString();

        // 任务所属分组
        String group = GoodsAddAfterTask.class.getName();

        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodsAddAfterTask.class).withIdentity(name, group).build();

        // 创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startAtTime)).build();

        // 将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 创建商品库存检查任务
     *
     * @param goodsId 商品Id
     * @throws Exception 异常
     */
    public void buildGoodsStockCheckTask(Long goodsId) throws Exception {

        //任务名称
        String name = UUID.randomUUID().toString();

        //任务所属分组
        String group = GoodsStockCheckTask.class.getName();

        // 调度器，执行频率30秒
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");

        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodsStockCheckTask.class).withIdentity(name, group).build();

        // 传递参数
        jobDetail.getJobDataMap().put("goodsId", goodsId);

        // 创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();

        // 将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

}

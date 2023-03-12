package com.mylar.lib.job.core;

import com.mylar.lib.job.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.control.SingleJobControlAggregation;
import com.mylar.lib.job.control.SingleJobControlResolver;
import com.mylar.lib.job.control.SingleJobControlParam;
import com.mylar.lib.job.scheduler.ISingleJobScheduler;
import com.mylar.lib.job.scheduler.SingleJobScheduler;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

/**
 * 单机任务自动配置
 *
 * @author wangz
 * @date 2023/3/8 0008 23:37
 */
@Configuration
@ConditionalOnProperty(prefix = "mylar.job.single", name = {"enable"}, havingValue = "true", matchIfMissing = true)
@Import({SingleJobScheduler.class, SingleJobControlAggregation.class})
public class SingleJobAutoConfig implements ApplicationRunner {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(SingleJobAutoConfig.class);

    /**
     * 应用上下文
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 单机任务调度器
     */
    @Autowired
    private ISingleJobScheduler singleJobScheduler;

    @Autowired
    private SingleJobControlAggregation singleJobControlAggregation;

    // endregion

    // region 接口实现

    /**
     * 运行
     *
     * @param args 参数
     * @throws Exception 异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 初始化全部单机任务
        this.initAllSingleJobs();
    }

    // endregion

    // region 私有方法

    /**
     * 初始化全部单机任务
     *
     * @throws Exception 异常
     */
    private void initAllSingleJobs() throws Exception {

        // 获取任务实例
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(SingleJobBasicConfig.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            // 任务实例
            Object instance = entry.getValue();
            if (AbstractSingleJob.class.isAssignableFrom(instance.getClass())) {

                // 初始化单机任务
                this.initSingleJob((AbstractSingleJob) instance);
            }
        }
    }

    /**
     * 初始化单机任务
     *
     * @param singleJob 任务实例
     * @throws Exception 异常
     */
    private void initSingleJob(AbstractSingleJob singleJob) throws Exception {

        // 获取任务基础配置
        SingleJobBasicConfig basicConfig = singleJob.getClass().getAnnotation(SingleJobBasicConfig.class);
        if (basicConfig == null) {
            return;
        }

        // 任务键
        JobKey jobKey = JobKey.jobKey(basicConfig.jobName(), basicConfig.jobGroup());

        // 获取任务控制参数
        SingleJobControlParam controlParam = SingleJobControlResolver.resolveJobControl(singleJob.getClass());

        // 添加到控制聚合
        this.singleJobControlAggregation.addControl(jobKey, basicConfig, controlParam);

        // 是否初始化
        boolean enableInit = this.singleJobControlAggregation.enableInit(jobKey);
        if (!enableInit) {
            return;
        }

        // 任务调度器
        Scheduler scheduler = this.singleJobScheduler.getScheduler();
        if (scheduler == null) {
            return;
        }

        // 任务监听器
        Class<? extends JobListener>[] listeners = basicConfig.jobListeners();
        if (listeners.length > 0) {
            for (Class<? extends JobListener> listener : listeners) {

                // 实例化任务监听器
                JobListener jobListenerInstance = listener.getDeclaredConstructor().newInstance();

                // 添加到监听管理器
                scheduler.getListenerManager().addJobListener(jobListenerInstance);
            }
        }

        // 任务触发器监听器
        Class<? extends TriggerListener>[] triggerListeners = basicConfig.triggerListeners();
        if (triggerListeners.length > 0) {
            for (Class<? extends TriggerListener> triggerListener : triggerListeners) {

                // 实例化任务触发器监听器
                TriggerListener triggerListenerInstance = triggerListener.getDeclaredConstructor().newInstance();

                // 添加到监听管理器
                scheduler.getListenerManager().addTriggerListener(triggerListenerInstance);
            }
        }

        // 创建任务明细
        JobDetail jobDetail = JobBuilder.newJob(singleJob.getClass())
                .withIdentity(basicConfig.jobName(), basicConfig.jobGroup())
                .build();

        // 获取 cron 表达式
        String cron = this.singleJobControlAggregation.getCron(jobKey);

        // 创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withPriority(2)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)
                        .withMisfireHandlingInstructionFireAndProceed()
                )
                .build();

        // 调度任务
        scheduler.scheduleJob(jobDetail, trigger);
    }

    // endregion
}

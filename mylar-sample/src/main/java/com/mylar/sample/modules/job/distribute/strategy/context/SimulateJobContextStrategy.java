package com.mylar.sample.modules.job.distribute.strategy.context;

import com.mylar.lib.job.distribute.context.IJobContextStrategy;
import com.mylar.lib.job.distribute.context.SimpleDistributeJobContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangz
 * @date 2023/3/19 0019 22:50
 */
@Component
public class SimulateJobContextStrategy implements IJobContextStrategy {

    // region 接口实现

    /**
     * 获取任务上下文
     *
     * @return 任务上下文
     */
    @Override
    public SimpleDistributeJobContext getJobContext() {
        SimpleDistributeJobContext context = new SimpleDistributeJobContext();
        List<String> allInstance = new ArrayList<>();
        allInstance.add("ins_1");
        allInstance.add("ins_2");
        allInstance.add("ins_3");
        allInstance.add("ins_4");
        allInstance.add("ins_5");
        context.setAllInstance(allInstance);
        context.setCurrentInstance("ins_2");
        return context;
    }

    // endregion
}

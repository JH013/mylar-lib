package com.mylar.sample.modules.job.distribute.strategy.context;

//import com.mylar.lib.job.distribute.context.IJobContextStrategy;
//import com.mylar.lib.job.distribute.context.SimpleDistributeJobContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;

/**
 * 上下文策略-基于 Eureka
 *
 * @author wangz
 * @date 2023/3/19 0019 23:46
 */
//@Component
//public class EurekaContextStrategy implements IJobContextStrategy {
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @Autowired
//    private EurekaRegistration eurekaRegistration;
//
//    /**
//     * 获取任务上下文
//     *
//     * @return 任务上下文
//     */
//    @Override
//    public SimpleDistributeJobContext getJobContext() {
//
//        // 当前服务名（自带集群号）
//        String serviceName = eurekaRegistration.getInstanceConfig().getAppname();
//
//        // 服务实例Id集合（IP + 端口）
//        List<String> instanceIds = new ArrayList<>();
//
//        // 根据服务名获取所有服务实例Id
//        List<ServiceInstance> allServiceInstances = discoveryClient.getInstances(serviceName);
//        for (ServiceInstance serviceInstance : allServiceInstances) {
//            if (serviceInstance instanceof EurekaDiscoveryClient.EurekaServiceInstance) {
//                EurekaDiscoveryClient.EurekaServiceInstance eurekaServiceInstance = (EurekaDiscoveryClient.EurekaServiceInstance) serviceInstance;
//                instanceIds.add(eurekaServiceInstance.getInstanceInfo().getInstanceId());
//            }
//        }
//
//        // 排序
//        instanceIds.sort(String::compareTo);
//
//        // 当前服务实例Id
//        String currentInstanceId = eurekaRegistration.getInstanceConfig().getInstanceId();
//
//        // 校验当前实例是否在集合中
//        if (instanceIds.size() <= 0 || !instanceIds.contains(currentInstanceId)) {
//            return null;
//        }
//
//        SimpleDistributeJobContext jobContext = new SimpleDistributeJobContext();
//        jobContext.setAllInstance(instanceIds);
//        jobContext.setCurrentInstance(currentInstanceId);
//        return jobContext;
//    }
//}

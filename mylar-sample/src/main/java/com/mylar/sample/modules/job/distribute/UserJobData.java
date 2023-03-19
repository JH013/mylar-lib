package com.mylar.sample.modules.job.distribute;

import com.mylar.lib.job.distribute.core.SimpleDistributeJobData;

/**
 * 会员任务数据
 *
 * @author wangz
 * @date 2023/3/19 0019 22:45
 */
public class UserJobData implements SimpleDistributeJobData {

    /**
     * 会员名
     *
     * @param memberName 会员名
     */
    public UserJobData(String memberName) {
        this.memberName = memberName;
    }

    /**
     * 会员名
     */
    private String memberName;

    /**
     * 获取唯一标识
     *
     * @return 唯一标识
     */
    @Override
    public String uniqueSign() {
        return this.memberName;
    }

    // region getter & setter

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    // endregion
}

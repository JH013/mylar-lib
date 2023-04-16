package com.mylar.lib.limiter.data;

/**
 * 限流参数校验结果
 *
 * @author wangz
 * @date 2023/4/17 0017 0:57
 */
public class RateLimitArgsVerifyResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 校验成功结果
     *
     * @return 实例
     */
    public static RateLimitArgsVerifyResult verifySuccess() {
        RateLimitArgsVerifyResult result = new RateLimitArgsVerifyResult();
        result.success = true;
        return result;
    }

    /**
     * 校验失败结果
     *
     * @param message 错误信息
     * @return 实例
     */
    public static RateLimitArgsVerifyResult verifyFailed(String message) {
        RateLimitArgsVerifyResult result = new RateLimitArgsVerifyResult();
        result.success = false;
        result.setMessage(message);
        return result;
    }

    // region getter & setter

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // endregion
}

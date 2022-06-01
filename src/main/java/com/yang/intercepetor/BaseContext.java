package com.yang.intercepetor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param loginId 员工id
     */
    public static void setCurrentId(String loginId) {
        log.info("当前设置setCurrentId线程id:{}", Thread.currentThread().getId());
        threadLocal.set(loginId);
    }

    /**
     * 获取值
     * @return 存在其中的值
     */
    public static String getCurrentId(){
        log.info("当前设置getCurrentId。。。线程id:{}", Thread.currentThread().getId());
        return threadLocal.get();
    }

    /**
     * 删除当前值
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }
}

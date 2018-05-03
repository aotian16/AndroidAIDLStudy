// ILoginAidlInterface.aidl
package com.qefee.androidaidlserver;

// Declare any non-default types here with import statements

interface ILoginAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
     * 自定义注册接口
     */
    String register(String name, String password);
    /**
     * 自定义登录接口
     */
    String login(String name, String password);
}

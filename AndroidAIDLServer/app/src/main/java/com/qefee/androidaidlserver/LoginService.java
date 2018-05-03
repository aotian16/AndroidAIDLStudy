package com.qefee.androidaidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginService extends Service {
    /**
     * log tag for LoginService
     */
    private static final String TAG = "LoginService";

    private Map<String, String> userMap = new HashMap<>();

    public LoginService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return loginBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    private IBinder loginBinder = new ILoginAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String register(String name, String password) throws RemoteException {
            int code;
            String msg;
            if (TextUtils.isEmpty(name)) {
                code = -1;
                msg = "用户名为空";
            } else if (TextUtils.isEmpty(password)) {
                code = -2;
                msg = "密码为空";
            } else if (userMap.containsKey(name)) {
                code = -3;
                msg = "用户已经被注册";
            } else {
                code = 0;
                msg = "注册成功";

                userMap.put(name, password);
            }
            JSONObject obj = new JSONObject();
            try {
                obj.put("code", code);
                obj.put("msg", msg);
            } catch (JSONException e) {
                throw new RemoteException("json化异常");
            }
            return obj.toString();
        }

        @Override
        public String login(String name, String password) throws RemoteException {
            int code;
            String msg;

            if (TextUtils.isEmpty(name)) {
                code = -1;
                msg = "用户名为空";
            } else if (TextUtils.isEmpty(password)) {
                code = -2;
                msg = "密码为空";
            } else if (!userMap.containsKey(name)) {
                code = -3;
                msg = "用户还未注册";
            } else {

                String pass = userMap.get(name);
                if (TextUtils.equals(pass, password)) {
                    code = 0;
                    msg = "登录成功";
                } else {
                    code = -4;
                    msg = "密码不正确";
                }
            }
            JSONObject obj = new JSONObject();
            try {
                obj.put("code", code);
                obj.put("msg", msg);
            } catch (JSONException e) {
                throw new RemoteException("json化异常");
            }
            return obj.toString();
        }
    };
}

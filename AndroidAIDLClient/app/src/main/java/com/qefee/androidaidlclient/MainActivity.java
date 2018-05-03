package com.qefee.androidaidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qefee.androidaidlserver.ILoginAidlInterface;

public class MainActivity extends AppCompatActivity {
    /**
     * log tag for MainActivity
     */
    private static final String TAG = "MainActivity";

    private ServiceConnection serviceConnection;
    private ILoginAidlInterface loginAidlInterface;
    private EditText et_name;
    private EditText et_password;
    private boolean bindService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::onClickFab);

        Button btn_bind = findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(this::onClickBind);

        Button btn_unbind = findViewById(R.id.btn_unbind);
        btn_unbind.setOnClickListener(this::onClickUnBind);

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this::onClickRegister);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this::onClickLogin);

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected: ");
                loginAidlInterface = ILoginAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected: ");
            }
        };
    }

    /**
     * 点击了绑定服务按钮
     *
     * @param view view
     */
    private void onClickBind(View view) {
        Intent intent = new Intent();
        intent.setPackage("com.qefee.androidaidlserver");
        intent.setAction("com.qefee.androidaidlserver.LoginService");
        intent.setComponent(new ComponentName("com.qefee.androidaidlserver", "com.qefee.androidaidlserver.LoginService"));
        bindService = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Log.i(TAG, String.format("onClickBind: bind = %b", bindService));
    }

    /**
     * 点击了解绑服务按钮
     *
     * @param view view
     */
    private void onClickUnBind(View view) {
        Log.i(TAG, "onClickUnBind: ");
        if (bindService) {
            unbindService(serviceConnection);
        }
    }

    private void onClickFab(View view) {
        Log.i(TAG, "onClickFab: ");
    }

    /**
     * 点击了注册按钮
     *
     * @param view view
     */
    private void onClickRegister(View view) {
        Log.i(TAG, "onClickRegister: ");
        if (loginAidlInterface != null) {
            String name = et_name.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            try {
                String json = loginAidlInterface.register(name, password);
                Snackbar.make(view, json, Snackbar.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(view, "还没有连接服务器", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击了登录按钮
     *
     * @param view view
     */
    private void onClickLogin(View view) {
        Log.i(TAG, "onClickLogin: ");
        if (loginAidlInterface != null) {
            String name = et_name.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            try {
                String json = loginAidlInterface.login(name, password);
                Snackbar.make(view, json, Snackbar.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(view, "还没有连接服务器", Snackbar.LENGTH_SHORT).show();
        }
    }
}

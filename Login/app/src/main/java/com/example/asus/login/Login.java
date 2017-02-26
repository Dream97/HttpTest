package com.example.asus.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asus.login.util.LoginUtil;

public class Login extends Activity {
    private  EditText id ;
    private  EditText pass;
    private ImageButton enter;
    private ImageButton regist;
    final int LOGIN_SUCCESSFUL = 1;

    public Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case LOGIN_SUCCESSFUL:
                    // 提示获取验证码成功
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case 304:
                    Toast.makeText(Login.this,String.valueOf(msg.obj),Toast.LENGTH_SHORT).show();
                    break;
                case 305:
                    Toast.makeText(Login.this,String.valueOf(msg.obj),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.pass);
        enter = (ImageButton) findViewById(R.id.enter);
        regist = (ImageButton) findViewById(R.id.register);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = id.toString();
                String password = pass.toString();
                new LoginUtil(phone,password,handler);
            }
        });

    }

}

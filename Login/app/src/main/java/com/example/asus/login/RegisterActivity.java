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

import com.example.asus.login.util.GetVerificationCodeUtil;
import com.example.asus.login.util.RegisterUtil;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by asus on 2017/2/23.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private EditText registerPhone;
    private EditText useName;
    private EditText registerpass;
    private EditText verificationCode;
    private ImageButton CodeBn;
    private ImageButton registBn;
    HttpClient httpClient;
    final int VERIFICATION_SUCCESSFUL = 1;
    //final int VERIFICATION_FAIL = 0;
    final int REGISTER_SUCCESSFUL = 2;
    //final int REGISTER_FAIL = 3;

    public Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case VERIFICATION_SUCCESSFUL:
                    // 提示获取验证码成功
                    Toast.makeText(RegisterActivity.this, "接受验证码中", Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_SUCCESSFUL:
                    //提示注册成功并关闭该Activity
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 201:
                    Intent intent = new Intent(RegisterActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                    break;
                case 300:
                    Toast.makeText(RegisterActivity.this,"获取验证码失败",Toast.LENGTH_SHORT).show();
                    break;
                case 301:
                    Toast.makeText(RegisterActivity.this,"手机号已被注册",Toast.LENGTH_SHORT).show();
                    break;
                case 302:
                    Toast.makeText(RegisterActivity.this,"用户名已被注册",Toast.LENGTH_SHORT).show();
                    break;
                case 303:
                    Toast.makeText(RegisterActivity.this,"验证码不正确或过期",Toast.LENGTH_SHORT).show();
                    break;
                case 304:
                    Toast.makeText(RegisterActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    break;
//                case 305:
//                    Toast.makeText(RegisterActivity.this,"新密码和旧密码相同",Toast.LENGTH_SHORT).show();
//                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerPhone = (EditText) findViewById(R.id.registphone);
        useName = (EditText) findViewById(R.id.registname);
        registerpass = (EditText) findViewById(R.id.registerpass);
        verificationCode = (EditText) findViewById(R.id.getVerificationCode);
        CodeBn = (ImageButton) findViewById(R.id.get);
        registBn = (ImageButton) findViewById(R.id.rgButton);
        httpClient = new DefaultHttpClient();

        CodeBn.setOnClickListener(this);//获取验证码监听
        registBn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.get:
                String phoneNumber = registerPhone.toString();
                //new Thread(new Runnable() {
                new GetVerificationCodeUtil(phoneNumber,handler);
                break;
            case R.id.rgButton:
                String phone = registerPhone.toString();
                String name = useName.toString();
                String password = registerpass.toString();
                String code = verificationCode.toString();
                new RegisterUtil(phone,name,password,code,handler);
                break;
            default:
                break;
        }
    }

}
package com.example.asus.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asus.login.util.PostUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {
    public String url = "http://120.77.16.36/api/";
    public String LOGIN = url+"auth/login";
    private  EditText id ;
    private  EditText pass;
    private ImageButton enter;
    private ImageButton regist;
    final int LOGIN_SUCCESSFUL = 1;
    public Map<String,String> maps;
    PostUtil postUtil ;
    private String result;

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
                String phone = id.getText().toString();
                String password = pass.getText().toString();
                maps = new HashMap<>();
                maps.put("phone",phone);
                maps.put("password",password);
                postUtil = new PostUtil();
                result = postUtil.postWithResult(LOGIN,maps);
                parseJSONWithJSONObject(result);
            }
        });

    }

    //解析JSON数据
    private void parseJSONWithJSONObject(String jsonData)
    {
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");
            String errmsg = jsonObject.getString("errmsg");

            Log.d("RegistActivity","status is "+status);
            Log.d("RegistActivity","errmsg is "+errmsg);

            if(status == 0)
            {
                int errcode = jsonObject.getInt("errcode");
                Log.d("RegistActivity", "errcode is " + errcode);
                Message message = new Message();
                message.what = errcode;
                handler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

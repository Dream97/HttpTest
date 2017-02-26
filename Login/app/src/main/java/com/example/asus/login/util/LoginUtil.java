package com.example.asus.login.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/2/26.
 */

public class LoginUtil {

    private  String phone;
    private String password;
    Handler handler;
    HttpClient httpClient = new DefaultHttpClient();
    int LOGIN_SUCCESSFUL = 1;


    public LoginUtil(final String phone, final String password, Handler handler) {
        this.phone = phone;
        this.password = password;
        this.handler = handler;

        new Thread() {
            @Override
            public void run()
            {
                try {
                    HttpPost post = new HttpPost("http://120.77.16.36/api/auth/login");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("phone", phone));
                    params.add(new BasicNameValuePair("password", password));
                    //设置参数
                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    //发送post请求
                    HttpResponse response = httpClient.execute(post);
                    if(response.getStatusLine().getStatusCode() == 200)
                    {
                        String msg = EntityUtils.toString(response.getEntity());
                        parseJSONWithJSONObject(msg);
                    }else {
                        Log.d("Login","连接服务器失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void parseJSONWithJSONObject(String jsonData)
    {
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");
            String errmsg = jsonObject.getString("errmsg");
            Message message = new Message();
            if(status == 1)
            {
                message.what = LOGIN_SUCCESSFUL;
                handler.sendMessage(message);
            }
            else{
                int errcode = jsonObject.getInt("errcode");
                message.what = errcode;
                message.obj = errmsg;
                handler.sendMessage(message);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

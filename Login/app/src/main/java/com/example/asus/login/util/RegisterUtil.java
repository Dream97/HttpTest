package com.example.asus.login.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
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

public class RegisterUtil {
    private String phone ;
    private String name ;
    private String password;
    private String code;
    private Handler handler;
    private HttpClient httpClient = new DefaultHttpClient();
    final int REGISTER_SUCCESSFUL = 2;
    public RegisterUtil(final String phone, final String name, final String password, final String code, Handler handler)
    {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.code = code;
        this.handler = handler;
        new Thread()
        {
            @Override
            public void run(){
                try{
                    HttpPost post = new HttpPost("http://120.77.16.36/api/auth/register");
                    //参数封装
                    List<BasicNameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("phone",phone));
                    params.add(new BasicNameValuePair("name",name));
                    params.add(new BasicNameValuePair("password",password));
                    params.add(new BasicNameValuePair("code",code));

                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    HttpResponse response = httpClient.execute(post);

                    //服务器做出响应
                    if(response.getStatusLine().getStatusCode() == 200)
                    {
                        String msg = EntityUtils.toString(response.getEntity());
                        //Looper.prepare();
                        parseJSONWithJSONObject(msg);//对数据进行分析
                        //Looper.loop();
                    }
                    else{
                        Log.d("RegisterUtil","连接服务器失败");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void parseJSONWithJSONObject(String jsonData)
    {
        try {
            JSONObject jsonObject  = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");
            String errmsg = jsonObject.getString("errmsg");

            Log.d("RegisterUtil","status is "+status);
            Log.d("RegisterUtil","errmsg is "+errmsg);

            if(status == 0)
            {
                int errcode  = jsonObject.getInt("errcode");
                Log.d("RegisterUtil","errcode is "+errcode);
                Message message = new Message();
                message.what = errcode;
                handler.sendMessage(message);
            }
            else{
                Message message = new Message();
                message.what = REGISTER_SUCCESSFUL;
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

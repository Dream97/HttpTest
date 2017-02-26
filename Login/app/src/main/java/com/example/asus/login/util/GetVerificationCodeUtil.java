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
 * Created by asus on 2017/2/25.
 */

public class GetVerificationCodeUtil {
    HttpClient httpClient  = new DefaultHttpClient();
    Handler handler;
    private  static String phone;
    public GetVerificationCodeUtil(final String phone, Handler handler) {
        this.phone = phone;
        this.handler = handler;
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpPost post = new HttpPost("http://120.77.16.36/api/auth/getVerificationCode");//③
                    // 如果传递参数个数比较多，可以对传递的参数进行封装
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair
                            ("phone", phone));
                    // 设置请求参数
                    post.setEntity(new UrlEncodedFormEntity(
                            params, HTTP.UTF_8));
                    // 发送POST请求
                    HttpResponse response = httpClient
                            .execute(post);  //④
                    // 如果服务器成功地返回响应

                    if (response.getStatusLine()
                            .getStatusCode() == 200) {
                        String msg = EntityUtils
                                .toString(response.getEntity());
                        //Looper.prepare();
                        parseJSONWithJSONObject(msg);
                        //Looper.loop();
                    } else {
                        Log.d("RegistActivity","连接服务器失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //}).start();
        }.start();
    }
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
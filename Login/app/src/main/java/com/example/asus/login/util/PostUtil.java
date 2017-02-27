package com.example.asus.login.util;

import android.os.Looper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by asus on 2017/2/27.
 */

public  class PostUtil {
    private static PostUtil instance = null;
    private HttpClient httpClient = getThreadSafeClient();//创建一个HttpClient
    public String result;

    //工具类的实例化
    public static PostUtil getInstance()
    {
        if (instance == null)
        {
            instance = new PostUtil();
        }
        return instance;
    }

    //增加线程安全的方法
    public DefaultHttpClient getThreadSafeClient()
    {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();

        client = new DefaultHttpClient(
                new ThreadSafeClientConnManager(params,mgr.getSchemeRegistry())
                ,params);
        return client;
    }
    public String postWithResult(final String url, final Map<String,String> maps)
    {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call(){
                        try{
                            HttpPost post = new HttpPost(url);
                            List<NameValuePair> params = new ArrayList<>();
                            for (String key:maps.keySet())
                            {
                                params.add(new BasicNameValuePair(key,maps.get(key)));
                            }
                            //设置属性
                            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            //发送POST请求
                            HttpResponse response = httpClient.execute(post);
                            if (response.getStatusLine().getStatusCode() ==200)
                            {
                                Looper.prepare();//消息循环？？其实我内心是拒绝的
                                result = EntityUtils.toString(response.getEntity());//获得JSON数据一份
                                return result;
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();//开始进行
        try {
            task.get();//我都说不懂FutureTask咯
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  result;
    }
}

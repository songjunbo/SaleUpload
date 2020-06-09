package com.pekon.saleupload;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();


		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				//其他配置
				.build();

		OkHttpUtils.initClient(okHttpClient);


		Hawk.init(this).build();
	}
}

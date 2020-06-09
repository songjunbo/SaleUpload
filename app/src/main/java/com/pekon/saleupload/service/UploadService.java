package com.pekon.saleupload.service;

import android.app.Service;
import android.app.TaskInfo;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.IBinder;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import com.orhanobut.hawk.Hawk;
import com.pekon.saleupload.asyncTasks.AsyncTaskGetData;
import com.pekon.saleupload.asyncTasks.AsyncTaskPutData;
import com.pekon.saleupload.interfaces.OnCallbackResult;
import com.pekon.saleupload.interfaces.ShareKey;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.util.CherryAESCoder;
import com.pekon.saleupload.util.DateUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import okhttp3.Call;

public class UploadService extends Service {


	private TimerTask timerTask;
	private Timer timer;
	private static final long perTime = 3 * 60 * 1000;  //轮询的时间


	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Hawk.put(ShareKey.firstLogin, false);  //开启服务了之后就不是首次登陆了
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timerTask = new TimerTask() {
			@Override
			public void run() {
				new AsyncTaskGetData(UploadService.this, new OnCallbackResult() {
					@Override
					public void OnSuccess() {
						Log.i("aaa", "插入数据库成功");
						new AsyncTaskPutData(UploadService.this).executeOnExecutor(Executors.newSingleThreadExecutor());
					}
				}).executeOnExecutor(Executors.newSingleThreadExecutor());
			}
		};
		timer = new Timer();
		timer.schedule(timerTask,0,perTime);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timerTask != null) {
			timerTask.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
	}

}

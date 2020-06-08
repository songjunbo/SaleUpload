package com.pekon.saleupload.service;

import android.app.Service;
import android.app.TaskInfo;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.IBinder;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

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
	public int onStartCommand(Intent intent, int flags, int startId) {
		timerTask = new TimerTask() {
			@Override
			public void run() {
				askPekonData();
			}
		};
		timer = new Timer();
		timer.schedule(timerTask,0,perTime);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	/**
	 * 请求数据
	 */
	private void askPekonData(){

		Map<String, String> map = new HashMap<>();
		map.put("TradeType", "GetSaleMainIncrement");
		map.put("CounterCode", "000001");  //柜台号
		map.put("CreateTime", DateUtil.getCurrentTime());
		map.put("DateSource", "Store");  //数据来源

		String param = null;
		try {
			param = CherryAESCoder.encrypt(obj2Json(map), BaseUrl.key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i("aaa", "url:" + BaseUrl.pekonUrl + "&paramData=" + param + "&mpos_id=1_AFU" + "&mpos_vcode=N");
		OkHttpUtils.get()
				.url(BaseUrl.pekonUrl)
				.addParams("paramData", param)
				.addParams("mpos_id", "1_AFU")
				.addParams("mpos_vcode", "N")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.i("aaa", "网络请求异常:" + e.toString());
					}

					@Override
					public void onResponse(String response, int id) {

					}
				});

	}



	public  String obj2Json(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		String dataLine = null;
		try {
			dataLine = objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			Log.i("aaa", "异常：" + e.toString());
		}
		// System.out.println(new SimpleDateFormat("yyyy:MM:dd hh:mm:ss.SSS").format((new Date()).getTime()));
		return dataLine;
	}

}

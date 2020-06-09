package com.pekon.saleupload.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.interfaces.OnCallbackResult;
import com.pekon.saleupload.interfaces.ShareKey;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.util.CherryAESCoder;
import com.pekon.saleupload.util.DateUtil;
import com.pekon.saleupload.util.JsonUtil;
import com.pekon.saleupload.view.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


/**
 * 获取销售数据
 */
public class AsyncTaskGetData extends AsyncTask<Void,Void,Boolean> {

	private Context context;
	private OnCallbackResult onCallbackResult;

	public AsyncTaskGetData(Context context, OnCallbackResult onCallbackResult) {
		this.context = context;
		this.onCallbackResult = onCallbackResult;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		boolean flag = false;
		String saleTime = Hawk.get(ShareKey.uoloadTime, "1970-01-01 00:00:00");
		Map<String, String> map = new HashMap<>();
		map.put("TradeType", "GetSaleMainIncrement");
		map.put("CounterCode", "000001");  //柜台号
		map.put("CreateTime", saleTime);
		map.put("DateSource", "Store");  //数据来源
		String param = "";
		try {
			param = CherryAESCoder.encrypt(JsonUtil.objectToJson(map), BaseUrl.key);
			Response response = OkHttpUtils.get()
					.url(BaseUrl.pekonUrl)
					.addParams("paramData", param)
					.addParams("appID", "witpos")
					.addParams("brandCode", "AFU")
					.build()
					.execute();
			String str = response.body().string();
			JSONObject root = new JSONObject(str);
			String errorCode = root.optString("ERRORCODE");
			if (!TextUtils.equals(errorCode, "0")) {
				return flag;
			}
			String resultContent = root.getString("ResultContent");
			if (TextUtils.isEmpty(resultContent)) {
				return flag;
			}
			String result = CherryAESCoder.decrypt(resultContent, BaseUrl.key);
			if (TextUtils.isEmpty(result)) {
				return flag;
			}
			List<MainOrderEntity> data = JsonUtil.jsonToList(result, MainOrderEntity.class);
			if (data != null && data.size() > 0) {
				//获取最后的销售时间
				MainOrderEntity mainOrderEntity = data.get(data.size() - 1);
				String saleTime1 = mainOrderEntity.getSaleTime();
				//存在获取的最新的销售时间
				Hawk.put(ShareKey.uoloadTime, saleTime1);
			}
			flag = new MainOrderDaoHelper(context).insertMainOrderList(data);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("aaa", "获取数据接口异常:" + e.toString());
		}
		return flag;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		super.onPostExecute(aBoolean);
		if (onCallbackResult == null) {
			return;
		}
		if (aBoolean) {
			onCallbackResult.OnSuccess();
		}

	}
}

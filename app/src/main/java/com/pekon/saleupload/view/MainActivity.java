package com.pekon.saleupload.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pekon.saleupload.R;
import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.util.CherryAESCoder;
import com.pekon.saleupload.util.DateUtil;
import com.pekon.saleupload.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {


	private Button btnSave;
	private Button btnUpload;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnSave = findViewById(R.id.btn_save);
		btnUpload = findViewById(R.id.btn_upload);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				askPekonData();
			}
		});
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainOrderEntity mainOrderEntity = new MainOrderDaoHelper(MainActivity.this).getUnUploadMainOrderEntity();
				if (mainOrderEntity == null) {
					return;
				}

				OkHttpUtils.post()
						.url(BaseUrl.upLoadUrl)
						.addParams("strCallUserCode", "fzself")
						.addParams("strCallPassword", "fz@gz123")
						.addParams("strStoreCode", "01")
						.addParams("strType", mainOrderEntity.getSaleType())
						.addParams("strSalesDate", mainOrderEntity.getSaleTime().split(" ")[0])
						.addParams("strSalesTime", mainOrderEntity.getSaleTime().split(" ")[1])
						.addParams("strSalesDocNo", mainOrderEntity.getBillCode())
						.addParams("strVipCode", "")
						.addParams("strTenderCode", "11")
						.addParams("strRemark", "")
						.addParams("strItems", "{01,100,100}")
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e, int id) {

								Log.i("aaa", "上传异常:" + e.toString());
							}

							@Override
							public void onResponse(String response, int id) {

								Log.i("aaa", "获取的上传数据:" + response);
							}
						});

			}
		});


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
		OkHttpUtils.get()
				.url(BaseUrl.pekonUrl)
				.addParams("paramData", param)
				.addParams("appID", "witpos")
				.addParams("brandCode", "AFU")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.i("aaa", "网络请求异常:" + e.toString());
					}

					@Override
					public void onResponse(String response, int id) {
						if (TextUtils.isEmpty(response)) {
							return;
						}
						Log.i("aaa", "--->" + response);
						try {
							JSONObject root = new JSONObject(response);
							String errorCode = root.optString("ERRORCODE");
							if (!TextUtils.equals(errorCode, "0")) {
								return;
							}
							String resultContent = root.getString("ResultContent");
							if (TextUtils.isEmpty(resultContent)) {
								return;
							}
							String result = CherryAESCoder.decrypt(resultContent, BaseUrl.key);
							if (TextUtils.isEmpty(result)) {
								return;
							}
							List<MainOrderEntity> data = JsonUtil.jsonToList(result, MainOrderEntity.class);
							new MainOrderDaoHelper(MainActivity.this).insertMainOrderList(data);
						} catch (Exception e) {
							Log.i("aaa", "获取的数据异常：" + e.toString());
							e.printStackTrace();
						}
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
		return dataLine;
	}

}

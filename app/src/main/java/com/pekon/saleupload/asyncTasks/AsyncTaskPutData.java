package com.pekon.saleupload.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.view.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class AsyncTaskPutData extends AsyncTask<Void,Void,Void> {

	private Context context;
	private MainOrderEntity mainOrderEntity;
	private int time; //上传的次数

	public AsyncTaskPutData(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... voids) {
		mainOrderEntity  = new MainOrderDaoHelper(context).getUnUploadMainOrderEntity();
		if (mainOrderEntity == null) {
			return null;
		}
		time = mainOrderEntity.getTimes(); //获取上传的次数
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
						updateMainOrderEntity();
					}

					@Override
					public void onResponse(String response, int id) {

						Log.i("aaa", "获取的上传数据:" + response);
						updateMainOrderEntity();
					}
				});
		return null;
	}

	/**
	 * 不管成功还是失败都需要更新单据的上传次数
	 */
	private void updateMainOrderEntity(){
		if (mainOrderEntity == null) {
			return;
		}
		time++;
		mainOrderEntity.setTimes(time);
		new MainOrderDaoHelper(context).updateMainOrderEntity(mainOrderEntity);
	}
}

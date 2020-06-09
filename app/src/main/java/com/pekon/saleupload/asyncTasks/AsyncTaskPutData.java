package com.pekon.saleupload.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.util.BaseUrl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


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
		try {
			time = mainOrderEntity.getTimes(); //获取上传的次数
			SoapObject soapObject = new SoapObject(BaseUrl.PACE, BaseUrl.W_NAME);
			soapObject.addProperty("strCallUserCode", "fzself");
			soapObject.addProperty("strCallPassword", "fz@gz123");
			soapObject.addProperty("strStoreCode", "A31910");
			soapObject.addProperty("strType", mainOrderEntity.getSaleType());
			soapObject.addProperty("strSalesDate", mainOrderEntity.getSaleTime().split(" ")[0].replace("-", ""));
			soapObject.addProperty("strSalesTime", mainOrderEntity.getSaleTime().split(" ")[1].replace(":", ""));
			soapObject.addProperty("strSalesDocNo", mainOrderEntity.getBillCode());
			soapObject.addProperty("strVipCode", "");
			soapObject.addProperty("strTenderCode", "{11,100,0,0}");
			soapObject.addProperty("strRemark", "");
			soapObject.addProperty("strItems", "{A3191001,100,100}");
			SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapSerializationEnvelope.setOutputSoapObject(soapObject);

			HttpTransportSE httpTransportSE = new HttpTransportSE(BaseUrl.SERVER_URL);
			httpTransportSE.call(BaseUrl.PACE + BaseUrl.W_NAME, soapSerializationEnvelope);
			SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
			Log.i("aaa", "--->上传数据成功");
		} catch (Exception e) {
			Log.e("aaa", "上传的数据异常:" + e.toString());
			e.printStackTrace();
		}
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

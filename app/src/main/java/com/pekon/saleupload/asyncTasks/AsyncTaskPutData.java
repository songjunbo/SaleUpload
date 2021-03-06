package com.pekon.saleupload.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.util.JsonUtil;
import com.pekon.saleupload.util.OkHttpUtil;
import java.net.URLEncoder;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class AsyncTaskPutData extends AsyncTask<Void,Void,Void> {

	private Context context;
	private int time; //上传的次数

	public AsyncTaskPutData(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... voids) {
		List<MainOrderEntity> data = new MainOrderDaoHelper(context).getAllUnUploadMainOrderEntity();
		//说明没有数据或者全部上传了
		if (data == null || data.size() == 0) {
			return null;
		}
		try {
			for (MainOrderEntity mainOrderEntity : data) {
				time = mainOrderEntity.getTimes(); //获取上传的次数
				while (time < 3) {
					Log.i("aaa", "上传了：" + mainOrderEntity.getBillCode() + "  次数：" + time);
					double amount = mainOrderEntity.getAmount();
					double quantity = mainOrderEntity.getQuantity();
					String strTenderCode = "{11," + amount + ",0,0}";
					String strItems = "{A3191001," + (int) quantity + "," + amount + "}";  //货品
					String strCallUserCode = URLEncoder.encode("fzself");
					String strCallPassword = URLEncoder.encode("fz@gz123");
					String strStoreCode = URLEncoder.encode("A31910");  //店铺号
					String strType = URLEncoder.encode(mainOrderEntity.getSaleType());
					String strSalesDate = URLEncoder.encode(mainOrderEntity.getSaleTime().split(" ")[0].replace("-", ""));
					String strSalesTime = URLEncoder.encode(mainOrderEntity.getSaleTime().split(" ")[1].replace(":", ""));
					String strSalesDocNo = URLEncoder.encode(mainOrderEntity.getBillCode());
					String strVipCode = URLEncoder.encode("");
					String strTenderCodeK = URLEncoder.encode(strTenderCode);
					String strRemark = URLEncoder.encode("");
					String strItemsK = URLEncoder.encode(strItems);

					String url = BaseUrl.SERVER_URL + "/PostSales?strCallUserCode=" + strCallUserCode
							+ "&strCallPassword=" + strCallPassword
							+ "&strStoreCode=" + strStoreCode
							+ "&strType=" + strType
							+ "&strSalesDate_YYYYMMDD=" + strSalesDate
							+ "&strSalesTime_HHMISS=" + strSalesTime
							+ "&strSalesDocNo=" + strSalesDocNo
							+ "&strVipCode=" + strVipCode
							+ "&strTenderCode=" + strTenderCodeK
							+ "&strRemark=" + strRemark
							+ "&strItems=" + strItemsK;
					String result = OkHttpUtil.get(url);

					if (TextUtils.isEmpty(result)) {
						updateMainOrderEntity(mainOrderEntity);
						break;
					}
					XmlToJson xmlToJson = new XmlToJson.Builder(result).build();
					String a = xmlToJson.toString();
					String errCode = JsonUtil.josnForUploadSale(a);
					if (TextUtils.equals(errCode, "-100")   //已经上传过了
							||TextUtils.equals(errCode, "0")) {  //上传成功
						mainOrderEntity.setStatus(1);
						updateMainOrderEntity(mainOrderEntity);
						break;
					}

					updateMainOrderEntity(mainOrderEntity);
				}
			}
		} catch (Exception e) {
			Log.e("aaa", "上传销售数据异常:" + e.toString());
			return null;
		}
			return null;
	}

	/**
	 * 不管成功还是失败都需要更新单据的上传次数
	 */
	private void updateMainOrderEntity(MainOrderEntity mainOrderEntity){
		if (mainOrderEntity == null) {
			return;
		}
		time++;
		mainOrderEntity.setTimes(time);
		new MainOrderDaoHelper(context).updateMainOrderEntity(mainOrderEntity);
	}
}

package com.pekon.saleupload.dao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pekon.saleupload.entity.DaoSession;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.entity.MainOrderEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 数据库的操作类
 */
public class MainOrderDaoHelper {

	private Context context;
	private DaoSession daoSession;
	private MainOrderEntityDao mainOrderEntityDao;

	public MainOrderDaoHelper(Context context) {
		this.context = context;
		daoSession = DaoSessionHelper.getInstance(context);
		mainOrderEntityDao = daoSession.getMainOrderEntityDao();
	}

	/**
	 * 插入数据库的数据
	 * @param data
	 */
	public void insertMainOrderList(List<MainOrderEntity> data){
		if (data == null || data.size() == 0) {
			return;
		}
		try {
			for (MainOrderEntity entity : data) {
				entity.setStatus(0); //默认状态是0  没有上传
				String saleType = entity.getSaleType();
				if (TextUtils.equals(saleType, "NS")) {
					entity.setSaleType("SA");
				}else {
					entity.setSaleType("SR");
				}
			}
			mainOrderEntityDao.insertOrReplaceInTx(data);
		} catch (Exception e) {
			Log.e("aaa", "插入数据库异常:" + e.toString());
		}

	}


	/**
	 * 获取未上传的主单数据
	 * @return
	 */
	public MainOrderEntity getUnUploadMainOrderEntity(){
		MainOrderEntity mainOrderEntity = null;
		QueryBuilder<MainOrderEntity> queryBuilder = mainOrderEntityDao.queryBuilder();
		//状态为0的就是未上传的数据
		queryBuilder.where(MainOrderEntityDao.Properties.Status.eq(0)).orderAsc(MainOrderEntityDao.Properties.SaleTime).limit(1);
		List<MainOrderEntity> lists = queryBuilder.build().forCurrentThread().list();
		if (lists != null && lists.size() != 0) {
			mainOrderEntity = lists.get(0);
		}
		return mainOrderEntity;
	}
}

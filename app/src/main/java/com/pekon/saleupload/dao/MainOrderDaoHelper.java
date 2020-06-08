package com.pekon.saleupload.dao;

import android.content.Context;
import android.util.Log;

import com.pekon.saleupload.entity.DaoSession;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.entity.MainOrderEntityDao;

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
			}
			mainOrderEntityDao.insertOrReplaceInTx(data);
		} catch (Exception e) {
			Log.e("aaa", "插入数据库异常:" + e.toString());
		}

	}
}

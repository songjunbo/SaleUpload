package com.pekon.saleupload.dao;

import android.content.Context;


import com.pekon.saleupload.entity.DaoMaster;
import com.pekon.saleupload.util.Constants;

import org.greenrobot.greendao.database.Database;

public class DBHelper extends DaoMaster.DevOpenHelper {

	public DBHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null);
	}
	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {
		//  2018-11-02  需要进行数据迁移更新的实体类 ，新增的不用加

	}

}

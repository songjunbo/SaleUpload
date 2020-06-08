package com.pekon.saleupload.dao;

import android.content.Context;


import com.pekon.saleupload.entity.DaoMaster;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.entity.MainOrderEntityDao;
import com.pekon.saleupload.util.Constants;

import org.greenrobot.greendao.database.Database;

public class DBHelper extends DaoMaster.DevOpenHelper {

	public DBHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null);
	}
	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {

		DBMigrationHelper.getInstance().migrate(db, MainOrderEntityDao.class);
	}

}

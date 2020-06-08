package com.pekon.saleupload.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pekon.saleupload.entity.DaoMaster;
import com.pekon.saleupload.entity.DaoSession;


public class DaoSessionHelper {

	public static DaoSession daoSession;

	public static DaoSession getInstance(Context context){
		if (daoSession == null){
			synchronized (DaoSessionHelper.class){
				if (daoSession == null){
					DatabaseContext databaseContext = new DatabaseContext(context);
					DBHelper dbHelper = new DBHelper(databaseContext);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					DaoMaster daoMaster = new DaoMaster(db);
					daoSession = daoMaster.newSession();
				}
			}
		}
		return daoSession;
	}

}

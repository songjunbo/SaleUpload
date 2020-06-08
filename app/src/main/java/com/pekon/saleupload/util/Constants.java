package com.pekon.saleupload.util;

import android.os.Environment;

public class Constants {

	/**
	 * DB
	 */
	// 数据库名
	public static final String DATABASE_NAME = "wpm.db";

	// SD卡路径
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	// android数据目录
	public static final String DATA_PATH = "/data/data/com.witpos.mobile";


	// 存储文件目录名
	public static final String APPLICATION_NAME = "com.pekon.saleupload".replace("com.", "").replace(".pekon", "").toUpperCase();


	// witpos的SD卡路径
	public static final String WITPOS_SD_PATH = SDCARD_PATH + "/" + APPLICATION_NAME;
	// witpos的系统路径
	public static final String WITPOS_DATA_PATH = DATA_PATH + "/" + APPLICATION_NAME;

	// witpos数据的实际存储位置
	public static final String WITPOS_PATH = existSdcard() ? WITPOS_SD_PATH : WITPOS_DATA_PATH;

	// 数据库路径
	public static final String DATABASE_PATH = WITPOS_PATH + "/" + ".system";

	// 数据库完整路径名
	public static final String DATABASE_PATHNAME = DATABASE_PATH + "/" + DATABASE_NAME;

	/**
	 * 判断sd卡是否安装
	 *
	 * @return
	 */
	public static boolean existSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}





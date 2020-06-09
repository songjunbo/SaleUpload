package com.pekon.saleupload.util;

import android.util.Log;

import java.io.File;

/**
 * 文件管理的工具类
 */
public class FileUtil {

	public static void delete(File file) {
		try {
			if(!file.exists())
				return;
			if(file.isFile() || file.list()==null) {
				file.delete();
			}else {
				File[] files = file.listFiles();
				for(File a:files) {
					delete(a);
				}
				file.delete();
			}
		} catch (Exception e) {
			Log.e("aaa", "删除异常:" + e.toString());
		}

	}

}

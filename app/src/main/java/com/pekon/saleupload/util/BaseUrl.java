package com.pekon.saleupload.util;

import android.net.Uri;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseUrl {

	public static final String pekonUrl = "http://192.168.102.88:8080/Pekonws/webservice/business";
	public static final String key = "RYzjBiIzcnkBsph8cL8U3g==";  //加密的密钥


	public static String appendParams(String url, Map<String, String> params)
	{
		if (url == null || params == null || params.isEmpty())
		{
			return url;
		}
		Uri.Builder builder = Uri.parse(url).buildUpon();
		Set<String> keys = params.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			builder.appendQueryParameter(key, params.get(key));
		}
		return builder.build().toString();
	}
}

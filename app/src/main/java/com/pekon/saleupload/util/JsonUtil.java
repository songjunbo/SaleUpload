package com.pekon.saleupload.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.std.DateSerializer;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;

/**
 * 解析的工具类
 */
public class JsonUtil {
	private static Gson mGson = null;

	// 定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 将对象转换成json字符串。
	 * <p>Title: pojoToJson</p>
	 * <p>Description: </p>
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data) {
		try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将json结果集转化为对象
	 *
	 * @param jsonData json数据
	 * @param beanType 对象中的object类型
	 * @return
	 */
	public static <T> T jsonToObj(String jsonData, Class<T> beanType) {
		try {
			T t = MAPPER.readValue(jsonData, beanType);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将json数据转换成pojo对象list
	 * <p>Title: jsonToList</p>
	 * <p>Description: </p>
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
		JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
		List<T> list = null;
		try {
			list = MAPPER.readValue(jsonData, javaType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list = (List<T>) parseJsonToBean(jsonData, beanType);
		}
		return list;
	}

	public static <T> T parseJsonToBean(String json, Class<T> tClass) {
		try {
			return getGson().fromJson(json, tClass);
		} catch (Exception e) {
			return null;
		}

	}

	public static Gson getGson() {
		if (mGson == null) {
			synchronized (JsonUtil.class) {
				if (mGson == null) {
					mGson = new GsonBuilder()
							.setDateFormat("yyyy-MM-dd")
							.setDateFormat("yyyy-MM-dd HH:mm:ss")
							.serializeNulls()//序列化null
							.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG)
							.create();
				}
			}
		}
		return mGson;
	}


	public static String formatObjectToJson(Object object) {
		return getGson().toJson(object);
	}

	public static Object toJSON(Object object) throws JSONException {
		if (object instanceof Map) {
			JSONObject json = new JSONObject();
			Map map = (Map) object;
			for (Object key : map.keySet()) {
				json.put(key.toString(), toJSON(map.get(key)));
			}
			return json;
		} else if (object instanceof Iterable) {
			JSONArray json = new JSONArray();
			for (Object value : ((Iterable) object)) {
				json.put(value);
			}
			return json;
		} else {
			if (object != null) {
				return object;
			} else {
				return "";
			}
		}
	}

	/**
	 * 上传销售单据后返回的json数据
	 * @param jsonString  json格式的数据
	 * @return
	 */
	public static String josnForUploadSale(String jsonString){
		String errorCode = "";
		if (TextUtils.isEmpty(jsonString)){
			return "-99";
		}
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject response = jsonObject.optJSONObject("Response");
			JSONObject result = response.optJSONObject("Result");
			errorCode = result.optString("ErrorCode");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("aaa","销售上传后的数据解析异常:" + e.toString());
		}
		return errorCode;
	}


}

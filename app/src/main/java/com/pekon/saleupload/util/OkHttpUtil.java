package com.pekon.saleupload.util;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 */
public class OkHttpUtil {

	private static final String TAG = OkHttpUtil.class.getSimpleName();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType TEXT_PLAIN = MediaType.parse("text/plain");
	public static final int DEFAULT_READ_TIMEOUT = 12000;
	public static final int DEFAULT_WRITE_TIMEOUT = 12000;
	public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
	private OkHttpClient mOkHttpClient;
	private List<OkHttpClient> mOkHttpClientList = new ArrayList<>();//各种不同连接时间的连接对象
	private static OkHttpUtil mInstance;
	public static final String EXCEPTION_MSG_CALL_CANCELED = "Call has been cancelled";
	private Call call;


	private OkHttpUtil() {
	}

	public static OkHttpUtil getInstance() {
		if (mInstance == null) {
			synchronized (OkHttpUtil.class) {
				if (mInstance == null) {
					mInstance = new OkHttpUtil();
				}
			}
		}
		return mInstance;
	}

	public void initClient() {
		mOkHttpClient = getOkHttpClient(DEFAULT_CONNECT_TIMEOUT);
	}

	public OkHttpClient newClient() {
		return newClient(DEFAULT_CONNECT_TIMEOUT);
	}

	public OkHttpClient newClient(long connectTimeout) {
		return newClient(DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, connectTimeout);
	}

	public OkHttpClient newClient(long readTimeout, long writeTimeout, long connectTimeout) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
		builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
		builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
		return builder.build();
	}



	public Request buildGetRequest(String url) {
		return new Request.Builder()
				.url(url)
				.build();
	}

	public Request buildGetRequest(String url, Object tag) {
		return new Request.Builder()
				.url(url)
				.tag(tag)
				.build();
	}

	public Request buildPostRequest(String url, String json) {
		return new Request.Builder()
				.url(url)
				.post(RequestBody.create(JSON, json))
				.build();
	}

    public Request buildPostRequest(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


	public Request buildPutRequest(String url, RequestBody requestBody, String userName, String password) {
		String credentials = Credentials.basic(userName,password);
		return new Request.Builder()
				.url(url)
				.addHeader("Authorization",credentials)
				.put(requestBody)
				.build();
	}


	public String doRequest(Request request, long connectTimeout) throws Exception {
		call = getOkHttpClient(connectTimeout).newCall(request);
		Response response = call.execute();
		if(call.isCanceled()){
			throw new RuntimeException(EXCEPTION_MSG_CALL_CANCELED);
		}
		ResponseBody responseBody = response.body();
		if (responseBody == null) {
			throw new RuntimeException("OkHttpUtil.doRequest(),response.Body() is null!");
		}
		String responseBodyString = responseBody.string();
		if (responseBodyString == null) {
			throw new RuntimeException("OkHttpUtil.doRequest(),responseBody.string() is null!");
		}
		return responseBodyString;
	}

	public String doPutRequest(Request request, long connectTimeout) throws Exception {
		call = getOkHttpClient(connectTimeout).newCall(request);
		Response response = call.execute();
		if(call.isCanceled()){
			throw new RuntimeException(EXCEPTION_MSG_CALL_CANCELED);
		}
		return response.code() + "";
	}

	public String getRequest(String url, long connectTimeout, Object tag) throws Exception {
		Request request = buildGetRequest(url,tag);
		return doRequest(request, connectTimeout);
	}

	public String getRequest(String url, long connectTimeout) throws Exception {
		Request request = buildGetRequest(url);
		return doRequest(request, connectTimeout);
	}

	public String postRequest(String url, String json) throws Exception {
		Request request = buildPostRequest(url, json);
		return doRequest(request, DEFAULT_CONNECT_TIMEOUT);
	}

    public String postRequest(String url, long connectTimeout, RequestBody requestBody) throws Exception {
        Request request = buildPostRequest(url, requestBody);
        return doRequest(request, connectTimeout);
    }


	public String putRequest(String url, long connectTimeout, RequestBody requestBody, String userName, String password) throws Exception {
		Request request = buildPutRequest(url, requestBody, userName, password);
		return doPutRequest(request, connectTimeout);
	}

	public OkHttpClient getOkHttpClient(long connectTimeout){
		OkHttpClient okHttpClient = null;
		for (OkHttpClient hasOkHttpClient : mOkHttpClientList) {
			if(hasOkHttpClient.connectTimeoutMillis() == connectTimeout){
				okHttpClient = hasOkHttpClient;
				break;
			}
		}
		if(okHttpClient == null){
			okHttpClient = newClient(connectTimeout);
			mOkHttpClientList.add(okHttpClient);
		}
		return okHttpClient;
	}

	public static String get(String url) throws Exception {
		return getInstance().getRequest(url, DEFAULT_CONNECT_TIMEOUT);
	}

	public static String get(String url, long connectTimeout, Object tag) throws Exception {
		return getInstance().getRequest(url, connectTimeout, tag);
	}


	public static String get(String url, long connectTimeout) throws Exception {
		return getInstance().getRequest(url, connectTimeout);
	}

	public static String post(String url, String json) throws Exception {
		return getInstance().postRequest(url, json);
	}

    public static String post(String url, long connectTimeout, RequestBody requestBody) throws Exception {
        return getInstance().postRequest(url, connectTimeout, requestBody);
    }


	public static String put(String url, long connectTimeout, RequestBody requestBody, String userName, String password) throws Exception {
		return getInstance().putRequest(url, connectTimeout, requestBody, userName, password);
	}

	//返回请求的call
	public Call getCall(){
		if (call != null) {
			return call;
		}else {
			return null;
		}

	}
}

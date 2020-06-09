package com.pekon.saleupload.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pekon.saleupload.R;
import com.pekon.saleupload.asyncTasks.AsyncTaskPutData;
import com.pekon.saleupload.dao.MainOrderDaoHelper;
import com.pekon.saleupload.entity.MainOrderEntity;
import com.pekon.saleupload.service.UploadService;
import com.pekon.saleupload.util.BaseUrl;
import com.pekon.saleupload.util.CherryAESCoder;
import com.pekon.saleupload.util.DateUtil;
import com.pekon.saleupload.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {


	private Button btnSave;
	private Button btnUpload;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnSave = findViewById(R.id.btn_save);
		btnUpload = findViewById(R.id.btn_upload);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				askPekonData();
			}
		});
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new AsyncTaskPutData(MainActivity.this).executeOnExecutor(Executors.newSingleThreadExecutor());
			}

		});

	}

}

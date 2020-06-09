package com.pekon.saleupload.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pekon.saleupload.R;
import com.pekon.saleupload.asyncTasks.AsyncTaskGetData;
import com.pekon.saleupload.asyncTasks.AsyncTaskPutData;
import com.pekon.saleupload.interfaces.OnCallbackResult;
import java.util.concurrent.Executors;



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
				new AsyncTaskGetData(MainActivity.this, new OnCallbackResult() {
					@Override
					public void OnSuccess() {

					}
				}).executeOnExecutor(Executors.newSingleThreadExecutor());
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

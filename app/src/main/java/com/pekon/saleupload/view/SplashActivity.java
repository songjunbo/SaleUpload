package com.pekon.saleupload.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.pekon.saleupload.R;
import com.pekon.saleupload.service.UploadService;
import com.pekon.saleupload.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


	private final int mRequestCode = 100;//权限请求码
	private List<String> mPermissionList = new ArrayList<>(); //未授权的权限

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			initPermission();
		} else {
			startMainActivity();
		}
	}

	/**
	 * 初始化权限
	 */
	private void initPermission() {
		mPermissionList.clear();
		//逐个判断你要的权限是否已经通过
		for (int i = 0; i < PermissionUtil.mPermissionArray.length; i++) {
			if (ContextCompat.checkSelfPermission(this, PermissionUtil.mPermissionArray[i]) != PackageManager.PERMISSION_GRANTED) {
				mPermissionList.add(PermissionUtil.mPermissionArray[i]);//添加还未授予的权限
			}
		}

		//申请权限
		if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
			ActivityCompat.requestPermissions(this, PermissionUtil.mPermissionArray, mRequestCode);
		}else{
			//说明权限都已经通过，可以做你想做的事情去
			startMainActivity();
		}

	}

	private void startMainActivity() {
//		Intent intent = new Intent(this, MainActivity.class);
//		startActivity(intent);
		Intent intent = new Intent(this, UploadService.class);
		startService(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}
		boolean hasPermissionDismiss = false;//有权限没有通过
		if (mRequestCode != requestCode) {
			return;
		}

		for (int i = 0; i < grantResults.length; i++) {
			if (grantResults[i] == -1) {
				hasPermissionDismiss = true;
			}
		}
		//如果有权限没有被允许
		if (hasPermissionDismiss) {
			new AlertDialog.Builder(this).setTitle("温馨提示").setCancelable(false).setMessage("未获得全部权限")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
						}
					}).show();
		}else{
			//全部权限通过，可以进行下一步操作。。。
			startMainActivity();

		}



	}
}

package com.prajitdas.sprivacy.policymanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;

public class DBOpsActivity extends Activity {
	private Button mBtnShowAllPolicies;
	private Button mBtnShowAllApplications;
	private Button mBtnShowAllProviders;
	private Button mBtnDelData;
	private Button mBtnLoadData;
	private PolicyDBHelper db;
	private SQLiteDatabase database;
	private PopupWindow popUp; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbops);
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		
		instantiateViews();
		addOnClickListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		database = db.getWritableDatabase();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	private void addOnClickListener() {
		mBtnShowAllPolicies.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllPoliciesActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnShowAllApplications.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllApplicationsActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnShowAllProviders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllProvidersActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnDelData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popUp.showAtLocation(v, Gravity.CENTER, 20, 20);
				popUp.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						db.deleteAllData(database);
					}
				});
			}
		});
		
		mBtnLoadData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.loadDefaultPoliciesIntoDB(database);
			}
		});
	}

	private void instantiateViews() {
		popUp = new PopupWindow(this);
		mBtnShowAllPolicies = (Button) findViewById(R.id.btnShowPols);
		mBtnShowAllApplications = (Button) findViewById(R.id.btnShowApps);
		mBtnShowAllProviders = (Button) findViewById(R.id.btnShowPros);
		mBtnDelData = (Button) findViewById(R.id.btnDelData);
		mBtnLoadData = (Button) findViewById(R.id.btnLdData);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbops, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

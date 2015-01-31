package com.example.biaqm;

import utils.UniversalFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class LuncherActivity extends Activity
{
	private int timeToMove = 3000;
	private TextView welcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luncher_activity);
		welcome = (TextView)findViewById(R.id.textView_loading);
//		UniversalFunctions.makeBlink(welcome, 500);
		Handler h=new Handler();
		h.postDelayed(new Runnable()
		{
			public void run()
			{
				startActivity(new Intent(LuncherActivity.this,LoginActivity.class));
				LuncherActivity.this.finish();
			}

		}, timeToMove);
	}



}

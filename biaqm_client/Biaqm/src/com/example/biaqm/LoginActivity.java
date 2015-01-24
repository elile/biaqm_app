package com.example.biaqm;
import intertnet_utils.Crud;

import java.net.InetAddress;

import model.User;
import utils.DataGlobal;
import utils.StoreObjects;
import utils.StoreSimpleType;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.ProgressBarIndeterminate;


public class LoginActivity extends Activity
{

	private static final String NO_CONNECT_TO_INTERNET = "המכשיר אינו מחובר לאינטרנט";
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String CHECK_BOX_USER_NAME_IS_CHECKED = "checkBoxUserNameIsChecked";
	private static final String CHECK_BOX_PASSWORD_IS_CHECKED = "checkBoxPasswordIsChecked";
	private EditText editTextUserName;
	private EditText editTextPassword;
	private CheckBox checkBoxUserName;
	private CheckBox checkBoxPassword;
	private TextView textViewWrongPasswordOrUserName;
	private ButtonRectangle buttonEnter;

	private String host = DataGlobal.host;

	private String serverUrlGetuser = host+"api/User/Getuser/";
	private String urlAuthenticate = "api/Users/Authenticate";

	private ProgressBarIndeterminate progressBarIndeterminate;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		initStaff();

		restoreState();

		buttonEnter.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				try 
				{
					progressBarIndeterminate.setVisibility(View.VISIBLE);

					if (validte()) 
					{
						if (isConnected()) 
						{
							new getToken().execute(host+urlAuthenticate);
							// all the flow move to on post
						} 
						else 
						{
							Toast.makeText(getApplicationContext(),	NO_CONNECT_TO_INTERNET,	Toast.LENGTH_LONG).show();
							progressBarIndeterminate.setVisibility(View.INVISIBLE);
						}
					} 
					else 
					{
						textViewWrongPasswordOrUserName.setVisibility(View.VISIBLE);
						progressBarIndeterminate.setVisibility(View.INVISIBLE);

					}

				} catch (Exception e) { progressBarIndeterminate.setVisibility(View.INVISIBLE);}
			}
		});
	}

	@Override
	protected void onResume()
	{
		progressBarIndeterminate.setVisibility(View.INVISIBLE);
		super.onStart();
	}

	private void initStaff() 
	{
		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		editTextPassword= (EditText) findViewById(R.id.editTextPassword);
		checkBoxUserName= (CheckBox) findViewById(R.id.checkBoxUserName);
		checkBoxPassword= (CheckBox) findViewById(R.id.checkBoxPassword);
		textViewWrongPasswordOrUserName= (TextView) findViewById(R.id.textViewWrongPasswordOrUserName);
		buttonEnter= (ButtonRectangle) findViewById(R.id.buttonEnter);

		progressBarIndeterminate = (ProgressBarIndeterminate) findViewById(R.id.progressBarIndeterminate);

		//		buttonEnter.setClickAfterRipple(false);
		//		buttonEnter.setRippleSpeed(20f);

	}


	private void restoreState() 
	{
		String pass = StoreSimpleType.getFromPreferences(this, CHECK_BOX_PASSWORD_IS_CHECKED);
		String userName = StoreSimpleType.getFromPreferences(this, CHECK_BOX_USER_NAME_IS_CHECKED);
		User uRetore = (User)StoreObjects.getFromPreferences(User.class, DataGlobal.CURRENT_USER, this);

		if (userName.compareTo(TRUE)==0 && pass.compareTo(TRUE)==0)
		{
			checkBoxUserName.setChecked(true);
			checkBoxPassword.setChecked(true);
			if (uRetore != null) 
			{
				editTextUserName.setText(uRetore.getLogin());
				editTextPassword.setText(uRetore.getPassword());
			}
		}
		else if (pass.compareTo(TRUE)==0)
		{
			checkBoxPassword.setChecked(true);
			if (uRetore != null) {
				editTextPassword.setText(uRetore.getPassword());
			}
		}
		else if (userName.compareTo(TRUE)==0)
		{
			checkBoxUserName.setChecked(true);
			if (uRetore != null) {
				editTextUserName.setText(uRetore.getLogin());
			}
		}
	}

	private void checkIfPasswordCheck() 
	{
		if (checkBoxPassword.isChecked()) 
		{
			StoreSimpleType.
			putInSharedPreferences(LoginActivity.this, CHECK_BOX_PASSWORD_IS_CHECKED, TRUE);
		}
		else {
			StoreSimpleType.
			putInSharedPreferences(LoginActivity.this, CHECK_BOX_PASSWORD_IS_CHECKED, FALSE);
		}
	}

	private void checkIfUserCheck() 
	{
		if (checkBoxUserName.isChecked()) 
		{
			StoreSimpleType.
			putInSharedPreferences(LoginActivity.this, CHECK_BOX_USER_NAME_IS_CHECKED, TRUE);
		}else {
			StoreSimpleType.
			putInSharedPreferences(LoginActivity.this, CHECK_BOX_USER_NAME_IS_CHECKED, FALSE);
		}
	}

	public class getToken extends AsyncTask<String, Integer, model.Status>
	{
		User user;
		@Override
		protected void onPreExecute() 
		{
			textViewWrongPasswordOrUserName.setVisibility(View.INVISIBLE);
			String userName = editTextUserName.getText().toString();
			String passName = editTextPassword.getText().toString();
			user = new User(userName, passName);
			super.onPreExecute();
		}
		@Override
		protected model.Status doInBackground(String... params) 
		{
			String url = params[0];
			return Crud.POST_For_Response(url, user, model.Status.class);
		}
		@Override
		protected void onPostExecute(model.Status st) 
		{
			if (st != null && st.isSuccesseded())
			{
				DataGlobal.TokenValue = st.getToken();
				try 
				{
					new checkIfUserExistAndGetHim().execute();
				} 
				catch (Exception e) 
				{
				} 

			}
			else 
			{
				textViewWrongPasswordOrUserName.setVisibility(View.VISIBLE);
			}
			progressBarIndeterminate.setVisibility(View.INVISIBLE);
			Log.e("eli", DataGlobal.TokenName + "  " +DataGlobal.TokenValue);
			super.onPostExecute(st);
		}
	}


	public class checkIfUserExistAndGetHim extends AsyncTask<String, Integer, User>
	{
		User user;

		@Override
		protected void onPreExecute() 
		{
			//			progressBarIndeterminate.setVisibility(View.VISIBLE);

			textViewWrongPasswordOrUserName.setVisibility(View.INVISIBLE);
			user = new User(editTextUserName.getText().toString(), editTextPassword.getText().toString());
			super.onPreExecute();
		}


		@Override
		protected User doInBackground(String... params) 
		{
			return (User)Crud.POST(serverUrlGetuser, user, User.class);
		}

		@Override
		protected void onPostExecute(User result) 
		{
			//			progressBarIndeterminate.setVisibility(View.INVISIBLE);
			progressBarIndeterminate.setVisibility(View.INVISIBLE);
			if (result==null) 
			{
				textViewWrongPasswordOrUserName.setVisibility(View.VISIBLE);
			}
			else 
			{
				// OK
				Log.e("eli", "ok");
				StoreObjects.putInSharedPreferences(result , DataGlobal.CURRENT_USER,LoginActivity.this);
				checkIfPasswordCheck();
				checkIfUserCheck();
				Intent iPreperd = new Intent(LoginActivity.this,ActionWriteActivity.class);
				iPreperd.putExtra("inWrite", "1");
				startActivity(iPreperd);
			}
			super.onPostExecute(result);
		}
	}

	private boolean validte() 
	{
		return !(editTextPassword.getText().toString().compareTo("")==0 || editTextPassword.getText().toString().compareTo("")==0);
	}

	public boolean isConnected()
	{
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;   
	}

	private boolean serverIsOnSendPing(String hostToCheck, int timeOutMillis) 
	{
		try 
		{// send ping to host
			return InetAddress.getByName(hostToCheck).isReachable(timeOutMillis);
		} 
		catch (Exception e) 
		{
			return true;
		} 
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();

	}
}

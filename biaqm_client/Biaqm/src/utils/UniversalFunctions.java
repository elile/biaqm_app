package utils;

import java.util.List;

import model.BaseSpinnerModel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biaqm.R;

public class UniversalFunctions 
{
	public static CharSequence getSmallHint(String text)
	{
		return Html.fromHtml("<small><small><small>" +text + "</small></small></small>");
	}

	public static void makeBlink(final View v, int period)
	{
		for (int i = 0; i < 10; i++) 
		{
			final int j = i;
			v.postDelayed(new Runnable() 
			{
				@Override
				public void run() 
				{
					if (j%2==1) v.setVisibility(View.VISIBLE);
					else v.setVisibility(View.INVISIBLE);
				}
			}, period*i);
		}
	}

	public static void copySpinners(Spinner from, Spinner to)
	{
		to.setAdapter(from.getAdapter());
	}

	public static void setSelectionSpinner(Spinner s, int pos) 
	{
		if (pos > 0 && 	pos < s.getAdapter().getCount()) 
		{
			setSelectionNoListen(s, pos);
		}
		else
		{
			setSelectionNoListen(s, 0);
		}
	}

	public static void setSelectionNoListen(Spinner s, int position)
	{
		if (s != null) {
			OnItemSelectedListener lis = s.getOnItemSelectedListener();
			s.setOnItemSelectedListener(null);
			s.setSelection(position, true);
			s.setOnItemSelectedListener(lis);
		}
	}

	public static  void startAnimForAdding(final View row)
	{
		if (row != null) {
			AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(500);
			anim.setRepeatCount(0);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					row.setEnabled(false);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					row.setEnabled(true);
				}
			});
			row.startAnimation(anim);
		}
	}

	public static  void startAnimForRemove(final View row, final LinearLayout wraper)
	{
		if (row != null && wraper!= null) 
		{
			AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
			anim.setDuration(500);
			anim.setRepeatCount(0);
			anim.setRepeatMode(Animation.REVERSE);
			row.startAnimation(anim);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					row.setEnabled(false);
					wraper.setEnabled(false);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					//				row.setEnabled(true);
					wraper.setEnabled(true);

					wraper.removeView(row);
				}
			});
		}
	}

	public static void BuildAdapterRegular(Context c ,Spinner spinner, List<String> listForSpinner)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, R.layout.spinner_custom_textview, listForSpinner);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	public static void myToast(Activity a, String s, int color)
	{
		LayoutInflater inflater = a.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup)a.findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.custom_toast_text);
		text.setText(s);
		text.setBackgroundColor(color);
		Toast toast = new Toast(a.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		//		if (color != Color.CYAN) 
		//			toast.setDuration(Toast.LENGTH_LONG);
		//		else 
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}


	public static Object getFromSpinner(Spinner spin, Object[] o) 
	{
		int s = spin.getSelectedItemPosition();
		if (s > 0)
		{
			return o[s-1];
		}
		return null;
	}

	public static Builder showDialog(Context c, String tilte, String text ) 
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(c);
		adb.setTitle(tilte );
		adb.setMessage(text);
		adb.setIcon(android.R.drawable.ic_dialog_info);
		return adb;
	}


	static ProgressDialog progressDialog;

	public static void showProgressBar(Context context)
	{
		stopProgressBar() ;// for mutli instance cancel
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(context.getString(R.string.loading));
		progressDialog.show();
	}

	public static void stopProgressBar() 
	{
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}

	public static void showPopAp(Context c, final View v)
	{
		if (StoreObjects.getFromPreferences(Boolean.class, "showTutorial", c) == null) 
		{
			StoreObjects.putInSharedPreferences(Boolean.valueOf(false), "showTutorial", c);
			LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.popup_window, null);
			final PopupWindow popupWindow = new PopupWindow(popupView,	LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			popupWindow.setOutsideTouchable(true);
			popupWindow.setTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
			popupWindow.setTouchInterceptor(new OnTouchListener() 
			{ 
				@Override public boolean onTouch(View v, MotionEvent event) 
				{
					popupWindow.dismiss(); 
					return false; 
				} 
			}); 
			v.postDelayed(new Runnable() 
			{
				@Override
				public void run()
				{
					popupWindow.showAsDropDown(v);
				}
			}, 1000);
			popupView.postDelayed(new Runnable() 
			{
				public void run()
				{
					popupWindow.dismiss();
				}
			}, 10000);
		}
	}
	
	public static long getIdFromSpinner(Spinner s, int selectionInSpinner, BaseSpinnerModel[] spinnerArray)
	{
		if (selectionInSpinner == 0)
		{
			return -1;
		}
		String selectedName = s.getItemAtPosition(selectionInSpinner).toString();
		for (BaseSpinnerModel bsm : spinnerArray) 
		{
			if (TextUtils.equals(selectedName, bsm.getSpinnerName()))
			{
				return bsm.getIdBase();
			}
		}
		return -1;
	}
	
	public static void setSpinnerSelectionOnId(Spinner s, long id, BaseSpinnerModel[] spinnerArray)
	{
		for (BaseSpinnerModel bsm : spinnerArray)
		{
			if (id == bsm.getIdBase())
			{
				int k = s.getAdapter().getCount();
				for (int i = 0; i < k; i++) 
				{
					String selectedName = s.getItemAtPosition(i).toString();
					if (TextUtils.equals(selectedName, bsm.getSpinnerName())) 
					{
						UniversalFunctions.setSelectionSpinner(s, i);
						break;
					}

				}
			}
		}
	}
	
	
}

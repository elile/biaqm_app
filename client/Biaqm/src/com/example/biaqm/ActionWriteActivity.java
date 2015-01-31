package com.example.biaqm;

import intertnet_utils.Crud;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import menu.MenuFragment;
import model.ActivityGroups;
import model.ActivityType;
import model.Block;
import model.Crop;
import model.Farm;
import model.InsertActivityFromPost;
import model.MachineAdvanceResult;
import model.MachineToInsert;
import model.Plot;
import model.User;
import model.Variety;
import model.Worker;
import model.WorkerAdvancedResult;
import model.WorkerToInsert;
import model.motoring_machinery;
import sub_screens.MachineAdvanced;
import sub_screens.WorkerAdvanced;
import utils.DataGlobal;
import utils.StoreObjects;
import utils.UniversalFunctions;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.slidinglayer.SlidingLayer;

import design_patterns.mediator.ColegaSpinner;
import design_patterns.mediator.MediatorImplement;

@SuppressLint("SimpleDateFormat")
public class ActionWriteActivity extends FragmentActivity  
implements OnClickListener, OnRefreshListener<ScrollView>
{

	private static final int WORKER = 1;
	private static final int MACHINE = 2;

	private LinearLayout wraper_adding_machinery, wraper_adding_worker;
	//	private boolean isBuildFarmFinish/*, isBuildScreenFinish*/, isBuildWorkersFinish, isBuildmotoring_machinerysFinish, isInRefreshProcess;
	private Spinner spinnerThatLunchFrom;
	private EditText editTextThatLunchFrom;
	private ImageView imgViewViThatLunchFrom;
	private Button buttonNoreThatLunchFrom;

	private User currentConnectedUser;

	private String host = DataGlobal.host;
	private String UrlGetActivityTypes;
	private String UrlGetBlocks;
	private String UrlGetPlots;
	private String UrlGetCrops;
	private String UrlGetVarietys;
	private String UrlGetWorker;
	private String UrlGetMachinery;
	private String UrlGetActivityGroups;
	private String UrlGetFarms;

	private Spinner spinner_activity_type, spinner_block, spinner_plot, 
	spinner_crop, spinner_variety, spinner_worker, 
	spinner_motoring_machinery, spinner_ActivityGroups, spinner_farm;

	private ButtonFlat buttonDateChange, buttonClear, button_ok /*buttonTimeChange, button_main_menu*/;

	private TextView textView_title, /*editTextHourCount,*/ editTextRemark;

	private int mYear, mMonth, mDay, mHour, mMinute;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID= 1;


	private ActivityGroups[] activityGroups;
	private ActivityType[] activityTypes;
	private Block[] blocks;
	private Plot[] plots;
	private Crop[] crops;
	private Variety[] varietys;
	private Worker[] workers; 
	private motoring_machinery[] motoring_machinerys;
	private Farm[] farms;
	//	private Trailing_machine[] Trailing_machines;

	private SimpleDateFormat sdf;
	private String StrDateSelect;
	private boolean inWrite = false;
	private String defultValueForSpinner;

	private ProgressDialog progressDialog;
	private PullToRefreshScrollView pullToRefreshView;

	//	private int prev_select_spinner_activity_type, prev_select_spinner_activity_group,
	//	prev_select_spinner_block,	/*prev_select_spinner_Trailing_machine,*/ 
	//	prev_select_spinner_motoring_machinery,	prev_select_spinner_crop,
	//	prev_select_spinner_variety,prev_select_spinner_worker,prev_select_spinner_plot, prev_select_spinner_farm;
	private Button btn_more_worker, btn_add_worker, btn_more_motoring, btn_add_motoring;

	private ArrayList<View> extraAddWorkers;
	private ArrayList<View> extraAddMachines;

	private ArrayList<WorkerAdvancedResult> extraWorkersResult;
	private ArrayList<MachineAdvanceResult> extraMachinesResult;

	EditText editText_worker, editText_motoring_machinery;
	private ImageView imageView_motoring_vi;
	private ImageView imageView_worker_vi;
	private RelativeLayout main_warper;

	private boolean isFromUser = true;
	private int prevPositionOfspinner_activity_type;
	private ProgressBarIndeterminate progressBarIndeterminate;
	private SlidingLayer mSlidingLayer;
	private ImageButton close_open_menu;
	private MediatorImplement mediator;
	private ColegaSpinner colActivity;
	private ColegaSpinner colActivityGroup;
	private LinearLayout wraper_sector;
	private ColegaSpinner colBlock;
	private ColegaSpinner colPlot;
	private ColegaSpinner colFarm;
	private ColegaSpinner colCrop;
	private ColegaSpinner colVariety;





	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.action_write_activity);

		initSlidingMenu();

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Loading....");

		Intent i = getIntent();
		String extra = i.getStringExtra("inWrite");
		inWrite = extra.compareTo("1")==0;

		currentConnectedUser = (User) StoreObjects.getFromPreferences(User.class, DataGlobal.CURRENT_USER, this);

		init();
		initMediator();
		// the new factory method of getting data from web
		//				new AsyncGetFrom("url").with(this).setMethod(Method.POST).setObjectToPost(new Object()).setTypeOfResponse(Block[].class).doOnResponse(new OnResponse()
		//				{
		//					@Override
		//					public <T> void onResponse(T response) 
		//					{
		//						
		//					}
		//				}).execute();
		new BuildFarmsFromWeb().execute();

	}

	private void buildUrlForFarmsGet() 
	{
		UrlGetFarms = host + DataGlobal.UrlGetFarms;
		UrlGetFarms = String.format(UrlGetFarms, currentConnectedUser.getCompany_id()+"");
	}

	public class BuildFarmsFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); 
			setProgressBarIndeterminateVisibility(true); 
			buildUrlForFarmsGet();
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			farms = Crud.GET(UrlGetFarms, Farm[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);

			colFarm.setArray(farms);
			buildAdapterToFarms();
			if (farms.length == 1)
			{
				spinner_farm.setSelection(1);
				wraper_sector.setVisibility(View.GONE);
			}
			//			isBuildFarmFinish = true;
			//			if (isBuildWorkersFinish && isBuildmotoring_machinerysFinish && isBuildScreenFinish) 
			//			{
			//				pullToRefreshView.onRefreshComplete();
			//				isInRefreshProcess = false;
			//				prevSpinnersSelectionRestoreInstance();
			//			}
			super.onPostExecute(result);
		}


	}

	private void buildAdapterToFarms()
	{
		spinner_farm.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{
				clearSpinnersAndExtra();

				if (position > 0)
				{
					Farm f = farms[position-1];
					currentConnectedUser.setFarm_id(f.getId());
					StoreObjects.putInSharedPreferences(currentConnectedUser, DataGlobal.CURRENT_USER, ActionWriteActivity.this);

					new BuildScreenFromWeb().execute(); //ActivityGroups , ActivityTypes, plots, blocks, Crops
					new BuildWorkersFromWeb().execute();
					new Buildmotoring_machinerysFromWeb().execute();
				}
				else
				{
					// toast farm cant be null

					// NOT WORK
					//					String[] dataDumyList = {""};
					//					ArrayAdapter<String> dummyAdapter = new ArrayAdapter<String>(ActionWriteActivity.this,R.layout.spinner_custom_textview, dataDumyList);
					//					dummyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					//					spinner_ActivityGroups.setAdapter(dummyAdapter);
					//					spinner_activity_type.setAdapter(dummyAdapter);
					//					spinner_block.setAdapter(dummyAdapter);
					//					spinner_plot.setAdapter(dummyAdapter);
					//					spinner_crop.setAdapter(dummyAdapter);
					//					spinner_variety.setAdapter(dummyAdapter);
					//					spinner_worker.setAdapter(dummyAdapter);
					//					spinner_motoring_machinery.setAdapter(dummyAdapter);
				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	private void initMediator() 
	{
		mediator = new MediatorImplement();

		colActivity = new ColegaSpinner(mediator, DataGlobal.SPINNER_ACTIVITY_NAME, spinner_activity_type,this);
		colActivityGroup = new ColegaSpinner(mediator, DataGlobal.SPINNER_ACTIVITY_GROUP_NAME, spinner_ActivityGroups, this);	
		colBlock = new ColegaSpinner(mediator, DataGlobal.SPINNER_BLOCK_NAME, spinner_block, this);	
		colPlot = new ColegaSpinner(mediator, DataGlobal.SPINNER_PLOT_NAME, spinner_plot, this);	
		colFarm = new ColegaSpinner(mediator, DataGlobal.SPINNER_FARM_NAME, spinner_farm, this);
		colCrop = new ColegaSpinner(mediator, DataGlobal.SPINNER_CROP_NAME, spinner_crop, this);
		colVariety = new ColegaSpinner(mediator, DataGlobal.SPINNER_VERIETY_NAME, spinner_variety, this);

		mediator.addColleague(colActivity);
		mediator.addColleague(colActivityGroup);
		mediator.addColleague(colBlock);
		mediator.addColleague(colPlot);
		mediator.addColleague(colFarm);
		mediator.addColleague(colCrop);
		mediator.addColleague(colVariety);


	}

	private void initSlidingMenu() 
	{
		mSlidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);
		MenuFragment menuFragment = new MenuFragment();
		menuFragment.setSlidingLayer(mSlidingLayer);
		getSupportFragmentManager().beginTransaction().replace(R.id.slidingLayer, menuFragment).commit();
		LayoutParams rlp = (LayoutParams) mSlidingLayer.getLayoutParams();
		rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mSlidingLayer.setLayoutParams(rlp);
		mSlidingLayer.setShadowWidth(0);
		mSlidingLayer.setShadowDrawable(null);
		mSlidingLayer.setOffsetWidth(0);
		mSlidingLayer.setCloseOnTapEnabled(true);
		mSlidingLayer.setOpenOnTapEnabled(false);
		mSlidingLayer.openLayer(true);
		close_open_menu = (ImageButton)findViewById(R.id.close_open_menu);
		close_open_menu.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if (mSlidingLayer.isOpened())
				{
					mSlidingLayer.closeLayer(true);
				}
				else 
				{
					mSlidingLayer.openLayer(true);
				}
			}
		});
	}

	private void setProgressBarIndeterminate(int visible) 
	{
		if (progressBarIndeterminate.getVisibility() != visible)
		{
			progressBarIndeterminate.setVisibility(visible);
		}
	}


	private void init() 
	{
		progressBarIndeterminate = (ProgressBarIndeterminate) findViewById(R.id.progressBarIndeterminate);

		extraWorkersResult = new ArrayList<WorkerAdvancedResult>();
		extraMachinesResult = new ArrayList<MachineAdvanceResult>();

		extraAddWorkers = new ArrayList<View>();
		extraAddMachines = new ArrayList<View>();

		wraper_sector = (LinearLayout)findViewById(R.id.wraper_sector);
		spinner_farm = (Spinner)findViewById(R.id.spinner_sector);
		main_warper = (RelativeLayout)findViewById(R.id.main_warper);

		imageView_motoring_vi = (ImageView)findViewById(R.id.imageView_motoring_vi);
		imageView_worker_vi = (ImageView)findViewById(R.id.imageView_worker_vi);
		imageView_motoring_vi.setVisibility(View.GONE);
		imageView_worker_vi.setVisibility(View.GONE); 

		editText_worker = (EditText) findViewById(R.id.editText_worker);
		editText_motoring_machinery= (EditText) findViewById(R.id.editText_motoring_machinery);

		wraper_adding_machinery = (LinearLayout) findViewById(R.id.wraper_adding_machinery);
		wraper_adding_worker = (LinearLayout) findViewById(R.id.wraper_adding_worker);

		btn_add_motoring=(Button)findViewById(R.id.btn_add_motoring);
		btn_add_worker=(Button)findViewById(R.id.btn_add_worker);
		btn_more_worker=(Button)findViewById(R.id.btn_more_worker);
		btn_more_motoring=(Button)findViewById(R.id.btn_more_motoring);

		pullToRefreshView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshView);
		pullToRefreshView.setOnRefreshListener(this);
		defultValueForSpinner = getString(R.string.select);
		Date theSelectedDate = new Date();
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		StrDateSelect = sdf.format(theSelectedDate);

		//		editTextHourCount = (TextView) findViewById(R.id.editTextHourCount);
		editTextRemark = (TextView) findViewById(R.id.editTextRemark);
		buttonDateChange = (ButtonFlat) findViewById(R.id.buttonDateChange);
		buttonClear = (ButtonFlat) findViewById(R.id.buttonClear);

		textView_title = (TextView) findViewById(R.id.textView_title);
		//		buttonTimeChange = (Button) findViewById(R.id.buttonTimeChange);
		//		spinner_Trailing_machine = (Spinner) findViewById(R.id.spinner_Trailing_machine);
		//		buttonTimeChange.setOnClickListener(this);

		spinner_worker = (Spinner)findViewById(R.id.spinner_worker);

		spinner_motoring_machinery = (Spinner)findViewById(R.id.spinner_motoring_machinery);
		spinner_crop = (Spinner) findViewById(R.id.spinner_crop);
		spinner_ActivityGroups = (Spinner)findViewById(R.id.spinner_group_activity_type);
		spinner_variety = (Spinner) findViewById(R.id.spinner_variety);
		spinner_activity_type = (Spinner) findViewById(R.id.spinner_activity_type);
		spinner_block = (Spinner) findViewById(R.id.spinner_block);
		spinner_plot = (Spinner) findViewById(R.id.spinner_plot);

		buttonDateChange.setOnClickListener(this);
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		//		buttonTimeChange.append("\n"+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
		buttonDateChange.setText(buttonDateChange.getTextView().getText() +"\n"+mDay+"-"+(mMonth+1)+"-"+mYear);
		buttonDateChange.setRippleSpeed(80f);
		buttonDateChange.getTextView().setTextSize(12);
		buttonDateChange.getTextView().setTextColor(Color.WHITE);
		buttonClear.setRippleSpeed(80f);
		buttonClear.getTextView().setTextColor(Color.WHITE);
		//		button_main_menu = (Button) findViewById(R.id.button_main_menu); 
		button_ok = (ButtonFlat) findViewById(R.id.button_ok);
		button_ok.setRippleSpeed(80f);
		button_ok.getTextView().setTextColor(Color.WHITE);


		button_ok.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
		//		button_main_menu.setOnClickListener(this);

		btn_add_worker.setOnClickListener(new addWorkerBtnListener());
		btn_add_motoring.setOnClickListener(new addMachineBtnListener());

		btn_more_worker.setOnClickListener(new startWorkerAdvancedListener(spinner_worker, editText_worker, imageView_worker_vi));
		btn_more_worker.setOnLongClickListener(new longClickCancel(imageView_worker_vi, spinner_worker, true));

		btn_more_motoring.setOnClickListener(new startMachineAdvancedListener(spinner_motoring_machinery, editText_motoring_machinery, imageView_motoring_vi));
		btn_more_motoring.setOnLongClickListener(new longClickCancel(imageView_motoring_vi, spinner_motoring_machinery, false));

		editText_worker.setHint(UniversalFunctions.getSmallHint( getString(R.string.hour)));
		editText_motoring_machinery.setHint(UniversalFunctions.getSmallHint(getString(R.string.hour)));

		editText_worker.addTextChangedListener(new TextChangeListenerToSyncAdvanced(editText_worker, btn_more_worker, WORKER));
		editText_motoring_machinery.addTextChangedListener(new TextChangeListenerToSyncAdvanced(editText_motoring_machinery, btn_more_motoring, MACHINE));


		if (!inWrite)
		{
			textView_title.setText(getString(R.string.action_preperd));
		}
	}




	public boolean isInWrite()
	{
		return inWrite;
	}

	public void setInWrite(boolean inWrite) 
	{
		this.inWrite = inWrite;
		if (!inWrite)
		{
			textView_title.setText(getString(R.string.action_preperd));
		}
		else 
		{
			textView_title.setText(getString(R.string.action_write));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.buttonDateChange:
			showDialog(DATE_DIALOG_ID);
			break;
			//		case R.id.buttonTimeChange:
			//			showDialog(TIME_DIALOG_ID);
			//			break;
		case R.id.button_ok:
			//						insertToServerOnWrite();
			insertCheck();

			break;
			//		case R.id.button_main_menu:
			//			startActivity(new Intent(this, MenuActivity.class));
			//			break;
		case R.id.buttonClear:
			clearSpinnersAndExtra();
			break;
		}		
	}


	private void clearSpinnersAndExtra() 
	{
		UniversalFunctions.setSelectionSpinner(spinner_ActivityGroups, 0);
		UniversalFunctions.setSelectionSpinner(spinner_activity_type, 0);
		UniversalFunctions.setSelectionSpinner(spinner_block, 0);
		UniversalFunctions.setSelectionSpinner(spinner_plot, 0);
		UniversalFunctions.setSelectionSpinner(spinner_crop, 0);
		UniversalFunctions.setSelectionSpinner(spinner_variety, 0);
		UniversalFunctions.setSelectionSpinner(spinner_worker, 0);
		UniversalFunctions.setSelectionSpinner(spinner_motoring_machinery, 0);

		extraAddWorkers.clear();
		extraAddMachines.clear();
		wraper_adding_machinery.removeAllViews();
		wraper_adding_worker.removeAllViews();

		editTextRemark.setText("");

		btn_more_worker.setTag(null);
		btn_more_motoring.setTag(null);

		// back to today?
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		buttonDateChange.setText(getString(R.string.change_date)+"\n"+mDay+"-"+(mMonth+1)+"-"+mYear);

		imageView_motoring_vi.setVisibility(View.GONE);
		imageView_worker_vi.setVisibility(View.GONE); 

		editText_worker.setText("");
		editText_motoring_machinery.setText("");
	}



	private void insertCheck() 
	{
		ActivityType activity_type_select = spinner_activity_type.getSelectedItemPosition()>0 ? activityTypes[spinner_activity_type.getSelectedItemPosition()-1]: null;
		Block block_select =spinner_block.getSelectedItemPosition() >0 ?blocks[spinner_block.getSelectedItemPosition()-1]: null;
		Plot plot_select = spinner_plot.getSelectedItemPosition()>0 ?plots[spinner_plot.getSelectedItemPosition()-1]: null;
		Crop crop_selected = spinner_crop.getSelectedItemPosition()>0 ?crops[spinner_crop.getSelectedItemPosition()-1]: null;
		Variety Variety_selected =spinner_variety.getSelectedItemPosition() >0 ?varietys[spinner_variety.getSelectedItemPosition()-1]: null;
		Worker worker_select =spinner_worker.getSelectedItemPosition() >0 ?workers[spinner_worker.getSelectedItemPosition()-1]: null;
		motoring_machinery motoring_machinery_select = spinner_motoring_machinery.getSelectedItemPosition()>0 ?motoring_machinerys[spinner_motoring_machinery.getSelectedItemPosition()-1]: null;

		ArrayList<WorkerToInsert> WorkersListToInsert = new ArrayList<WorkerToInsert>();
		ArrayList<MachineToInsert> MachinesListToInsert = new ArrayList<MachineToInsert>();

		String hourOne =  editText_worker.getText().toString().compareTo("")==0 ? "0" : editText_worker.getText().toString();
		String hourTwo =  editText_motoring_machinery.getText().toString().compareTo("")==0 ? "0" : editText_motoring_machinery.getText().toString();

		if (worker_select != null) 
		{
			WorkerToInsert wToInsert= new WorkerToInsert(worker_select.getId(), hourOne);
			wToInsert.setWorkerAdvance(getWorkerAdvanceExtraById(worker_select.getId()));
			WorkersListToInsert.add(wToInsert);
		}
		if (motoring_machinery_select != null) 
		{
			MachineToInsert mToInsert = new MachineToInsert(motoring_machinery_select.getId(), hourTwo);
			mToInsert.setMachineAdvanceResult(getMachineAdvanceExtraById(motoring_machinery_select.getId()));
			MachinesListToInsert.add(mToInsert);
		}
		for (View v : extraAddWorkers)
		{
			Button b_more = (Button) v.findViewById(R.id.btn_more);
			if (b_more.getTag() != null)
			{
				WorkerAdvancedResult wres = (WorkerAdvancedResult)b_more.getTag();
				if (wres != null && wres.getW() != null) 
				{
					WorkerToInsert wToInsert = new WorkerToInsert(wres.getW()
							.getId(), wres.getTotalTime() + "");
					wToInsert.setWorkerAdvance(wres);
					WorkersListToInsert.add(wToInsert);
				}
			}
		}
		for (View v : extraAddMachines)
		{
			Button b_more = (Button) v.findViewById(R.id.btn_more);
			if (b_more.getTag() != null)
			{
				MachineAdvanceResult mres = (MachineAdvanceResult)b_more.getTag();
				if (mres != null && mres.getM() != null) 
				{
					MachineToInsert mToInsert = new MachineToInsert(mres.getM().getId(), mres.getTotalTime() + "");
					mToInsert.setMachineAdvanceResult(mres);
					MachinesListToInsert.add(mToInsert);
				}
			}
		}

		final InsertActivityFromPost insert = new InsertActivityFromPost(
				activity_type_select!=null?activity_type_select.getId()+"":null, 
						currentConnectedUser.getCompany_id()+"", 
						currentConnectedUser.getFarm_id()+"", 
						editTextRemark.getText().toString(), 
						block_select!=null?block_select.getID() + "":null, 
								plot_select!=null?plot_select.getId() + "":null, 
										crop_selected!=null?crop_selected.getId() + "":null, 
												Variety_selected!=null?Variety_selected.getId() + "":null,
														StrDateSelect,
														WorkersListToInsert,
														MachinesListToInsert
				);

		if (checkCodeIfOk()) 
		{
			new insertActivity().execute(insert);
		}
	}

	private WorkerAdvancedResult getWorkerAdvanceExtraById(int id)
	{
		for (WorkerAdvancedResult wExtra : extraWorkersResult) 
		{
			if (wExtra.getW() != null && wExtra.getW().getId() == id)
			{
				return wExtra;
			}
		}
		return null;
	}

	private void deleteWorkerAdvanceExtraById(int id)
	{
		int d = -1;
		for (int i = 0; i < extraWorkersResult.size(); i++) 
			if (extraWorkersResult.get(i).getW() != null)
				if (extraWorkersResult.get(i).getW().getId() == id)
					d=i;
		if (d != -1)
			extraWorkersResult.remove(d);
	}

	private MachineAdvanceResult getMachineAdvanceExtraById(int id)
	{
		for (MachineAdvanceResult mExtra : extraMachinesResult) 
		{
			if (mExtra.getM() != null && mExtra.getM().getId() == id)
			{
				return mExtra;
			}
		}
		return null;	
	}

	//	private void deleteMachineAdvanceExtraById(int id)
	//	{
	//		int d = -1;
	//		for (int i = 0; i < extraMachinesResult.size(); i++) 
	//			if (extraMachinesResult.get(i).getM() != null)
	//				if (extraMachinesResult.get(i).getM().getId() == id)
	//					d=i;
	//		if (d != -1)
	//			extraMachinesResult.remove(d);
	//	}

	class insertActivity extends AsyncTask<InsertActivityFromPost, Void, String>
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progressDialog.show();
			button_ok.setEnabled(false);
		}
		@Override
		protected String doInBackground(InsertActivityFromPost... params)
		{
			try 
			{
				String ret = "0";

				ret = Crud.POST_For_Response_String(host + DataGlobal.UrlPostInsertActivityWithRoute + "?inWrite=" + inWrite,
						params[0],	InsertActivityFromPost.class);
				return ret;
			} 
			catch (Exception e)
			{
				Log.e("eli", "error", e);
				return "0";
			}
		}
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			button_ok.setEnabled(true);
			if (result.compareTo("1")==0)
			{
				UniversalFunctions.myToast(ActionWriteActivity.this, ActionWriteActivity.this.getString(R.string.success), Color.GREEN);
			}
			else 
			{
				UniversalFunctions.myToast(ActionWriteActivity.this, ActionWriteActivity.this.getString(R.string.fail), Color.RED);
			}
			progressDialog.dismiss();
		}
	}

	private boolean checkCodeIfOk()
	{
		String code = checkCodeOfScreen();
		if (code.startsWith("0"))
		{
			// no activity
			UniversalFunctions.myToast(this, getString(R.string.Not_selected_activities), Color.RED);
			UniversalFunctions.makeBlink(findViewById(R.id.textView2), 100);
			return false;
		}
		else if (code.startsWith("10000")) 
		{
			// אין גוש וחלקה 
			UniversalFunctions.myToast(this, getString(R.string.Not_selected_blocks) + " , " +getString(R.string.Not_selected_plots), Color.RED);
			UniversalFunctions.makeBlink(findViewById(R.id.textView3), 100);
			UniversalFunctions.makeBlink(findViewById(R.id.textView4), 100);
			return false;
		}
		return true;
	}

	private String checkCodeOfScreen()
	{
		String code = "";
		if (spinner_activity_type.getSelectedItemPosition()>0) 	code += "1";
		else code += "0";
		if (spinner_block.getSelectedItemPosition() > 0) code += "1";
		else 	code += "0";
		if (spinner_plot.getSelectedItemPosition()>0) 	code += "1";
		else 	code += "0";
		if (spinner_crop.getSelectedItemPosition()>0) 	code += "1";
		else 	code += "0";
		if (spinner_variety.getSelectedItemPosition()>0) code += "1";
		else 	code += "0";
		if (spinner_worker.getSelectedItemPosition()>0) code += "1";
		else 	code += "0";
		if (spinner_motoring_machinery.getSelectedItemPosition()>0) code += "1";
		else code += "0";
		code += "0";
		return code;
	}



	public class BuildWorkersFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); setProgressBarIndeterminateVisibility(true); 
			buildUrlToWorker();
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			Log.e("eli", UrlGetWorker);
			workers = Crud.GET(UrlGetWorker, Worker[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			if (workers != null) {
				BuildAdapterToWorker();
			}
			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);
			//			isBuildWorkersFinish= true;
			//			if (isBuildmotoring_machinerysFinish && isBuildFarmFinish)
			//			{
			//				pullToRefreshView.onRefreshComplete();
			//				isInRefreshProcess = false;
			//				prevSpinnersSelectionRestoreInstance();
			//			}
			super.onPostExecute(result);
		}

	}



	private void buildUrls() 
	{
		Log.e("eli", "Farm_id "+currentConnectedUser.getFarm_id());

		UrlGetActivityTypes = host+DataGlobal.UrlGetActivityTypesRoute+"company_id="+currentConnectedUser.getCompany_id();
		UrlGetBlocks = host+DataGlobal.UrlGetBlocksRoute+"FarmID="+currentConnectedUser.getFarm_id();
		UrlGetCrops = host+DataGlobal.UrlGetCropsRoute+"company_id="+currentConnectedUser.getCompany_id();
		UrlGetActivityGroups = host+DataGlobal.UrlGetActivityGroupsRoute+"company_id="+currentConnectedUser.getCompany_id();
	}

	public class BuildScreenFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); 
			setProgressBarIndeterminateVisibility(true); 

			buildUrls();
			buildUrlForPlotsGet(
					currentConnectedUser.getFarm_id() + "",
					"-1", 
					StrDateSelect, 
					StrDateSelect);
			buildUrlForVarietyssGet("-1", "-1");
			super.onPreExecute();
		}


		@Override
		protected User doInBackground(String... params) 
		{
			activityGroups = Crud.GET(UrlGetActivityGroups, ActivityGroups[].class, 1);
			activityTypes = Crud.GET(UrlGetActivityTypes, ActivityType[].class, 1);

			plots = Crud.GET(UrlGetPlots, Plot[].class, 3);
			blocks = Crud.GET(UrlGetBlocks, Block[].class, 2);
			crops = Crud.GET(UrlGetCrops, Crop[].class, 1);
			varietys = Crud.GET(UrlGetVarietys, Variety[].class, 1);

			colActivityGroup.setArrayFull(activityGroups);
			colActivity.setArrayFull(activityTypes);
			colPlot.setArrayFull(plots);
			colBlock.setArrayFull(blocks);
			colCrop.setArrayFull(crops);
			colVariety.setArrayFull(varietys);

			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			colActivity.setArray(activityTypes);
			colActivityGroup.setArray(activityGroups);
			BuildAdapterToActivityTypes();
			BuildAdapterToActivityGroups();

			colBlock.setArray(blocks);
			colPlot.setArray(plots);
			BuildAdapterToPlots();
			BuildAdapterToBlocks();

			colCrop.setArray(crops);
			BuildAdapterToCrops();

			colVariety.setArray(varietys);
			BuildAdapterToVarietys();

			//			new PlotsFromWeb().execute();

			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);

			//			isBuildScreenFinish = true;
			//			if (isBuildWorkersFinish && isBuildmotoring_machinerysFinish && isBuildFarmFinish) 
			//			{
			//				pullToRefreshView.onRefreshComplete();
			//				isInRefreshProcess = false;
			//				prevSpinnersSelectionRestoreInstance();
			//			}
			super.onPostExecute(result);
		}

	}

	private void BuildAdapterToActivityTypes() 
	{
		spinner_activity_type.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				colActivity.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
	}


	//	private boolean containsExtraData(ArrayList<View> list) 
	//	{
	//		for (View v : list) 
	//		{
	//			ImageView extra = (ImageView) v.findViewById(R.id.imageView_vi);
	//			if (extra.getVisibility() == View.VISIBLE)
	//				return true;
	//		}
	//		return false;
	//	}

	private void BuildAdapterToActivityGroups() 
	{		
		spinner_ActivityGroups.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
			{
				colActivityGroup.setSelection(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{

			}
		});
	}


	public void buildUrlToWorker()
	{
		UrlGetWorker = host+DataGlobal.UrlGetWorkerRoute+"farm_id="+currentConnectedUser.getFarm_id()+"&company_id="+currentConnectedUser.getCompany_id()+"&start_date="+StrDateSelect+"&EndDate="+StrDateSelect;	
	}

	public void buildUrlToMotoring_machinery()
	{
		UrlGetMachinery = host+DataGlobal.UrlGetMachineryRoute+"company_id="+currentConnectedUser.getCompany_id()+"&purchase_date="+StrDateSelect;
	}

	public class Buildmotoring_machinerysFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); 
			setProgressBarIndeterminateVisibility(true); 
			buildUrlToMotoring_machinery();
			//			buildUrlToTrailing_machine();
			super.onPreExecute();
		}
		@Override
		protected User doInBackground(String... params) 
		{
			motoring_machinerys = Crud.GET(UrlGetMachinery, motoring_machinery[].class, 1);
			//			Trailing_machines = Crud.GET(UrlGetTrailing_machine, Trailing_machine[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			BuildAdapterToMotoring_machinery();
			//			BuildAdapterToTrailing_machines();
			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);
			//			isBuildmotoring_machinerysFinish= true;
			//			if (isBuildWorkersFinish  && isBuildFarmFinish)
			//			{
			//				pullToRefreshView.onRefreshComplete();
			//				isInRefreshProcess = false;
			//				prevSpinnersSelectionRestoreInstance();
			//			}
			super.onPostExecute(result);
		}

	}

	private void BuildAdapterToMotoring_machinery()
	{
		List<String> listMotoring_machinery = new ArrayList<String>();
		listMotoring_machinery.add(defultValueForSpinner);

		if (motoring_machinerys != null) {
			for (motoring_machinery M : motoring_machinerys)
				listMotoring_machinery.add(M.getName());
		}
		UniversalFunctions.BuildAdapterRegular(this, spinner_motoring_machinery	,listMotoring_machinery);
		spinner_motoring_machinery.setOnItemSelectedListener(new spinnerChangeListenerToSyncAdvanced(spinner_motoring_machinery, btn_more_motoring, MACHINE));

	}


	private void BuildAdapterToWorker()
	{
		List<String> listWorkers = new ArrayList<String>();
		listWorkers.add(defultValueForSpinner);
		if (workers != null) {
			for (Worker W : workers)
				listWorkers.add(W.getName());
		}
		UniversalFunctions.BuildAdapterRegular(this, spinner_worker	,listWorkers);	
		spinner_worker.setOnItemSelectedListener(new spinnerChangeListenerToSyncAdvanced(spinner_worker, btn_more_worker, WORKER));
	}

	private void buildUrlForVarietyssGet(String crop_id, String plot_is) 
	{
		UrlGetVarietys = host+DataGlobal.UrlGetVarietysRoute+ "company_id="+currentConnectedUser.getCompany_id()+"&crop_id="+crop_id+"&plot_id="+plot_is;
	}

	public class VarietysFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); 
			setProgressBarIndeterminateVisibility(true); 
			if (crops!=null && crops.length>0 && plots!=null && plots.length>0 && spinner_crop.getSelectedItemPosition()>0 && spinner_plot.getSelectedItemPosition()>0) 
			{
				String crop = crops[spinner_crop.getSelectedItemPosition()-1].getId()+"";
				String plot = plots[spinner_plot.getSelectedItemPosition()-1].getId()+"";
				buildUrlForVarietyssGet(crop, plot);
			}
			else 
			{
				buildUrlForVarietyssGet("","");
			}
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			varietys = Crud.GET(UrlGetVarietys, Variety[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			if (varietys!= null) 
			{
				BuildAdapterToVarietys();
			}
			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
	}



	private void buildUrlForPlotsGet(String farm_id, String block_id, String start_date, String end_date) 
	{
		//http://localhost/api/Plots/GetPlots?farm_id=3&block_id=246&start_date=2014-06-28 16:23:31.294&end_date=2014-06-28 16:23:31.294
		UrlGetPlots = host+DataGlobal.UrlGetPlotsRoute+"farm_id="+farm_id+"&block_id="+block_id+"&start_date="+start_date+"&end_date="+end_date;
		//		Log.e("eli url", UrlGetPlots);
	}

	public class PlotsFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			setProgressBarIndeterminate(View.VISIBLE); 
			setProgressBarIndeterminateVisibility(true); 
			if (blocks!=null && blocks.length>0 && spinner_block.getSelectedItemPosition()>0) 
			{
				buildUrlForPlotsGet(
						currentConnectedUser.getFarm_id() + "",
						blocks[spinner_block.getSelectedItemPosition()-1].getID() + ""
						, StrDateSelect
						, StrDateSelect);
			}
			else {
				buildUrlForPlotsGet(
						currentConnectedUser.getFarm_id() + "",
						"-1", 
						StrDateSelect, 
						StrDateSelect);
			}
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			plots = Crud.GET(UrlGetPlots, Plot[].class, 3);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			if (plots!= null) 
			{
				BuildAdapterToPlots();
				//				new VarietysFromWeb().execute();
			}

			setProgressBarIndeterminate(View.INVISIBLE); 
			setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
	}

	private void BuildAdapterToVarietys()
	{
		//		List<String> listVarietys = new ArrayList<String>();
		//		listVarietys.add(defultValueForSpinner);
		//
		//		if (varietys != null) 
		//		{
		//			for (Variety V : varietys)
		//				listVarietys.add(V.getName());
		//		}
		//		UniversalFunctions.BuildAdapterRegular(this, spinner_variety, listVarietys);
		spinner_variety.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{
				if (position > 0)
				{
					colVariety.setSelection(position);
				}
				else
				{
					
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	private void BuildAdapterToCrops()
	{
		//		List<String> listCrops = new ArrayList<String>();
		//		listCrops.add(defultValueForSpinner);
		//
		//		if (Crops != null) {
		//			for (Crop C : Crops)
		//				listCrops.add(C.getName());
		//		}
		//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActionWriteActivity.this,R.layout.spinner_custom_textview, listCrops);
		//		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//		spinner_crop.setAdapter(dataAdapter);
		spinner_crop.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int position, long arg3) 
			{
				if (position > 0)
				{
					colCrop.setSelection(position);
					//				if (pos > 0) 
					//				{
					//					new VarietysFromWeb().execute();
					//					//				Log.e("eli", ActivityTypes[pos].toString());
					//				}
					//				else 
					//				{
					//					Varietys = null;
					//					BuildAdapterToVarietys();
					//				}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		} );		
	}

	private void BuildAdapterToPlots()
	{
		//		List<String> listPlots = new ArrayList<String>();
		//		listPlots.add(defultValueForSpinner);
		//
		//		if (plots != null)
		//		{
		//			for (Plot P : plots)
		//				listPlots.add(P.getName());
		//		}
		//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActionWriteActivity.this,R.layout.spinner_custom_textview, listPlots);
		//		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//		spinner_plot.setAdapter(dataAdapter);
		spinner_plot.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int position, long arg3) 
			{
				if (position > 0 )
				{
					colPlot.setSelection(position);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		} );		
	}

	private void BuildAdapterToBlocks() 
	{
		//		List<String> listBlocksNames = new ArrayList<String>();
		//		listBlocksNames.add(defultValueForSpinner);
		//
		//		if (blocks != null) {
		//			for (Block B : blocks)
		//				listBlocksNames.add(B.getNameHe());
		//		}
		//		ArrayAdapter<String> dataBlocsAdapter = new ArrayAdapter<String>(this,R.layout.spinner_custom_textview, listBlocksNames);
		//		dataBlocsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//		spinner_block.setAdapter(dataBlocsAdapter);
		spinner_block.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int position, long arg3) 
			{
				if (position > 0) 
				{
					colBlock.setSelection(position);
				}
				else 
				{
					colPlot.setArray(plots);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		} );
	}


	// Register  DatePickerDialog listener
	private DatePickerDialog.OnDateSetListener mDateSetListener =new DatePickerDialog.OnDateSetListener() 
	{                 
		public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) 
		{
			// TODO check if not in future?
			mYear = yearSelected;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			UniversalFunctions.myToast(ActionWriteActivity.this, getString(R.string.date_sucses_changed)+mDay+"-"+(mMonth+1)+"-"+mYear, Color.CYAN);
			buttonDateChange.setText(getString(R.string.change_date)+"\n"+mDay+"-"+(mMonth+1)+"-"+mYear);
			setLastKnowTimeAndDate();
			new PlotsFromWeb().execute();
			new BuildWorkersFromWeb().execute();
			new Buildmotoring_machinerysFromWeb().execute();
			// need to check if the time change dont crash the advanced data
		}
	};

	private void setLastKnowTimeAndDate() 
	{
		try 
		{
			StrDateSelect = URLEncoder.encode(mYear+"-"+(mMonth+1)+"-"+mDay+" "+mHour+":"+mMinute+":00.000", "utf-8");
		} 
		catch (UnsupportedEncodingException e)
		{
		};
	}

	//	private TimePickerDialog.OnTimeSetListener mTimeSetListener =  new TimePickerDialog.OnTimeSetListener() 
	//	{
	//		public void onTimeSet(TimePicker view, int hourOfDay, int min) 
	//		{
	//			// TODO check if not in future?
	//			mHour = hourOfDay;
	//			mMinute = min;
	//			UniversalFunctions.myToast(ActionWriteActivity.this, "Time selected is: "+mHour+"-"+mMinute, Color.CYAN);
	//			buttonTimeChange.setText("Time selection\n"+mHour+"-"+mMinute);
	//			setLastKnowTimeAndDate();
	//			//new PlotsFromWeb().execute();
	//			//new BuildWorkersFromWeb().execute();
	//			//new Buildmotoring_machinerysFromWeb().execute();
	//
	//		}
	//	};

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,mDateSetListener, mYear, mMonth, mDay);
		case TIME_DIALOG_ID:
			//			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		}
		return null;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) 
	{
		new AsyncTask<Void, Void, Void>()
		{
			int prevCountFarms, prevCountWorkers, prevCountMachine;
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				if (farms != null) 
				{
					prevCountFarms = farms.length;
				}
				if (motoring_machinerys != null)
				{
					prevCountMachine = motoring_machinerys.length;
				}
				if (workers != null)
				{
					prevCountWorkers = workers.length;
				}
			}
			@Override
			protected Void doInBackground(Void... params) 
			{
				farms = Crud.GET(UrlGetFarms, Farm[].class, 1);// TODO need to refresh the screen get
				workers = Crud.GET(UrlGetWorker, Worker[].class, 1);
				motoring_machinerys = Crud.GET(UrlGetMachinery, motoring_machinery[].class, 1);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) 
			{
				super.onPostExecute(result);
				pullToRefreshView.onRefreshComplete();
				if (farms != null && workers!= null && motoring_machinerys!= null)
				{
					if (prevCountFarms < farms.length) 
					{
						int d = farms.length - prevCountFarms;
						UniversalFunctions.myToast(ActionWriteActivity.this,
								" נוספו " + d + " משקים חדשים ", Color.GREEN);
					}
					if (prevCountWorkers < workers.length) 
					{
						int d = workers.length - prevCountWorkers;
						UniversalFunctions.myToast(ActionWriteActivity.this,
								" נוספו " + d + " עובדים חדשים ", Color.GREEN);
					}
					if (prevCountMachine < motoring_machinerys.length)
					{
						int d = motoring_machinerys.length - prevCountMachine;
						UniversalFunctions.myToast(ActionWriteActivity.this,
								" נוספו " + d + " מיכונים חדשים ", Color.GREEN);
					}
				}
			}
		}.execute();
		//		prevSpinnersSelectionSaveInstance();
		//		isBuildFarmFinish= isBuildWorkersFinish= isBuildmotoring_machinerysFinish=false;
		//		isInRefreshProcess = true;
		//		new BuildFarmsFromWeb().execute();
		////		new BuildScreenFromWeb().execute();
		//		new BuildWorkersFromWeb().execute();
		//		new Buildmotoring_machinerysFromWeb().execute();
	}

	//
	//	private void prevSpinnersSelectionSaveInstance()
	//	{
	//		// TODO is not good idea to save position, because it will be modify
	//		prev_select_spinner_farm = spinner_sector.getSelectedItemPosition();
	//		prev_select_spinner_activity_group = spinner_ActivityGroups.getSelectedItemPosition();
	//		prev_select_spinner_activity_type = spinner_activity_type.getSelectedItemPosition();
	//		prev_select_spinner_block= spinner_block.getSelectedItemPosition();
	//		prev_select_spinner_plot= spinner_plot.getSelectedItemPosition();
	//		prev_select_spinner_crop= spinner_crop.getSelectedItemPosition();
	//		prev_select_spinner_variety= spinner_variety.getSelectedItemPosition();
	//		prev_select_spinner_worker= spinner_worker.getSelectedItemPosition();
	//		prev_select_spinner_motoring_machinery= spinner_motoring_machinery.getSelectedItemPosition();
	//		//		prev_select_spinner_Trailing_machine= spinner_Trailing_machine.getSelectedItemPosition();
	//	}

	//	private void prevSpinnersSelectionRestoreInstance()
	//	{
	//		UniversalFunctions.setSelectionSpinner(spinner_sector, prev_select_spinner_farm);
	//		UniversalFunctions.setSelectionSpinner(spinner_ActivityGroups, prev_select_spinner_activity_group);
	//		UniversalFunctions.setSelectionSpinner(spinner_activity_type, prev_select_spinner_activity_type);
	//		UniversalFunctions.setSelectionSpinner(spinner_block, prev_select_spinner_block);
	//		UniversalFunctions.setSelectionSpinner(spinner_plot, prev_select_spinner_plot);
	//		UniversalFunctions.setSelectionSpinner(spinner_crop, prev_select_spinner_crop);
	//		UniversalFunctions.setSelectionSpinner(spinner_variety, prev_select_spinner_variety);
	//		UniversalFunctions.setSelectionSpinner(spinner_worker, prev_select_spinner_worker);
	//		UniversalFunctions.setSelectionSpinner(spinner_motoring_machinery, prev_select_spinner_motoring_machinery);
	//		//		setSelectionSpinner(spinner_Trailing_machine, prev_select_spinner_Trailing_machine);
	//		restoreExtraOnRefresh(extraAddWorkers, spinner_worker);
	//		restoreExtraOnRefresh(extraAddMachines, spinner_motoring_machinery);
	//	}


	private void restoreExtraOnRefresh(ArrayList<View> views, Spinner sp) 
	{
		if (views.size() > 0) 
		{
			for (View v : views)
			{
				Spinner s = (Spinner) v.findViewById(R.id.spinner_in_list);
				int prevSelection = s.getSelectedItemPosition();
				UniversalFunctions.copySpinners(sp, s);
				if (prevSelection < s.getCount())
				{
					s.setSelection(prevSelection);
				}	
				else
				{
					s.setSelection(0);
				}	
			}
		}
	}

	public class spinnerChangeListenerToSyncAdvanced  implements OnItemSelectedListener
	{
		Button moreDatails;
		int type;
		Spinner spin;

		public spinnerChangeListenerToSyncAdvanced(Spinner spin ,Button moreDatails, int type)
		{
			this.moreDatails=moreDatails;
			this.type = type;
			this.spin = spin;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) 
		{
			if (moreDatails.getTag() != null)
			{
				if (type == WORKER)
				{
					final Worker w1 = (Worker) UniversalFunctions.getFromSpinner(spin, workers);
					final Worker w2 = ((WorkerAdvancedResult)moreDatails.getTag()).getW();
					if (w1 != null && w2!= null && w1.getId()!=w2.getId()) 
					{
						AlertDialog.Builder adb= UniversalFunctions.showDialog(ActionWriteActivity.this, 
								getString(R.string.text_change_remove_sync), getString(R.string.are_you_sore));
						adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) 
							{
								((WorkerAdvancedResult)moreDatails.getTag()).setW(w1);
							} 
						});
						adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which)
							{
								for (int i = 0; i < workers.length; i++) 
									if ( workers[i].getId() == w2.getId())
										UniversalFunctions.setSelectionNoListen(spin, i+1);
							} 
						});
						adb.show();
					}
				}
				else if (type == MACHINE)
				{
					final motoring_machinery m1 = (motoring_machinery) UniversalFunctions.getFromSpinner(spin, motoring_machinerys);
					final motoring_machinery m2 = ((MachineAdvanceResult)moreDatails.getTag()).getM();
					if (m1 != null && m2!= null && m1.getId()!=m2.getId()) 
					{
						AlertDialog.Builder adb= UniversalFunctions.showDialog(ActionWriteActivity.this, 
								getString(R.string.text_change_remove_sync), getString(R.string.are_you_sore));
						adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) 
							{
								((MachineAdvanceResult)moreDatails.getTag()).setM(m1);
							} 
						});
						adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which)
							{
								for (int i = 0; i < motoring_machinerys.length; i++) 
									if ( motoring_machinerys[i].getId() == m2.getId())
										UniversalFunctions.setSelectionNoListen(spin, i+1);
							} 
						});
						adb.show();
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) 
		{
		}

	}


	public class TextChangeListenerToSyncAdvanced implements TextWatcher
	{
		EditText eTextHours ;
		Button moreDatails;
		int type;

		public TextChangeListenerToSyncAdvanced(EditText eTextHours, Button moreDatails, int type)
		{
			this.moreDatails=moreDatails;
			this.eTextHours = eTextHours;
			this.type = type;
		}

		@Override
		public void beforeTextChanged(final CharSequence s, int start, int count,	int after) 
		{

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{	}

		@Override
		public void afterTextChanged(final Editable s) 
		{	
			if (moreDatails.getTag() != null && isFromUser) 
			{
				AlertDialog.Builder adb= UniversalFunctions.showDialog(ActionWriteActivity.this, getString(R.string.text_change_remove_sync), getString(R.string.are_you_sore));
				adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						if (type == WORKER)
						{
							eTextHours.removeTextChangedListener(TextChangeListenerToSyncAdvanced.this);
							WorkerAdvancedResult wa = (WorkerAdvancedResult) moreDatails.getTag();
							try 
							{
								wa.setTotalTime(Double.parseDouble(s+""));
							} 
							catch (Exception e) 
							{
								wa.setTotalTime(0);
							}
							//							moreDatails.setTag(wa);
							eTextHours.addTextChangedListener(TextChangeListenerToSyncAdvanced.this);
						}
						else if (type == MACHINE)
						{
							eTextHours.removeTextChangedListener(TextChangeListenerToSyncAdvanced.this);
							MachineAdvanceResult ma = (MachineAdvanceResult) moreDatails.getTag();
							try 
							{
								ma.setTotalTime(Double.parseDouble(s+""));
							} 
							catch (Exception e) 
							{
								ma.setTotalTime(0);
							}
							//							moreDatails.setTag(ma);
							eTextHours.addTextChangedListener(TextChangeListenerToSyncAdvanced.this);
						}
					} 
				});
				adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which)
					{
						if (type == WORKER)
						{
							eTextHours.removeTextChangedListener(TextChangeListenerToSyncAdvanced.this);
							WorkerAdvancedResult wa = (WorkerAdvancedResult) moreDatails.getTag();
							eTextHours.setText(wa.getTotalTime() + "");
							eTextHours.addTextChangedListener(TextChangeListenerToSyncAdvanced.this);
						}
						else if (type == MACHINE)
						{
							eTextHours.removeTextChangedListener(TextChangeListenerToSyncAdvanced.this);
							MachineAdvanceResult ma = (MachineAdvanceResult) moreDatails.getTag();
							eTextHours.setText(ma.getTotalTime() + "");
							eTextHours.addTextChangedListener(TextChangeListenerToSyncAdvanced.this);
						}
					} 
				});
				adb.show();
			}
		}
	}

	public class addWorkerBtnListener implements OnClickListener
	{

		@Override
		public void onClick(View v) 
		{
			final View row = getLayoutInflater().inflate(R.layout.row_for_extra_spinner, null);
			final ImageView imageView_vi = (ImageView) row.findViewById(R.id.imageView_vi);
			imageView_vi.setVisibility(View.GONE);
			TextView text_spinner_list = (TextView) row.findViewById(R.id.text_spinner_list);
			final Spinner spinner_in_list = (Spinner) row.findViewById(R.id.spinner_in_list);
			EditText editText_hour = (EditText) row.findViewById(R.id.editText_hour);
			editText_hour.setHint(UniversalFunctions.getSmallHint(getString(R.string.hour)));

			final Button btn_more = (Button) row.findViewById(R.id.btn_more);
			Button btn_remove = (Button) row.findViewById(R.id.btn_remove);
			text_spinner_list.setText(getString(R.string.worker));
			UniversalFunctions.copySpinners(spinner_worker, spinner_in_list);
			btn_more.setOnClickListener(new startWorkerAdvancedListener(spinner_in_list, editText_hour, imageView_vi));
			btn_more.setOnLongClickListener(new longClickCancel(imageView_vi, spinner_in_list, true));
			editText_hour.addTextChangedListener(new TextChangeListenerToSyncAdvanced(editText_hour, btn_more, WORKER));
			spinner_in_list.setOnItemSelectedListener(new spinnerChangeListenerToSyncAdvanced(spinner_in_list, btn_more, WORKER));

			btn_remove.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View removeBtn) 
				{
					removeBtn.setEnabled(false);
					//					UniversalFunctions.startAnimForRemove(row, wraper_adding_worker);
					extraAddWorkers.remove(row);
					imageView_vi.setVisibility(View.GONE);
					btn_more.setTag(null);
					wraper_adding_worker.removeView(row);
				}
			});
			extraAddWorkers.add(row);
			UniversalFunctions.startAnimForAdding(row);
			wraper_adding_worker.addView(row, 0);
		}

	}	

	public class addMachineBtnListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			final View row = getLayoutInflater().inflate(R.layout.row_for_extra_spinner, null);
			final ImageView imageView_vi = (ImageView) row.findViewById(R.id.imageView_vi);
			imageView_vi.setVisibility(View.GONE);
			TextView text_spinner_list = (TextView) row.findViewById(R.id.text_spinner_list);
			final Spinner spinner_in_list = (Spinner) row.findViewById(R.id.spinner_in_list);
			EditText editText_hour = (EditText) row.findViewById(R.id.editText_hour);
			editText_hour.setHint(UniversalFunctions.getSmallHint(getString(R.string.hour)));
			final Button btn_more = (Button) row.findViewById(R.id.btn_more);
			Button btn_remove = (Button) row.findViewById(R.id.btn_remove);
			text_spinner_list.setText(getString(R.string.motoring));
			UniversalFunctions.copySpinners(spinner_motoring_machinery, spinner_in_list);
			btn_more.setOnClickListener(new startMachineAdvancedListener(spinner_in_list, editText_hour, imageView_vi));
			btn_more.setOnLongClickListener(new longClickCancel(imageView_vi, spinner_in_list, false));
			editText_hour.addTextChangedListener(new TextChangeListenerToSyncAdvanced(editText_hour, btn_more, MACHINE));
			spinner_in_list.setOnItemSelectedListener(new spinnerChangeListenerToSyncAdvanced(spinner_in_list, btn_more, MACHINE));


			btn_remove.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View removeBtn) 
				{
					removeBtn.setEnabled(false);
					//					UniversalFunctions.startAnimForRemove(row, wraper_adding_machinery);
					extraAddMachines.remove(row);
					imageView_vi.setVisibility(View.GONE);
					btn_more.setTag(null);
					wraper_adding_machinery.removeView(row);

				}
			});
			extraAddMachines.add(row);
			UniversalFunctions.startAnimForAdding(row);
			wraper_adding_machinery.addView(row, 0);
		}
	}

	public class longClickCancel implements OnLongClickListener
	{
		ImageView img;
		Spinner s;
		boolean isWorkerRow;// or machine

		public longClickCancel(ImageView img, Spinner s, boolean isWorkerRow) 
		{
			this.img = img;
			this.s = s;
			this.isWorkerRow = isWorkerRow;
		}

		@Override
		public boolean onLongClick(View v) 
		{
			if (img.getVisibility() == View.VISIBLE)
			{
				v.setTag(null);
				img.setVisibility(View.GONE);
				int selection = s.getSelectedItemPosition();
				if (selection > 0)
				{
					selection-- ;
					if (isWorkerRow)
					{
						Worker w = workers[selection];
						deleteWorkerAdvanceExtraById(w.getId());

					}
					else 
					{
						motoring_machinery m = motoring_machinerys[selection];
						deleteWorkerAdvanceExtraById(m.getId());
					}
				}
				return true;
			}
			else 
			{
				return false;
			}
		}

	}

	public class startMachineAdvancedListener implements OnClickListener
	{
		Spinner s;
		EditText e;
		ImageView img;

		public startMachineAdvancedListener(Spinner s, EditText e, ImageView img)
		{
			this.s = s;
			this.e = e;
			this.img = img;
		}
		@Override
		public void onClick(View v) 
		{
			// i am save the spinner to the onActivityresult
			spinnerThatLunchFrom = s;
			editTextThatLunchFrom = e;
			imgViewViThatLunchFrom = img;
			buttonNoreThatLunchFrom = (Button)v;
			startMachineAdvanced((Button)v, s, e);
		}
	}

	public class startWorkerAdvancedListener implements OnClickListener
	{
		Spinner s;
		EditText e;
		ImageView img;

		public startWorkerAdvancedListener(Spinner s, EditText e, ImageView img)
		{
			this.s = s;
			this.e = e;
			this.img = img;
		}
		@Override
		public void onClick(View v) 
		{
			// i am save the spinner to the onActivityresult
			spinnerThatLunchFrom = s;
			editTextThatLunchFrom = e;
			imgViewViThatLunchFrom = img;
			buttonNoreThatLunchFrom = (Button)v;
			startWorkerAdvanced((Button)v, s, e);
		}
	}

	public void startWorkerAdvanced(Button btn, Spinner s, EditText e) 
	{
		WorkerAdvanced firstFragment = new WorkerAdvanced();
		firstFragment.setBtnLunchFrom(btn);
		Bundle b = new Bundle();
		b.putString(DataGlobal.DATE_KEY, StrDateSelect.toString());
		b.putInt(DataGlobal.SELECTED_ITEM_KEY, s.getSelectedItemPosition());
		b.putString(DataGlobal.WORKER_HOURS, e.getText().toString() );

		if (btn.getTag() == null)
		{
			ActivityType activityTypeCurrent;
			int selected = spinner_activity_type.getSelectedItemPosition();
			if (selected > 0) 
			{
				selected--;
				activityTypeCurrent = activityTypes[selected];
				if (activityTypeCurrent.getActualPrice() != null && activityTypeCurrent.getActualPrice().compareTo(BigDecimal.valueOf(-1)) != 0) 
				{
					b.putString(DataGlobal.ActualPrice, activityTypeCurrent	.getActualPrice().toString());
				} 
				else 
				{
					b.putString(DataGlobal.ActualPrice, "0");
				}
				if (activityTypeCurrent.getYieldDescription() != -1) 
				{
					b.putString(DataGlobal.YieldDescription,activityTypeCurrent.getYieldDescription() + "");
				} 
				else 
				{
					b.putString(DataGlobal.YieldDescription, "0");
				}
			} 
			else
			{
				b.putString(DataGlobal.ActualPrice, "0");
				b.putString(DataGlobal.YieldDescription, "0");
			}
			firstFragment.setArguments(b);
			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.main_warper, firstFragment, "WorkerAdvanced").addToBackStack(null).commit();
		}
		else
		{
			WorkerAdvancedResult workerRes = (WorkerAdvancedResult)btn.getTag();
			b.putSerializable(DataGlobal.WORKER_ADVANCED, workerRes);  
			firstFragment.setArguments(b);
			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.main_warper, firstFragment, "WorkerAdvanced").addToBackStack(null).commit();
		}

	}

	public void startMachineAdvanced(Button btn, Spinner s, EditText e) 
	{
		MachineAdvanced firstFragment = new MachineAdvanced();
		firstFragment.setBtnLunchFrom(btn);
		Bundle b = new Bundle();
		b.putString(DataGlobal.DATE_KEY, StrDateSelect.toString());
		b.putInt(DataGlobal.SELECTED_ITEM_KEY, s.getSelectedItemPosition());
		b.putString(DataGlobal.MACHINE_HOURS, e.getText().toString() );

		if (btn.getTag() == null)
		{

			ActivityType activityTypeCurrent;
			int selected = spinner_activity_type.getSelectedItemPosition() ;
			if (selected > 0)
			{
				selected--;
				activityTypeCurrent =  activityTypes[selected];
				if (activityTypeCurrent.getActualPrice() != null && activityTypeCurrent.getActualPrice().compareTo(BigDecimal.valueOf(-1)) != 0)
				{
					b.putString(DataGlobal.ActualPrice, activityTypeCurrent.getActualPrice().toString());
				}
				else 
				{
					b.putString(DataGlobal.ActualPrice, "0");
				}
				if (activityTypeCurrent.getYieldDescription() != -1) 
				{
					b.putString(DataGlobal.YieldDescription,	activityTypeCurrent.getYieldDescription() + "");
				}
				else 
				{
					b.putString(DataGlobal.YieldDescription,	"0");
				}
			}
			else
			{
				b.putString(DataGlobal.ActualPrice, "0");
				b.putString(DataGlobal.YieldDescription,  "0");
			}
			firstFragment.setArguments(b);
			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.main_warper, firstFragment, "MachineAdvanced").addToBackStack(null).commit();
		}
		else
		{
			MachineAdvanceResult machineRes = (MachineAdvanceResult)btn.getTag();
			//			Log.e("eli", "machineRes = " + machineRes.toString());
			b.putSerializable(DataGlobal.MACHINE_ADVANCED, machineRes);  
			firstFragment.setArguments(b);
			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.main_warper, firstFragment, "MachineAdvanced").addToBackStack(null).commit();
		}
	}

	public void setMachineAdvanceResult(MachineAdvanceResult machineRes) 
	{
		imgViewViThatLunchFrom.setVisibility(View.VISIBLE);
		extraMachinesResult.add(machineRes);
		for (int i = 0; i < motoring_machinerys.length; i++) 
			if (machineRes.getM() != null && motoring_machinerys[i].getId() == machineRes.getM().getId())
				UniversalFunctions.setSelectionNoListen(spinnerThatLunchFrom, i+1);
		isFromUser = false;
		editTextThatLunchFrom.setText(machineRes.getTotalTime()+"");
		isFromUser = true;
		getSupportFragmentManager().popBackStack();
		UniversalFunctions.showPopAp(this, buttonNoreThatLunchFrom);

	}


	public void setWorkerAdvancedResult(WorkerAdvancedResult workerRes) 
	{
		imgViewViThatLunchFrom.setVisibility(View.VISIBLE);
		extraWorkersResult.add(workerRes);
		for (int i = 0; i < workers.length; i++) 
			if (workerRes.getW()!=null && workers[i].getId() == workerRes.getW().getId())
				UniversalFunctions.setSelectionNoListen(spinnerThatLunchFrom, i+1);
		isFromUser = false;
		editTextThatLunchFrom.setText(workerRes.getTotalTime()+"");
		isFromUser = true;
		getSupportFragmentManager().popBackStack();
		UniversalFunctions.showPopAp(this, buttonNoreThatLunchFrom);

	}


	/*******************************
	 ***** old execute of insert****
	 *******************************/
	//	private void insertToServerOnWrite() 
	//	{
	//		String code = checkCodeOfScreen();
	//
	//		ActivityType activity_type_select =null;
	//		Block block_select =null;
	//		Plot plot_select =null;
	//		Crop crop_selected =null;
	//		Variety Variety_selected=null;
	//		Worker worker_select =null;
	//		motoring_machinery motoring_machinery_select =null;
	//		Trailing_machine Trailing_machine_select =null;
	//		String HourCount = "0";
	//		String Remarks = "";
	//
	//		//		HourCount = editTextHourCount.getText().toString().compareTo("") == 0 ? "0" : editTextHourCount.getText().toString();
	//		Remarks = editTextRemark.getText().toString();
	//		if (code.startsWith("0"))
	//		{
	//			// error no activity
	//			myToast(getString(R.string.Not_selected_activities), Color.RED);
	//			makeBlink(R.id.textView2);
	//		}
	//		else if (code.startsWith("10000")) 
	//		{
	//			// אין גוש וחלקה 
	//			myToast(getString(R.string.Not_selected_blocks) + " , " +getString(R.string.Not_selected_plots), Color.RED);
	//			makeBlink(R.id.textView3);
	//			makeBlink(R.id.textView4);
	//		}
	//		else if (code.startsWith("11000")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					"",
	//					"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("10100")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					"",
	//					plot_select.getId()+"",
	//					"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("11100")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					plot_select.getId()+"",
	//					"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("10010")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					"",
	//					"",
	//					crop_selected.getId()+"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("10011")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			Variety_selected = Varietys[spinner_variety.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					"",
	//					"",
	//					crop_selected.getId()+"",
	//					Variety_selected.getId()+"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("11010")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					"",
	//					crop_selected.getId()+"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("11110")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					plot_select.getId()+"",
	//					crop_selected.getId()+"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("10110")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					"",
	//					plot_select.getId()+"",
	//					crop_selected.getId()+"",
	//					"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("11011")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			Variety_selected = Varietys[spinner_variety.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					"",
	//					crop_selected.getId()+"",
	//					Variety_selected.getId()+"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("11111")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			block_select = blocks[spinner_block.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			Variety_selected = Varietys[spinner_variety.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					block_select.getID()+"",
	//					plot_select.getId()+"",
	//					crop_selected.getId()+"",
	//					Variety_selected.getId()+"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}
	//		else if (code.startsWith("10111")) 
	//		{
	//			activity_type_select = ActivityTypes[spinner_activity_type.getSelectedItemPosition()-1];
	//			plot_select = plots[spinner_plot.getSelectedItemPosition()-1];
	//			crop_selected = Crops[spinner_crop.getSelectedItemPosition()-1];
	//			Variety_selected = Varietys[spinner_variety.getSelectedItemPosition()-1];
	//			int worker_num_in_spinner = spinner_worker.getSelectedItemPosition();
	//			int machine_num1_in_spinner = spinner_motoring_machinery.getSelectedItemPosition();
	//			//			int machine_num2_in_spinner = spinner_Trailing_machine.getSelectedItemPosition();
	//			worker_select = worker_num_in_spinner > 0 ? Workers[worker_num_in_spinner-1]: null;
	//			motoring_machinery_select = machine_num1_in_spinner > 0 ? motoring_machinerys[machine_num1_in_spinner-1]:null;
	//			//			Trailing_machine_select = machine_num2_in_spinner > 0 ? Trailing_machines[machine_num2_in_spinner-1]:null;
	//			insertActivityOrPlan(
	//					activity_type_select.getId()+"",
	//					Remarks,
	//					"",
	//					plot_select.getId()+"",
	//					crop_selected.getId()+"",
	//					Variety_selected.getId()+"",
	//					worker_select==null ? "":(worker_select.getId()+""),
	//							HourCount,
	//							Trailing_machine_select == null ? "":(Trailing_machine_select.getId()+""),
	//									HourCount,
	//									motoring_machinery_select == null ? "":(motoring_machinery_select.getId()+""),
	//											HourCount	
	//					);
	//		}else {
	//			// unknown
	//		}
	//	}
	//
	//
	//	private void insertActivityOrPlan(String ActivityType_id, String Remarks, String BlockID, String PlotID, String CropID, String VarietyID, String WorkerID, String totalTime, String MachineryID, String MachineryHours, String MotorizedMachineryID, String MotorizedMachineryHours) 
	//	{
	//		if (inWrite) 
	//		{
	//			String urlInsertWrite = host+"api/InsertActivity/InsertActivity?"
	//					+ "FromDate="+StrDateSelect
	//					+"&ToDate="+StrDateSelect+
	//					"&ActivityType_id="+ActivityType_id+
	//					"&CompanyID="+currentConnectedUser.getCompany_id()+
	//					"&farm_id="+currentConnectedUser.getFarm_id()+
	//					"&Remarks="	+Remarks+
	//					"&BlockID="	+BlockID+
	//					"&PlotID="+PlotID+
	//					"&CropID="	+CropID+
	//					"&VarietyID="+VarietyID+
	//					"&WorkerID="+WorkerID+
	//					"&totalTime="+totalTime+
	//					"&MachineryID="	+MachineryID+
	//					"&MachineryHours="+MachineryHours+
	//					"&MotorizedMachineryID="+MotorizedMachineryID+
	//					"&MotorizedMachineryHours="+MotorizedMachineryHours;
	//			// http get
	//			new insertActivityOrPlan().execute(urlInsertWrite);
	//		}
	//		else {
	//			// is plan
	//			String urlInsertWrite = host+"api/InsertActivity/InsertActivityPlan?FromDate="
	//					+StrDateSelect+"&ToDate="
	//					+StrDateSelect+"&ActivityType_id="
	//					+ActivityType_id+"&CompanyID="
	//					+currentConnectedUser.getCompany_id()+"&farm_id="
	//					+currentConnectedUser.getFarm_id()+"&Remarks="
	//					+Remarks+"&BlockID="
	//					+BlockID+"&PlotID="
	//					+PlotID+"&CropID="
	//					+CropID+"&VarietyID="
	//					+VarietyID+"&WorkerID="
	//					+WorkerID+"&totalTime="
	//					+totalTime+"&MachineryID="
	//					+MachineryID+"&MachineryHours="
	//					+MachineryHours+"&MotorizedMachineryID="
	//					+MotorizedMachineryID+"&MotorizedMachineryHours="
	//					+MotorizedMachineryHours;
	//			new insertActivityOrPlan().execute(urlInsertWrite);
	//
	//		}
	//
	//	}
	//
	//	public class insertActivityOrPlan extends AsyncTask<String, Integer, String>
	//	{
	//		@Override
	//		protected void onPreExecute() 
	//		{
	//			//			setProgressBarIndeterminate(View.VISIBLE); setProgressBarIndeterminateVisibility(true); 
	//			progressDialog.show();
	//
	//			button_ok.setEnabled(false);
	//			super.onPreExecute();
	//		}
	//
	//		@Override
	//		protected String doInBackground(String... params) 
	//		{
	//			BufferedReader in = null;
	//			String url = params[0];
	//			String line = "-11";
	//			Log.e("eli", url);
	//			HttpClient httpclient = new DefaultHttpClient();
	//
	//			HttpGet request = new HttpGet();
	//			request.setHeader(DataGlobal.TokenName, DataGlobal.TokenValue);
	//
	//			URI website = null;
	//			try 
	//			{
	//				website = new URI(url);
	//			} 
	//			catch (URISyntaxException e) 
	//			{
	//			}
	//			request.setURI(website);
	//			HttpResponse response = null;
	//			try 
	//			{
	//				response = httpclient.execute(request);
	//			} 
	//			catch (ClientProtocolException e) 
	//			{
	//			} 
	//			catch (IOException e) 
	//			{
	//			}
	//			try 
	//			{
	//				in = new BufferedReader(new InputStreamReader(	response.getEntity().getContent()));
	//			} 
	//			catch (IllegalStateException e) 
	//			{
	//			} 
	//			catch (IOException e) 
	//			{
	//			}
	//			try 
	//			{
	//				line = in.readLine();
	//			} 
	//			catch (IOException e) 
	//			{
	//			}
	//			return line;
	//		}
	//		@Override
	//		protected void onPostExecute(String result) 
	//		{
	//			super.onPostExecute(result);
	//			button_ok.setEnabled(true);
	//			Log.e("eli", "onPostExecute " + result);
	//			if (result.compareTo("1")==0)
	//			{
	//				myToast(ActionWriteActivity.this.getString(R.string.success), Color.GREEN);
	//			}
	//			else 
	//			{
	//				myToast(ActionWriteActivity.this.getString(R.string.fail), Color.RED);
	//			}
	//			//			setProgressBarIndeterminate(View.INVISIBLE); setProgressBarIndeterminateVisibility(false);
	//			progressDialog.dismiss();
	//		}
	//	}


}

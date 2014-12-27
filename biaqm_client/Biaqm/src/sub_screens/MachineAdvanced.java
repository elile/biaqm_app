package sub_screens;

import intertnet_utils.Crud;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import model.MachineAdvanceResult;
import model.User;
import model.Yield_Descriptions;
import model.motoring_machinery;
import utils.DataGlobal;
import utils.StoreObjects;
import utils.UniversalFunctions;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.biaqm.ActionWriteActivity;
import com.example.biaqm.R;
import com.gc.materialdesign.views.ProgressBarIndeterminate;

public class MachineAdvanced extends Fragment implements OnWheelChangedListener 
{
	private WheelView to_hour, to_mins, from_hour, from_mins;
	private String host = DataGlobal.host;
	private User userConnected;
	private String StrDateSelect;
	private String defultValueForSpinner;

	private String UrlGetMachines;
	private String UrlGetYield_Descriptions;
	private motoring_machinery[] machinerys;
	private Yield_Descriptions[] Yield_Descriptionss;
	private Spinner Spinner_tfooka_details;
	private EditText editTextTfookaNum, editText_TfookaCost, editTextRemarks, editTextTimeTotal;
	private Button button_ok;
	private String StrYieldDescription ;
	private String ActualPrice;
	private Spinner spinner_machine;
	private int spinnerMachineSelection = 0 ;
	private ActionWriteActivity actionWriteActivity;
	private Button btnLunchFrom;
	private String hoursFromOutside;
	private ProgressBarIndeterminate progressBarIndeterminate;

	
	private void setProgressBarIndeterminate(int visible) 
	{
		if (progressBarIndeterminate != null && progressBarIndeterminate.getVisibility() != visible)
		{
			progressBarIndeterminate.setVisibility(visible);
		}
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		actionWriteActivity = (ActionWriteActivity)activity;
	}

	public Button getBtnLunchFrom() 
	{
		return btnLunchFrom;
	}

	public void setBtnLunchFrom(Button btnLunchFrom) 
	{
		this.btnLunchFrom = btnLunchFrom;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.layout_machine_advanced, container, false);
		initFind(v);
		userConnected = (User) StoreObjects.getFromPreferences(User.class, DataGlobal.CURRENT_USER, getActivity());
		defultValueForSpinner = getActivity().getString(R.string.select);
		initTimeWheels();
		//		updateTimeTotatEditText();
		getExtras();
		new BuildMachinesFromWeb().execute();
		new BuildYield_DescriptionsWeb().execute();
		initListenerToOkButton();
		return v;
	}

	private void initListenerToOkButton() 
	{
		button_ok.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				motoring_machinery m = (motoring_machinery)UniversalFunctions.getFromSpinner(spinner_machine, machinerys);
				double totalTime;
				try
				{
					totalTime = Double.parseDouble(editTextTimeTotal.getText().toString());
				} 
				catch (Exception e) 
				{
					totalTime = 0;
				}
				Yield_Descriptions yd = (Yield_Descriptions)UniversalFunctions.getFromSpinner(Spinner_tfooka_details, Yield_Descriptionss);
				int numOfYd;
				try 
				{
					numOfYd = Integer.parseInt(editTextTfookaNum.getText().toString());
				} 
				catch (Exception e) 
				{
					numOfYd = 0 ;
				}
				double costOfYd;
				try 
				{
					costOfYd = Double.parseDouble(editText_TfookaCost.getText().toString());
				} 
				catch (Exception e) 
				{
					costOfYd = 0 ;
				}
				String remarks = editTextRemarks.getText().toString();
				MachineAdvanceResult mRes = new MachineAdvanceResult(m, totalTime, yd, numOfYd, costOfYd, remarks);
				btnLunchFrom.setTag(mRes);

				actionWriteActivity.setMachineAdvanceResult(mRes);
			}
		});		
	}


	private void getExtras() 
	{
		Bundle b = getArguments();
		if (b != null) 
		{
			MachineAdvanceResult machineRes = (MachineAdvanceResult)b.getSerializable(DataGlobal.MACHINE_ADVANCED);
			if (machineRes != null)
			{
				editTextTfookaNum.setText(machineRes.getNumOfYd()+"");
				editTextTimeTotal.setText(machineRes.getTotalTime()+"");
				editTextRemarks.setText(machineRes.getRemarks());
				ActualPrice = machineRes.getCostOfYd()+"";
				editText_TfookaCost.setText(ActualPrice);
				if (machineRes.getYd() != null) 
				{
					StrYieldDescription = machineRes.getYd().getID() + "";
				}
			}
			else 
			{
				StrYieldDescription = b.getString(DataGlobal.YieldDescription);
				ActualPrice = b.getString(DataGlobal.ActualPrice);
			}
			StrDateSelect = b.getString(DataGlobal.DATE_KEY);
			spinnerMachineSelection = b.getInt(DataGlobal.SELECTED_ITEM_KEY, 0);
			hoursFromOutside = b.getString(DataGlobal.MACHINE_HOURS);
			//			Log.e("eli", "hoursFromOutside = " + hoursFromOutside);
			if (hoursFromOutside != null) 
			{

				editTextTimeTotal.setText(hoursFromOutside);
			}
		}
	}

	private void initFind(View v) 
	{
		progressBarIndeterminate = (ProgressBarIndeterminate)v.findViewById(R.id.progressBarIndeterminate);

		Spinner_tfooka_details = (Spinner)v.findViewById(R.id.Spinner_tfooka_details);
		spinner_machine = (Spinner)v.findViewById(R.id.spinner_machine);
		editTextTfookaNum = (EditText)v.findViewById(R.id.editTextTfookaNum);
		editText_TfookaCost = (EditText)v.findViewById(R.id.editText_TfookaCost);
		editTextRemarks = (EditText)v.findViewById(R.id.editTextRemarks);
		editTextTimeTotal = (EditText)v.findViewById(R.id.editTextTimeTotal);
		button_ok = (Button)v.findViewById(R.id.button_ok);
		from_mins = (WheelView)v.findViewById(R.id.from_mins);
		from_hour = (WheelView)v.findViewById(R.id.from_hour);
		to_hour = (WheelView)v.findViewById(R.id.to_hour);
		to_mins = (WheelView)v.findViewById(R.id.to_mins);
	}

	public void buildUrlToYield_Descriptions()
	{
		UrlGetYield_Descriptions = host + DataGlobal.UrlGetYield_DescriptionsRoute+
				"company_id="+userConnected.getCompany_id();
	}

	public class BuildYield_DescriptionsWeb extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			//			UniversalFunctions.showProgressBar(getActivity());
			setProgressBarIndeterminate(View.VISIBLE);
			buildUrlToYield_Descriptions();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			Yield_Descriptionss = Crud.GET(UrlGetYield_Descriptions, Yield_Descriptions[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			BuildAdapterToYield_Descriptions();
			//			UniversalFunctions.stopProgressBar();
			setProgressBarIndeterminate(View.INVISIBLE); 

		}

	}
	private void BuildAdapterToYield_Descriptions()
	{
		List<String> Yield_Descriptions_List = new ArrayList<String>();
		Yield_Descriptions_List.add(defultValueForSpinner);
		if (Yield_Descriptionss != null) 
		{
			for (Yield_Descriptions y : Yield_Descriptionss)
				Yield_Descriptions_List.add(y.getName());
		}
		UniversalFunctions.BuildAdapterRegular(getActivity(), Spinner_tfooka_details	,Yield_Descriptions_List);	
		if (Yield_Descriptionss != null)
		{
			for (int i = 0; i < Yield_Descriptionss.length; i++) 
			{
				if (StrYieldDescription != null) 
					if ((Yield_Descriptionss[i].getID() + "").compareTo(StrYieldDescription) == 0) 
						Spinner_tfooka_details.setSelection(i + 1, true);
			}
		}
	}

	public void buildUrlToMachines()
	{
		UrlGetMachines = host+DataGlobal.UrlGetMachineryRoute+"company_id="+userConnected.getCompany_id()+"&purchase_date="+StrDateSelect;
	}

	public class BuildMachinesFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			//			UniversalFunctions.showProgressBar(getActivity());
			setProgressBarIndeterminate(View.VISIBLE);
			buildUrlToMachines();
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			machinerys = Crud.GET(UrlGetMachines, motoring_machinery[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			BuildAdapterToMachinerys();
			spinner_machine.setSelection(spinnerMachineSelection, true);
			//			UniversalFunctions.stopProgressBar();
			setProgressBarIndeterminate(View.INVISIBLE); 

			super.onPostExecute(result);
		}
	}

	private void BuildAdapterToMachinerys() 
	{
		List<String> list_machinery = new ArrayList<String>();
		list_machinery.add(defultValueForSpinner);

		if (machinerys != null) {
			for (motoring_machinery M : machinerys)
				list_machinery.add(M.getName());
		}
		UniversalFunctions.BuildAdapterRegular(getActivity(), spinner_machine, list_machinery);
	}

	private void initTimeWheels() 
	{
		from_hour.setViewAdapter(new NumericWheelAdapter(getActivity(), 0, 23));
		from_mins.setViewAdapter(new NumericWheelAdapter(getActivity(), 0, 59, "%02d"));
		to_hour.setViewAdapter(new NumericWheelAdapter(getActivity(), 0, 23));
		to_mins.setViewAdapter(new NumericWheelAdapter(getActivity(), 0, 59, "%02d"));

		to_mins.setCyclic(true);
		from_mins.setCyclic(true);

		customizeWheel(to_hour);
		customizeWheel(to_mins);
		customizeWheel(from_hour);
		customizeWheel(from_mins);

		to_hour.addChangingListener(this);
		to_mins.addChangingListener(this);
		from_hour.addChangingListener(this);
		from_mins.addChangingListener(this);

	}


	private void customizeWheel(WheelView Wheel) 
	{
		Wheel.setVisibleItems(3); // Number of items
		Wheel.setWheelBackground(R.drawable.buttonshape);
		//		Wheel.setWheelForeground(R.drawable.buttonshape);
		Wheel.setShadowColor(0x44000000, 0x22000000, 0x44000000);
		//		Wheel.setCurrentItem(3);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		updateTimeTotatEditText();
		//		switch (wheel.getId()) 
		//		{
		//		case R.id.to_hour:
		//
		//			break;
		//		case R.id.to_mins:
		//
		//			break;
		//		case R.id.from_hour:
		//
		//			break;
		//		case R.id.from_mins:
		//
		//			break;
		//		}
	}

	public void updateTimeTotatEditText()
	{
		editTextTimeTotal.post(new Runnable() 
		{
			@Override
			public void run()
			{
				int th, tm, fh, fm;
				th = to_hour.getCurrentItem();
				tm = to_mins.getCurrentItem();
				fh = from_hour.getCurrentItem();
				fm = from_mins.getCurrentItem();

				int from = convertHourAndMinToMinInDay(fh, fm);
				int to = convertHourAndMinToMinInDay(th, tm);
				double timeInterval = 	timeBetweenMinInDay(from, to);
				if (timeInterval >= 0)
				{
					DecimalFormat df = new DecimalFormat("##.##");
					editTextTimeTotal.setText(df.format(timeInterval));
				}
				else
				{
					editTextTimeTotal.setText(getString(R.string.time_not_valid));
				}
			}
		});
	}
	public int convertHourAndMinToMinInDay(int h, int m) 
	{
		return (60*h)+m;
	}
	public double timeBetweenMinInDay(int start, int end) 
	{
		return ((double)(end - start))/((double)60);
	}



}
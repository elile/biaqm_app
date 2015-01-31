package sub_screens;

import intertnet_utils.Crud;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import model.User;
import model.Worker;
import model.WorkerAdvancedResult;
import model.Yield_Descriptions;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.biaqm.ActionWriteActivity;
import com.example.biaqm.R;
import com.gc.materialdesign.views.ProgressBarIndeterminate;

public class WorkerAdvanced extends Fragment implements OnWheelChangedListener
{
	private WheelView to_hour, to_mins, from_hour, from_mins;
	private String host = DataGlobal.host;
	private User userConnected;
	private String StrDateSelect;
	private String defultValueForSpinner;

	private String UrlGetWorker;
	private String UrlGetYield_Descriptions;
	private Worker[] Workers;
	private Yield_Descriptions[] Yield_Descriptionss;
	private Spinner spinner_worker, Spinner_tfooka_details;
	private EditText editTextTfookaNum, editText_TfookaCost, editTextRemarks, editText_worker_quantity, editTextTimeTotal;
	private Button button_ok;
	private String StrYieldDescription;
	private String ActualPrice;
	private int spinnerWorkerSelection = 0;
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
		View v = inflater.inflate(R.layout.layout_worker_advanced, container, false);
		initFind(v);
		userConnected = (User) StoreObjects.getFromPreferences(User.class, DataGlobal.CURRENT_USER, getActivity());
		defultValueForSpinner = getString(R.string.select);		
		initTimeWheels();
		//		updateTimeTotatEditText();
		getExtras();
		new BuildWorkersFromWeb().execute();
		new BuildYield_DescriptionsWeb().execute();
		lisitnerToOkButton();
		return v;
	}

	private void lisitnerToOkButton() 
	{
		button_ok.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Worker w =  (Worker)UniversalFunctions.getFromSpinner(spinner_worker, Workers);
				int workerQuantity;
				try 
				{
					workerQuantity = Integer.parseInt(editText_worker_quantity.getText().toString());
				} 
				catch (Exception e)
				{
					workerQuantity = 0;
				}
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
					numOfYd = 0;
				}
				double costOfYd;
				try 
				{
					costOfYd = Double.parseDouble(editText_TfookaCost.getText().toString());
				} 
				catch (Exception e)
				{
					costOfYd = 0;
				}
				String remarks = editTextRemarks.getText().toString();
				WorkerAdvancedResult wRes = new WorkerAdvancedResult(w, workerQuantity, totalTime, yd, numOfYd, costOfYd, remarks);

				int th, tm, fh, fm;
				th = to_hour.getCurrentItem();
				tm = to_mins.getCurrentItem();
				fh = from_hour.getCurrentItem();
				fm = from_mins.getCurrentItem();

				int from = convertHourAndMinToMinInDay(fh, fm);
				int to = convertHourAndMinToMinInDay(th, tm);
				double timeInterval = 	timeBetweenMinInDay(from, to);
				if (timeInterval > 0)
				{
					wRes.setStartWorkingHour(fh+":"+fm);
					wRes.setEndWorkingHour(th+":"+tm);
				}
				else
				{
					wRes.setStartWorkingHour("0:0");
					wRes.setEndWorkingHour("0:0");
				}



				btnLunchFrom.setTag(wRes);

				actionWriteActivity.setWorkerAdvancedResult(wRes);
			}
		});		
	}


	private void getExtras() 
	{
		Bundle b = getArguments();

		if (b != null) 
		{
			WorkerAdvancedResult workerRes = (WorkerAdvancedResult)b.getSerializable(DataGlobal.WORKER_ADVANCED);
			if (workerRes != null) 
			{
				editText_worker_quantity.setText(workerRes.getWorkerQuantity()+"");
				editTextTfookaNum.setText(workerRes.getNumOfYd()+"");
				editTextTimeTotal.setText(workerRes.getTotalTime()+"");

				editTextRemarks.setText(workerRes.getRemarks());

				ActualPrice = workerRes.getCostOfYd()+"";
				editText_TfookaCost.setText(ActualPrice);

				if (workerRes.getYd() != null) 
				{
					StrYieldDescription = workerRes.getYd().getID() + "";
				}
			}
			else
			{
				StrYieldDescription = b.getString(DataGlobal.YieldDescription );
				ActualPrice = b.getString(DataGlobal.ActualPrice );
			}
			spinnerWorkerSelection = b.getInt(DataGlobal.SELECTED_ITEM_KEY, 0);
			StrDateSelect = b.getString(DataGlobal.DATE_KEY);
			hoursFromOutside = b.getString(DataGlobal.WORKER_HOURS);
		}
	}

	private void initFind(View v) 
	{
		progressBarIndeterminate = (ProgressBarIndeterminate)v.findViewById(R.id.progressBarIndeterminate);

		Spinner_tfooka_details = (Spinner)v.findViewById(R.id.Spinner_tfooka_details);
		spinner_worker = (Spinner)v.findViewById(R.id.spinner_worker);

		editText_worker_quantity = (EditText)v.findViewById(R.id.editText_worker_quantity);
		editTextTfookaNum = (EditText)v.findViewById(R.id.editTextTfookaNum);
		editText_TfookaCost = (EditText)v.findViewById(R.id.editText_TfookaCost);
		editTextRemarks = (EditText)v.findViewById(R.id.editTextRemarks);
		editTextTimeTotal = (EditText)v.findViewById(R.id.editTextTimeTotal);

		button_ok = (Button)v.findViewById(R.id.button_ok);

		from_mins = (WheelView) v.findViewById(R.id.from_mins);
		from_hour = (WheelView) v.findViewById(R.id.from_hour);
		to_hour = (WheelView) v.findViewById(R.id.to_hour);
		to_mins = (WheelView) v.findViewById(R.id.to_mins);

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
					if ((Yield_Descriptionss[i].getID()+"").compareTo(StrYieldDescription)==0 )
						Spinner_tfooka_details.setSelection(i+1, true);
			}
		}
	}

	public void buildUrlToWorker()
	{
		UrlGetWorker = host+DataGlobal.UrlGetWorkerRoute+"farm_id="+userConnected.getFarm_id()+
				"&company_id="+userConnected.getCompany_id()+
				"&start_date="+StrDateSelect+"&EndDate="+StrDateSelect;	
	}

	public class BuildWorkersFromWeb extends AsyncTask<String, Integer, User>
	{
		@Override
		protected void onPreExecute() 
		{
			//			UniversalFunctions.showProgressBar(getActivity());
			setProgressBarIndeterminate(View.VISIBLE);
			buildUrlToWorker();
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) 
		{
			//			Log.e("eli", UrlGetWorker);
			Workers = Crud.GET(UrlGetWorker, Worker[].class, 1);
			return null;
		}

		@Override
		protected void onPostExecute(User result) 
		{
			BuildAdapterToWorker();
			spinner_worker.setSelection(spinnerWorkerSelection, true);
			//			UniversalFunctions.stopProgressBar();
			setProgressBarIndeterminate(View.INVISIBLE); 

			super.onPostExecute(result);
		}

	}

	private void BuildAdapterToWorker()
	{
		List<String> listWorkers = new ArrayList<String>();
		listWorkers.add(defultValueForSpinner);
		if (Workers != null)
		{
			for (Worker W : Workers)
				listWorkers.add(W.getName());
		}
		UniversalFunctions.BuildAdapterRegular(getActivity(), spinner_worker	,listWorkers);	
		spinner_worker.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{
				if (hoursFromOutside != null && hoursFromOutside.length()>0)
				{
					editTextTimeTotal.setText(hoursFromOutside);
					hoursFromOutside = null;
					return;
				}
				if (position > 0) 
				{
					position--;
					editTextTimeTotal.setText(Workers[position].getStandard_hours() + "");
					utils.UniversalFunctions.makeBlink(editTextTimeTotal, 100);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
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
		Wheel.setShadowColor(0x44000000, 0x22000000, 0x44000000);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		updateTimeTotatEditText();
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

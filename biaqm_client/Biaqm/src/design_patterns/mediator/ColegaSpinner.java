package design_patterns.mediator;

import model.BaseSpinnerModel;
import model.EmptySpinnerValue;
import utils.UniversalFunctions;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.biaqm.R;



public class ColegaSpinner extends Colleague
{
	private String name;
	private Spinner spinner;
	private BaseSpinnerModel [] arrayLoading;
	private Context context;

	public ColegaSpinner(Mediator mediator, String name, Spinner spinner, Context context) 
	{
		super(mediator);
		this.name = name;
		this.spinner = spinner;
		this.context = context;
		arrayLoading = new BaseSpinnerModel[1];
		arrayLoading[0] = new EmptySpinnerValue(context);
	}	

	public ColegaSpinner(Mediator mediator, String name, Spinner spinner, BaseSpinnerModel[] arrayLoading) 
	{
		super(mediator);
		this.name = name;
		this.spinner = spinner;
		setArray(arrayLoading);
	}

	@Override
	public String getNameColleague()
	{
		return name;	
	}

	@Override
	public void setSelection(int selection) 
	{
		mediator.setSelection(selection, this);
		//		UniversalFunctions.setSelectionSpinner(spinner, selection);

	}

	@Override
	public void keepThisId(long id)
	{
		mediator.keepThisId(id, this);
		//		if (id >= 0 && arrayLoading != null)
		//		{
		//			int count = 0;
		//			for (BaseSpinnerModel bs : arrayLoading)
		//				if (bs.getIdBase() == id || bs.getIdBase() == -1)
		//					count ++;
		//			BaseSpinnerModel[] arrayNew = new BaseSpinnerModel[count];
		//			int j = 0;
		//			for (int i = 0; i < arrayLoading.length; i++) 
		//			{
		//				BaseSpinnerModel bs = arrayLoading[i];
		//				if (bs.getIdBase() == id || bs.getIdBase() == -1)
		//				{
		//					arrayNew[j] = bs;
		//					j++;
		//				}
		//			}
		//			arrayLoading = arrayNew;
		//		}
	}

	@Override
	public void setArray(BaseSpinnerModel[] array) 
	{
		mediator.setArray(array, this);
		//		array = addSelectTo(array);
		//		if (array != null)
		//		{
		//			this.arrayLoading = array;
		//			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_custom_textview, convertArryToList(array));
		//			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//			spinner.setAdapter(dataAdapter);
		//		}

	}

	@Override
	public Spinner getSpinner() 
	{
		return this.spinner;
	}

	@Override
	public BaseSpinnerModel[] getArrayLoading() 
	{
		return this.arrayLoading;
	}

	@Override
	public Context getContext() 
	{
		return this.context;
	}

	@Override
	public void setArrayLoading(BaseSpinnerModel[] arrayLoading)
	{
		this.arrayLoading = arrayLoading;		
	}



	//	private BaseSpinnerModel[] addSelectTo(BaseSpinnerModel[] array) 
	//	{
	//		if (array != null )
	//		{
	//			BaseSpinnerModel[] arrayNew = new BaseSpinnerModel[array.length+1];
	//			arrayNew[0] = new EmptySpinnerValue(context);
	//			for (int i = 0; i < array.length; i++)
	//			{
	//				arrayNew[i+1] = array[i];
	//			}
	//			return arrayNew;
	//		}
	//		else 
	//		{
	//			return new BaseSpinnerModel[0];
	//		}
	//	}
	//
	//	private String[] convertArryToList(BaseSpinnerModel[] array) 
	//	{
	//		String[] strArr = new String[array.length];
	//		for (int i = 0; i < array.length; i++)
	//		{
	//			strArr[i] = array.toString();
	//		}
	//		return strArr;
	//	}




}
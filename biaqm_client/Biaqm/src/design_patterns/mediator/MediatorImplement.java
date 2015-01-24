package design_patterns.mediator;
import java.util.ArrayList;

import com.example.biaqm.R;

import android.content.Context;
import android.widget.ArrayAdapter;
import model.BaseSpinnerModel;
import model.EmptySpinnerValue;
import utils.DataGlobal;
import utils.UniversalFunctions;


public class MediatorImplement implements Mediator
{
	private ArrayList<Colleague> colleagues;

	public MediatorImplement() 
	{
		this.colleagues = new ArrayList<Colleague>();
	}

	public MediatorImplement(ArrayList<Colleague> colleagues) 
	{
		this.colleagues = colleagues;
	}


	@Override
	public void addColleague(Colleague colega)
	{
		colleagues.add(colega);
	}

	@Override
	public void setArray(BaseSpinnerModel[] array, Colleague from) 
	{
		array = addSelectTo(array, from.getContext());
		if (array != null)
		{
			from.setArrayLoading(array);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(from.getContext(), R.layout.spinner_custom_textview, convertArryToList(array));
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			from.getSpinner().setAdapter(dataAdapter);
		}

	}

	private BaseSpinnerModel[] addSelectTo(BaseSpinnerModel[] array, Context context) 
	{
		if (array != null )
		{
			BaseSpinnerModel[] arrayNew = new BaseSpinnerModel[array.length+1];
			arrayNew[0] = new EmptySpinnerValue(context);
			for (int i = 0; i < array.length; i++)
			{
				arrayNew[i+1] = array[i];
			}
			return arrayNew;
		}
		else 
		{
			return new BaseSpinnerModel[0];
		}
	}

	private String[] convertArryToList(BaseSpinnerModel[] array) 
	{
		String[] strArr = new String[array.length];
		for (int i = 0; i < array.length; i++)
		{
			strArr[i] = array[i].getSpinnerName();
		}
		return strArr;
	}


	@Override
	public void setSelection(int selection, Colleague from) 
	{
		switch (from.getNameColleague()) 
		{
		case DataGlobal.SPINNER_PLOT_NAME:
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_BLOCK_NAME:
						UniversalFunctions.setSelectionSpinner(to.getSpinner(), selection);
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_ACTIVITY_NAME:// activity was change
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_ACTIVITY_GROUP_NAME:// activity group mechanism
						UniversalFunctions.setSelectionSpinner(to.getSpinner(), selection);
						//						to.setSelection(selection);
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_ACTIVITY_GROUP_NAME:// activity group was change
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{

				}
			}
			break;
		}

	}


	@Override
	public void keepThisId(long id, Colleague from) 
	{
		if (id >= 0 && from.getArrayLoading() != null)
		{
			int count = 0;
			for (BaseSpinnerModel bs : from.getArrayLoading())
			{	
				if (bs.getIdBase() == id || bs.getIdBase() == -1)
				{	
					count ++;
				}
			}
			BaseSpinnerModel[] arrayNew = new BaseSpinnerModel[count];
			int j = 0;
			for (int i = 0; i < from.getArrayLoading().length; i++) 
			{
				BaseSpinnerModel bs = from.getArrayLoading()[i];
				if (bs.getIdBase() == id || bs.getIdBase() == -1)
				{
					arrayNew[j] = bs;
					j++;
				}
			}
			from.setArrayLoading(arrayNew);
		}
	}



}

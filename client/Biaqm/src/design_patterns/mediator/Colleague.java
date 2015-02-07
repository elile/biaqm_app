package design_patterns.mediator;

import android.content.Context;
import android.widget.Spinner;
import model.BaseSpinnerModel;

public abstract class Colleague 
{
	protected Mediator mediator;

	public Colleague(Mediator mediator) 
	{
		this.mediator = mediator;
	}
	
	public abstract void setArray(BaseSpinnerModel[] array);
	public abstract void setSelection(int selection, String FromDate, String ToDate);
	public abstract void keepThisId(long id);
	public abstract String getNameColleague();
	

	public abstract Spinner getSpinner() ;
	public abstract BaseSpinnerModel[] getArrayLoading();
	public abstract Object[] getArrayFull();

	public abstract void setArrayLoading(BaseSpinnerModel[] arrayLoading);

	public abstract Context getContext() ;

	public abstract void setFull(String who) ;

	

}

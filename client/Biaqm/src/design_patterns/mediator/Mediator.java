package design_patterns.mediator;

import model.BaseSpinnerModel;


public interface Mediator
{
	public abstract void setArray(BaseSpinnerModel[] array, Colleague from);
	public abstract void setSelection(int selection, Colleague from, String FromDate, String ToDate) ;
	public abstract void keepThisId(long id, Colleague from);
	
	public void addColleague(Colleague colega);

}

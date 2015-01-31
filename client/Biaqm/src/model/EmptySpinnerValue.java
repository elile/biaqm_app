package model;

import com.example.biaqm.R;

import android.content.Context;

public class EmptySpinnerValue extends BaseSpinnerModel
{
	private Context c;

	public EmptySpinnerValue(Context c) 
	{
		this.c=c;
	}

	@Override
	public long getIdBase()
	{
		return -1;
	}

	@Override
	public String toString() 
	{
		return c.getString(R.string.select);
	}

	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return c.getString(R.string.select);
	}

}

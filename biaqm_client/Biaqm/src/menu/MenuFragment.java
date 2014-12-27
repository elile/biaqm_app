package menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.biaqm.ActionWriteActivity;
import com.example.biaqm.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.slidinglayer.SlidingLayer;

public class MenuFragment extends Fragment implements OnClickListener
{
	ButtonRectangle buttonActionWrite;
	ButtonRectangle buttonActionPreperd;
	
	ActionWriteActivity act;
	SlidingLayer mSlidingLayer;
	private ImageView ImageView_plan_selecred;
	private ImageView ImageView_write_selecred;
	
	
	public void setSlidingLayer(SlidingLayer mSlidingLayer) 
	{
		this.mSlidingLayer = mSlidingLayer;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		act = (ActionWriteActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.menu_activity, container, false);
		
		buttonActionWrite = (ButtonRectangle)v.findViewById(R.id.buttonActionWrite);
		buttonActionPreperd = (ButtonRectangle)v.findViewById(R.id.buttonActionPreperd);
		
		ImageView_plan_selecred = (ImageView)v.findViewById(R.id.ImageView_plan_selecred);
		ImageView_write_selecred = (ImageView)v.findViewById(R.id.ImageView_write_selecred);

		buttonActionPreperd.setOnClickListener(this);
		buttonActionWrite.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) 
	{
		if (act == null || mSlidingLayer == null)
		{
			return;
		}
		v.requestFocus();
		switch (v.getId()) 
		{

		case R.id.buttonActionPreperd:
			ImageView_plan_selecred.setVisibility(View.VISIBLE);
			ImageView_write_selecred.setVisibility(View.GONE);
//			Intent iPreperd= new Intent(getActivity(), ActionWriteActivity.class);
//			iPreperd.putExtra("inWrite", "0");
//			startActivity(iPreperd);
			act.setInWrite(false);
			mSlidingLayer.closeLayer(true);

			break;
		case R.id.buttonActionWrite:
			ImageView_plan_selecred.setVisibility(View.GONE);
			ImageView_write_selecred.setVisibility(View.VISIBLE);
//			Intent iWrite= new Intent(getActivity(), ActionWriteActivity.class);
//			iWrite.putExtra("inWrite", "1");
//			startActivity(iWrite);
			act.setInWrite(true);
			mSlidingLayer.closeLayer(true);
			break;
		
		}			
	}
}

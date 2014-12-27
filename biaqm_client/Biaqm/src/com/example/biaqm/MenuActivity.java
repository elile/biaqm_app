package com.example.biaqm;

import menu.MenuFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MenuActivity extends FragmentActivity
{	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity_container);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();

	}

}

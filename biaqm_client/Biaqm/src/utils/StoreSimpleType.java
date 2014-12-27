package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class StoreSimpleType 
{

		public static void putInSharedPreferences(Context c, String id, String value) 
		{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
			Editor editor = prefs.edit();
			editor.putString(id, value);
			editor.commit();
		}
		

		public static String getFromPreferences(Context c, String id) 
		{
			SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
			return prefs.getString(id, "");
		}
}

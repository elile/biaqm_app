package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class StoreObjects 
{
	// example: putInSharedPreferences(new User("eli","levi"), "eli_3", Context) 
	public static void putInSharedPreferences(Object toPut, String id, Context c) 
	{
		Gson gson = new Gson();
		String jsonStrToPost = gson.toJson(toPut);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
		Editor editor = prefs.edit();
		editor.putString(id, jsonStrToPost);
		editor.commit();
	}
	
	// example: User u = (User)getFromPreferences(new User(), "eli_3", Context) 
	public static <T> T getFromPreferences(Class<T> class1, String id, Context c) 
	{
		Gson gson = new Gson();
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
		String jsonStr = prefs.getString(id, "");
		return gson.fromJson(jsonStr, class1);
	}

}

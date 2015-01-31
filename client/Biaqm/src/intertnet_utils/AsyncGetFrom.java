package intertnet_utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncGetFrom <T> extends AsyncTask<Void, Void, T>
{
	private String url;
	private Context c;
	private Class<T> responseType;
	private OnResponse onResponse;
	public enum Method {GET, POST};
	private Method method;
	private Object toPost;
	
	
	public AsyncGetFrom (String url)
	{
		this.url = url;
	}
	
	public AsyncGetFrom setMethod(Method method)
	{
		this.method = method;
		return this;
	}
	
	public AsyncGetFrom with(Context c)
	{
		this.c = c;
		return this;
	}
	
	public AsyncGetFrom doOnResponse(OnResponse onResponse)
	{
		this.onResponse = onResponse;
		return this;
	}
	
	public AsyncGetFrom setTypeOfResponse(Class<T> responseType) 
	{
		this.responseType = responseType;
		return this;
	}
	
	public AsyncGetFrom setObjectToPost(Object toPost)
	{
		this.toPost = toPost;
		return this;
	}
	
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		((Activity)c).setProgressBarIndeterminateVisibility(true);

	}
	
	@Override
	protected T doInBackground(Void... params) 
	{
		switch (method)
		{
		case GET:
			return Crud.GET(url, responseType, 1);
		case POST:
			return Crud.POST_For_Response(url, toPost, responseType);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(T result) 
	{
		super.onPostExecute(result);
		((Activity)c).setProgressBarIndeterminateVisibility(false);
		onResponse.onResponse(result);
	}

}

package intertnet_utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import utils.DataGlobal;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Crud 
{
	// example to call: User u = (User)Crud.GET("http://...", User.Class)
	public static <T> T GET(String url, Class<T> responseType, int typeOfDateAndTime) 
	{
		//		Log.e("eli", url);
		Gson gson;
		switch (typeOfDateAndTime) 
		{
		case 1:
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
			break;
		case 2:
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
			break;
		case 3:
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			break;
		default:
			gson = new Gson();
			break;
		}
		//		try
		//		{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader(DataGlobal.TokenName, DataGlobal.TokenValue);
		HttpResponse response = null;
		try
		{
			response = httpclient.execute(request);
//			HttpParams ps= request.getParams();
//			Header[] hs = request.getAllHeaders();
//			for (Header h : hs)
//			{
////				Log.e("eli", h.getName()+ " " + h.getValue());
//			}

		} 
		catch (ClientProtocolException e) 
		{
		}
		catch (IOException e) 
		{
		}
		String returnedJson = null;
		try 
		{
			returnedJson = EntityUtils.toString(response.getEntity());
		}
		catch (ParseException e) 
		{
		} 
		catch (IOException e) 
		{
		}
		//			Log.e("eli", returnedJson);
		return gson.fromJson(returnedJson, responseType);
		//		} 
		//		catch (Exception e) 
		//		{
		//			Log.e("eli exception Cause", e.getCause().getMessage());
		//			return null;
		//		}
	}


	// example to call: User u = (User)Crud.POST("http://...", new userToPost(), new User())
	public static Object POST(String url,Object toPost, Class<?> class1) 
	{
		Gson gson = new Gson();
		String jsonStrToPost = gson.toJson(toPost);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(DataGlobal.TokenName, DataGlobal.TokenValue);
//		Log.e("eli", "TokenValue = " + DataGlobal.TokenValue);

		StringEntity se = null;
		try 
		{
			se = new StringEntity(jsonStrToPost);
		} 
		catch (UnsupportedEncodingException e) 
		{
		}
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		HttpResponse httpResponse = null;
		try 
		{
			httpResponse = httpclient.execute(httpPost);
		} 
		catch (ClientProtocolException e) 
		{
		} 
		catch (IOException e) 
		{
		}

		if (httpResponse.getStatusLine().getStatusCode() != 200)
		{
			return null;
		}
		else
		{
			String jsonStrRetorned = null;
			try 
			{
				jsonStrRetorned = EntityUtils.toString(httpResponse.getEntity());
				//				Log.e("eli json String Retorned", jsonStrRetorned);
			}
			catch (ParseException e) 
			{
			} 
			catch (IOException e) 
			{
			}
			Object obj = gson.fromJson(jsonStrRetorned,  class1);
			return obj;
		}
	}

	public static <T> T POST_For_Response(String url, Object toPost, Class<T> class1) 
	{
		Gson gson = new Gson();
		String jsonStrToPost = gson.toJson(toPost);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(DataGlobal.TokenName, DataGlobal.TokenValue);

		StringEntity se = null;
		try 
		{
			se = new StringEntity(jsonStrToPost);
		}
		catch (UnsupportedEncodingException e) {}
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		HttpResponse httpResponse = null;
		try
		{
			httpResponse = httpclient.execute(httpPost);
		}
		catch (ClientProtocolException e) 
		{
		} 
		catch (IOException e) 
		{
		}
		if (httpResponse.getStatusLine().getStatusCode() != 200)
		{
			return null;
		}
		else
		{
			String jsonStrRetorned = null;
			try 
			{
				jsonStrRetorned = EntityUtils.toString(httpResponse.getEntity());
				//				Log.e("eli json String Retorned", jsonStrRetorned);
			}
			catch (ParseException e) 
			{
			} 
			catch (IOException e) 
			{
			}
			return gson.fromJson(jsonStrRetorned,  class1);
		}
	}

	public static <T> String POST_For_Response_String(String url, Object toPost, Class<T> class1) 
	{
		Gson gson = new Gson();
		String jsonStrToPost = gson.toJson(toPost);
//		Log.e("eli", "DataGlobal.TokenValue = " + DataGlobal.TokenValue +"    " + jsonStrToPost);
		Log.e("eli", jsonStrToPost);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(DataGlobal.TokenName, DataGlobal.TokenValue);

		StringEntity se = null;
		try 
		{
			se = new StringEntity(jsonStrToPost);
		}
		catch (UnsupportedEncodingException e) {}
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		HttpResponse httpResponse = null;
		try
		{
			httpResponse = httpclient.execute(httpPost);
		}
		catch (ClientProtocolException e) 
		{
		} 
		catch (IOException e) 
		{
		}
		if (httpResponse.getStatusLine().getStatusCode() != 200)
		{
			return null;
		}
		else
		{
			String jsonStrRetorned = null;
			try 
			{
				jsonStrRetorned = EntityUtils.toString(httpResponse.getEntity());
				//				Log.e("eli json String Retorned", jsonStrRetorned);
			}
			catch (ParseException e) 
			{
			} 
			catch (IOException e) 
			{
			}
			return gson.fromJson(jsonStrRetorned,  String.class);
		}
	}



}

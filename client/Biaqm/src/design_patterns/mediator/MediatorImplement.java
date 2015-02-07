package design_patterns.mediator;
import intertnet_utils.Crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import model.ActivityGroups;
import model.ActivityType;
import model.BaseSpinnerModel;
import model.Block;
import model.Crop;
import model.EmptySpinnerValue;
import model.Plot;
import model.User;
import model.Variety;
import utils.DataGlobal;
import utils.StoreObjects;
import utils.UniversalFunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.biaqm.R;


public class MediatorImplement implements Mediator
{
	private ArrayList<Colleague> colleagues;
	private Context c;

	public MediatorImplement(Context c) 
	{
		this.colleagues = new ArrayList<Colleague>();
		this.c = c;
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
			String prevName = "";
			if (from.getSpinner() != null && from.getSpinner().getSelectedItem() != null)
			{
				prevName = from.getSpinner().getSelectedItem().toString();
			}
			from.setArrayLoading(array);
			OnItemSelectedListener onIsel = from.getSpinner().getOnItemSelectedListener();
			from.getSpinner().setOnItemSelectedListener(null);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(from.getContext(), R.layout.spinner_custom_textview, convertArryToList(array));
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			from.getSpinner().setAdapter(dataAdapter);
			if (!TextUtils.isEmpty(prevName))
			{
				for (int i = 0; i < from.getSpinner().getCount(); i++) 
				{
					String currentName = from.getSpinner().getItemAtPosition(i).toString();
					if (TextUtils.equals(currentName, prevName))
					{
							UniversalFunctions.setSelectionNoListen(from.getSpinner(), i);
					}
				}
			}
			from.getSpinner().setOnItemSelectedListener(onIsel);
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
	
	private User getCurrentConnectedUser() 
	{
		User currentConnectedUser = (User) StoreObjects.getFromPreferences(User.class, DataGlobal.CURRENT_USER, c);
		return currentConnectedUser;
	}


	@Override
	public void setSelection(int selection, Colleague from, final String FromDate, final String ToDate) 
	{
		switch (from.getNameColleague()) 
		{
		case DataGlobal.SPINNER_VERIETY_NAME:
			String selectedVarietyName = from.getSpinner().getItemAtPosition(selection).toString();
			Variety[] varietys = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, Variety[].class);
			int varietySelectedId = -1;
			int varietySelectedCropId = -1;
			if (varietys != null)
			{
				for (Variety v : varietys) 
				{
					String vName = v.getName();
					if (TextUtils.equals(vName,	selectedVarietyName)) 
					{
						varietySelectedId = v.getId();
						varietySelectedCropId = v.getCrop_id();
					}
				}
			}
			for (final Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_BLOCK_NAME:
						// load all block that has the VERIETY
						final long varietySelectedId2 = varietySelectedId;
						if (varietySelectedId2 > -1)
						{
							try 
							{
								new AsyncTask<Void, Void, Block[]>()
								{
									@Override
									protected Block[] doInBackground(Void... params) 
									{
										String UrlGetBlocksByVariety = DataGlobal.host+ DataGlobal.GetBlocksByVariety;
										UrlGetBlocksByVariety = String.format(UrlGetBlocksByVariety, varietySelectedId2,FromDate, ToDate);
										Block[] blocks = Crud.GET(UrlGetBlocksByVariety, Block[].class, 2);
										return blocks;
									}
									@Override
									protected void onPostExecute(Block[] blocks)
									{
										to.setArray(blocks);
									}
								}.execute().get();
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							} 
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							}
						}
						break;
					case DataGlobal.SPINNER_CROP_NAME:
						// select the crop of the variety
//						Crop[] crops = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Crop[].class);
						BaseSpinnerModel[] crops = to.getArrayLoading();//FIX
						if (crops != null)
						{
							for (int i = 0; i < crops.length; i++) 
							{
								if (crops[i].getIdBase() == varietySelectedCropId) 
								{
									//									to.getSpinner().setSelection(i+1);
									UniversalFunctions.setSelectionNoListen(to.getSpinner(), i);
								}
							}
						}
						break;
					case DataGlobal.SPINNER_PLOT_NAME:
						// load all plot of the variety
						final long varietySelectedId3 = varietySelectedId;
						try 
						{
							new AsyncTask<Void, Void, Plot[]>()
							{
								@Override
								protected Plot[] doInBackground(Void... params) 
								{
									String UrlGetPlotsByVariety = DataGlobal.host+ DataGlobal.GetPlotsByVariety;
									UrlGetPlotsByVariety = String.format(UrlGetPlotsByVariety, varietySelectedId3, FromDate, ToDate);
									Plot[] plots = Crud.GET(UrlGetPlotsByVariety, Plot[].class, 3);
									return plots;
								}
								@Override
								protected void onPostExecute(Plot[] blocks)
								{
									to.setArray(blocks);
								}
							}.execute().get();
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						} 
						catch (ExecutionException e) 
						{
							e.printStackTrace();
						}
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_BLOCK_NAME:
			String selectedBlockName = from.getSpinner().getItemAtPosition(selection).toString();
			Block[] blocks1 = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, Block[].class);
			long block_id = 0;
			if (blocks1 != null)
			{
				for (Block b : blocks1) 
				{
					String bName = b.getNameHe();
					if (TextUtils.equals(bName,	selectedBlockName)) 
					{
						block_id = b.getID();
					}
				}
			}

			for (final Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_CROP_NAME:
						// load the crop of the block
						final long blockIdFinal = block_id;
						if (block_id > -1)
						{
							try 
							{
								new AsyncTask<Void, Void, Crop[]>()
								{
									@Override
									protected Crop[] doInBackground(Void... params) 
									{
										String UrlGetCropByBlock = DataGlobal.host+ DataGlobal.UrlGetCropByBlock;
										UrlGetCropByBlock = String.format(UrlGetCropByBlock, blockIdFinal,FromDate, ToDate);
										Crop[] crops = Crud.GET(UrlGetCropByBlock,	Crop[].class, 1);
										return crops;
									}
									@Override
									protected void onPostExecute(Crop[] crops)
									{
										to.setArray(crops);

									}
								}.execute().get();
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							} 
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							}
						}
						break;

					case DataGlobal.SPINNER_VERIETY_NAME:
						// get variety by the block
						final long blockIdFinal2 = block_id;
						if (block_id > -1)
						{
							try 
							{
								new AsyncTask<Void, Void, Variety[]>()
								{
									@Override
									protected Variety[] doInBackground(Void... params) 
									{
										String UrlGetVarietysByBlock = DataGlobal.host+ DataGlobal.UrlGetVarietysByBlock;
										UrlGetVarietysByBlock = String.format(UrlGetVarietysByBlock, blockIdFinal2,FromDate, ToDate);
										Variety[] varietys = Crud.GET(UrlGetVarietysByBlock,	Variety[].class, 1);
										return varietys;
									}
									@Override
									protected void onPostExecute(Variety[] crops)
									{
										to.setArray(crops);
									}
								}.execute().get();
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							} 
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							}
						}
						break;
					case DataGlobal.SPINNER_PLOT_NAME:
						// load plot of the block
						Plot[] plots = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Plot[].class);
						if (plots != null) 
						{
							if (block_id > -1) 
							{
								ArrayList<Plot> plotNew = new ArrayList<Plot>();
								if (plots != null) 
								{
									for (Plot p : plots) 
									{
										if (p.getBlock_id() == block_id) 
										{
											plotNew.add(p);
										}
									}
								}
								to.setArray(plotNew.toArray(new Plot[plotNew.size()]));
							}
						}
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_CROP_NAME:
			String selectedCropName = from.getSpinner().getItemAtPosition(selection).toString();
			Crop[] crops = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, Crop[].class);
			int cropSelectedId = -1;
			if (crops != null)
			{
				for (Crop c : crops) 
				{
					String cropName = c.getName();
					if (TextUtils.equals(cropName,	selectedCropName)) 
					{
						cropSelectedId = c.getId();
					}
				}
			}
			for (final Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_BLOCK_NAME:
						// load all block of the crop
						final long cropSelectedId2 = cropSelectedId;
						if (cropSelectedId2 > -1)
						{
							try 
							{
								new AsyncTask<Void, Void, Block[]>()
								{
									@Override
									protected Block[] doInBackground(Void... params) 
									{
										String UrlGetBlocksByCrop = DataGlobal.host+ DataGlobal.GetBlocksByCrop;
										UrlGetBlocksByCrop = String.format(UrlGetBlocksByCrop, cropSelectedId2,FromDate, ToDate);
										Block[] blocks = Crud.GET(UrlGetBlocksByCrop, Block[].class, 2);
										return blocks;
									}
									@Override
									protected void onPostExecute(Block[] blocks)
									{
										to.setArray(blocks);
									}
								}.execute().get();
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							} 
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							}
						}
						break;
					case DataGlobal.SPINNER_PLOT_NAME:
						Plot[] plots = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Plot[].class);
						if (plots != null)
						{
							ArrayList<Plot> plotNew = new ArrayList<Plot>();
							for (Plot p : plots) 
							{
								if (p.getCrop_id() == cropSelectedId) 
								{
									plotNew.add(p);
								}
							}
							//							String selectedPlotName = to.getSpinner().getSelectedItem().toString();// save prev name
							to.setArray(plotNew.toArray(new Plot[plotNew.size()]));
							//							for (int i = 0; i < plotNew.size(); i++)
							//							{
							//								if (TextUtils.equals(plotNew.get(i).getName(), selectedPlotName) )
							//								{
							//									to.setSelection(i+1, FromDate, ToDate);
							//									break;
							//								}
							//							}
						}
						break;


					case DataGlobal.SPINNER_VERIETY_NAME:
						Variety[] varietys2 = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Variety[].class);
						if (varietys2 != null)
						{
							ArrayList<Variety> varietyNew = new ArrayList<Variety>();
							for (Variety v : varietys2) 
							{
								if (v.getCrop_id() == cropSelectedId) 
								{
									varietyNew.add(v);
								}
							}
							to.setArray(varietyNew.toArray(new Variety[varietyNew.size()]));
						}

						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_PLOT_NAME:
			String selectedPlotName = from.getSpinner().getItemAtPosition(selection).toString();
			Plot[] plots = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, Plot[].class);
			int plotId = -1;
			if (plots != null)
			{
				for (Plot p : plots) 
				{
					String plotName = p.getName();
					if (TextUtils.equals(plotName, selectedPlotName)) 
					{
						plotId = p.getId();
					}
				}
			}

			for (final Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_VERIETY_NAME:
						// load the variety of the plot
						final long plotIdFinal = plotId;
						if (plotId > -1)
						{
							try 
							{
								new AsyncTask<Void, Void, Variety[]>()
								{
									@Override
									protected Variety[] doInBackground(Void... params) 
									{
										String UrlGetVarietys = DataGlobal.host+ 
												DataGlobal.UrlGetVarietysRoute+ "company_id="+getCurrentConnectedUser().getCompany_id()+"&crop_id=-1&plot_id="+plotIdFinal;
										Variety[] varietys = Crud.GET(UrlGetVarietys,	Variety[].class, 1);
										return varietys;
									}
									
									@Override
									protected void onPostExecute(Variety[] crops)
									{
										to.setArray(crops);

									}
								}.execute().get();
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							} 
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							}
						}
						break;
					case DataGlobal.SPINNER_CROP_NAME:
						// load the crop of the plot
						Crop[] crops2 = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Crop[].class);
						long plotCropId = -1;
						if (plots != null)
						{
							for (Plot p : plots) 
							{
								String plotName = p.getName();
								if (TextUtils.equals(plotName, selectedPlotName)) 
								{
									plotCropId = p.getCrop_id();
								}
							}
						}
						if (crops2 != null)
						{
							ArrayList<Crop> cropNew = new ArrayList<Crop>();
							for (int i = 0; i < crops2.length; i++) 
							{
								if (crops2[i].getId() == plotCropId) 
								{
									cropNew.add(crops2[i]);
								}
							}
							to.setArray(cropNew.toArray(new Crop[cropNew.size()]));
						}
						break;
					case DataGlobal.SPINNER_BLOCK_NAME:
						// from plot to block
						BaseSpinnerModel[] blocks = to.getArrayLoading();
						long plotBlockId = -1;
						if (plots != null)
						{
							for (Plot p : plots) 
							{
								String plotName = p.getName();
								if (TextUtils.equals(plotName, selectedPlotName)) 
								{
									plotBlockId = p.getBlock_id();
								}
							}
						}
						if (blocks != null) 
						{
							for (int i = 0; i < blocks.length; i++) 
							{
								if (blocks[i].getIdBase() == plotBlockId) 
								{
									UniversalFunctions.setSelectionNoListen(to.getSpinner(), i);
								}
							}
						}
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_ACTIVITY_NAME:
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_ACTIVITY_GROUP_NAME:
						if (selection > 0) 
						{
							String selectedActivityName = from.getSpinner().getItemAtPosition(selection).toString();
							long activityGroupId = -1;
							ActivityType[] activityTypes = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, ActivityType[].class);
							ActivityGroups[] activityGroups = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, ActivityGroups[].class);
							if (activityTypes != null)
							{
								for (ActivityType a : activityTypes) 
								{
									String activityName = a.getName();
									if (TextUtils.equals(activityName,	selectedActivityName)) 
									{
										activityGroupId = a.getActivityGroup();
									}
								}
							}
							if (activityGroups != null)
							{
								for (int i = 0; i < activityGroups.length; i++) 
								{
									if (activityGroups[i].getID() == activityGroupId) 
									{
										UniversalFunctions.setSelectionNoListen(to.getSpinner(), i+1);
									}
								}
							}
						}
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
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_ACTIVITY_NAME:
						ActivityType[] activityTypes = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, ActivityType[].class);
						ActivityGroups[] activityGroups = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, ActivityGroups[].class);
						if (selection > 0)
						{
							if (activityGroups != null)
							{
								long gId = activityGroups[selection - 1].getID();
								if (gId > -1) 
								{
									ArrayList<ActivityType> ActivityTypeNew = new ArrayList<ActivityType>();
									if (activityTypes != null) 
									{
										for (ActivityType a : activityTypes) 
										{
											if (a.getActivityGroup() == gId) 
											{
												ActivityTypeNew.add(a);
											}
										}
									}
									to.setArray(ActivityTypeNew.toArray(new ActivityType[ActivityTypeNew.size()]));
								}
							}
						}
						else 
						{
							to.setArray(activityTypes);
						}
						break;
					}
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

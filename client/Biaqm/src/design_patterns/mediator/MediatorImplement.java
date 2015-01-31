package design_patterns.mediator;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.biaqm.R;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import model.ActivityGroups;
import model.ActivityType;
import model.BaseSpinnerModel;
import model.Block;
import model.Crop;
import model.EmptySpinnerValue;
import model.Plot;
import model.Variety;
import utils.DataGlobal;
import utils.UniversalFunctions;


public class MediatorImplement implements Mediator
{
	private ArrayList<Colleague> colleagues;

	public MediatorImplement() 
	{
		this.colleagues = new ArrayList<Colleague>();
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
						UniversalFunctions.setSelectionSpinner(from.getSpinner(), i );
					}
				}
			}
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


	@Override
	public void setSelection(int selection, Colleague from) 
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
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_BLOCK_NAME:
						// load all block that has the VERIETY
						break;
					case DataGlobal.SPINNER_CROP_NAME:
						Crop[] crops = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Crop[].class);
						if (crops != null)
						{
							for (int i = 0; i < crops.length; i++) 
							{
								if (crops[i].getId() == varietySelectedCropId) 
								{
									//									to.getSpinner().setSelection(i+1);
									UniversalFunctions.setSelectionSpinner(to.getSpinner(), i+1);
								}
							}
						}
						break;
					case DataGlobal.SPINNER_PLOT_NAME:
						Plot[] plots = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Plot[].class);
						ArrayList<Plot> plotNew = new ArrayList<Plot>();
						if (plots != null) 
						{
							for (Plot p : plots) 
							{
								if (p.getCrop_id() == varietySelectedCropId) 
								{
									plotNew.add(p);
								}
							}
						}
						to.setArray(plotNew.toArray(new Plot[plotNew.size()]));
						break;
					}
				}
			}
			break;
		case DataGlobal.SPINNER_BLOCK_NAME:
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_CROP_NAME:
						// load the crop of the block
						break;
					case DataGlobal.SPINNER_VERIETY_NAME:
						// load the variety of the block
						break;
					case DataGlobal.SPINNER_PLOT_NAME:
						Plot[] plots = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Plot[].class);
						Block[] blocks = Arrays.copyOf(from.getArrayFull(), from.getArrayFull().length, Block[].class);
						if (blocks != null) 
						{
							long bId = blocks[selection - 1].getID();
							if (bId > -1) 
							{
								ArrayList<Plot> plotNew = new ArrayList<Plot>();
								if (plots != null) 
								{
									for (Plot p : plots) 
									{
										if (p.getBlock_id() == bId) 
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
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{
					case DataGlobal.SPINNER_BLOCK_NAME:
						// load all block of the crop
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
							String selectedPlotName = to.getSpinner().getSelectedItem().toString();// save prev name
							to.setArray(plotNew.toArray(new Plot[plotNew.size()]));
							for (int i = 0; i < plotNew.size(); i++)
							{
								if (TextUtils.equals(plotNew.get(i).getName(), selectedPlotName) )
								{
									to.setSelection(i+1);
									break;
								}
							}
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
			
			for (Colleague to : colleagues) 
			{
				if (to != from)
				{
					switch (to.getNameColleague()) 
					{

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
							for (int i = 0; i < crops2.length; i++) 
							{
								if (crops2[i].getId() == plotCropId) 
								{
									UniversalFunctions.setSelectionSpinner(to.getSpinner(), i+1);
								}
							}
						}
						break;
					case DataGlobal.SPINNER_BLOCK_NAME:
						Block[] blocks = Arrays.copyOf(to.getArrayFull(), to.getArrayFull().length, Block[].class);
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
								if (blocks[i].getID() == plotBlockId) 
								{
									UniversalFunctions.setSelectionSpinner(to.getSpinner(), i+1);
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
										UniversalFunctions.setSelectionSpinner(to.getSpinner(), i+1);
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

package utils;


public class DataGlobal 
{
	public static final String host = "http://10.0.3.2/";
	//	private String host = "http://10.0.3.2/"; /* http://10.0.3.2/ on geny | http://10.0.2.2/ on emulator*/
	//	private String host = "http://192.168.1.100/"; /* http://10.0.3.2/ on geny | http://10.0.2.2/ on emulator*/
	
	
	public static final String UrlGetActivityGroupsRoute = "api/ActivityGroups/GetActivityGroups?";
	public static final String UrlGetActivityTypesRoute = "api/ActivityTypes/GetActivityTypes?";
	public static final String UrlGetBlocksRoute = "api/Blocks/GetBlocks?";
	public static final String UrlGetPlotsRoute = "api/Plots/GetPlots?";
	public static final String UrlGetCropsRoute = "api/Crop/GetCrops?";
	public static final String UrlGetVarietysRoute = "api/Variety/GetVarietys?";
	public static final String UrlGetWorkerRoute = "api/Worker/GetWorkers?";
	public static final String UrlGetMachineryRoute = "api/motoring_machinery/Getmotoring_machinerys?";
	public static final String UrlGetYield_DescriptionsRoute =  "api/Yield_Descriptions/GetYield_Descriptions?";
	public static final String UrlPostInsertActivityWithRoute =  "api/InsertActivityFromPost/InsertActivity";
	public static final String UrlGetFarms =  "/api/Farm/GetFarms?company_id=%s";
	public static final String UrlGetCropByBlock = "api/Crop/GetCropByBlock?block_id=%s&FromDate=%s&ToDate=%s";
	public static final String UrlGetVarietysByBlock = "api/Variety/GetVarietysByBlock?block_id=%s&FromDate=%s&ToDate=%s";
	public static final String GetBlocksByCrop = "api/Blocks/GetBlocksByCrop?crop_id=%s&FromDate=%s&ToDate=%s";
	public static final String GetBlocksByVariety = "api/Blocks/GetBlocksByVariety?variety_id=%s&FromDate=%s&ToDate=%s";
	public static final String GetPlotsByVariety = "api/Plots/GetPlotsByVariety?variety_id=%s&start_date=%s&end_date=%s";



	//	private String UrlGetTrailing_machine = host+"api/Trailing_machine/GetTrailing_machines?company_id=";

	public static final String TokenName = "X-Token";
	public static String TokenValue = "";


	public static final String YieldDescription = "YieldDescription";
	public static final String ActualPrice = "ActualPrice";
	public static final String CURRENT_USER = "CURRENT_USER";
	public static final String DATE_KEY = "DATE_KEY";
	public static final String SELECTED_ITEM_KEY = "SELECTED_ITEM_KEY";
	public static final String WORKER_RETURNED_KEY = "WORKER_RETURNED_KEY";
	public static final String MACHINE_RETURNED_KEY = "MACHINE_RETURNED_KEY";
	public static final String NUM_OF_YIELD_DESC_WORKER_ADVANCED = "NUM_OF_YIELD_DESC_WORKER_ADVANCED";
	public static final String REMARKS_WORKER_ADVANCED = "REMARKS_WORKER_ADVANCED";
	public static final String TOTAL_TIME_WORKER_ADVANCED = "TOTAL_TIME_WORKER_ADVANCED";
	public static final String WORKER_ADVANCED = "WORKER_ADVANCED";
	public static final String WORKER_HOURS = "WORKER_HOURS";
	public static final String MACHINE_ADVANCED = "MACHINE_ADVANCED";
	public static final String MACHINE_HOURS = "MACHINE_HOURS";
	
	
	public static final String SPINNER_ACTIVITY_NAME = "SPINNER_ACTIVITY_NAME";
	public static final String SPINNER_ACTIVITY_GROUP_NAME = "SPINNER_ACTIVITY_GROUP_NAME";
	public static final String SPINNER_BLOCK_NAME = "SPINNER_BLOCK_NAME";
	public static final String SPINNER_PLOT_NAME = "SPINNER_PLOT_NAME";
	public static final String SPINNER_CROP_NAME = "SPINNER_CROP_NAME";
	public static final String SPINNER_VERIETY_NAME = "SPINNER_VERIETY_NAME";
	public static final String SPINNER_FARM_NAME = "SPINNER_FARM_NAME";


}

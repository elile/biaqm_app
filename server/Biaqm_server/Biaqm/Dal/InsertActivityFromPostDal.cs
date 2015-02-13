using Biaqm.Models.MyModel;
using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class InsertActivityFromPostDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
        public static string prefix = "[biaqm].[dbo].";

        public static Int64 InsertActivity(InsertActivityFromPost insertActivityFromPost, Boolean inWrite)
        {
            Int64 ActivityInID = -1;
            Int64 ActivityPlanID = -1;
            Int64 rowAffected2 = 0;
            Int64 rowAffected3 = 0;
            Int64 rowAffected4 = 0;
            Int64 rowAffected6 = 0;
            string area = "-1";
            String ToDate = "CONVERT(varchar, '" + HttpContext.Current.Server.UrlDecode(insertActivityFromPost.date) + "', 103)";
            String FromDate = ToDate;

            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                connection.Open();
                // Start a local transaction.
                SqlTransaction sqlTran = connection.BeginTransaction();
                // Enlist a command in the current transaction.
                SqlCommand cmd = connection.CreateCommand();
                cmd.Transaction = sqlTran;
                try
                {
                    // Execute 1
                    cmd.CommandType = CommandType.Text;
                    cmd.CommandText = BuildActivityPlanQStr(
                                        FromDate,
                                        ToDate,
                                        insertActivityFromPost.ActivityType_id,
                                        insertActivityFromPost.CompanyID,
                                        insertActivityFromPost.farm_id,
                                        insertActivityFromPost.Remarks,
                                        ActivityPlanID.ToString());
                    object idPlan = cmd.ExecuteScalar();
                    if (idPlan != null)
                    {
                        ActivityPlanID = (Int64)idPlan;
                    }
                    else
                    {
                        sqlTran.Rollback();
                        return 0;
                    }

                    // Execute 2 - no need on plan
                    if (inWrite)
                    {
                        cmd.CommandText = BuildActivityQStr(
                                        FromDate,
                                        ToDate,
                                        insertActivityFromPost.ActivityType_id,
                                        insertActivityFromPost.CompanyID,
                                        insertActivityFromPost.farm_id,
                                        insertActivityFromPost.Remarks,
                                        ActivityPlanID.ToString());
                        object idActivity = cmd.ExecuteScalar();
                        if (idActivity != null)
                        {
                            ActivityInID = (Int64)idActivity;
                        }
                        else
                        {
                            sqlTran.Rollback();
                            return 0;
                        }
                    }
                    else
                    {
                        ActivityInID = ActivityPlanID;
                    }

                    // Execute 3
                    area = CalculateArea(
                        HttpContext.Current.Server.UrlDecode( insertActivityFromPost.date),
                        insertActivityFromPost.BlockID,
                        insertActivityFromPost.PlotID,
                        insertActivityFromPost.CropID,
                        insertActivityFromPost.VarietyID,
                        cmd);
                    cmd.CommandText = BuildPlotQueryString(
                        ActivityInID.ToString(),
                        area,
                        insertActivityFromPost.BlockID,
                        insertActivityFromPost.PlotID,
                        insertActivityFromPost.CropID,
                        insertActivityFromPost.VarietyID);
                    if (cmd.CommandText != "")
                    {
                        rowAffected2 = (Int64)cmd.ExecuteNonQuery();
                        if (rowAffected2 <= 0 || area == "-1")
                        {
                            sqlTran.Rollback();
                            return 0;
                        }
                    }

                    // Execute 4
                    for (int i = 0; i < insertActivityFromPost.workerToInsert.Length; i++)
                    {
                        cmd.CommandText = BuildWorkerQueryString(ActivityInID.ToString(), insertActivityFromPost, insertActivityFromPost.workerToInsert[i], new SqlConnection(connectionString));
                        if (cmd.CommandText != "")
                        {
                            if (insertActivityFromPost.workerToInsert[i].id != -1)
                            {
                                rowAffected3 = (Int64)cmd.ExecuteNonQuery();
                            }
                            else
                            {
                                rowAffected3 = 1;
                            }
                            if (rowAffected3 <= 0)
                            {
                                sqlTran.Rollback();
                                return 0;
                            }
                        }
                    }

                    // Execute 5
                    for (int i = 0; i < insertActivityFromPost.machineToInsert.Length; i++)
                    {
                        cmd.CommandText = BuildMachineryQueryString(
                                    ActivityInID.ToString(),
                                    insertActivityFromPost.machineToInsert[i]);
                        if (cmd.CommandText != "")
                        {
                            if (insertActivityFromPost.machineToInsert[i].id != -1)
                            {
                                rowAffected4 = (Int64)cmd.ExecuteNonQuery();
                            }
                            else
                            {
                                rowAffected4 = 1;
                            }
                            if (rowAffected4 <= 0)
                            {
                                sqlTran.Rollback();
                                return 0;
                            }
                        }
                    }

                    // Execute 6
                    cmd.CommandText = UpdateActivityCostQuery(ActivityInID.ToString());
                    if (cmd.CommandText != "")
                    {
                        rowAffected6 = (Int64)cmd.ExecuteNonQuery();
                    }
                    if (rowAffected6 <= 0)
                    {
                        sqlTran.Rollback();
                        return 0;
                    }
                    // Commit the transaction.
                    sqlTran.Commit();
                }
                catch (Exception ex)
                {
                    try
                    {
                        sqlTran.Rollback();
                        return 0;
                    }
                    catch (Exception exRollback)
                    {
                        return 0;
                    }
                }
            }
            return 1;
        }



        private static string BuildActivityPlanQStr(String FromDate, String ToDate, String ActivityType_id, String CompanyID, String farm_id, String Remarks, String ActivityPlanID)
        {
            //String ToDate = "Convert(varchar, '" + DateAndTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + "', 103)";
            //String FromDate = ToDate;
            return string.Format("INSERT INTO " + prefix + "[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) OUTPUT INSERTED.ID VALUES ({0} , {1}, '{2}', '{3}', '{4}', 0, 0, 0, null, '{5}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks);
        }

        private static string BuildActivityQStr(String FromDate, String ToDate, String ActivityType_id, String CompanyID, String farm_id, String Remarks, String ActivityPlanID)
        {
            return string.Format("INSERT INTO " + prefix + "[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) OUTPUT INSERTED.ID VALUES ({0}, {1}, '{2}', '{3}', '{4}', 0, 0, 1, '{6}', '{5}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks, ActivityPlanID);
        }

        private static string CalculateArea(string dDate, string BlockID, string PlotID, string CropID, string VarietyID, SqlCommand cmd)
        {
            string query = string.Empty;
            string dDateQuery = " AND ( (P.[start_date] IS NULL";
            dDateQuery = dDateQuery + " OR P.[start_date] <= CONVERT(varchar, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " AND (P.[end_date] IS NULL";
            dDateQuery = dDateQuery + " OR P.[end_date] >= CONVERT(varchar, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " OR (P.[start_date] >= CONVERT(varchar, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " AND (P.[start_date] <= CONVERT(varchar, '" + dDate + "', 103) ) )";

            string dVarietyDateQuery = " AND ( (VP.[StartDate] IS NULL";
            dVarietyDateQuery = dVarietyDateQuery + " OR VP.[StartDate] <= CONVERT(varchar, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " AND (VP.[EndDate] IS NULL";
            dVarietyDateQuery = dVarietyDateQuery + " OR VP.[EndDate] >= CONVERT(varchar, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " OR (VP.[StartDate] >= CONVERT(varchar, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " AND (VP.[StartDate] <= CONVERT(varchar, '" + dDate + "', 103) ) )";

            if (!Information.IsNumeric(VarietyID) & !Information.IsNumeric(CropID))
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT CASE WHEN P.[PerimeterArea] IS NULL THEN P.[NetArea] ELSE P.[PerimeterArea] END AS [Area]  ";
                    query = query + " FROM " + prefix + "[plot] AS P ";
                    query = query + " WHERE [id] = " + PlotID;
                    query = query + dDateQuery;
                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(CASE WHEN P.[PerimeterArea] IS NULL THEN P.[NetArea] ELSE P.[PerimeterArea] END) AS [Area]  ";
                    query = query + " FROM " + prefix + "[plot] AS P ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + dDateQuery;
                }
            }
            else if (Information.IsNumeric(VarietyID))
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE P.[id] = " + PlotID;
                    query = query + " AND VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + " AND ( [StartDate] IS NULL OR [StartDate] <= CONVERT(varchar,'" + dDate + "',103) ) ";
                    query = query + " AND ( [EndDate] IS NULL OR [EndDate] >= CONVERT(varchar,'" + dDate + "',103) ) ";

                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + " AND VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + " AND ( [StartDate] IS NULL OR [StartDate] <= CONVERT(varchar,'" + dDate + "',103) ) ";
                    query = query + " AND ( [EndDate] IS NULL OR [EndDate] >= CONVERT(varchar,'" + dDate + "',103) ) ";
                }
                else
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }

            }
            else
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN " + prefix + "[variety] AS V ON V.[id] = VP.[variety_id] ";
                    query = query + " WHERE P.[id] = " + PlotID;
                    query = query + " AND V.[crop_id] = " + CropID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN " + prefix + "[variety] AS V ON V.[id] = VP.[variety_id] ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + " AND V.[crop_id] = " + CropID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }
                else
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM " + prefix + "[variety_to_plot] AS VP ";
                    query = query + " INNER JOIN " + prefix + "[plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN " + prefix + "[variety] AS V ON V.[id] = VP.[variety_id] ";
                    query = query + " WHERE V.[crop_id] = " + CropID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }
            }
            string Area = "0";
            if (query != string.Empty)
            {
                string connectionString = GetConnectionString();
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand exe = new SqlCommand(query, connection);
                    SqlDataReader reader = exe.ExecuteReader();
                    if (reader.HasRows)
                    {
                        reader.Read();
                        if (Information.IsNumeric(ConvertDBNullToString(reader["Area"])))
                        {
                            Area = Convert.ToDouble(reader["Area"]).ToString("0.###");
                        }
                    }
                    reader.Close();
                }
            }
            return Area;
        }

        private static string BuildPlotQueryString(string ActivityID, string TotalArea/*CalculateArea*/, string Block, string Plot, string Crop, string Variety)
        {
            string query = string.Empty;

            if (!(Information.IsNumeric(Plot)))
            {
                Plot = " null ";
            }

            if (!(Information.IsNumeric(Block)))
            {
                Block = " null ";
            }

            if (!(Information.IsNumeric(Crop)))
            {
                Crop = " null ";
            }

            if (!(Information.IsNumeric(Variety)))
            {
                Variety = " null ";
            }

            query = "INSERT INTO " + prefix + "[Plots_to_activities] ([Activity_id] ," + prefix + "[plot] ";
            query = query + "  ,[Rows] ,[Trees] ";
            query = query + "  ,[Variety_id] ,[crop] ";
            query = query + "  ,[region] ," + prefix + "[Block],[area] ";
            query = query + "  ,[Coordinates]  ,[Notes] ";
            query = query + " ) ";

            if (Information.IsNumeric(Plot))
            {
                query = query + " SELECT " + ActivityID + " , " + Plot;
                query = query + " , null , null ";
                query = query + " , " + Variety + " , " + Crop;
                query = query + " , [region_id] , [block_id] , " + TotalArea;
                query = query + " , null , '' ";
                query = query + " FROM " + prefix + "[plot] ";
                query = query + " WHERE [id] = " + Plot;
            }
            else if (Information.IsNumeric(Block))
            {
                query = query + " SELECT " + ActivityID + " , null ";
                query = query + " , null , null ";
                query = query + " , " + Variety + " , " + Crop;
                query = query + " , [RegionID] , " + Block + " , " + TotalArea;
                query = query + " , null , '' ";
                query = query + " FROM " + prefix + "[Block]";
                query = query + " WHERE [ID] = " + Block;
            }
            else if (Information.IsNumeric(Variety) | Information.IsNumeric(Crop))
            {
                query = query + " VALUES ( " + ActivityID + " , null ";
                query = query + " , null , null ";
                query = query + " , " + Variety + " , " + Crop;
                query = query + " , null , null , " + TotalArea;
                query = query + " , null , '' ) ";
            }
            else
            {
                query = string.Empty;
            }

            return query;
        }


        private static string BuildWorkerQueryString(string ActivityID, InsertActivityFromPost activityToInsert, WorkerToInsert w, SqlConnection connection)
        {
            string totalTimeStr = w.totalTime;
            double totalTime = Convert.ToDouble(totalTimeStr);
            string WorkerID = w.id.ToString();
            if (WorkerID == "-1") return "-1";
            string FromDate = HttpContext.Current.Server.UrlDecode( activityToInsert.date);
            string query = string.Empty;
            SqlDataReader reader = null;
            string WorkersQuantity = "1";
            double WorkerCost = 0;
            double standardHours = 0;
            double Overtime1 = 0;
            double Overtime2 = 0;
            double Night = 0;

            if (Information.IsNumeric(WorkerID))
            {
                query = " SELECT [PaymentTypeID], [standard_hours], [first_overtime] ";
                query = query + " , [second_overtime], [first_overtime_hours], [night_work_FromHour]";
                query = query + " , [night_work_ToHour], [hour_cost] ";
                query = query + " From " + prefix + "[worker] WHERE [id] = " + WorkerID;
                SqlCommand cmd = new SqlCommand(query, connection/*, tx*/);
                connection.Open();
                reader = cmd.ExecuteReader();
                reader.Read();

                switch (reader["PaymentTypeID"].ToString())
                {
                    case "1":
                        if (totalTime != 0)
                        {
                            if ((totalTime > Convert.ToDouble(reader["standard_hours"])))
                            {
                                standardHours = Convert.ToDouble(reader["standard_hours"]);
                                totalTime = totalTime - standardHours;
                                if (totalTime > 0)
                                {
                                    if ((totalTime > Convert.ToDouble(reader["first_overtime_hours"])))
                                    {
                                        Overtime1 = Convert.ToDouble(reader["first_overtime_hours"]);
                                        totalTime = totalTime - Overtime1;
                                        Overtime2 = totalTime;
                                    }
                                    else
                                    {
                                        Overtime1 = totalTime;
                                    }
                                }
                            }
                            else
                                standardHours = totalTime;
                            double hour_cost = 0;
                            double first_overtime = 0;
                            double second_overtime = 0;
                            if (Information.IsNumeric(reader["hour_cost"]))
                                hour_cost = Convert.ToDouble(reader["hour_cost"]);
                            if (Information.IsNumeric(reader["first_overtime"]))
                                first_overtime = Convert.ToDouble(reader["first_overtime"]);
                            if (Information.IsNumeric(reader["second_overtime"]))
                                second_overtime = Convert.ToDouble(reader["second_overtime"]);
                            WorkerCost = hour_cost * standardHours + hour_cost * first_overtime / 100 * Overtime1 + hour_cost * second_overtime / 100 * Overtime2;
                        }
                        break;
                    default:
                        standardHours = totalTime;
                        break;
                }
                reader.Close();
                connection.Close();
            }
            if (w.workerAdvancedResult == null)
            {
                query = " INSERT INTO " + prefix + "[Activity_workers] ";
                query = query + "        ([activity_id]     , [worker_id]     , [WorkersQuantity] " + " , [WorkingDate]                                   , [StartWorkingHour]      , [EndWorkingHour]" + "            , [standard_hours]                 , [first_overtime]            , [second__overtime]          , [night_hours]               , [YieldUnit_Cost]" + ", [Yield_Units]        , [YieldDescription]  , [workers_cost]                , [Remarks]) ";
                query = query + " VALUES (" + ActivityID + ", " + WorkerID + ", " + WorkersQuantity + " , CONVERT(varchar, '" + FromDate + "', 103) " + " , '0:0'" + "              , '0:0'" + "                       , " + standardHours.ToString() + " ," + Overtime1.ToString() + " ," + Overtime2.ToString() + " , " + Night.ToString() + "    , null                 , null                 , null " + "          , " + WorkerCost.ToString() + " , ''       )";
                return query;
            }
            else
            {
                WorkerAdvancedResult wAdv = w.workerAdvancedResult;
                string YieldDescription = "null";
                if (wAdv.yd != null)
                {
                    YieldDescription = wAdv.yd.ID.ToString();
                }
                query = " INSERT INTO " + prefix + "[Activity_workers] ";
                query = query + "        ([activity_id]     , [worker_id]     ,     [WorkersQuantity] " + " , [WorkingDate]                                   ,     [StartWorkingHour]              ,  [EndWorkingHour]    " + "        , [standard_hours]                 , [first_overtime]            , [second__overtime]          , [night_hours]               , [YieldUnit_Cost]" + "    , [Yield_Units]            ,  [YieldDescription]     ,     [workers_cost]            , [Remarks]) ";
                query = query + " VALUES (" + ActivityID + ", " + WorkerID + ", '" + wAdv.workerQuantity + "' , CONVERT(varchar, '" + FromDate + "', 103) " + " , '" + wAdv.StartWorkingHour + "', '" + wAdv.EndWorkingHour + "', '" + standardHours.ToString() + "' ,'" + Overtime1.ToString() + "' ,'" + Overtime2.ToString() + "' , '" + Night.ToString() + "'    , '" + wAdv.costOfYd + "'    , '" + wAdv.numOfYd + "'     , " + YieldDescription + ", '" + WorkerCost.ToString() + "' , '" + wAdv.remarks + "' )";
                return query;
            }
        }



        private static string BuildMachineryQueryString(string ActivityID, MachineToInsert machineToInsert)
        {
            long MachineryIDlong = machineToInsert.id;
            string MachineryHours = machineToInsert.totalTime;
            string query = string.Empty;
            string MachineryID = MachineryIDlong.ToString();
            if (Information.IsNumeric(MachineryID))
            {
                //string MachineryHours = txtHours.Text;

                if (!Information.IsNumeric(MachineryHours))
                {
                    MachineryHours = "0";
                }

                if (machineToInsert.machineAdvanceResult == null)
                {
                    query = " INSERT INTO " + prefix + "[activity_machinery] ([activity_id], [machinery_id], [machinery_hours] " + ", [machinery_cost], [YieldUnit_Cost] " + ", [Yield_Units], [YieldDescription], [Remarks]) ";
                    query = query + " SELECT " + ActivityID + ", " + MachineryID + ", " + MachineryHours;
                    query = query + " ," + MachineryHours + " * M.[hour_price] , null ";
                    query = query + " , null , null ";
                    query = query + " , ''";
                    query = query + " FROM " + prefix + "[machine] AS M";
                    query = query + " WHERE M.[id] = " + MachineryID;
                }
                else
                {
                    MachineAdvancedResult machineAdvance = machineToInsert.machineAdvanceResult;
                    String machinery_cost = machineAdvance.m.hour_price + "";
                    String YieldUnit_Cost = machineAdvance.costOfYd + "";
                    String Yield_Units = machineAdvance.numOfYd + "";
                    String YieldDescription = "null";
                    if (machineAdvance.yd != null)
                    {
                        YieldDescription = machineAdvance.yd.ID + "";
                    }
                    String Remarks = machineAdvance.remarks;

                    query = " INSERT INTO " + prefix + "[activity_machinery] ([activity_id]  , [machinery_id] , [machinery_hours] " + ", [machinery_cost], [YieldUnit_Cost] " + ", [Yield_Units], [YieldDescription], [Remarks]) ";
                    query = query + " SELECT " + ActivityID + ", " + MachineryID + ", " + MachineryHours + ", " + machinery_cost + ", " + YieldUnit_Cost + ", " + Yield_Units + ", " + YieldDescription + ", '" + Remarks + "'";
                    query = query + " FROM " + prefix + "[machine] AS M";
                    query = query + " WHERE M.[id] = " + MachineryID;
                }
            }

            return query;
        }

        private static string UpdateActivityCostQuery(string ActivityID)
        {
            string query = " UPDATE " + prefix + "[Activities] ";
            query = query + " SET [ActivityCost] = COALESCE((SELECT SUM(W.workers_cost) ";
            query = query + " FROM " + prefix + "[Activity_workers] AS W ";
            query = query + " WHERE W.[activity_id] = " + prefix + "[Activities].[id]), 0) ";
            query = query + " + COALESCE((SELECT SUM(M.machinery_cost) ";
            query = query + " FROM " + prefix + "[activity_machinery] AS M ";
            query = query + " WHERE M.[activity_id] = " + prefix + "[Activities].[id]), 0) ";
            query = query + " + COALESCE(" + prefix + "[Activities].[ItemCount] * " + prefix + "[Activities].[ItemPrice] ";
            query = query + ", 0) WHERE " + prefix + "[Activities].[id] = " + ActivityID;

            return query;
        }


        private static object ConvertDBNullToString(object Area)
        {
            return (Area == DBNull.Value) ? "null" : Area;
        }

        private static string GetConnectionString()
        {
            return ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
        }


    }
}
using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Transactions;
using System.Web;

namespace Biaqm.Dal
{
    public class InsertActivityDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static TransactionOptions option = new TransactionOptions
        {
            IsolationLevel = System.Transactions.IsolationLevel.ReadCommitted,
            Timeout = TimeSpan.FromSeconds(20)
        };

        public static Int64 InsertActivityPlan(
           String FromDate,
            String ToDate,
            String ActivityType_id,
            String CompanyID,
            String farm_id,
            String Remarks,
            String BlockID,
            String PlotID,
            String CropID,
            String VarietyID,
            String WorkerID,
            double totalTime,
            String MachineryID,
            String MachineryHours,
            String MotorizedMachineryID,
            String MotorizedMachineryHours
            )
        {
            Int64 ActivityPlanID = -1;
            object idPlan;

            //FromDate = ToDate = Strings.Format(DateAndTime.Now, "yyyy-MM-dd HH:mm:ss");
            FromDate = ToDate = "Convert(DateTime, '" + DateAndTime.Now.ToString("dd/MM/yyyy HH:mm:ss") + "', 103)";

            Int64 rowAffected2 = 0;
            Int64 rowAffected3 = 0;
            Int64 rowAffected4 = 0;
            Int64 rowAffected5 = 0;
            Int64 rowAffected6 = 0;

            string area = "-1";

            using (var scopeOuter = new TransactionScope(TransactionScopeOption.Required, option))
            {
                using (var con = new SqlConnection(connectionString))
                {
                    con.Open();
                    using (SqlCommand cmd = con.CreateCommand())
                    {
                        cmd.CommandType = CommandType.Text;
                        cmd.CommandText = BuildActivityPlanQStr(FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks, ActivityPlanID.ToString());
                        idPlan = cmd.ExecuteScalar();
                        if (idPlan != null)
                            ActivityPlanID = (Int64)idPlan;
                        else
                            ActivityPlanID = -1;
                    }
                }
                using (var scopeInner2 = new TransactionScope(TransactionScopeOption.Required, option))
                {
                    using (var con = new SqlConnection(connectionString))
                    {
                        con.Open();
                        using (SqlCommand cmd = con.CreateCommand())
                        {
                            area = CalculateArea(BlockID, PlotID, CropID, VarietyID, FromDate);
                            cmd.CommandType = CommandType.Text;
                            cmd.CommandText = BuildPlotQueryString(ActivityPlanID.ToString(), area, BlockID, PlotID, CropID, VarietyID);
                            if (cmd.CommandText != "")
                            {
                                rowAffected2 = (Int64)cmd.ExecuteNonQuery();
                            }
                        }
                    }
                    using (var scopeInner3 = new TransactionScope(TransactionScopeOption.Required, option))
                    {
                        using (var con = new SqlConnection(connectionString))
                        {
                            con.Open();
                            using (SqlCommand cmd = con.CreateCommand())
                            {
                                cmd.CommandType = CommandType.Text;
                                cmd.CommandText = BuildWorkerQueryString(ActivityPlanID.ToString(), WorkerID, FromDate, totalTime, con);
                                if (cmd.CommandText != "")
                                {
                                    if (WorkerID != "-1")
                                    {
                                        rowAffected3 = (Int64)cmd.ExecuteNonQuery();
                                    }
                                    else
                                    {
                                        rowAffected3 = 1;
                                    }
                                }
                            }
                        }
                        using (var scopeInner4 = new TransactionScope(TransactionScopeOption.Required, option))
                        {
                            using (var con = new SqlConnection(connectionString))
                            {
                                con.Open();
                                using (SqlCommand cmd = con.CreateCommand())
                                {
                                    cmd.CommandType = CommandType.Text;
                                    cmd.CommandText = BuildMachineryQueryString(ActivityPlanID.ToString(), MachineryID, MachineryHours);
                                    if (cmd.CommandText != "")
                                    {
                                        if (MachineryID != "-1")
                                        {
                                            rowAffected4 = (Int64)cmd.ExecuteNonQuery();
                                        }
                                        else
                                        {
                                            rowAffected4 = 1;
                                        }
                                    }
                                }
                            }
                            using (var scopeInner5 = new TransactionScope(TransactionScopeOption.Required, option))
                            {
                                using (var con = new SqlConnection(connectionString))
                                {
                                    con.Open();
                                    using (SqlCommand cmd = con.CreateCommand())
                                    {
                                        cmd.CommandType = CommandType.Text;
                                        cmd.CommandText = BuildMachineryQueryString(ActivityPlanID.ToString(), MotorizedMachineryID, MotorizedMachineryHours);
                                        if (cmd.CommandText != "")
                                        {
                                            if (MotorizedMachineryID != "-1")
                                            {
                                                rowAffected5 = (Int64)cmd.ExecuteNonQuery();
                                            }
                                            else
                                            {
                                                rowAffected5 = 1;
                                            }
                                        }
                                    }
                                }
                                using (var scopeInner6 = new TransactionScope(TransactionScopeOption.Required, option))
                                {
                                    using (var con = new SqlConnection(connectionString))
                                    {
                                        con.Open();
                                        using (SqlCommand cmd = con.CreateCommand())
                                        {
                                            cmd.CommandType = CommandType.Text;
                                            cmd.CommandText = UpdateActivityCostQuery(ActivityPlanID.ToString());
                                            if (cmd.CommandText != "")
                                            {
                                                rowAffected6 = (Int64)cmd.ExecuteNonQuery();
                                            }
                                        }
                                    }
                                    // inner here
                                    if (rowAffected6 > 0)
                                    {
                                        scopeInner6.Complete();
                                    }
                                    else
                                        return 0;
                                }
                                if (rowAffected5 > 0)
                                {
                                    scopeInner5.Complete();
                                }
                                else
                                    return 0;
                            }
                            if (rowAffected4 > 0)
                            {
                                scopeInner4.Complete();
                            }
                            else
                                return 0;
                        }
                        if (rowAffected3 > 0)
                        {
                            scopeInner3.Complete();
                        }
                        else
                            return 0;
                    }
                    if (rowAffected2 > 0 && area != "-1")
                    {
                        scopeInner2.Complete();
                    }
                    else
                        return 0;
                }
                if (ActivityPlanID > 0)
                {
                    //the outer transaction is committed first, then the inner 1 2 3... if one not commit it roll back
                    scopeOuter.Complete();
                    return 1;
                }
                else
                    return 0;
            }
        }

        public static int InsertActivity(
            String FromDate,
            String ToDate,
            String ActivityType_id,
            String CompanyID,
            String farm_id,
            String Remarks,
            String BlockID,
            String PlotID,
            String CropID,
            String VarietyID,
            String WorkerID,
            double totalTime,
            String MachineryID,
            String MachineryHours,
            String MotorizedMachineryID,
            String MotorizedMachineryHours
            )
        {
            Int64 ActivityPlanID = -1;
            Int64 ActivityInID = -1;
            object idPlan;
            object idActivity;

            Int64 rowAffected2 = 0;
            Int64 rowAffected3 = 0;
            Int64 rowAffected4 = 0;
            Int64 rowAffected5 = 0;
            Int64 rowAffected6 = 0;

            //FromDate = ToDate = Strings.Format(DateAndTime.Now, "yyyy-MM-dd HH:mm:ss");
            FromDate = ToDate = "Convert(DateTime, '" + DateAndTime.Now.ToString("dd/MM/yyyy HH:mm:ss") + "', 103)";

            
            string area = "-1";

            using (var scopeOuter = new TransactionScope(TransactionScopeOption.Required, option))
            {
                using (var con = new SqlConnection(connectionString))
                {
                    con.Open();
                    using (SqlCommand cmd = con.CreateCommand())
                    {
                        cmd.CommandType = CommandType.Text;
                        cmd.CommandText = BuildActivityPlanQStr(FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks, ActivityPlanID.ToString());
                        idPlan = cmd.ExecuteScalar();
                        if (idPlan != null)
                            ActivityPlanID = (Int64)idPlan;
                        else
                            ActivityPlanID = -1;
                    }
                }
                using (var scopeInner1 = new TransactionScope(TransactionScopeOption.Required, option))
                {
                    using (var con = new SqlConnection(connectionString))
                    {
                        con.Open();
                        using (SqlCommand cmd = con.CreateCommand())
                        {
                            cmd.CommandType = CommandType.Text;
                            cmd.CommandText = BuildActivityQStr(FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks, ActivityPlanID.ToString());
                            idActivity = cmd.ExecuteScalar();
                            if (idActivity != null)
                                ActivityInID = (Int64)idActivity;
                            else
                                ActivityInID = -1;
                        }
                    }
                    using (var scopeInner2 = new TransactionScope(TransactionScopeOption.Required, option))
                    {
                        using (var con = new SqlConnection(connectionString))
                        {
                            con.Open();
                            using (SqlCommand cmd = con.CreateCommand())
                            {
                                area = CalculateArea(BlockID, PlotID, CropID, VarietyID, FromDate);
                                cmd.CommandType = CommandType.Text;
                                cmd.CommandText = BuildPlotQueryString(ActivityInID.ToString(), area, BlockID, PlotID, CropID, VarietyID);
                                if (cmd.CommandText != "")
                                {
                                    rowAffected2 = (Int64)cmd.ExecuteNonQuery();
                                }
                            }
                        }
                        using (var scopeInner3 = new TransactionScope(TransactionScopeOption.Required, option))
                        {
                            using (var con = new SqlConnection(connectionString))
                            {
                                con.Open();
                                using (SqlCommand cmd = con.CreateCommand())
                                {
                                    cmd.CommandType = CommandType.Text;
                                    cmd.CommandText = BuildWorkerQueryString(ActivityInID.ToString(), WorkerID, FromDate, totalTime, con);
                                    if (cmd.CommandText != "")
                                    {
                                        if (WorkerID != "-1")
                                        {
                                            rowAffected3 = (Int64)cmd.ExecuteNonQuery();
                                        }
                                        else
                                        {
                                            rowAffected3 = 1;
                                        }
                                    }
                                }
                            }
                            using (var scopeInner4 = new TransactionScope(TransactionScopeOption.Required, option))
                            {
                                using (var con = new SqlConnection(connectionString))
                                {
                                    con.Open();
                                    using (SqlCommand cmd = con.CreateCommand())
                                    {
                                        cmd.CommandType = CommandType.Text;
                                        cmd.CommandText = BuildMachineryQueryString(ActivityInID.ToString(), MachineryID, MachineryHours);
                                        if (cmd.CommandText != "")
                                        {
                                            if (MachineryID != "-1")
                                            {
                                                rowAffected4 = (Int64)cmd.ExecuteNonQuery();
                                            }
                                            else
                                            {
                                                rowAffected4 = 1;
                                            }
                                        }
                                    }
                                }
                                using (var scopeInner5 = new TransactionScope(TransactionScopeOption.Required, option))
                                {
                                    using (var con = new SqlConnection(connectionString))
                                    {
                                        con.Open();
                                        using (SqlCommand cmd = con.CreateCommand())
                                        {
                                            cmd.CommandType = CommandType.Text;
                                            cmd.CommandText = BuildMachineryQueryString(ActivityInID.ToString(), MotorizedMachineryID, MotorizedMachineryHours);
                                            if (cmd.CommandText != "")
                                            {
                                                if (MotorizedMachineryID != "-1")
                                                {
                                                    rowAffected5 = (Int64)cmd.ExecuteNonQuery();
                                                }
                                                else
                                                {
                                                    rowAffected5 = 1;
                                                }
                                            }
                                        }
                                    }
                                    using (var scopeInner6 = new TransactionScope(TransactionScopeOption.Required, option))
                                    {
                                        using (var con = new SqlConnection(connectionString))
                                        {
                                            con.Open();
                                            using (SqlCommand cmd = con.CreateCommand())
                                            {
                                                cmd.CommandType = CommandType.Text;
                                                cmd.CommandText = UpdateActivityCostQuery(ActivityInID.ToString());
                                                if (cmd.CommandText != "")
                                                {
                                                    rowAffected6 = (Int64)cmd.ExecuteNonQuery();
                                                }
                                            }
                                        }
                                        // inner here
                                        if (rowAffected6 > 0)
                                        {
                                            scopeInner6.Complete();// Update Activity Cost
                                        }
                                        else
                                            return -7;
                                    }
                                    if (rowAffected5 > 0)
                                    {
                                        scopeInner5.Complete();// Machinery
                                    }
                                    else
                                        return -6;
                                }
                                if (rowAffected4 > 0)
                                {
                                    scopeInner4.Complete(); // Machinery
                                }
                                else
                                    return -5;
                            }
                            if (rowAffected3 > 0)
                            {
                                scopeInner3.Complete();// worker
                            }
                            else
                                return -4;
                        }
                        if (rowAffected2 > 0 && area != "-1")
                        {
                            scopeInner2.Complete();// plot
                        }
                        else
                            return -3;
                    }
                    if (ActivityInID > 0)
                    {
                        scopeInner1.Complete();// Activity
                    }
                    else
                        return -2;
                }
                if (ActivityPlanID > 0)
                {
                    //the outer transaction is committed first, then the inner 1 2 3... if one not commit it roll back
                    scopeOuter.Complete();// ActivityPlan
                    return 1;
                }
                else
                    return -1;
            }
        }
        // BlockID, PlotID, CropID, VarietyID, ActivityID, WorkerID, MachineryID, MachineryHours

        private static string BuildActivityPlanQStr(String FromDate, String ToDate, String ActivityType_id, String CompanyID, String farm_id, String Remarks, String ActivityPlanID)
        {
            return string.Format("INSERT INTO [biaqm].[dbo].[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) OUTPUT INSERTED.ID VALUES ({0} , {1}, '{2}', '{3}', '{4}', 0, 0, 0, null, '{5}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks);
        }

        private static string BuildActivityQStr(String FromDate, String ToDate, String ActivityType_id, String CompanyID, String farm_id, String Remarks, String ActivityPlanID)
        {
            return string.Format("INSERT INTO [biaqm].[dbo].[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) OUTPUT INSERTED.ID VALUES ({0}, {1}, '{2}', '{3}', '{4}', 0, 0, 1, '{6}', '{5}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, Remarks, ActivityPlanID);
        }

        private static string CalculateArea(string BlockID, string PlotID, string CropID, string VarietyID, string dDate)
        {
            dDate = DateAndTime.Now.ToString("dd/MM/yyyy HH:mm:ss");
            string query = string.Empty;
            string dDateQuery = " AND ( (P.[start_date] IS NULL";
            dDateQuery = dDateQuery + " OR P.[start_date] <= CONVERT(datetime, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " AND (P.[end_date] IS NULL";
            dDateQuery = dDateQuery + " OR P.[end_date] >= CONVERT(datetime, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " OR (P.[start_date] >= CONVERT(datetime, '" + dDate + "', 103) )";
            dDateQuery = dDateQuery + " AND (P.[start_date] <= CONVERT(datetime, '" + dDate + "', 103) ) )";

            string dVarietyDateQuery = " AND ( (VP.[StartDate] IS NULL";
            dVarietyDateQuery = dVarietyDateQuery + " OR VP.[StartDate] <= CONVERT(datetime, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " AND (VP.[EndDate] IS NULL";
            dVarietyDateQuery = dVarietyDateQuery + " OR VP.[EndDate] >= CONVERT(datetime, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " OR (VP.[StartDate] >= CONVERT(datetime, '" + dDate + "', 103) )";
            dVarietyDateQuery = dVarietyDateQuery + " AND (VP.[StartDate] <= CONVERT(datetime, '" + dDate + "', 103) ) )";

            if (!Information.IsNumeric(VarietyID) & !Information.IsNumeric(CropID))
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT CASE WHEN P.[PerimeterArea] IS NULL THEN P.[NetArea] ELSE P.[PerimeterArea] END AS [Area]  ";
                    query = query + " FROM [plot] AS P ";
                    query = query + " WHERE [id] = " + PlotID;
                    query = query + dDateQuery;
                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(CASE WHEN P.[PerimeterArea] IS NULL THEN P.[NetArea] ELSE P.[PerimeterArea] END) AS [Area]  ";
                    query = query + " FROM [plot] AS P ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + dDateQuery;
                }
            }
            else if (Information.IsNumeric(VarietyID))
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE P.[id] = " + PlotID;
                    query = query + " AND VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + " AND ( [StartDate] IS NULL OR [StartDate] <= CONVERT(DATETIME,'" + dDate + "',103) ) ";
                    query = query + " AND ( [EndDate] IS NULL OR [EndDate] >= CONVERT(DATETIME,'" + dDate + "',103) ) ";

                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + " AND VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + " AND ( [StartDate] IS NULL OR [StartDate] <= CONVERT(DATETIME,'" + dDate + "',103) ) ";
                    query = query + " AND ( [EndDate] IS NULL OR [EndDate] >= CONVERT(DATETIME,'" + dDate + "',103) ) ";
                }
                else
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " WHERE VP.[variety_id] = " + VarietyID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }

            }
            else
            {
                if (Information.IsNumeric(PlotID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN [variety] AS V ON V.[id] = VP.[variety_id] ";
                    query = query + " WHERE P.[id] = " + PlotID;
                    query = query + " AND V.[crop_id] = " + CropID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }
                else if (Information.IsNumeric(BlockID))
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN [variety] AS V ON V.[id] = VP.[variety_id] ";
                    query = query + " WHERE P.[block_id] = " + BlockID;
                    query = query + " AND V.[crop_id] = " + CropID;
                    query = query + dDateQuery;
                    query = query + dVarietyDateQuery;
                }
                else
                {
                    query = "SELECT SUM(VP.[Area]) AS [Area] FROM [variety_to_plot] AS VP ";
                    query = query + " INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] ";
                    query = query + " INNER JOIN [variety] AS V ON V.[id] = VP.[variety_id] ";
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

            query = "INSERT INTO [Plots_to_activities] ([Activity_id] ,[Plot] ";
            query = query + "  ,[Rows] ,[Trees] ";
            query = query + "  ,[Variety_id] ,[crop] ";
            query = query + "  ,[region] ,[block] ,[area] ";
            query = query + "  ,[Coordinates]  ,[Notes] ";
            query = query + " ) ";

            if (Information.IsNumeric(Plot))
            {
                query = query + " SELECT " + ActivityID + " , " + Plot;
                query = query + " , null , null ";
                query = query + " , " + Variety + " , " + Crop;
                query = query + " , [region_id] , [block_id] , " + TotalArea;
                query = query + " , null , '' ";
                query = query + " FROM [plot] ";
                query = query + " WHERE [id] = " + Plot;
            }
            else if (Information.IsNumeric(Block))
            {
                query = query + " SELECT " + ActivityID + " , null ";
                query = query + " , null , null ";
                query = query + " , " + Variety + " , " + Crop;
                query = query + " , [RegionID] , " + Block + " , " + TotalArea;
                query = query + " , null , '' ";
                query = query + " FROM [Block] ";
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

        private static string BuildWorkerQueryString(string ActivityID, string WorkerID, string FromDate, double totalTime, SqlConnection connection)
        {
            if (WorkerID == "-1")
            {
                return "-1";
            }
            // "dd/MM/yyyy HH:mm:ss"
            FromDate = DateAndTime.Now.ToString("dd/MM/yyyy HH:mm:ss");
            //SqlTransaction tx = new SqlTransaction();
            //SqlConnection connection = new SqlConnection(connectionString);
            string query = string.Empty;
            SqlDataReader reader = null;
            double WorkerCost = 0;

            if (Information.IsNumeric(WorkerID))
            {
                //string FromDate = txtFromDate.Text;
                string WorkersQuantity = "1";
                if (!Information.IsNumeric(WorkersQuantity))
                    WorkersQuantity = "1";
                query = " SELECT [PaymentTypeID], [standard_hours], [first_overtime] ";
                query = query + " , [second_overtime], [first_overtime_hours], [night_work_FromHour]";
                query = query + " , [night_work_ToHour], [hour_cost] ";
                query = query + " From [worker] WHERE [id] = " + WorkerID;

                //double totalTime = 0;
                double standardHours = 0;
                double Overtime1 = 0;
                double Overtime2 = 0;
                double Night = 0;

                SqlCommand exe = new SqlCommand(query, connection/*, tx*/);

                reader = exe.ExecuteReader();
                reader.Read();

                //double.TryParse(txtHours.Text, totalTime);

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
                            {
                                standardHours = totalTime;
                            }

                            double hour_cost = 0;
                            double first_overtime = 0;
                            double second_overtime = 0;
                            if (Information.IsNumeric(reader["hour_cost"]))
                            {
                                hour_cost = Convert.ToDouble(reader["hour_cost"]);
                            }
                            if (Information.IsNumeric(reader["first_overtime"]))
                            {
                                first_overtime = Convert.ToDouble(reader["first_overtime"]);
                            }
                            if (Information.IsNumeric(reader["second_overtime"]))
                            {
                                second_overtime = Convert.ToDouble(reader["second_overtime"]);
                            }

                            WorkerCost = hour_cost * standardHours + hour_cost * first_overtime / 100 * Overtime1 + hour_cost * second_overtime / 100 * Overtime2;
                        }

                        break;
                    default:
                        standardHours = totalTime;
                        break;
                }
                reader.Close();

                query = " INSERT INTO [Activity_workers] ([activity_id], [worker_id], [WorkersQuantity] " + ", [WorkingDate], [StartWorkingHour], [EndWorkingHour]" + ", [standard_hours], [first_overtime], [second__overtime], [night_hours], [YieldUnit_Cost]" + ", [Yield_Units], [YieldDescription], [workers_cost], [Remarks]) ";
                query = query + " VALUES (" + ActivityID + ", " + WorkerID + ", " + WorkersQuantity;
                query = query + " , CONVERT(datetime, '" + FromDate + "', 103) ";
                query = query + " , '0:0'";
                query = query + " , '0:0'";
                query = query + " , " + standardHours.ToString() + " ," + Overtime1.ToString() + " ," + Overtime2.ToString();
                query = query + " , " + Night.ToString() + " , null , null , null ";
                query = query + " , " + WorkerCost.ToString();
                query = query + " , '' )";

                reader.Close();
            }

            return query;
        }

        private static string BuildMachineryQueryString(string ActivityID, string MachineryID, string MachineryHours)
        {
            string query = string.Empty;

            if (Information.IsNumeric(MachineryID))
            {
                //string MachineryHours = txtHours.Text;

                if (!Information.IsNumeric(MachineryHours))
                {
                    MachineryHours = "0";
                }

                query = " INSERT INTO [activity_machinery] ([activity_id], [machinery_id], [machinery_hours] " + ", [machinery_cost], [YieldUnit_Cost] " + ", [Yield_Units], [YieldDescription], [Remarks]) ";
                query = query + " SELECT " + ActivityID + ", " + MachineryID + ", " + MachineryHours;
                query = query + " ," + MachineryHours + " * M.[hour_price] , null ";
                query = query + " , null , null ";
                query = query + " , ''";
                query = query + " FROM [machine] AS M";
                query = query + " WHERE M.[id] = " + MachineryID;
            }

            return query;
        }

        private static string UpdateActivityCostQuery(string ActivityID)
        {
            string query = " UPDATE [Activities] ";
            query = query + " SET [ActivityCost] = COALESCE((SELECT SUM(W.workers_cost) ";
            query = query + " FROM [Activity_workers] AS W ";
            query = query + " WHERE W.[activity_id] = [Activities].[id]), 0) ";
            query = query + " + COALESCE((SELECT SUM(M.machinery_cost) ";
            query = query + " FROM [activity_machinery] AS M ";
            query = query + " WHERE M.[activity_id] = [Activities].[id]), 0) ";
            query = query + " + [Activities].[ItemCount] * [Activities].[ItemPrice] ";
            query = query + " WHERE [Activities].[id] = " + ActivityID;

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

    }// class
}// namespace

/*
using (var scopeInner2 = new TransactionScope(TransactionScopeOption.Required, option))
{
    using (var con = new SqlConnection(connectionString))
    {
        con.Open();
        using (SqlCommand cmd = con.CreateCommand())
        {
            string query = string.Format("");
            cmd.CommandType = CommandType.Text;
            cmd.CommandText = query;
            rowAffected2 = (Int64)cmd.ExecuteNonQuery();
        }
    }
    // inner here
    if (rowAffected2 > 0)
    {
        scopeInner2.Complete();
    }
    else
        return 0;
}
 * 
 * 
 * 
 * 
 * 
 * 
    public static int InsertActivity(
            String FromDate,
            String ToDate,
            String ActivityType_id,
            String CompanyID,
            String farm_id,
            String ActivityCost,
            String ActivityArea,
            String Remarks)
        {
            Int64 ActivityPlan = -1;
            object idPlan;
            Int64 rowAffected;

            string queryPlan = string.Format("INSERT INTO [biaqm].[dbo].[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) OUTPUT INSERTED.ID VALUES ('{0}', '{1}', '{2}', '{3}', '{4}', '{5}', '{6}', 0, null, '{7}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, ActivityCost, ActivityArea, Remarks);


            using (var scopeOuter = new TransactionScope(TransactionScopeOption.Required, option))
            {
                using (var con = new SqlConnection(connectionString))
                {
                    con.Open();
                    using (SqlCommand cmd = con.CreateCommand())
                    {
                        cmd.CommandType = CommandType.Text;
                        cmd.CommandText = queryPlan;
                        idPlan = cmd.ExecuteScalar();
                        if (idPlan != null)
                            ActivityPlan = (Int64)idPlan;
                        else
                            ActivityPlan = -1;
                    }
                }
                using (var scopeInner1 = new TransactionScope(TransactionScopeOption.Required, option))
                {
                    using (var con = new SqlConnection(connectionString))
                    {
                        con.Open();
                        using (SqlCommand cmd = con.CreateCommand())
                        {
                            string query = string.Format("INSERT INTO [biaqm].[dbo].[Activities]([FromDate],[ToDate],[ActivityType_id],[CompanyID],[farm_id],[ActivityCost],[ActivityArea],[ActivityDone],[ActivityPlan],[Remarks]) VALUES ('{0}', '{1}', '{2}', '{3}', '{4}', '{5}', '{6}', 1, '{8}', '{7}')", FromDate, ToDate, ActivityType_id, CompanyID, farm_id, ActivityCost, ActivityArea, Remarks, ActivityPlan);
                            cmd.CommandType = CommandType.Text;
                            cmd.CommandText = query;
                            rowAffected = (Int64)cmd.ExecuteNonQuery();
                        }
                    }
                    using (var scopeInner2 = new TransactionScope(TransactionScopeOption.Required, option))
                    {
                        using (var con = new SqlConnection(connectionString))
                        {
                            con.Open();
                            using (SqlCommand cmd = con.CreateCommand())
                            {
                                string query = string.Format("");
                                cmd.CommandType = CommandType.Text;
                                cmd.CommandText = query;
                                rowAffected = (Int64)cmd.ExecuteNonQuery();
                            }
                        }
                        using (var scopeInner3 = new TransactionScope(TransactionScopeOption.Required, option))
                        {
                            using (var con = new SqlConnection(connectionString))
                            {
                                con.Open();
                                using (SqlCommand cmd = con.CreateCommand())
                                {
                                    string query = string.Format("");
                                    cmd.CommandType = CommandType.Text;
                                    cmd.CommandText = query;
                                    rowAffected = (Int64)cmd.ExecuteNonQuery();
                                }
                            }
                            // inner here
                            if (ActivityPlan > 0)
                            {
                                scopeInner3.Complete();
                            }
                            else
                                return 0;
                        }
                        if (ActivityPlan > 0)
                        {
                            scopeInner2.Complete();
                        }
                        else
                            return 0;
                    }
                    if (ActivityPlan > 0)
                    {
                        scopeInner1.Complete();
                    }
                    else
                        return 0;
                }
                if (rowAffected == 1)
                {
                    //the outer transaction is committed first, then the inner 1 2 3
                    scopeOuter.Complete();
                    return 1;
                }
                else
                    return 0;
            }
        }

       
*/
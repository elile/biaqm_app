using Biaqm.Models.MyModel;
using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Biaqm.Controllers
{
    public class checkingController : ApiController
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
        public static string prefix = "[biaqm].[dbo].";

        public string get()
        {
            WorkerToInsert worker = new WorkerToInsert()
            {
                id = 69,
                totalTime = "12.0",
                workerAdvancedResult = new WorkerAdvancedResult()
                {
                    yd = new Yield_Descriptions()
                    {
                        Name = "חודשית",
                        CorrelatedField = -1,
                        ID = 67
                    },
                    w = new Worker()
                    {
                        payment_type = "יומי",
                        name = "אופיר אילן",
                        farm_id = 1,
                        first_overtime = 125,
                        first_overtime_hours = 2,
                        hour_cost = 35,
                        id = 69,
                        company_id = 33,
                        PaymentTypeID = 1,
                        second_overtime = 150,
                        standard_hours = (float)8.0,
                        worker_type_id = 12
                    },
                    remarks = "fff",
                    workerQuantity = 1,
                    costOfYd = 12.0,
                    totalTime = 12.0,
                    numOfYd = 2
                }
            };
            InsertActivityFromPost insertActivityFromPost = new InsertActivityFromPost()
            {
                workerToInsert = new[] { worker },
                ActivityType_id = "356",
                BlockID = "147",
                CompanyID = "33",
                CropID = "1",
                PlotID = "019",
                Remarks = "ttt",
                VarietyID = "9",
                date = "2014-11-29T09:21:06.829",
                farm_id = "1",
                machineToInsert = new[] { new MachineToInsert() { totalTime = "9", id = 564 } },
            };

            string activityInID = "312921";

            string commandText = BuildWorkerQueryString(activityInID, insertActivityFromPost, worker, new SqlConnection(connectionString));

            return "";
        }


        private static string BuildWorkerQueryString(string ActivityID, InsertActivityFromPost activityToInsert, WorkerToInsert w, SqlConnection connection)
        {
            string totalTimeStr = w.totalTime;
            double totalTime = Convert.ToDouble(totalTimeStr);
            string WorkerID = w.id.ToString();
            if (WorkerID == "-1") return "-1";
            string FromDate = activityToInsert.date;
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

                query = " INSERT INTO " + prefix + "[Activity_workers] ";
                query = query + "        ([activity_id]     , [worker_id]     ,     [WorkersQuantity] " + " , [WorkingDate]                                   ,     [StartWorkingHour]              ,  [EndWorkingHour]    " + "        , [standard_hours]                 , [first_overtime]            , [second__overtime]          , [night_hours]               , [YieldUnit_Cost]" + "    , [Yield_Units]            ,           [YieldDescription]                 ,     [workers_cost]          , [Remarks]) ";
                query = query + " VALUES (" + ActivityID + ", " + WorkerID + ", " + wAdv.workerQuantity + " , CONVERT(varchar, '" + FromDate + "', 103) " + " , '" + wAdv.StartWorkingHour + "'" + ", '" + wAdv.EndWorkingHour + "'" + ", " + standardHours.ToString() + " ," + Overtime1.ToString() + " ," + Overtime2.ToString() + " , " + Night.ToString() + "    , " + wAdv.costOfYd + "    , " + wAdv.numOfYd + "     , " + wAdv.yd != null ? wAdv.yd.Name : null + ", " + WorkerCost.ToString() + " , '"+wAdv.remarks+"' )";
                return query;
            }
        }

    }
}

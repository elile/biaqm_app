using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Globalization;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class WorkerDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Worker> getWorker(String farm_id, String company_id, String start_date, String EndDate)
        {
            string query = string.Format("SELECT [id],[name] ,[PaymentTypeID] ,[hour_cost] ,[standard_hours] ,[first_overtime] ,[second_overtime] ,[first_overtime_hours] ,[worker_type_id] ,[farm_id] ,[company_id] ,[payment_type] FROM  [biaqm].[dbo].[worker] WHERE  (company_id='{1}' ) AND  (farm_id='{0}' OR farm_id is null ) AND (PaymentTypeID='1' OR PaymentTypeID='3' OR PaymentTypeID='4') AND (start_date<='{2}') AND (EndDate IS NULL OR EndDate>'{3}')", farm_id, company_id, start_date, EndDate);
            List<Worker> Workers = new List<Worker>();
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                con.Open();
                using (SqlCommand command = new SqlCommand(query, con))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            while (reader.Read())
                            {
                                Worker w = new Worker();
                                w.id = Convert.ToInt32(reader[0]);
                                w.name = Convert.ToString(reader[1]);
                                w.PaymentTypeID = Convert.ToInt32(reader[2]);
                                w.hour_cost = (reader[3] == DBNull.Value) ? -1 : float.Parse(Convert.ToString(reader[3]), CultureInfo.InvariantCulture.NumberFormat);
                                w.standard_hours = (reader[4] == DBNull.Value) ? -1 : float.Parse(Convert.ToString(reader[4]), CultureInfo.InvariantCulture.NumberFormat);
                                w.first_overtime = (reader[5] == DBNull.Value) ? -1 : float.Parse(Convert.ToString(reader[5]), CultureInfo.InvariantCulture.NumberFormat);
                                w.second_overtime = (reader[6] == DBNull.Value) ? -1 : float.Parse(Convert.ToString(reader[6]), CultureInfo.InvariantCulture.NumberFormat);
                                w.first_overtime_hours = (reader[7] == DBNull.Value) ? -1 : float.Parse(Convert.ToString(reader[7]), CultureInfo.InvariantCulture.NumberFormat);
                                w.worker_type_id = (reader[8] == DBNull.Value) ? -1 : Convert.ToInt32(reader[8]);
                                w.farm_id = (reader[9] == DBNull.Value) ? -1 : Convert.ToInt32(reader[9]);
                                w.company_id = (reader[10] == DBNull.Value) ? -1 : Convert.ToInt32(reader[10]);
                                w.payment_type = (reader[11] == DBNull.Value) ? "":Convert.ToString(reader[11]);
                                Workers.Add(w);
                            }
                        }
                    }
                }
            }
            return Workers;
        }
    }
}
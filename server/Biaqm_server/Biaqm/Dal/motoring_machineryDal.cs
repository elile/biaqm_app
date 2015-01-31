using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class motoring_machineryDal
    {

        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<motoring_machinery> getmotoring_machinerys(String company_id, String purchase_date)
        {
            string query = string.Format("SELECT [id],[name],[hour_price] FROM [biaqm].[dbo].[machine] WHERE (MachineryUsageID='1' or MachineryUsageID='3' or MachineryUsageID is null) and (company_id='{0}') and ( purchase_date <= '{1}' )", company_id, purchase_date);
            List<motoring_machinery> motoring_machinerys = new List<motoring_machinery>();
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
                                motoring_machinery m = new motoring_machinery();
                                m.id = Convert.ToInt32(reader[0]);
                                m.name = Convert.ToString(reader[1]);
                                m.hour_price = (reader[2] == DBNull.Value) ? -1 : Convert.ToDouble(reader[2]);
                                motoring_machinerys.Add(m);
                            }
                        }
                    }
                }
            }
            return motoring_machinerys;
        }
    }
}
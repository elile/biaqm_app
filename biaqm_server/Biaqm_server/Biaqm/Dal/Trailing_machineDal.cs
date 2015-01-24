using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class Trailing_machineDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Trailing_machine> getTrailing_machine(String company_id, String purchase_date)
        {
            string query = string.Format("SELECT [id],[name],[hour_price] FROM [biaqm].[dbo].[machine] WHERE (MachineryUsageID='1' or MachineryUsageID='3' or MachineryUsageID is null) and (company_id='{0}') and ( purchase_date <= '{1}' )", company_id, purchase_date);
            List<Trailing_machine> Trailing_machines = new List<Trailing_machine>();
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
                                Trailing_machine t = new Trailing_machine();
                                t.id = Convert.ToInt32(reader[0]);
                                t.name = Convert.ToString(reader[1]);
                                t.hour_price = (reader[2] == DBNull.Value) ? -1 : Convert.ToDouble(reader[2]);
                                Trailing_machines.Add(t);
                            }
                        }
                    }
                }
            }
            return Trailing_machines;
        }
    }
}
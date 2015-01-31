using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class CropDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;


        public static List<Crop> getCrop(String company_id)
        {
            string query = string.Format("SELECT [id],[name] FROM [biaqm].[dbo].[crop] WHERE company_id='{0}'", company_id);
            List<Crop> Crops = new List<Crop>();
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
                                Crops.Add(new Crop()
                                {
                                    id = Convert.ToInt32(reader[0]),
                                    name = Convert.ToString(reader[1])
                                });
                            }
                        }
                    }
                }
            }
            return Crops;
        }
    }
}
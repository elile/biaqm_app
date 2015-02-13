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

        internal static List<Crop> getCropByBlock(String block_id, String FromDate, String ToDate)
        {
            string query = string.Format("SELECT DISTINCT C.[id], C.[name] FROM  [crop] AS C INNER JOIN [variety] AS V ON V.[crop_id] = C.[id] INNER JOIN [variety_to_plot] AS VP ON VP.[variety_id] = V.[id] INNER JOIN [plot] AS P ON P.[id] = VP.[plot_id] INNER JOIN [Block] AS B ON B.[ID] = P.[block_id] WHERE B.[ID] = '{0}' AND VP.[StartDate] <= '{1}' AND (VP.[EndDate] IS NULL OR VP.[EndDate] >=  '{2}')", block_id, FromDate, ToDate);
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
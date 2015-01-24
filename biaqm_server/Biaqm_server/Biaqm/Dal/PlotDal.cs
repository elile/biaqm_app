using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class PlotDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Plot> getPlots(String farm_id, String block_id, String start_date, String end_date)
        {
            string query = "";
            if (block_id == "-1")
            {//all plots
                query = string.Format("SELECT [id],[name],[farm_id],[region_id],[block_id],[start_date],[end_date] FROM [biaqm].[dbo].[plot] WHERE (farm_id='{0}') AND start_date<='{1}' AND (end_date IS NULL OR end_date>'{2}')", farm_id, start_date, end_date);
            }
            else
            {
                query = string.Format("SELECT [id],[name],[farm_id],[region_id],[block_id],[start_date],[end_date] FROM [biaqm].[dbo].[plot] WHERE (farm_id='{0}') AND (block_id IS NULL OR block_id='{1}') AND start_date<='{2}' AND (end_date IS NULL OR end_date>'{3}')", farm_id, block_id, start_date, end_date);  
            }          
            List<Plot> Plots = new List<Plot>();
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
                                Plot p = new Plot();
                                p.id = Convert.ToInt32(reader[0]);
                                p.name = Convert.ToString(reader[1]);
                                p.farm_id = Convert.ToInt64(reader[2]);
                                p.region_id = (reader[3] == DBNull.Value) ? -1 : Convert.ToInt32(reader[3]);
                                p.block_id = (reader[4] == DBNull.Value) ? -1 : Convert.ToInt32(reader[4]);
                                p.start_date = Convert.ToDateTime( reader[5]);
                                p.end_date = (reader[6] == DBNull.Value) ? (DateTime?)null : Convert.ToDateTime(reader[6]);
                                Plots.Add(p);
                            }
                        }
                    }
                }
            }
            return Plots;
        }
    }
}
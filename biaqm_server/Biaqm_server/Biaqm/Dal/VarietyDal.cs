using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class VarietyDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;


        public static List<Variety> getVariety(String company_id, String crop_id, String plot_id)
        {
            string query = string.Format("SELECT v.id, v.name, v.crop_id FROM [biaqm].[dbo].[variety] as v, [biaqm].[dbo].[variety_to_plot] as v_p WHERE v_p.plot_id='{2}' and v_p.variety_id=v.id and v.company_id='{0}' AND v.crop_id='{1}'", company_id, crop_id, plot_id);
            List<Variety> Varietys = new List<Variety>();
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
                                Variety v = new Variety();
                                v.id = Convert.ToInt32(reader[0]);
                                v.name = Convert.ToString(reader[1]);
                                v.crop_id = (reader[2] == DBNull.Value) ? -1 : Convert.ToInt32(reader[2]);
                                Varietys.Add(v);
                            }
                        }
                    }
                }
            }
            return Varietys;
        }
    }
}
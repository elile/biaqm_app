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
            string query = "";
            if (!String.Equals(crop_id, "-1") && !String.Equals(plot_id, "-1"))
            {
                query = string.Format("SELECT v.id, v.name, v.crop_id FROM [biaqm].[dbo].[variety] as v, [biaqm].[dbo].[variety_to_plot] as v_p WHERE v_p.plot_id='{2}' and v_p.variety_id=v.id and v.company_id='{0}' AND v.crop_id='{1}'", company_id, crop_id, plot_id);
            }
            else if (!String.Equals(crop_id, "-1") && String.Equals(plot_id, "-1"))
            {// plot_id = null
                query = string.Format("SELECT v.id, v.name, v.crop_id FROM [biaqm].[dbo].[variety] as v WHERE v.company_id='{0}' AND v.crop_id='{1}'", company_id, crop_id);
            }
            else if (String.Equals(crop_id, "-1") && !String.Equals(plot_id, "-1"))
            {// crop_id = null
                query = string.Format("SELECT v.id, v.name, v.crop_id FROM [biaqm].[dbo].[variety] as v, [biaqm].[dbo].[variety_to_plot] as v_p WHERE v_p.plot_id='{1}' and v_p.variety_id=v.id and v.company_id='{0}'", company_id, plot_id);
            }
            else
            {// crop_id = plot_id = null
                query = string.Format("SELECT v.id, v.name, v.crop_id FROM [biaqm].[dbo].[variety] as v WHERE v.company_id='{0}'", company_id);
            }
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

        internal static List<Variety> getVarietysByBlock(string block_id, string FromDate, string ToDate)
        {
            string query = string.Format("SELECT DISTINCT V.[id], V.[name], V.[crop_id] FROM [biaqm].[dbo].[variety] as V INNER JOIN [biaqm].[dbo].[variety_to_plot] AS VP ON VP.[variety_id] = V.[id] INNER JOIN [biaqm].[dbo].[plot] AS P ON P.[id] = VP.[plot_id] WHERE P.[block_id] = '{0}' AND VP.[StartDate] <= '{1}' AND (VP.[EndDate] IS NULL OR VP.[EndDate] >= '{2}' )", block_id, FromDate, ToDate);
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

        internal static List<Variety> getVarietysByPlot(string plot_id, string FromDate, string ToDate)
        {
            // not in use - change the query

            string query = string.Format("SELECT DISTINCT V.[id], V.[name], V.[crop_id] FROM [biaqm].[dbo].[variety] as V INNER JOIN [biaqm].[dbo].[variety_to_plot] AS VP ON VP.[variety_id] = V.[id] INNER JOIN [biaqm].[dbo].[plot] AS P ON P.[id] = VP.[plot_id] WHERE P.[block_id] = '{0}' AND VP.[StartDate] <= '{1}' AND (VP.[EndDate] IS NULL OR VP.[EndDate] >= '{2}' )", plot_id, FromDate, ToDate);
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
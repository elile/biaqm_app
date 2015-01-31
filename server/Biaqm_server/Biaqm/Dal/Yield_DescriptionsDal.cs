using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class Yield_DescriptionsDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Yield_Descriptions> getYield_Descriptions(String company_id)
        {
            string query = string.Format("SELECT [ID],[Name],[CorrelatedField] FROM [biaqm].[dbo].[Yield_Descriptions] WHERE CompanyID='{0}'", company_id);
            List<Yield_Descriptions> Yield_Descriptionss = new List<Yield_Descriptions>();
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
                                Yield_Descriptionss.Add( new Yield_Descriptions()
                                {
                                    ID = Convert.ToInt32(reader[0]),
                                    Name = Convert.ToString(reader[1]),
                                    CorrelatedField = (reader[2] == DBNull.Value) ? -1 : Convert.ToInt32(reader[2])
                                });
                            }
                        }
                    }
                }
            }
            return Yield_Descriptionss;
        }
    }
}
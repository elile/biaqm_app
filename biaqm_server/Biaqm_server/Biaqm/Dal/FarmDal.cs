using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class FarmDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Farm> getFarms(String company_id)
        {
            string query = string.Format("SELECT [id],[InsertDate],[InsertUser],[LastUpdate],[LastUpdateUser],[name],[name_eng],[AreaUOM],[LengthUOM],[UnitsConversion],[contact_name],[contact_phone],[contact_fax],[contact_email],[tax_number],[company_id]  FROM [biaqm].[dbo].[farm]  WHERE company_id='{0}'", company_id);
            List<Farm> farms = new List<Farm>();
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
                                Farm f = new Farm();
                                f.id = Convert.ToInt32( reader["id"]);
                                f.InsertDate = Convert.ToString(reader["InsertDate"]);
                                f.InsertUser = (reader["InsertUser"]==DBNull.Value) ? -1 : Convert.ToInt16(reader["InsertUser"]);
                                f.LastUpdate = (reader["LastUpdate"]==DBNull.Value) ? "" : Convert.ToString(reader["LastUpdate"]);
                                f.LastUpdateUser =(reader["LastUpdateUser"]==DBNull.Value) ? -1 : Convert.ToInt16(reader["LastUpdateUser"]);
                                f.name = Convert.ToString(reader["name"]);
                                f.name_eng = Convert.ToString(reader["name_eng"]);
                                f.AreaUOM = (reader["AreaUOM"]==DBNull.Value) ? -1 : Convert.ToInt16(reader["AreaUOM"]);
                                f.LengthUOM = (reader["LengthUOM"]==DBNull.Value) ? -1 : Convert.ToInt16(reader["LengthUOM"]);
                                f.UnitsConversion = (reader["UnitsConversion"]==DBNull.Value) ? -1 : Convert.ToDecimal(reader["UnitsConversion"]);
                                f.contact_name = Convert.ToString(reader["contact_name"]);
                                f.contact_phone = Convert.ToString(reader["contact_phone"]);
                                f.contact_fax = Convert.ToString(reader["contact_fax"]);
                                f.contact_email = Convert.ToString(reader["contact_email"]);
                                f.tax_number = Convert.ToString(reader["tax_number"]);
                                f.company_id = (reader["company_id"] == DBNull.Value) ? -1 : Convert.ToInt64(reader["company_id"]);
                                farms.Add(f);
                            }
                        }
                    }
                }
            }
            return farms;
        }
    }
}
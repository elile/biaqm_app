using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class ActivityGroupsDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
       
        public static List<ActivityGroups> getActivityGroups(String company_id)
        {
            string query = string.Format("SELECT [ID],[GroupName] FROM [biaqm].[dbo].[ActivityGroups]  where company_id='{0}'", company_id);
            List<ActivityGroups> ActivityGroupsList = new List<ActivityGroups>();
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
                                ActivityGroupsList.Add(new ActivityGroups()
                                {
                                    ID = Convert.ToInt64(reader[0]),
                                    GroupName = Convert.ToString(reader[1]),
                                });
                            }
                        }
                    }
                }
            }
            return ActivityGroupsList;
        }
    }
}
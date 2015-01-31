using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class ActivityTypesDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
        public static List<ActivityType> getActivityType(String company_id)
        {
            string query = string.Format("SELECT [id],[name],[ActivityGroup],[ActualPrice],[YieldDescription] FROM [biaqm].[dbo].[Activities_type] WHERE company_id='{0}'", company_id);
            List<ActivityType> ActivityTypes = new List<ActivityType>();
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
                                ActivityTypes.Add(new ActivityType()
                                {
                                    id = Convert.ToInt32(reader[0]),
                                    name = Convert.ToString(reader[1]),
                                    ActivityGroup = (reader[2] == DBNull.Value) ? -1 : Convert.ToInt64(reader[2]),
                                    ActualPrice = (reader[3] == DBNull.Value) ? -1 : Convert.ToDecimal(reader[3]),
                                    YieldDescription = (reader[4] == DBNull.Value) ? -1 : Convert.ToInt32(reader[4])
                                });
                            }
                        }
                    }
                }
            }
            return ActivityTypes;
        }

    }
}
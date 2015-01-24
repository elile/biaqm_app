using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Secure
{
    public class IdentityStore
    {
        //public static List<user> ValidUsers = new List<user>();
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static bool IsValidUser(user userBody)
        {
            string query = string.Format("SELECT [id], [first_name], [last_name], [phone_number], [mobile_phone], [fax_number], [skype_name], [country], [address], [email], [login], [password], [syte_lang], [company_id], [role], [farm_id], [enabled], [User_RegDateTime] FROM [biaqm].[dbo].[user] WHERE ((LOWER(login)='{0}') AND (LOWER(password)='{1}'))", userBody.login.ToLower(), userBody.password.ToLower());
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                con.Open();
                using (SqlCommand command = new SqlCommand(query, con))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            try
                            {
                                while (reader.Read())
                                {
                                    return true;
                                }
                            }
                            catch (Exception e)
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        }

        public static bool IsValidUserId(string userId)
        {
            // if sql contain the userid
            string query = string.Format("SELECT [id] FROM [biaqm].[dbo].[user] WHERE LOWER(login)='{0}'", userId.ToLower());
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                con.Open();
                using (SqlCommand command = new SqlCommand(query, con))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            try
                            {
                                while (reader.Read())
                                {
                                    return true;
                                }
                            }
                            catch (Exception e) { return false; }
                        }
                    }
                }
            }
            return false;
        }
    }
}
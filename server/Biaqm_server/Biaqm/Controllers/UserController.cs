using System;
using System.Collections.Generic;
using System.Data;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using Biaqm.Models.MyModel;
using System.Configuration;
using System.Data.SqlClient;

namespace Biaqm.Controllers
{
    public class UserController : ApiController
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        [HttpPost]
        public user Getuser([FromBody]user userBody)
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
                                    user u = new user();
                                    u.id = Convert.ToInt64(reader[0]);
                                    u.first_name = Convert.ToString(reader[1]);
                                    u.last_name = Convert.ToString(reader[2]);
                                    u.phone_number = Convert.ToString(reader[3]);
                                    u.mobile_phone = Convert.ToString(reader[4]);
                                    u.fax_number = Convert.ToString(reader[5]);
                                    u.skype_name = Convert.ToString(reader[6]);
                                    u.country = Convert.ToString(reader[7]);
                                    u.address = Convert.ToString(reader[8]);
                                    u.email = Convert.ToString(reader[9]);
                                    u.login = Convert.ToString(reader[10]);
                                    u.password = Convert.ToString(reader[11]);
                                    u.syte_lang = Convert.ToString(reader[12]);
                                    u.company_id = (reader[13] == DBNull.Value) ? -1 : Convert.ToInt32(reader[13]);
                                    u.role = Convert.ToInt32(reader[14]);
                                    u.farm_id = (reader[15] == DBNull.Value) ? -1 : Convert.ToInt32(reader[15]);
                                    u.enabled = (reader[16] == DBNull.Value) ? ((short)-1) : ((short)Convert.ToInt32(reader[16]));
                                    u.User_RegDateTime = (reader[17] == DBNull.Value) ? DateTime.Now : Convert.ToDateTime(reader[17]);
                                    return u;
                                }
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }
                }
            }
            return null;
        }

    }
}



//// Post api/User/Getuser
//       [HttpPost]
//       public user Getuser([FromBody]user userBody)
//       {
//           return (from u in db.users where (u.login.ToLower() == userBody.login.ToLower() && u.password.ToLower() == userBody.password.ToLower()) select u).FirstOrDefault(); 
//           //foreach (user u in db.users.AsEnumerable())
//           //{
//           //    if (u.login.ToLower() == userBody.login.ToLower() && u.password.ToLower() == userBody.password.ToLower())
//           //    {
//           //        return new user()
//           //              {
//           //                  address = u.address,
//           //                  company_id = u.company_id,
//           //                  country = u.country,
//           //                  email = u.email,
//           //                  enabled = u.enabled,
//           //                  farm_id = u.farm_id,
//           //                  fax_number = u.fax_number,
//           //                  first_name = u.first_name,
//           //                  id = u.id,
//           //                  last_name = u.last_name,
//           //                  login = u.login,
//           //                  mobile_phone = u.mobile_phone,
//           //                  password = u.password,
//           //                  phone_number = u.phone_number,
//           //                  role = u.role,
//           //                  skype_name = u.skype_name,
//           //                  syte_lang = u.syte_lang,
//           //                  User_RegDateTime = u.User_RegDateTime
//           //              };
//           //    }
//           //}
//           //return null;
//       }
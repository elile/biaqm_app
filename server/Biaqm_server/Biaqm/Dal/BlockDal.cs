using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace Biaqm.Dal
{
    public class BlockDal
    {
        public static string connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;

        public static List<Block> getBlocks(String FarmID)
        {
            string query = string.Format("SELECT [ID],[InsertDate],[InsertUser],[LastUpdate],[LastUpdateUser],[NameEng],[NameHe],[RegionID],[FarmID],[Remarks]  FROM [biaqm].[dbo].[Block]  WHERE FarmID='{0}'", FarmID);
            List<Block> Blocks = new List<Block>();
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
                                Block b = new Block();
                                b.ID = Convert.ToInt32(reader[0]);
                                b.InsertDate = Convert.ToDateTime(reader[1]);
                                b.InsertUser = (reader[2] == DBNull.Value) ? -1 : Convert.ToInt32(reader[2]);
                                b.LastUpdate = Convert.ToDateTime(reader[3]);
                                b.LastUpdateUser = (reader[4] == DBNull.Value) ? -1 : Convert.ToInt32(reader[4]);
                                b.NameEng = (reader[5] == DBNull.Value) ? "" : Convert.ToString(reader[5]);
                                b.NameHe = (reader[6] == DBNull.Value) ? "" : Convert.ToString(reader[6]);
                                b.RegionID = (reader[7] == DBNull.Value) ? -1 : Convert.ToInt32(reader[7]);
                                b.FarmID = (reader[8] == DBNull.Value) ? -1 : Convert.ToInt64(reader[8]);
                                b.Remarks = (reader[9] == DBNull.Value) ? "" : Convert.ToString(reader[9]);
                                Blocks.Add(b);
                            }
                        }
                    }
                }
            }
            return Blocks;
        }

    }
}
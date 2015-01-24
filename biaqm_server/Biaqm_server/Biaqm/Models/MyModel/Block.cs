using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Block
    {
        public int ID { get; set; }
        public System.DateTime InsertDate { get; set; }
        public Nullable<int> InsertUser { get; set; }
        public Nullable<System.DateTime> LastUpdate { get; set; }
        public Nullable<int> LastUpdateUser { get; set; }
        public string NameEng { get; set; }
        public string NameHe { get; set; }
        public Nullable<int> RegionID { get; set; }
        public Nullable<long> FarmID { get; set; }
        public string Remarks { get; set; }
    }
}
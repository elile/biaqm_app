using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Plot
    {
        public int id { get; set; }
        public string name { get; set; }
        public long farm_id { get; set; }
        public Nullable<int> region_id { get; set; }
        public Nullable<int> block_id { get; set; }
        public System.DateTime start_date { get; set; }
        public Nullable<System.DateTime> end_date { get; set; }
       
        

    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class ActivityType
    {
        public int id { get; set; }
        public string name { get; set; }
        public long ActivityGroup { get; set; }
        public Decimal ActualPrice { get; set; }
        public int YieldDescription { get; set; }
    }
}
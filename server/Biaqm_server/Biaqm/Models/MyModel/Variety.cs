using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Variety
    {
        public int id { get; set; }
        public string name { get; set; }
        public Nullable<int> crop_id { get; set; }

    }
}
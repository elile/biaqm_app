using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Farm
    {
        public long id { get; set; }
        public string InsertDate { get; set; }
        public Nullable<int> InsertUser { get; set; }
        public string LastUpdate { get; set; }
        public Nullable<int> LastUpdateUser { get; set; }
        public string name { get; set; }
        public string name_eng { get; set; }
        public Nullable<int> AreaUOM { get; set; }
        public Nullable<int> LengthUOM { get; set; }
        public Nullable<decimal> UnitsConversion { get; set; }
        public string contact_name { get; set; }
        public string contact_phone { get; set; }
        public string contact_fax { get; set; }
        public string contact_email { get; set; }
        public string tax_number { get; set; }
        public Nullable<long> company_id { get; set; }
    }
}
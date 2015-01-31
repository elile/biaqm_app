using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Worker
    {
        public int id { get; set; }
        public string name { get; set; }
        public int PaymentTypeID { get; set; }
        public Nullable<float> hour_cost { get; set; }
        public Nullable<float> standard_hours { get; set; }
        public Nullable<float> first_overtime { get; set; }
        public Nullable<float> second_overtime { get; set; }
        public Nullable<float> first_overtime_hours { get; set; }
        public Nullable<int> worker_type_id { get; set; }
        public Nullable<int> farm_id { get; set; }
        public Nullable<int> company_id { get; set; }
        public string payment_type { get; set; }
        

    }
}
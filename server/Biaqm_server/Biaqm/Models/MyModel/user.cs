using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class user
    {
        public Int64 id { get; set; }
        public string first_name { get; set; }
        public string last_name { get; set; }
        public string phone_number { get; set; }
        public string mobile_phone { get; set; }
        public string fax_number { get; set; }
        public string skype_name { get; set; }
        public string country { get; set; }
        public string address { get; set; }
        public string email { get; set; }
        public string login { get; set; }
        public string password { get; set; }
        public string syte_lang { get; set; }
        public Nullable<int> company_id { get; set; }
        public int role { get; set; }
        public Nullable<int> farm_id { get; set; }
        public Nullable<short> enabled { get; set; }
        public Nullable<System.DateTime> User_RegDateTime { get; set; }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class Company
    {
        public long id { get; set; }
        public string name { get; set; }
        public string English_name { get; set; }
        public string Company_number { get; set; }
        public string Tax_number { get; set; }
        public decimal County { get; set; }
        public double area { get; set; }
        public decimal Tax_percentage { get; set; }
        public string Company_Logo_Path { get; set; }
        public Nullable<int> length_unit_id { get; set; }
        public Nullable<System.DateTime> trial_end_date { get; set; }
        public Nullable<bool> UnderPayment { get; set; }
        public Nullable<decimal> LastPayment { get; set; }
        public Nullable<System.DateTime> LastPaymetDate { get; set; }
        public string Status { get; set; }
        public Nullable<int> PaymentMonth { get; set; }
        public string ContactPerson { get; set; }
        public string TelephoneNumber { get; set; }
        public string CellPhoneNumber { get; set; }
        public string Remarks { get; set; }
        public string Address { get; set; }
        public string Address2 { get; set; }
        public string CountyTxt { get; set; }
        public string CityTownShip { get; set; }
        public string Country { get; set; }
        public string StateProvince { get; set; }
        public string PostalZipCode { get; set; }
        public Nullable<System.DateTime> trial_start_date { get; set; }

    }
}
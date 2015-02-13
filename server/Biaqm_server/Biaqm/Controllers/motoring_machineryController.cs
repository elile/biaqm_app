using Biaqm.Dal;
using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace Biaqm.Controllers
{
    public class motoring_machineryController : ApiController
    {
        public List<motoring_machinery> Getmotoring_machinerys(String company_id, String purchase_date)
        {
            return motoring_machineryDal.getmotoring_machinerys(company_id, HttpContext.Current.Server.UrlDecode(purchase_date));
        }
    }
}

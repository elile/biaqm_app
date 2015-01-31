using Biaqm.Dal;
using Biaqm.Models.MyModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Biaqm.Controllers
{
    public class VarietyController : ApiController
    {
        public List<Variety> GetVarietys(String company_id, String crop_id, String plot_id)
        {
            return VarietyDal.getVariety(company_id, crop_id, plot_id);
        }
    }
}

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
    public class FarmController : ApiController
    {
        public List<Farm> GetFarms(string company_id)
        {
            return FarmDal.getFarms(company_id);
        }
    }
}

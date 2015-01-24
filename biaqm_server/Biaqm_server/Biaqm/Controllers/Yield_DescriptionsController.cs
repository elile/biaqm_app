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
    public class Yield_DescriptionsController : ApiController
    {
        public List<Yield_Descriptions> GetYield_Descriptions(string company_id)
        {
            return Yield_DescriptionsDal.getYield_Descriptions(company_id);
        }
    }
}

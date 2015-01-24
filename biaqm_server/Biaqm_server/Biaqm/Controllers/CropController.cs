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
    public class CropController : ApiController
    {
        public List<Crop> GetCrops(String company_id)
        {
            return CropDal.getCrop(company_id);
        }
    }
}

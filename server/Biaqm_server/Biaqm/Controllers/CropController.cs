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
    public class CropController : ApiController
    {
        public List<Crop> GetCrops(String company_id)
        {
            return CropDal.getCrop(company_id);
        }

        public List<Crop> GetCropByBlock(String block_id, String FromDate, String ToDate)
        {
            return CropDal.getCropByBlock(block_id,HttpContext.Current.Server.UrlDecode( FromDate), HttpContext.Current.Server.UrlDecode(ToDate));
        }
    }
}

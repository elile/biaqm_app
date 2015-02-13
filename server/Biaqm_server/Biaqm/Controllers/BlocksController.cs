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
    public class BlocksController : ApiController
    {
        public List<Block> GetBlocks(String FarmID)
        {
            return BlockDal.getBlocks(FarmID);
        }

        public List<Block> GetBlocksByCrop(String crop_id, String FromDate, String ToDate)
        {
            return BlockDal.getBlocksByCrop(crop_id, HttpContext.Current.Server.UrlDecode(FromDate), HttpContext.Current.Server.UrlDecode(ToDate));
        }

        public List<Block> GetBlocksByVariety(String variety_id, String FromDate, String ToDate)
        {
            return BlockDal.getBlocksByVariety(variety_id, HttpContext.Current.Server.UrlDecode(FromDate), HttpContext.Current.Server.UrlDecode(ToDate));
        }
    }
}

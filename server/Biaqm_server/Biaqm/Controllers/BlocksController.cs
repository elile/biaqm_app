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
    public class BlocksController : ApiController
    {
        public List<Block> GetBlocks(String FarmID)
        {
            return BlockDal.getBlocks(FarmID);
        }
    }
}

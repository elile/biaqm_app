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
    public class PlotsController : ApiController
    {
        public List<Plot> GetPlots([FromUri]String farm_id, [FromUri]String block_id, [FromUri]String start_date, [FromUri]String end_date)
        {
            return PlotDal.getPlots(farm_id,  block_id,  start_date,  end_date);
        }
    }
}

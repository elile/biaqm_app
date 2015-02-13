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
    public class PlotsController : ApiController
    {
        public List<Plot> GetPlots([FromUri]String farm_id, [FromUri]String block_id, [FromUri]String start_date, [FromUri]String end_date)
        {
            return PlotDal.getPlots(farm_id,  block_id,  HttpContext.Current.Server.UrlDecode(start_date),  HttpContext.Current.Server.UrlDecode(end_date));
        }

        public List<Plot> GetPlotsByVariety(String variety_id, String start_date, String end_date)
        {
            return PlotDal.getPlotsByVariety(variety_id, HttpContext.Current.Server.UrlDecode(start_date), HttpContext.Current.Server.UrlDecode(end_date));
        }
    }
}

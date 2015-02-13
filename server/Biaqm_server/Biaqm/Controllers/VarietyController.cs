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
    public class VarietyController : ApiController
    {
        public List<Variety> GetVarietys(String company_id, String crop_id, String plot_id)
        {
            return VarietyDal.getVariety(company_id, crop_id, plot_id);
        }

        public List<Variety> GetVarietysByBlock(String block_id, String FromDate, String ToDate)
        {//SELECT V.[id], V.[name], V.[crop_id] FROM [biaqm].[dbo].[variety] as V INNER JOIN [biaqm].[dbo].[variety_to_plot] AS VP ON VP.[variety_id] = V.[id] INNER JOIN [biaqm].[dbo].[plot] AS P ON P.[id] = VP.[plot_id] WHERE P.[block_id] = '144' AND VP.[StartDate] <= '2015' AND (VP.[EndDate] IS NULL OR VP.[EndDate] >= '2015' )
            return VarietyDal.getVarietysByBlock(block_id, HttpContext.Current.Server.UrlDecode(FromDate), HttpContext.Current.Server.UrlDecode(ToDate));
        }

        public List<Variety> GetVarietysByPlot(String plot_id, String FromDate, String ToDate)
        {
            // not in use - change the query

            return VarietyDal.getVarietysByPlot(plot_id, HttpContext.Current.Server.UrlDecode(FromDate), HttpContext.Current.Server.UrlDecode(ToDate));
        }
        
    }
}

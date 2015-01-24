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
    public class WorkerController : ApiController
    {
        public List<Worker> GetWorkers(String farm_id, String company_id, String start_date, String EndDate)
        {
            return WorkerDal.getWorker(farm_id, company_id, start_date, EndDate);
        }
    }
}

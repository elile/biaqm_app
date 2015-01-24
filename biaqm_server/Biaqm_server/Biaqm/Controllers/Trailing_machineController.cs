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
    public class Trailing_machineController : ApiController
    {
        public List<Trailing_machine> GetTrailing_machines(String company_id, String purchase_date)
        {
            return Trailing_machineDal.getTrailing_machine(company_id, purchase_date);
        }
    }
}

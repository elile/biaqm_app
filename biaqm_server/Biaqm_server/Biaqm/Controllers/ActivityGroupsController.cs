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
    public class ActivityGroupsController : ApiController
    {
        public List<ActivityGroups> GetActivityGroups(string company_id)
        {
            return ActivityGroupsDal.getActivityGroups(company_id);
        }
    }
}

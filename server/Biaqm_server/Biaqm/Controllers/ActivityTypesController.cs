using Biaqm.Dal;
using Biaqm.Models.MyModel;
using Biaqm.Secure;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Biaqm.Controllers
{
    public class ActivityTypesController : ApiController
    {
        //[Authorize]
        public List<ActivityType> GetActivityTypes(string company_id)
        {
            //var re = Request;
            //var header = re.Headers;
            //string tokenCode = "";
            //if (header.Contains("X-Token"))
            //{
            //    tokenCode = header.GetValues("X-Token").First();
            //}
            //CryptographyHelper cryp = new CryptographyHelper();
            //string userPass = cryp.Decrypt(cryp.GetX509Certificate("CN=WebAPI-Token"), tokenCode);
            //if ( true)
            //{
                return ActivityTypesDal.getActivityType(company_id);
                
            //}
        }
    }
}

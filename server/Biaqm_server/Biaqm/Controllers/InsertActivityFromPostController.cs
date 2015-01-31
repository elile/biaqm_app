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
    public class InsertActivityFromPostController : ApiController
    {
        [HttpPost]
        public Int64 InsertActivity(InsertActivityFromPost insertActivityFromPost, [FromUri]Boolean inWrite)
        {

            return InsertActivityFromPostDal.InsertActivity(insertActivityFromPost, inWrite);
        }
    }
}

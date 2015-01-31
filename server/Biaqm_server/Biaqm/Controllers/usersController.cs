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
    public class UsersController : ApiController
    {
        public Status Authenticate(user user)
        {
            //if (user == null)
            //    throw new HttpResponseException(new HttpResponseMessage() { StatusCode = HttpStatusCode.Unauthorized, Content = new StringContent("Please provide the credentials.") });

            if (user != null)
            {
                if (IdentityStore.IsValidUser(user))
                {
                    Token token = new Token(user.login, Request.GetClientIP());
                    return new Status { Successeded = true, Token = token.Encrypt(), Message = "Successfully signed in." };
                } 
            }
            return null;
            //else
            //{
            //    throw new HttpResponseException(new HttpResponseMessage() { StatusCode = HttpStatusCode.Unauthorized, Content = new StringContent("Invalid user name or password.") });
            //}
        }
    }
}

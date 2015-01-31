using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace Biaqm.Secure
{
    public class HTTPSGuard : DelegatingHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            //if (!request.RequestUri.Scheme.Equals(Uri.UriSchemeHttps, StringComparison.OrdinalIgnoreCase))
            //{
            //   HttpResponseMessage reply = request.CreateErrorResponse(HttpStatusCode.BadRequest, "Security reason, Error.");
            //    return Task.FromResult(reply);
            //}

            return base.SendAsync(request, cancellationToken);
        }
    }
}
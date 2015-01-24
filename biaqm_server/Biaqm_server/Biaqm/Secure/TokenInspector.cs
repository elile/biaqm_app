﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace Biaqm.Secure
{
    public class TokenInspector : DelegatingHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            const string TOKEN_NAME = "X-Token";

            if (request.Headers.Contains(TOKEN_NAME))
            {
                string encryptedToken = request.Headers.GetValues(TOKEN_NAME).First();
                try
                {
                    Token token = Token.Decrypt(encryptedToken);
                    bool isValidUserId = IdentityStore.IsValidUserId(token.UserId);
                    bool requestIPMatchesTokenIP = token.IP.Equals(request.GetClientIP());

                    if (!isValidUserId || !requestIPMatchesTokenIP)
                    {
                        HttpResponseMessage reply = request.CreateErrorResponse(HttpStatusCode.Unauthorized, "Unauthorized");
                        return Task.FromResult(reply);
                    }
                }
                catch (Exception ex)
                {
                    HttpResponseMessage reply = request.CreateErrorResponse(HttpStatusCode.Unauthorized, "Unauthorized");
                    return Task.FromResult(reply);
                }
            }
            else
            {
                HttpResponseMessage reply = request.CreateErrorResponse(HttpStatusCode.Unauthorized, "Unauthorized");
                return Task.FromResult(reply);
            }

            return base.SendAsync(request, cancellationToken);
        }

    }
}
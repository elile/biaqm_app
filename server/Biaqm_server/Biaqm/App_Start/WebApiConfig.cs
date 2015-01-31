using Biaqm.Secure;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Dispatcher;

namespace Biaqm
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            //Create and instance of TokenInspector setting the default inner handler
            TokenInspector tokenInspector = new TokenInspector() { InnerHandler = new HttpControllerDispatcher(config) };

            var json = config.Formatters.JsonFormatter;
            json.SerializerSettings.PreserveReferencesHandling = Newtonsoft.Json.PreserveReferencesHandling.Objects;
            config.Formatters.Remove(config.Formatters.XmlFormatter);

            //Just exclude the users controllers from need to provide valid token, so they could authenticate
            config.Routes.MapHttpRoute(
                name: "Authentication",
                routeTemplate: "api/users/{id}",
                defaults: new { controller = "users" }
            );

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional },
                constraints: null,
                handler: tokenInspector
            );

            // Uncomment the following line of code to enable query support for actions with an IQueryable or IQueryable<T> return type.
            // To avoid processing unexpected or malicious queries, use the validation settings on QueryableAttribute to validate incoming queries.
            // For more information, visit http://go.microsoft.com/fwlink/?LinkId=279712.
            //config.EnableQuerySupport();

            // To disable tracing in your application, please comment out or remove the following line of code
            // For more information, refer to: http://www.asp.net/web-api
            config.EnableSystemDiagnosticsTracing();
            config.MessageHandlers.Add(new HTTPSGuard()); //Global handler - applicable to all the requests

        }
    }
}

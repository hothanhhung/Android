using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;

namespace hthservices
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Map this rule first
            config.Routes.MapHttpRoute(
                 "WithActionApi",
                 "api/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional }
             );

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
            config.MessageHandlers.Add(new hthservices.Utils.RequestFilterHandler());
            // Uncomment the following line of code to enable query support for actions with an IQueryable or IQueryable<T> return type.
            // To avoid processing unexpected or malicious queries, use the validation settings on QueryableAttribute to validate incoming queries.
            // For more information, visit http://go.microsoft.com/fwlink/?LinkId=279712.
            //config.EnableQuerySupport();
        }
    }
}
﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace TiviOnline
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");
            routes.IgnoreRoute("Content/*");
            routes.IgnoreRoute("Scripts/*");
            routes.IgnoreRoute("Images/*");
            routes.MapRoute(
                    name: "Contact",
                    url: "lien-he",
                    defaults: new
                    {
                        controller = "Home",
                        action = "Contact"
                    }
                    );
            routes.MapRoute(
                    name: "Schedule",
                    url: "lich-phat-song/{channel}/{date}",
                    defaults: new
                    {
                        controller = "Home",
                        action = "Schedule",
                        channel = UrlParameter.Optional,
                        date = UrlParameter.Optional
                    }
                    );
            routes.MapRoute(
                    name: "WatchTivi",
                    url: "Xem-Tivi/{channel}/{id}",
                    defaults: new
                    {
                        controller = "Home",
                        action = "Index",
                        channel = UrlParameter.Optional,
                        id = UrlParameter.Optional
                    }
                    );
            routes.MapRoute(
                   name: "Report",
                   url: "Report/{channel}/{id}",
                   defaults: new
                   {
                       controller = "Home",
                       action = "Report",
                       channel = UrlParameter.Optional,
                       id = UrlParameter.Optional
                   }
                   );
            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional }
            );
        }
    }
}
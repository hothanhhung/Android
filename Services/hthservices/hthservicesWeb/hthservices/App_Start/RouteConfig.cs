﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace hthservices
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("FileMan/*");
            routes.IgnoreRoute("Admin/*");
            routes.IgnoreRoute("AdminWeb/*");
            routes.IgnoreRoute("CKEditorUpload.ashx");
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");
            routes.MapRoute(
                    name: "ProgrammingContent",
                    url: "chi-tiet/{category_name}/{content_name}/{id}",
                    defaults: new
                    {
                        controller = "Programming",
                        action = "Content",
                        category_name = UrlParameter.Optional,
                        content_name = UrlParameter.Optional,
                        id = UrlParameter.Optional
                    }
                    );
            routes.MapRoute(
                    name: "ProgrammingCategory",
                    url: "danh-muc/{category_name}/{id}",
                    defaults: new
                    {
                        controller = "Programming",
                        action = "Content",
                        category_name = UrlParameter.Optional,
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
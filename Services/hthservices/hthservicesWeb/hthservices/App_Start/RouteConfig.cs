using System;
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
            routes.IgnoreRoute("Web/*");
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
                        action = "Category",
                        category_name = UrlParameter.Optional,
                        id = UrlParameter.Optional
                    }
                    );
            routes.MapRoute(
                    name: "ProjectContent",
                    url: "chi-tiet/du-an/{content_name}/{id}",
                    defaults: new
                    {
                        controller = "Project",
                        action = "Content",
                        content_name = UrlParameter.Optional,
                        id = UrlParameter.Optional
                    }
                    );

            routes.MapRoute(
                name: "About",
                url: "gioi-thieu",
                defaults: new { controller = "Home", action = "About", id = UrlParameter.Optional }
            );

            routes.MapRoute(
                name: "Contact",
                url: "lien-he",
                defaults: new { controller = "Home", action = "Contact", id = UrlParameter.Optional }
            );

            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Programming", action = "Index", id = UrlParameter.Optional }
            );
        }
    }
}
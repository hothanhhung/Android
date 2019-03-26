using hthservices.Models.Website;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Web;

namespace hthservices.DataBusiness
{
    public class WebsiteDataContext: DbContext
    {
        public WebsiteDataContext(DbConnection connection)
            : base(connection, true) 
        {
        }
        public WebsiteDataContext(string ConnectString)
        {
            // TODO: Complete member initialization
            this.Database.Connection.ConnectionString = ConnectString;
        }
        public DbSet<ProgrammingCategory> ProgrammingCategories { get; set; }

        public DbSet<ProgrammingContent> ProgrammingContents { get; set; }
        public DbSet<ProgrammingComment> ProgrammingComments { get; set; }
        public DbSet<Project> Projects { get; set; }
        
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            // Chinook Database does not pluralize table names
            modelBuilder.Conventions
                .Remove<PluralizingTableNameConvention>();
            //modelBuilder.Entity<ProgrammingCategory>()
            //     .HasMany(c => c.Contents)
            //        .WithOptional()
            //        .Map(m => m.MapKey("CategoryId"));

            //modelBuilder.Entity<ProgrammingContent>()
            //     .HasRequired(w => w.Category)
            //     .WithMany()
            //     .Map(m => m.MapKey("CategoryId"));
        }
    }
}
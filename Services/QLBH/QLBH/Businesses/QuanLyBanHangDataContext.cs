using QLBH.Models;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Web;

namespace QLBH.Businesses
{
    public class QuanLyBanHangDataContext: DbContext
    {
        public QuanLyBanHangDataContext(DbConnection connection)
            : base(connection, true) 
        {
        }
        public QuanLyBanHangDataContext(string ConnectString)
        {
            // TODO: Complete member initialization
            this.Database.Connection.ConnectionString = ConnectString;
        }
        public DbSet<Category> Categories { get; set; }
        public DbSet<Customer> Customers { get; set; }
        public DbSet<IssueProduct> IssueProducts { get; set; }
        public DbSet<OrderDetail> OrderDetails { get; set; }
        public DbSet<Order> Orders { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<Receipt> Receipts { get; set; }
        
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            // Chinook Database does not pluralize table names
            //modelBuilder.Conventions
            //    .Remove<PluralizingTableNameConvention>();
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
using QLBH.Commons;
using QLBH.Models;
using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Businesses
{
    public class ProductProcesser
    {
        public static List<Product> GetProducts()
        {
            List<Product> products = new List<Product>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                products = context.Products.ToList();
                foreach (var pd in products)
                {
                    pd.CategoryName = context.Categories.Where(p => p.CategoryId == pd.CategoryId).First().CategoryName;
                }
            }
            return products;
        }
        public static Product GetProduct(int productId)
        {
            Product obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var products = context.Products.Where(p => p.ProductId == productId);

                obj = products.FirstOrDefault();
            }
            return obj;
        }

        public static bool SaveProduct(Product product)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Products.FirstOrDefault(p => p.ProductId == product.ProductId);
                if (obj == null)
                {
                    context.Products.Add(product);
                }
                else
                {
                    obj.ProductName = product.ProductName;
                    obj.CategoryId = product.CategoryId;
                    obj.PriceForSelling = product.PriceForSelling;
                    obj.Unit = product.Unit;
                    obj.Note = product.Note;
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }

        public static bool DeleteProduct(int productId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Products.FirstOrDefault(p => p.ProductId == productId);
                if (obj != null)
                {
                    context.Products.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

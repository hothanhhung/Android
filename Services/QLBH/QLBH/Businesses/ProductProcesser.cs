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
        public static List<Product> GetProducts(bool isIncludeQuantity = false)
        {
            List<Product> products = new List<Product>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                products = context.Products.ToList();
                if (isIncludeQuantity)
                {
                    foreach (var product in products)
                    {
                        var receiptsForProduct = context.Receipts.Where(r => r.IsSellAll == 0 && r.ProductId == product.ProductId);
                        if (receiptsForProduct.Any())
                        {
                            var issueProducts = context.IssueProducts.Where(i => receiptsForProduct.Any(r => r.ReceiptId == i.ReceiptId));
                            var orderDetailsOfIssuedProduct = context.OrderDetails.Where(o => (issueProducts.Any(i => i.OrderDetailId == o.OrderDetailId)) || (o.Lock == 0 && o.ProductId == product.ProductId));

                            product.Quantity = receiptsForProduct.Sum(r => r.Quantity);
                            if (orderDetailsOfIssuedProduct.Any())
                            {
                                product.Quantity = product.Quantity - orderDetailsOfIssuedProduct.Sum(r => r.Quantity);
                            }
                        }
                        
                    }
                }
            }
            return products;
        }

        public static List<Product> GetProducts(List<int> productIds, string beforeDate, int? ignoreOrderId, bool isIncludeQuantity = false)
        {
            List<Product> products = new List<Product>();
            if (productIds != null && productIds.Count > 0)
            {
                using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
                {
                    products = context.Products.Where(p => productIds.Any(id => id == p.ProductId)).ToList();
                    if (isIncludeQuantity)
                    {
                        foreach (var product in products)
                        {
                            var receiptsForProduct = context.Receipts.Where(r => r.IsSellAll == 0 && r.ProductId == product.ProductId && r.DatedReceipt.CompareTo(beforeDate)<=0);
                            if (receiptsForProduct.Any())
                            {
                                var issueProducts = context.IssueProducts.Where(i => receiptsForProduct.Any(r => r.ReceiptId == i.ReceiptId));
                                var orderDetailsOfIssuedProduct = context.OrderDetails.Where(o => (issueProducts.Any(i => i.OrderDetailId == o.OrderDetailId)) || (o.Lock == 0 && o.ProductId == product.ProductId) || (ignoreOrderId.HasValue && o.OrderDetailId == ignoreOrderId.Value));

                                product.Quantity = receiptsForProduct.Sum(r => r.Quantity);
                                if (orderDetailsOfIssuedProduct.Any())
                                {
                                    product.Quantity = product.Quantity - orderDetailsOfIssuedProduct.Sum(r => r.Quantity);
                                }
                            }

                        }
                    }
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

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
    public class OrderProcesser
    {
        public static List<Order> GetOrders(string phone = "", string from = "", string to = "")
        {
            List<Order> categories = new List<Order>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var query = context.Orders.AsQueryable();
                if (!string.IsNullOrWhiteSpace(phone))
                {
                    query = query.Where(p => p.Phone == phone);
                }
                if (!string.IsNullOrWhiteSpace(from))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(from) >= 0);
                }
                if (!string.IsNullOrWhiteSpace(to))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(to) <= 0);
                }
                categories = query.ToList();
            }
            return categories;
        }
        public static Order GetOrder(int orderId)
        {
            Order obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var orders = context.Orders.Where(p => p.OrderId == orderId);

                obj = orders.FirstOrDefault();
            }
            return obj;
        }

        public static Order SaveOrder(Order order)
        {
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Orders.FirstOrDefault(p => p.OrderId == order.OrderId);
                if (obj == null)
                {
                    obj = context.Orders.Add(order);
                }
                else
                {
                    obj.CustomerName = order.CustomerName ;
                    obj.Phone = order.Phone;
                    obj.CreatedDate = order.CreatedDate;
                    obj.Address = order.Address;
                    obj.Note = order.Note;
                    obj.FeeForShipping = order.FeeForShipping;
                    obj.Status = order.Status;
                }
                if(context.SaveChanges() > 0)
                {
                    return obj;
                }
            }
            return order;
        }

        public static bool DeleteOrder(int orderId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Orders.FirstOrDefault(p => p.OrderId == orderId);
                if (obj != null)
                {
                    context.Orders.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

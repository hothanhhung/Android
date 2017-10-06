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
    public class OrderDetailProcesser
    {
        public static List<OrderDetail> GetOrderDetails(int? orderId = null)
        {
            List<OrderDetail> orderDetails = new List<OrderDetail>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var query = context.OrderDetails.AsQueryable();
                if (orderId.HasValue && orderId.Value > 0)
                {
                    query = query.Where(o => o.OrderId == orderId.Value);
                }
                orderDetails = query.ToList();
            }
            return orderDetails;
        }
        public static OrderDetail GetOrderDetail(int orderDetailId)
        {
            OrderDetail obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var orderDetails = context.OrderDetails.Where(p => p.OrderDetailId == orderDetailId);

                obj = orderDetails.FirstOrDefault();
            }
            return obj;
        }

        public static OrderDetail SaveOrderDetail(OrderDetail orderDetail)
        {
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.OrderDetails.FirstOrDefault(p => p.OrderDetailId == orderDetail.OrderDetailId);
                if (obj == null)
                {
                    obj = context.OrderDetails.Add(orderDetail);
                }
                else
                {
                    obj.OrderId = orderDetail.OrderId;
                    obj.ProductId = orderDetail.ProductId;
                    obj.Quantity = orderDetail.Quantity;
                    obj.PriceForUnit = orderDetail.PriceForUnit;
                    obj.Lock = orderDetail.Lock;
                }
                if (context.SaveChanges() > 0)
                    return obj;
            }
            return orderDetail;
        }

        public static bool DeleteOrderDetail(int orderDetailId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.OrderDetails.FirstOrDefault(p => p.OrderDetailId == orderDetailId);
                if (obj != null)
                {
                    context.OrderDetails.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

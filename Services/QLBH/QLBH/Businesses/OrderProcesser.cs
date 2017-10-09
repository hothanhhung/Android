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
        public static List<Order> GetOrders(int ?status, string name = "", string phone = "", string from = "", string to = "")
        {
            List<Order> categories = new List<Order>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var query = context.Orders.AsQueryable();
                if (status.HasValue)
                {
                    query = query.Where(p => p.Status == status.Value);
                }
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

                if (!string.IsNullOrWhiteSpace(name))
                {
                    categories = categories.Where(p => MethodHelpers.RemoveSign4VietnameseString(p.CustomerName.Trim().ToLower()).Contains(MethodHelpers.RemoveSign4VietnameseString(name.Trim().ToLower()))).ToList();
                }
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

        public static bool CancelOrder(Order order)
        {
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Orders.FirstOrDefault(p => p.OrderId == order.OrderId);
                if (obj != null)
                {
                    obj.Status = order.Status = 3;
                    var orderDetails = context.OrderDetails.Where(o => o.OrderId == order.OrderId);
                    if (orderDetails!=null)
                    {
                        foreach (var orderDetail in orderDetails)
                        {
                            orderDetail.Lock = 1;
                        }
                    }
                    return context.SaveChanges() > 0;
                }
            }
            return false;
        }

        public static bool DoneOrder(Order order)
        {
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Orders.FirstOrDefault(p => p.OrderId == order.OrderId);
                if (obj != null)
                {
                    obj.Status = order.Status = 2;
                    var orderDetails = context.OrderDetails.Where(o => o.OrderId == order.OrderId);
                    if (orderDetails != null)
                    {
                        foreach (var orderDetail in orderDetails)
                        {
                            int remainNumber = orderDetail.Quantity;
                            var receipts = context.Receipts.Where(r => r.RemainAfterDone > 0 && r.ProductId == orderDetail.ProductId).OrderBy(r => r.DatedReceipt);
                            foreach (var receipt in receipts)
                            {
                                int reduct = 0;
                                if (receipt.RemainAfterDone <= remainNumber)
                                {
                                    remainNumber = remainNumber - receipt.RemainAfterDone;
                                    reduct = receipt.RemainAfterDone;
                                    receipt.RemainAfterDone = 0;
                                }
                                else
                                {
                                    receipt.RemainAfterDone = receipt.RemainAfterDone - remainNumber;
                                    reduct = remainNumber;
                                    remainNumber = 0;
                                }

                                context.IssueProducts.Add(new IssueProduct()
                                {
                                    OrderDetailId = orderDetail.OrderDetailId,
                                    ReceiptId = receipt.ReceiptId,
                                    Quantity = reduct
                                });

                                if (remainNumber == 0)
                                {
                                    break;
                                }
                            }
                            orderDetail.Lock = 1;
                        }
                    }
                    return context.SaveChanges() > 0;
                }
            }
            return false;
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

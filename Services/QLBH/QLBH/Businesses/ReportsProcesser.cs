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
    public class ReportsProcesser
    {
        public static List<ReportItem> GenerateReportOfProfit(int? productId, string from, string to, int group = 0, bool isIncludeFee = true, string phone = "")
        {
            var reportItems= new List<ReportItem>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                int lengthOfDate = 10;
                List<Order> orders = new List<Order>();
                var query = context.Orders.Where(o=>o.Status == 2);
                if (!string.IsNullOrWhiteSpace(from))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(from) >= 0);
                }
                if (!string.IsNullOrWhiteSpace(to))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(to) <= 0);
                }
                orders = query.ToList();
                if (!string.IsNullOrWhiteSpace(phone))
                {
                    orders = orders.Where(p => p.Phone.Contains(phone)).ToList();
                }
                foreach (var order in orders)
                {
                    var reportItem = new ReportItem()
                    {
                        CustomerName = order.CustomerName,
                        PhoneNumber = order.Phone,
                        FeeForShip = order.FeeForShipping,
                        DateForReport = order.CreatedDate.Substring(0, lengthOfDate)
                    };
                    var orderDetails = context.OrderDetails.Where(o => o.OrderId == order.OrderId);

                    if (productId.HasValue)
                    {
                        orderDetails = orderDetails.Where(o => o.ProductId == productId.Value);
                    }

                    if (orderDetails != null && orderDetails.Count() > 0)
                    {
                        reportItem.InCome = orderDetails.Sum(od => od.Quantity * od.PriceForUnit);

                        var issueProducts = context.IssueProducts.Where(i => orderDetails.Any(o => o.OrderDetailId == i.OrderDetailId));
                        var receipts = context.Receipts.Where(r => issueProducts.Any(i => i.ReceiptId == r.ReceiptId)).ToList();

                        foreach (var issueProduct in issueProducts)
                        {
                            var priceOfReceipt = receipts.FirstOrDefault(i => i.ReceiptId == issueProduct.ReceiptId);
                            if (priceOfReceipt != null)
                            {
                                reportItem.OutCome += issueProduct.Quantity * priceOfReceipt.PriceOfAllForReceipting;
                            }
                        }

                        reportItems.Add(reportItem);
                    }
                }
                if (reportItems.Count > 0)
                {
                    switch (group)
                    {
                        case 1: // month
                            lengthOfDate = 7;
                            reportItems = reportItems.GroupBy(r => r.DateForReport.Substring(0, lengthOfDate)).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                CustomerName = gp.First().CustomerName,
                                PhoneNumber = gp.First().PhoneNumber,
                                DateForReport = gp.Key,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                        case 2: // year
                            lengthOfDate = 4;
                            reportItems = reportItems.GroupBy(r => r.DateForReport.Substring(0, lengthOfDate)).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                CustomerName = gp.First().CustomerName,
                                PhoneNumber = gp.First().PhoneNumber,
                                DateForReport = gp.Key,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                        default: // date
                            reportItems = reportItems.GroupBy(r => r.DateForReport).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                CustomerName = gp.First().CustomerName,
                                PhoneNumber = gp.First().PhoneNumber,
                                DateForReport = gp.Key,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                    }

                }
                if (isIncludeFee)
                {
                    var operationFees = context.OperationFees.AsQueryable();
                    if (!string.IsNullOrWhiteSpace(from))
                    {
                        operationFees = operationFees.Where(p => p.CreatedDate.CompareTo(from) >= 0);
                    }
                    if (!string.IsNullOrWhiteSpace(to))
                    {
                        operationFees = operationFees.Where(p => p.CreatedDate.CompareTo(to) <= 0);
                    }

                    foreach (var operationFee in operationFees)
                    {
                        var reportItem = new ReportItem()
                        {
                            DateForReport = operationFee.CreatedDate.Substring(0, lengthOfDate),
                            Name = operationFee.OperationFeeName,
                            Fee = operationFee.Fee
                        };
                        reportItems.Add(reportItem);
                    }
                }
            }
            
            reportItems = reportItems.OrderBy(r => r.DateForReport).ToList();
            return reportItems;
        }


        public static List<ReportItem> GenerateReportOfProfitOnProducts(List<int> productIds, string from, string to, int group = 0)
        {
            var reportItems = new List<ReportItem>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                int lengthOfDate = 10;
                List<Order> orders = new List<Order>();
                var query = context.Orders.Where(o=>o.Status == 2);
                if (!string.IsNullOrWhiteSpace(from))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(from) >= 0);
                }
                if (!string.IsNullOrWhiteSpace(to))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(to) <= 0);
                }
                orders = query.ToList();
                var orderDetailsQuery = context.OrderDetails.Where(o => query.Any(order => o.OrderId == order.OrderId) && productIds.Any(p => o.ProductId == p));
                var orderDetailsList = orderDetailsQuery.ToList();
                var issueProductsQuery = context.IssueProducts.Where(i => orderDetailsQuery.Any(o => o.OrderDetailId == i.OrderDetailId));
                var issueProductsList = issueProductsQuery.ToList();
                var receiptsList = context.Receipts.Where(r => issueProductsQuery.Any(i => i.ReceiptId == r.ReceiptId)).ToList();

                foreach (var order in orders)
                {
                    var orderDetails = orderDetailsList.Where(o => o.OrderId == order.OrderId);
                    
                    foreach (var orderDetail in orderDetails)
                    {
                        var reportItem = new ReportItem()
                        {
                            ProductId = orderDetail.ProductId,
                            DateForReport = order.CreatedDate.Substring(0, lengthOfDate)
                        };
                        reportItem.InCome = orderDetail.Quantity * orderDetail.PriceForUnit;

                        var issueProducts = issueProductsList.Where(i => orderDetail.OrderDetailId == i.OrderDetailId);
                        var receipts = receiptsList.Where(r => issueProducts.Any(i => i.ReceiptId == r.ReceiptId)).ToList();

                        foreach (var issueProduct in issueProducts)
                        {
                            var priceOfReceipt = receipts.FirstOrDefault(i => i.ReceiptId == issueProduct.ReceiptId);
                            if (priceOfReceipt != null)
                            {
                                reportItem.OutCome += issueProduct.Quantity * priceOfReceipt.PriceOfAllForReceipting;
                            }
                        }

                        reportItems.Add(reportItem);
                    }
                }
                if (reportItems.Count > 0)
                {
                    switch (group)
                    {
                        case 1: // month
                            lengthOfDate = 7;
                            reportItems = reportItems.GroupBy(r => new { ProductId = r.ProductId, Date = r.DateForReport.Substring(0, lengthOfDate) }).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                DateForReport = gp.Key.Date,
                                ProductId = gp.Key.ProductId,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = 0,// gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                        case 2: // year
                            lengthOfDate = 4;
                            reportItems = reportItems.GroupBy(r => new { ProductId = r.ProductId, Date = r.DateForReport.Substring(0, lengthOfDate) }).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                DateForReport = gp.Key.Date,
                                ProductId = gp.Key.ProductId,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = 0,// gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                        default: // date
                            reportItems = reportItems.GroupBy(r => new { ProductId = r.ProductId, Date = r.DateForReport}).Select(gp => new ReportItem()
                            {
                                Name = "Bán Hàng",
                                DateForReport = gp.Key.Date,
                                ProductId = gp.Key.ProductId,
                                Fee = 0,// gp.Sum(g => g.Fee),
                                FeeForShip = 0,// gp.Sum(g => g.FeeForShip),
                                InCome = gp.Sum(g => g.InCome),
                                OutCome = gp.Sum(g => g.OutCome)
                            }).ToList();
                            break;
                    }

                }
                
            }
            reportItems = reportItems.OrderBy(r => r.DateForReport).ToList();
            return reportItems;
        }
    }
}

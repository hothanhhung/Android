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
    public class ReceiptProcesser
    {
        public static List<Receipt> GetReceipts(int? productId = null, string from = "", string to = "", bool isGroup = false)
        {
            List<Receipt> receipts = new List<Receipt>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var query = context.Receipts.AsQueryable();
                if (productId.HasValue && productId.Value > 0)
                {
                    query = query.Where(p => p.ProductId == productId.Value);
                }
                if(!string.IsNullOrWhiteSpace(from))
                {
                    query = query.Where(p => p.DatedReceipt.CompareTo(from) >= 0);
                }
                if (!string.IsNullOrWhiteSpace(to))
                {
                    query = query.Where(p => p.DatedReceipt.CompareTo(to) <= 0);
                }
                if (isGroup)
                {
                    receipts = query.ToList().GroupBy(p => p.ProductId).Select(group => new Receipt() { 
                        ProductId = group.First().ProductId, 
                        Quantity = group.Sum(g => g.Quantity), 
                        PriceOfAllForReceipting = group.Sum(g => g.PriceOfAllForReceipting),
                        IsSellAll = group.Any(g=>g.IsSellAll==0)?0:1
                    }).ToList();
                }
                else
                {
                    receipts = query.ToList();
                }
            }
            return receipts;
        }
        public static Receipt GetReceipt(int receiptId)
        {
            Receipt obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var receipts = context.Receipts.Where(p => p.ReceiptId == receiptId);

                obj = receipts.FirstOrDefault();
            }
            return obj;
        }

        public static bool SaveReceipt(Receipt receipt)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Receipts.FirstOrDefault(p => p.ReceiptId == receipt.ReceiptId);
                if (obj == null)
                {
                    context.Receipts.Add(receipt);
                }
                else
                {
                    obj.ProductId = receipt.ProductId;
                    obj.Quantity = receipt.Quantity;
                    obj.IsSellAll = receipt.IsSellAll;
                    obj.DatedReceipt = receipt.DatedReceipt;
                    obj.Note = receipt.Note;
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }

        public static bool DeleteReceipt(int receiptId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Receipts.FirstOrDefault(p => p.ReceiptId == receiptId);
                if (obj != null)
                {
                    context.Receipts.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

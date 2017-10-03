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
        public static List<Receipt> GetReceipts()
        {
            List<Receipt> receipts = new List<Receipt>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                receipts = context.Receipts.ToList();
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
                    obj.IsSellAll = 0;
                    obj.DatedReceipt = MethodHelpers.ConvertDateTimeToCorrectString(DateTime.Now);
                    context.Receipts.Add(receipt);
                }
                else
                {
                    obj.ProductId = receipt.ProductId;
                    obj.Quantity = receipt.Quantity;
                    obj.IsSellAll = receipt.IsSellAll;
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

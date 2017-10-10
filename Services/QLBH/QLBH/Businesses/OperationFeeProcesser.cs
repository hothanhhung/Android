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
    public class OperationFeeProcesser
    {
        public static List<OperationFee> GetOperationFees(string name, string from, string to)
        {
            List<OperationFee> operationFees = new List<OperationFee>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var query = context.OperationFees.AsQueryable();
                if (!string.IsNullOrWhiteSpace(from))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(from) >= 0);
                }
                if (!string.IsNullOrWhiteSpace(to))
                {
                    query = query.Where(p => p.CreatedDate.CompareTo(to) <= 0);
                }
                operationFees = query.ToList();

                if (!string.IsNullOrWhiteSpace(name))
                {
                    operationFees = operationFees.Where(p => MethodHelpers.RemoveSign4VietnameseString(p.OperationFeeName.Trim().ToLower()).Contains(MethodHelpers.RemoveSign4VietnameseString(name.Trim().ToLower()))).ToList();
                }
            }
            return operationFees;
        }
        public static OperationFee GetOperationFee(int operationFeeId)
        {
            OperationFee obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var operationFees = context.OperationFees.Where(p => p.OperationFeeId == operationFeeId);

                obj = operationFees.FirstOrDefault();
            }
            return obj;
        }

        public static OperationFee SaveCustomer(OperationFee operationFee)
        {
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.OperationFees.FirstOrDefault(p => p.OperationFeeId == operationFee.OperationFeeId);
                if (obj == null)
                {
                    obj = context.OperationFees.Add(operationFee);
                }
                else
                {
                    obj.OperationFeeName = operationFee.OperationFeeName;
                    obj.Fee = operationFee.Fee;
                    obj.Note = operationFee.Note;
                    obj.CreatedDate = operationFee.CreatedDate;

                }
                if (context.SaveChanges()>0)
                {
                    return obj;
                }
            }
            return operationFee;
        }

        public static bool DeleteOperationFee(int operationFeeId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.OperationFees.FirstOrDefault(p => p.OperationFeeId == operationFeeId);
                if (obj != null)
                {
                    context.OperationFees.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

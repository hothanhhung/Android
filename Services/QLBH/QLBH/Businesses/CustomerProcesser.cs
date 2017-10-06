
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
    public class CustomerProcesser
    {
        public static List<Customer> GetCustomers()
        {
            List<Customer> customers = new List<Customer>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                customers = context.Customers.ToList();
            }
            return customers;
        }
        public static Customer GetCustomer(string phoneNumber)
        {
            Customer obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var customers = context.Customers.Where(p => p.PhoneNumber == phoneNumber);

                obj = customers.FirstOrDefault();
            }
            return obj;
        }

        public static bool SaveCustomer(Customer customer, bool isUpdatingInfo = false)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Customers.FirstOrDefault(p => p.PhoneNumber == customer.PhoneNumber);
                if (obj == null)
                {
                    context.Customers.Add(customer);
                }
                else
                {
                    obj.CustomerName = customer.CustomerName;
                    obj.DeliveryAddress = customer.DeliveryAddress;

                    if (!isUpdatingInfo)
                    {
                        obj.Address = customer.Address;
                        obj.Email = customer.Email;
                        obj.Note = customer.Note;
                    }
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }

        public static bool DeleteCustomer(string phoneNumber)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Customers.FirstOrDefault(p => p.PhoneNumber == phoneNumber);
                if (obj != null)
                {
                    context.Customers.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

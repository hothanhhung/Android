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
    public class CategoryProcesser
    {
        public static List<Category> GetCategories()
        {
            List<Category> categories = new List<Category>();
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                categories = context.Categories.ToList();
                foreach (var cat in categories)
                {
                    cat.NumberOfProducts = context.Products.Where(p => p.CategoryId == cat.CategoryId).Count();
                }
            }
            return categories;
        }
        public static Category GetCategory(int categorId)
        {
            Category obj = null;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var categories = context.Categories.Where(p => p.CategoryId == categorId);

                obj = categories.FirstOrDefault();
            }
            return obj;
        }

        public static bool SaveCategory(Category category)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                var obj = context.Categories.FirstOrDefault(p => p.CategoryId == category.CategoryId);
                if (obj == null)
                {
                    context.Categories.Add(category);
                }
                else
                {
                    obj.CategoryName = category.CategoryName;
                    obj.Note = category.Note;
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }

        public static bool DeleteCategory(int categoryId)
        {
            bool succ = false;
            using (var context = new QuanLyBanHangDataContext(new SQLiteConnection(ConstData.ConnectionString)))
            {
                if(context.Products.Any(p=>p.CategoryId == categoryId))
                {
                    return false;
                }
                var obj = context.Categories.FirstOrDefault(p => p.CategoryId == categoryId);
                if (obj != null)
                {
                    context.Categories.Remove(obj);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
    }
}

using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QLBH.Models
{
    public class Order
    {
        [Key]
       public int OrderId {get; set;}
       public string CustomerName {get; set;}
       public string Phone { get; set; }
       public string Address { get; set; }
       public int FeeForShipping {get; set;}
       public string CreatedDate { get; set; }
       public int Status {get; set;}
       public string Note { get; set; }
       [NotMapped]
       public string StatusInString
       {
           get
           {
               switch (Status)
               {
                   case 0: return "Đang Tạo";
                   case 1: return "Đang Xử Lý";
                   case 2: return "Hoàn Thành";
                   case 3: return "Đã Hủy Bỏ";
                   default: return "Không Xác Định";
               }
           }
       }
       [NotMapped]
       public int NumberOfProducts { get; set; }
       [NotMapped]
       public int TotalPrices { get; set; }
    }
}

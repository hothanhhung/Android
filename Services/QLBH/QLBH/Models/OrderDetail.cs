using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QLBH.Models
{
    public class OrderDetail
    {
        [Key]
       public int OrderDetailId {get; set;}
       public int OrderId {get; set;}
       public int ProductId {get; set;}
       public int Quantity {get; set;}
       public int PriceForUnit {get; set;}
       public string Note { get; set; }
        [NotMapped]
       public string ProductName { get; set; }
        [NotMapped]
        public int OrderDetailTotalPrice { get { return Quantity * PriceForUnit; } }
    }
}

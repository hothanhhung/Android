using System.ComponentModel.DataAnnotations;

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
    }
}

using System.ComponentModel.DataAnnotations;

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
    }
}

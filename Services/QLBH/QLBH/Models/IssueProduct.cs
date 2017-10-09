using System.ComponentModel.DataAnnotations;

namespace QLBH.Models
{
    public class IssueProduct
    {
        [Key]
       public int IssueProductId {get; set;}
       public int ReceiptId {get; set;}
       public int OrderDetailId { get; set; }
       public int Quantity { get; set; }
    }
}

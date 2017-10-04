using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QLBH.Models
{
    public class Receipt
    {
        [Key]
       public int ReceiptId {get; set;}
       public int ProductId {get; set;}
       public int Quantity {get; set;}
       public int PriceOfAllForReceipting {get; set;}
       public int IsSellAll {get; set;}
       public string DatedReceipt {get; set;}
       public string Note { get; set; }
        [NotMapped]
       public string ProductName { get; set; }
    }
}

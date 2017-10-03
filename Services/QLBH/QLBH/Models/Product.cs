using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QLBH.Models
{
    public class Product
    {
        [Key]
        public int ProductId {get; set;}
        public int CategoryId { get; set; }
        public string ProductName { get; set; }
        public int PriceForSelling { get; set; }
        public string Unit { get; set; }
        public string Note { get; set; }
        [NotMapped]
        public string CategoryName { get; set; }
        [NotMapped]
        public int Quantity { get; set; }
    }
}

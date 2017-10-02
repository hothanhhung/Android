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
        public int ProductId;
        public int CategoryId;
        public string ProductName;
        public int PriceForSelling;
        public string Unit;
        public string Note;
    }
}

using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Models
{
    public class Category
    {
        [Key]
        public int CategoryId { get; set; }
        public string CategoryName { get; set; }
        public string Note { get; set; }
        [NotMapped]
        public int NumberOfProducts { get; set; }
    }
}

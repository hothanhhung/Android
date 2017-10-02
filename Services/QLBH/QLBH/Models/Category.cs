using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Models
{
    public class Category
    {
        [Key]
        public int CategoryId;
        public string CategoryName;
        public string NOTE;
    }
}

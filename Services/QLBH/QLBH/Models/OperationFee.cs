using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Models
{
    public class OperationFee
    {
        [Key]
        public int OperationFeeId { get; set; }
        public string OperationFeeName { get; set; }
        public int Fee { get; set; }
        public string Note { get; set; }
        public string CreatedDate { get; set; }
    }
}

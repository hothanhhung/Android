using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RestauranceManage.Backend.Domain
{
    public class OrderDetail
    {
        public OrderDetail() { }

        [Key]
        public Guid ID { get; set; }

        public Guid OrderID { get; set; }

        public Guid MenuOrderID { get; set; }

        public decimal Quantity { get; set; }

        public decimal UnitPrice { get; set; }

        public decimal TotalPrice { get; set; }

        [ForeignKey("MenuOrderID")]
        public MenuOrder MenuOrder { get; set; }

        [ForeignKey("OrderID")]
        public Order Order { get; set; }

        [DefaultValue(true)]
        public bool IsActive { get; set; }

        [DefaultValue(false)]
        public bool IsUpdate { get; set; }
        public string UpdatedBy { get; set; }
        public string CreatedBy { get; set; }
        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; }
        public int Status { get; set; }
        public DateTime? CompletedDate { get; set; }
    }
}

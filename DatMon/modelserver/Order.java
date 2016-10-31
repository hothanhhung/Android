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
    public class Order
    {
        public Order()
        {

        }

        [Key]
        public Guid ID { get; set; }

        public Guid DeskID { get; set; }

        public string DeskName { get; set; }

        [ForeignKey("DeskID")]
        public Desk Desk { get; set; }

        public DateTime InputDate { get; set; }

        public Guid CustomerID { get; set; }

        [ForeignKey("CustomerID")]
        public Customer Customer { get; set; }

        [DefaultValue(true)]
        public bool IsActive { get; set; }
        [DefaultValue(false)]
        public bool IsUpdate { get; set; }

        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; }
        public int Status { get; set; }
        public string UpdatedBy { get; set; }
        public string CreatedBy { get; set; }
        public DateTime? CompletedDate { get; set; }
        public List<OrderDetail> OrderDetails { get; set; }
        public List<HistoryOrderDetail> HistoryOrderDetails { get; set; }
    }
}

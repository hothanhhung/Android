using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RestauranceManage.Common.Model
{
    public class OrderViewModel
    {
        public OrderViewModel()
        {
            OrderDetails = new List<OrderDetailViewModel>();
        }
        public Guid ID { get; set; }
        public DateTime CreatedDate { get; set; }
        public string CreatedBy { get; set; }
        public string EmployeeName { get; set; }
        public string AreaName { get; set; }
        public string DeskName { get; set; }
        public int Status { get; set; }
        public string CustomerName { get; set; }
        public decimal Total { get; set; }
        public string UpdatedBy { get; set; }
        public DateTime? CompletedTime { get; set; }
        public List<OrderDetailViewModel> OrderDetails { get; set; } 
        public DateTime? SlowestTime { get; set; }
        public bool IsDoneAll { get; set; }
    }

    public class OrderDetailViewModel {
        public OrderDetailViewModel()
        {
            HistoryOrderDetails = new List<HistoryOrderDetailViewMode>();
        }
        public Guid ID { get; set; }
        public DateTime CreatedDate { get; set; }
        public int Quanlity { get; set; }
        public string MenuOrderName { get; set; }
        public decimal UnitPrice { get; set; }
        public decimal SubTotal { get; set; }
        public int Status{get;set;}
        public string UpdatedBy { get; set; }
        public DateTime? CompletedTime { get; set; }
        public string CreatedBy { get; set; }
        public List<HistoryOrderDetailViewMode> HistoryOrderDetails { get; set; }
    }

    public class HistoryOrderDetailViewMode {
        public Guid ID { get; set; }
        public DateTime CreatedDate { get; set; }
        public string MenuOrderName { get; set; }
        public decimal UnitPrice { get; set; }
        public int Status { get; set; }
        public string UpdatedBy { get; set; }
        public string CreatedBy { get; set; }
        public DateTime? CompletedTime { get; set; }
    }

    public class OrderDetailMenu {
        public Guid OrderId { get; set; }
        public List<Guid> MenuOrderIds { get;set;}
    }
}

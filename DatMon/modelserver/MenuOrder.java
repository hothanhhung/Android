using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RestauranceManage.Backend.Domain
{
    public class MenuOrder
    {
        public MenuOrder()
        {

        }

        [Key]
        public Guid ID { get; set; }
        public string PathImage { get; set; }
        public string Name { get; set; }
        public decimal Cost { get; set; }
        public decimal Price { get; set; }

        //LỢI NHUẬN GỘP
        public decimal GrossProfit { get; set; }
        //% CHI PHÍ /GIÁ BÁN
        public decimal PercentageCostToPrice { get; set; }

       // % CHI PHÍ CỐ ĐỊNH
        public decimal FixedCostPercent { get; set; }

        //% LỢI NHUẬN RÒNG
        public decimal GrossProfitPercent { get; set; }

        // Thành tiền
        public decimal Amount { get; set; }

        [DefaultValue(true)]
        public bool IsActive { get; set; }

        [DefaultValue(false)]
        public bool IsUpdate { get; set; }

        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; }

    }
}

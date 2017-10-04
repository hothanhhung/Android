using System.ComponentModel.DataAnnotations;

namespace QLBH.Models
{
    public class Customer
    {
        [Key]
        public string PhoneNumber { get; set; }
        public string CustomerName { get; set; }
        public string Address { get; set; }
        public string DeliveryAddress { get; set; }
        public string Email { get; set; }
        public string Note { get; set; }
    }
}

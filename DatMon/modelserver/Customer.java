using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RestauranceManage.Backend.Domain
{
    public class Customer
    {
        public Customer() { }

        [Key]
        public Guid ID { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public DateTime? BithDay { get; set; }
        public byte[] Image { get; set; }
        public string Job { get; set; }
        public string Address { get; set; }
        public string PhoneNumber { get; set; }
        public string Email { get; set; }
        public string Facebook { get; set; }
        public string Zalo { get; set; }

        public string CarNumber { get; set; }

        [DefaultValue(true)]
        public bool IsActive { get; set; }

        [DefaultValue(false)]
        public bool IsUpdate { get; set; }

        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; } 
    }
   
}

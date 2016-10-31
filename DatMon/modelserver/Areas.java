using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace RestauranceManage.Backend.Domain
{
    public class Areas
    {
        public Areas(){}
        [Key]
        public Guid ID { get; set; }
        public string Name { get; set; }
        public byte[] Image { get; set; }
        [DefaultValue(true)]
        public bool IsActive { get; set; }

        [DefaultValue(false)]
        public bool IsUpdate { get; set; }

        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; }
    }
}

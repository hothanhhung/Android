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
    public class Desk
    {
        [Key]
        public Guid ID { get; set; }
        public string Name { get; set; }

        public Guid AreaId { get; set; }
        [ForeignKey("AreaId")]
        public Areas Areas { get; set; }

        [DefaultValue(true)]
        public bool IsActive { get; set; }

        [DefaultValue(false)]
        public bool IsUpdate { get; set; }

        public string UserId { get; set; }
        public DateTime CreatedDate { get; set; }
        public DateTime? UpdatedDate { get; set; }
    }
}

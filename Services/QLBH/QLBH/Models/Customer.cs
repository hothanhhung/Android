using System.ComponentModel.DataAnnotations;

namespace QLBH.Models
{
    public class Customer
    {
        [Key]
       public int PhoneNumber {get; set;}
       public int CustomerName {get; set;}
       public int Address {get; set;}
       public int Email {get; set;}
       public int Note {get; set;}
    }
}

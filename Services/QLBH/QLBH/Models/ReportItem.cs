﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Models
{
    public class ReportItem
    {
        public string Name { get; set; }
        public int InCome { get; set; }
        public int OutCome { get; set; }
        public int Fee { get; set; }
        public int FeeForShip { get; set; }
        public int Profit { get { return InCome - Fee - FeeForShip - OutCome; } }
        public string DateForReport { get; set; }
    }
}

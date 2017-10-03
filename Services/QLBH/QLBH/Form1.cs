﻿using QLBH.Views;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace QLBH
{
    public partial class Form1 : Form
    {
        ProductManagement productManagement = new ProductManagement();
        ReceiptsManager receiptsManager = new ReceiptsManager();

        public Form1()
        {
            InitializeComponent();
        }

        private void btProduct_Click(object sender, EventArgs e)
        {
            if (productManagement == null)
            {
                productManagement = new QLBH.Views.ProductManagement();
            }
            productManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(productManagement);
        }

        private void btReceipt_Click(object sender, EventArgs e)
        {
            if (receiptsManager == null)
            {
                receiptsManager = new QLBH.Views.ReceiptsManager();
            }
            receiptsManager.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(receiptsManager);
        }
    }
}

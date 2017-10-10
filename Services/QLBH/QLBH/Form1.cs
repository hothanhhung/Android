using QLBH.Views;
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
        ProductManagement productManagement = null;
        ReceiptsManager receiptsManager = null;
        CustomersManagement customersManagement = null;
        IssuesManagement issuesManagement = null;
        OperationFeesManagement operationFeesManagement = null;

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

        private void btCustomers_Click(object sender, EventArgs e)
        {
            if (customersManagement == null)
            {
                customersManagement = new QLBH.Views.CustomersManagement();
            }
            customersManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(customersManagement);
        }

        private void btOrder_Click(object sender, EventArgs e)
        {
            if (issuesManagement == null)
            {
                issuesManagement = new QLBH.Views.IssuesManagement();
            }
            issuesManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(issuesManagement);
            
        }

        private void btFeeToSell_Click(object sender, EventArgs e)
        {
            if (operationFeesManagement == null)
            {
                operationFeesManagement = new QLBH.Views.OperationFeesManagement();
            }
            operationFeesManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(operationFeesManagement);
        }
    }
}

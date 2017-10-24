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
        ReportsManagement reportsManagement = null;

        public Form1()
        {
            InitializeComponent();
        }

        private void btProduct_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (productManagement == null)
            {
                productManagement = new QLBH.Views.ProductManagement();
            }
            productManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(productManagement);
            Cursor = Cursors.Arrow;
        }

        private void btReceipt_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (receiptsManager == null)
            {
                receiptsManager = new QLBH.Views.ReceiptsManager();
            }
            else
            {
                receiptsManager.ReLoadData();
            }
            receiptsManager.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(receiptsManager);
            Cursor = Cursors.Arrow;
        }

        private void btCustomers_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (customersManagement == null)
            {
                customersManagement = new QLBH.Views.CustomersManagement();
            }
            customersManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(customersManagement);
            Cursor = Cursors.Arrow;
        }

        private void btOrder_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (issuesManagement == null)
            {
                issuesManagement = new QLBH.Views.IssuesManagement();
            }
            issuesManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(issuesManagement);
            Cursor = Cursors.Arrow;
            
        }

        private void btFeeToSell_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (operationFeesManagement == null)
            {
                operationFeesManagement = new QLBH.Views.OperationFeesManagement();
            }
            operationFeesManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(operationFeesManagement);
            Cursor = Cursors.Arrow;
        }

        private void btReport_Click(object sender, EventArgs e)
        {
            Cursor = Cursors.WaitCursor;
            if (reportsManagement == null)
            {
                reportsManagement = new QLBH.Views.ReportsManagement();
            }
            else
            {
                reportsManagement.ReloadData();
            }
            reportsManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(reportsManagement);
            Cursor = Cursors.Arrow;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            btOrder.Select();
            Cursor = Cursors.WaitCursor;
            if (issuesManagement == null)
            {
                issuesManagement = new QLBH.Views.IssuesManagement();
            }
            issuesManagement.Dock = DockStyle.Fill;
            pnlMain.Controls.Clear();
            pnlMain.Controls.Add(issuesManagement);
            Cursor = Cursors.Arrow;
        }
    }
}

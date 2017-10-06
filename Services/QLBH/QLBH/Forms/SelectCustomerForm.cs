using QLBH.Businesses;
using QLBH.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace QLBH.Forms
{
    public partial class SelectCustomerForm : Form
    {
        public Customer CurrentSelectedCustomer = null;
        public BindingSource CustomersBinding = new BindingSource();

        List<Customer> customers = null;

        public SelectCustomerForm()
        {
            InitializeComponent();
        }

        private void SelectCustomerForm_Load(object sender, EventArgs e)
        {
            CurrentSelectedCustomer = null;
            grdCustomers.DataSource = CustomersBinding;
            LoadCustomer(true);
        }

        private void LoadCustomer(bool isReload = false)
        {
            if (customers == null || isReload)
            {
                customers = CustomerProcesser.GetCustomers();
            }

            var query = customers.AsQueryable();
            if (!string.IsNullOrWhiteSpace(txtCustomerPhone.Text))
            {
                query = query.Where(p => p.PhoneNumber.Contains(txtCustomerPhone.Text.Trim()));
            }
            if (!string.IsNullOrWhiteSpace(txtCustomerName.Text))
            {
                query = query.Where(p => p.CustomerName.Contains(txtCustomerName.Text.Trim()));
            }
            CustomersBinding.DataSource = query.ToList();
            CustomersBinding.ResetBindings(true);
        }

        private void txtCustomerName_TextChanged(object sender, EventArgs e)
        {
            LoadCustomer(false);
        }

        private void txtCustomerPhone_TextChanged(object sender, EventArgs e)
        {
            LoadCustomer(false);
        }

        private void grdCustomers_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (grdCustomers.CurrentRow != null && grdCustomers.CurrentRow.DataBoundItem!=null)
            {
                CurrentSelectedCustomer = (Customer)grdCustomers.CurrentRow.DataBoundItem;
                this.DialogResult = DialogResult.OK;
                this.Close();
            }
        }
    }
}

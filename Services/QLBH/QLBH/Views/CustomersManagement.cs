﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using QLBH.Models;
using QLBH.Businesses;

namespace QLBH.Views
{
    public partial class CustomersManagement : UserControl
    {
        List<Customer> customers = null;
        public CustomersManagement()
        {
            InitializeComponent();
        }

        private void CustomersManagement_Load(object sender, EventArgs e)
        {
            LoadCustomer(true);
        }

        private void LoadCustomer(bool isReload = false)
        {
            if (customers == null || isReload)
            {
                customers = CustomerProcesser.GetCustomers();
            }

            var query = customers.AsQueryable();
            if (!string.IsNullOrWhiteSpace(txtPhoneForSearch.Text))
            {
                query = query.Where(p => p.PhoneNumber.Contains(txtPhoneForSearch.Text.Trim()));
            }
            if (!string.IsNullOrWhiteSpace(txtNameForSearch.Text))
            {
                query = query.Where(p => p.CustomerName.Contains(txtNameForSearch.Text.Trim()));
            }
            grdCustomers.DataSource = query.ToList();
        }

        private void btSaveCustomer_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtCustomerName.Text))
            {
                MessageBox.Show("Nhập tên khách hàng", "Lưu Khách Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtCustomerName.Focus();
            }
            else if (string.IsNullOrWhiteSpace(txtCustomerPhone.Text))
            {
                MessageBox.Show("Nhập số điện thoại khách hàng", "Lưu Khách Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtCustomerPhone.Focus();
            }
            else
            {
                Customer customer = new Customer()
                {
                    PhoneNumber = txtCustomerPhone.Text.Trim(),
                    CustomerName = txtCustomerName.Text.Trim(),
                    Address = txtCustomerAddress.Text.Trim(),
                    DeliveryAddress = txtDeliveryAddress.Text.Trim(),
                    Email = txtCustomerEmail.Text.Trim(),
                    Note = txtCustomerNote.Text.Trim()
                };

                if (CustomerProcesser.SaveCustomer(customer))
                {
                    txtCustomerPhone.Text = string.Empty;
                    txtCustomerName.Text = string.Empty;
                    txtCustomerAddress.Text = string.Empty;
                    txtDeliveryAddress.Text = string.Empty;
                    txtCustomerEmail.Text = string.Empty;
                    txtCustomerNote.Text = string.Empty;
                    LoadCustomer(true);
                }
                else
                {
                    MessageBox.Show("Có lỗi khi lưu khách hàng", "Lưu Khách Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }

        private void btClearAll_Click(object sender, EventArgs e)
        {
            txtCustomerPhone.Text = string.Empty;
            txtCustomerName.Text = string.Empty;
            txtCustomerAddress.Text = string.Empty;
            txtDeliveryAddress.Text = string.Empty;
            txtCustomerEmail.Text = string.Empty;
            txtCustomerNote.Text = string.Empty;
            txtNameForSearch.Text = string.Empty;
            txtPhoneForSearch.Text = string.Empty;
            
        }
        
        private void grdCustomers_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (customers != null && e.RowIndex > -1 && customers.Count >= e.RowIndex)
            {
                txtCustomerPhone.Text = customers[e.RowIndex].PhoneNumber;
                txtCustomerName.Text = customers[e.RowIndex].CustomerName;
                txtCustomerAddress.Text = customers[e.RowIndex].Address;
                txtDeliveryAddress.Text = customers[e.RowIndex].DeliveryAddress;
                txtCustomerEmail.Text = customers[e.RowIndex].DeliveryAddress;
                txtCustomerNote.Text = customers[e.RowIndex].Note;
            }
        }

        private void txtNameForSearch_TextChanged(object sender, EventArgs e)
        {
            LoadCustomer(false);
        }

        private void txtPhoneForSearch_TextChanged(object sender, EventArgs e)
        {
            LoadCustomer(false);
        }
    }
}
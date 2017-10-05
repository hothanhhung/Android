using System;
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
    public partial class IssuesManagement : UserControl
    {
        List<Product> Products = null;
        List<OrderDetail> OrderDetails = new List<OrderDetail>();
        public IssuesManagement()
        {
            InitializeComponent();
        }

        private void IssuesManagement_Load(object sender, EventArgs e)
        {
            loadProducts(true);

            var dataGridViewComboBoxColumn = (DataGridViewComboBoxColumn)grdOrderDetail.Columns["productIdDataGridViewTextBoxColumn"];
            dataGridViewComboBoxColumn.Items.Clear();
            dataGridViewComboBoxColumn.Items.AddRange(Products.ToArray());
            dataGridViewComboBoxColumn.DisplayMember = "ProductName";
            dataGridViewComboBoxColumn.ValueMember = "ProductId";
        }

        private void loadProducts(bool isReload = false)
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
            }

        }

        private void grdOrderDetail_EditingControlShowing(object sender, DataGridViewEditingControlShowingEventArgs e)
        {
            if (grdOrderDetail.CurrentCell.ColumnIndex == 0 && e.Control is ComboBox)
            {
                ComboBox comboBox = e.Control as ComboBox;                
                comboBox.SelectedIndexChanged -= LastColumnComboSelectionChanged;
                comboBox.SelectedIndexChanged += LastColumnComboSelectionChanged;
            }
        }

        private void LastColumnComboSelectionChanged(object sender, EventArgs e)
        {
            grdOrderDetail.CurrentRow.Cells["priceForUnitDataGridViewTextBoxColumn"].Value = 1;
            //var sendingCB = sender as DataGridViewComboBoxEditingControl;
            //if (sendingCB.SelectedItem != null)
            //{
            //    DataGridViewTextBoxCell cel = (DataGridViewTextBoxCell)grdOrderDetail.Rows[grdOrderDetail.CurrentCell.RowIndex].Cells["priceForUnitDataGridViewTextBoxColumn"];
            //    cel.Value = ((Product)(sendingCB.SelectedItem)).PriceForSelling;
            //}
        }
    }
}

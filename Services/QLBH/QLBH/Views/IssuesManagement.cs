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
using QLBH.Forms;
using QLBH.Commons;

namespace QLBH.Views
{
    public partial class IssuesManagement : UserControl
    {
        List<Product> Products = null;
        List<OrderDetail> OrderDetails = new List<OrderDetail>();
        Order CurrentOrder = new Order();
        public BindingSource OrderDetailsBinding = new BindingSource();

        public IssuesManagement()
        {
            InitializeComponent();
        }

        private void IssuesManagement_Load(object sender, EventArgs e)
        {
            loadProducts(true);
            grdOrderDetail.DataSource = OrderDetailsBinding;
            OrderDetailsBinding.DataSource = OrderDetails;
            OrderDetailsBinding.ResetBindings(true);
            ResetUIAddProduct();
        }

        private void loadcbbProducts(bool isReload = false)
        {
            loadProducts(isReload);
            cbbProducts.DataSource = Products.Where(p=> !OrderDetails.Any(o=> o.ProductId == p.ProductId)).ToList();
            cbbProducts.DisplayMember = "ProductName";
            cbbProducts.ValueMember = "ProductId";
            cbbProducts.Text = string.Empty;
            cbbProducts.SelectedIndex = -1;
        }

        private void ResetUIAddProduct()
        {
            loadcbbProducts(false);
            txtQuantity.Value = 0;
            lblPriceForSelling.Text = "0";
            lblTotalPriceForSelling.Text = "0";
            lblTotalPrice.Text = string.Format("{0:n0}",OrderDetails.Sum(o => o.OrderDetailTotalPrice));
            txtNote.Text = string.Empty;
        }

        private void loadProducts(bool isReload = false)
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
            }

        }
        
        private void btAddOrderDetail_Click(object sender, EventArgs e)
        {
            if (cbbProducts.SelectedItem == null)
            {
                MessageBox.Show("Chọn sản phẩm", "Tạo Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbProducts.Focus();
            }
            else if (txtQuantity.Value <= 0)
            {
                MessageBox.Show("Nhập số lượng", "Tạo Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbProducts.Focus();
            }
            else
            {
                var product = (Product)cbbProducts.SelectedItem;
                var orderDetail = OrderDetails.Where(o => o.ProductId == product.ProductId).FirstOrDefault();
                if (orderDetail == null)
                {
                    orderDetail = new OrderDetail()
                                        {
                                            ProductId = product.ProductId,
                                            ProductName = product.ProductName,
                                            Quantity = Decimal.ToInt32(txtQuantity.Value),
                                            PriceForUnit = product.PriceForSelling,
                                            Note = txtNote.Text.Trim()
                                        };
                    OrderDetails.Add(orderDetail);
                }
                else
                {
                    orderDetail.Quantity = orderDetail.Quantity + Decimal.ToInt32(txtQuantity.Value);
                    orderDetail.PriceForUnit = product.PriceForSelling;
                    orderDetail.Note = txtNote.Text.Trim();
                }
                //grdOrderDetail.DataSource = null;
               // grdOrderDetail.DataSource = OrderDetails;
                OrderDetailsBinding.ResetBindings(true);
                ResetUIAddProduct();
            }
        }

        private void cbbProducts_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (cbbProducts.SelectedItem != null)
            {
                var product = (Product)cbbProducts.SelectedItem;
                lblPriceForSelling.Text = string.Format("{0:n0}", product.PriceForSelling);
                lblTotalPriceForSelling.Text = string.Format("{0:n0}", product.PriceForSelling * txtQuantity.Value);
            }
        }

        private void txtQuantity_ValueChanged(object sender, EventArgs e)
        {
            if (cbbProducts.SelectedItem != null)
            {
                var product = (Product)cbbProducts.SelectedItem;
                lblTotalPriceForSelling.Text = string.Format("{0:n0}", product.PriceForSelling * txtQuantity.Value);
            }
        }

        private void btDeleteOrderDetail_Click(object sender, EventArgs e)
        {
            if (CurrentOrder != null && CurrentOrder.OrderId > 0)
            {
                if (grdOrderDetail.CurrentRow != null && grdOrderDetail.CurrentRow.DataBoundItem != null)
                {
                    var orderDetail = (OrderDetail)grdOrderDetail.CurrentRow.DataBoundItem;
                    if (MessageBox.Show("Bạn Muốn Xóa Sản Phẩm: " + orderDetail.ProductName + " ?", "Tạo Đơn Hàng", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                    {
                        var obj = OrderDetails.Where(o => o.ProductId == orderDetail.ProductId).FirstOrDefault();
                        if (obj != null)
                        {
                            OrderDetails.Remove(obj);
                            OrderDetailsBinding.ResetBindings(true);
                        }
                    }
                }
            }
            else
            {
                MessageBox.Show("Không Thể Xóa Sản Phẩm Vì Đơn Hàng Đang Xử Lý.", "Tạo Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void grdOrderDetail_SelectionChanged(object sender, EventArgs e)
        {
            if (grdOrderDetail.CurrentRow != null)
            {
                btDeleteOrderDetail.Enabled = true;
            }
            else
            {
                btDeleteOrderDetail.Enabled = false;
            }
        }

        private void linkGetCustomerInfo_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            SelectCustomerForm selectCustomerForm = new SelectCustomerForm();
            if (selectCustomerForm.ShowDialog(this) == DialogResult.OK && selectCustomerForm.CurrentSelectedCustomer != null)
            {
                if (string.IsNullOrWhiteSpace(selectCustomerForm.CurrentSelectedCustomer.DeliveryAddress))
                {
                    txtCustomerDeliveryAddress.Text = selectCustomerForm.CurrentSelectedCustomer.Address;
                }
                else {
                    txtCustomerDeliveryAddress.Text = selectCustomerForm.CurrentSelectedCustomer.DeliveryAddress;
                }
                txtCustomerName.Text = selectCustomerForm.CurrentSelectedCustomer.CustomerName;
                txtCustomerPhone.Text = selectCustomerForm.CurrentSelectedCustomer.PhoneNumber;
                //txtCustomerNote.Text = selectCustomerForm.CurrentSelectedCustomer.Note;
            }
        }

        private string ValidateOrders()
        {
            var products = ProductProcesser.GetProducts(OrderDetails.Select(o => o.ProductId).ToList(), MethodHelpers.ConvertDateToCorrectString(dtDateForSelling.Value.AddDays(1)), CurrentOrder.OrderId, true);
            StringBuilder message = new StringBuilder();
            foreach (var orderDetail in OrderDetails)
            {
                var product = products.Where(p=>p.ProductId == orderDetail.ProductId).FirstOrDefault();
                if(product == null){
                    message.AppendLine(orderDetail.ProductName + ": 0");
                }
                else if(orderDetail.Quantity > product.Quantity){
                    message.AppendLine(orderDetail.ProductName + ": " + product.Quantity);
                }
            }
            return message.ToString();
        }

        private void btSaveOrder_Click(object sender, EventArgs e)
        {
            string validateMessage = ValidateOrders();
            if(OrderDetails == null || OrderDetails.Count <= 0)
            {
                MessageBox.Show("Chưa Có Sản Phẩm", "Lưu Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbProducts.Focus();

            }else if (!string.IsNullOrWhiteSpace(validateMessage))
            {
                MessageBox.Show("Một Số Sản Phẩm Không Đủ Hàng. Vui Lòng Kiểm Tra Lại.\n" + validateMessage, "Lưu Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else if (string.IsNullOrWhiteSpace(txtCustomerName.Text))
            {
                MessageBox.Show("Nhập Tên Khách Hàng", "Lưu Đơn Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtCustomerName.Focus();
            }
            else
            {
                CurrentOrder.CustomerName = txtCustomerName.Text.Trim();
                CurrentOrder.Phone = txtCustomerPhone.Text.Trim();
                CurrentOrder.CreatedDate = MethodHelpers.ConvertDateTimeToCorrectString(dtDateForSelling.Value);
                CurrentOrder.Address = txtCustomerDeliveryAddress.Text.Trim();
                CurrentOrder.Note = txtCustomerNote.Text.Trim();
                CurrentOrder.FeeForShipping = Decimal.ToInt32(txtCustomerShipFee.Value);
                CurrentOrder.Status = 1;
                //Saving order 
                CurrentOrder = OrderProcesser.SaveOrder(CurrentOrder);
                if (CurrentOrder.OrderId > 0)
                {
                    for (int i = 0; i < OrderDetails.Count; i++)
                    {
                        OrderDetails[i].Lock = 0;
                        OrderDetails[i].OrderId = CurrentOrder.OrderId;
                        OrderDetails[i] = OrderDetailProcesser.SaveOrderDetail(OrderDetails[i]);
                    }
                }
                ///Saving Customer Info
                if (cbUpdateCustomerInfo.Checked)
                {
                    var customer = new Customer()
                    {
                        CustomerName = CurrentOrder.CustomerName,
                        DeliveryAddress = CurrentOrder.Address,
                        PhoneNumber = CurrentOrder.Phone,
                    };
                    CustomerProcesser.SaveCustomer(customer, true);
                }
            }
        }

        private void grdOrderDetail_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            lblTotalPrice.Text = string.Format("{0:n0}", OrderDetails.Sum(o => o.OrderDetailTotalPrice));
        }
    }
}

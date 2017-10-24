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
using QLBH.Commons;

namespace QLBH.Views
{
    public partial class ReceiptsManager : UserControl
    {
        List<Product> Products = null;
        Receipt CurrentReceipt = null;

        public ReceiptsManager()
        {
            InitializeComponent();
        }

        public void ReLoadData()
        {
            loadProducts(true);
        }

        private void LoadReceiptUI(Receipt receipt)
        {
            CurrentReceipt = receipt;
            if (CurrentReceipt == null)
            {
                CurrentReceipt = new Receipt();
                cbbProductForReceipt.SelectedIndex = -1;
                txtQuantity.Value = 0;
                txtQuantity.Value = 0;
                txtUnitPrice.Value = 0;
                txtNote.Text = string.Empty;
                dtReceiptedDate.Value = DateTime.Now;
                btReceipt.Text = "Nhập Hàng";
                btCancelReceipt.Enabled = false;
            }
            else
            {
                foreach(var item in cbbProductForReceipt.Items)
                {
                    if (((Product)item).ProductId == CurrentReceipt.ProductId)
                    {
                        cbbProductForReceipt.SelectedItem = item;
                    }
                }
                txtQuantity.Value = CurrentReceipt.Quantity;
                txtUnitPrice.Value = CurrentReceipt.PriceOfAllForReceipting;
                txtNote.Text = CurrentReceipt.Note;
                dtReceiptedDate.Value = MethodHelpers.ConvertStringDateTimeToDateTime(CurrentReceipt.DatedReceipt);
                btReceipt.Text = "Lưu";
                btCancelReceipt.Enabled = true;
            }
        }

        private void LoadReceipts()
        {
            int productId = 0;
            string from = MethodHelpers.ConvertDateToCorrectString(dtMinForView.Value),
                to =  MethodHelpers.ConvertDateToCorrectString(dtMaxForView.Value.AddDays(1));

            if(cbbProductsForView.SelectedItem!=null)
            {
                productId = ((Product)cbbProductsForView.SelectedItem).ProductId;
            }

            var receipts = ReceiptProcesser.GetReceipts(productId, from, to, cbGroupProduct.Checked);

            if (Products != null)
            {
                foreach (var receipt in receipts)
                {
                    var product = Products.Where(p => p.ProductId == receipt.ProductId).FirstOrDefault();
                    if (product != null)
                    {
                        receipt.ProductName = product.ProductName;
                    }
                }
            }
            grdReceipts.DataSource = new SortableBindingList<Receipt>(receipts);
            if (receipts != null)
            {
                txtTotalPriceForAll.Value = receipts.Sum(r => r.PriceOfAllForReceipting * r.Quantity);
            }
            else
            {
                txtTotalPriceForAll.Value = 0;
            }
        }

        private void loadProducts(bool isReload = true)
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
            }

            cbbProductForReceipt.DataSource = Products;
            cbbProductForReceipt.DisplayMember = "ProductId";
            cbbProductForReceipt.DisplayMember = "ProductName";

            cbbProductsForView.Items.Clear();
            cbbProductsForView.Items.Add(new Product() { ProductId = 0, ProductName = "--Tất Cả--" });
            foreach (var item in Products)
            {
                cbbProductsForView.Items.Add(item);
            }

            cbbProductsForView.DisplayMember = "ProductId";
            cbbProductsForView.DisplayMember = "ProductName";

        }

        private void ReceiptsManager_Load(object sender, EventArgs e)
        {
            cbbQuickView.SelectedIndex = 2;
            UpdateView();
            loadProducts();
            LoadReceipts();
            LoadReceiptUI(null);
        }

        private void UpdateView()
        {
            DateTime now = DateTime.Now;
            switch(cbbQuickView.SelectedIndex + 1)
            {
                case 1:
                    dtMinForView.Value = now;
                    dtMaxForView.Value = now;
                    break;
                case 2:
                    dtMinForView.Value = now.AddDays(-1);
                    dtMaxForView.Value = now.AddDays(-1);
                    break;
                case 3:
                    dtMinForView.Value = now.AddDays(-7);
                    dtMaxForView.Value = now;
                    break;
                case 4:
                    dtMinForView.Value = now.AddDays(-(int)now.DayOfWeek);
                    dtMaxForView.Value = now;
                    break;
                case 5:
                    dtMinForView.Value = now.AddDays(-(int)now.DayOfWeek - 6);
                    dtMaxForView.Value = dtMinForView.Value.AddDays(6);
                    break;
                case 6:
                    dtMinForView.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxForView.Value = now;
                    break;
                case 7:
                    dtMinForView.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxForView.Value = (new DateTime(now.Year, now.Month + 1, 1)).AddDays(-1);
                    break;
                case 8:
                    dtMinForView.Value = new DateTime(now.Year, 1, 1);
                    dtMaxForView.Value = now;
                    break;
                case 9:
                    dtMinForView.Value = new DateTime(now.Year - 1, 1, 1);
                    dtMaxForView.Value = new DateTime(now.Year - 1, 12, 31);
                    break;
            }
        }

        private void cbbQuickView_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateView();
        }

        private void btReceipt_Click(object sender, EventArgs e)
        {
            int offsetQuantity = Decimal.ToInt32(txtQuantity.Value) - CurrentReceipt.Quantity;
            if (offsetQuantity + CurrentReceipt.RemainAfterDone<0)
            {
                MessageBox.Show("Số Lượng Sản Phẩm Giảm Không Được Phép", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            if (offsetQuantity < 0)
            {
                if (MessageBox.Show("Số Lượng Sản Phẩm Đang Giảm. Điều Này Có Thể Làm Một Số Đơn Hàng Thiếu Hàng.\nBạn Muốn Tiếp Tục", "Nhập Hàng", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.No)
                {
                    return;
                }
            }
            if (cbbProductForReceipt.SelectedItem == null || ((Product)cbbProductForReceipt.SelectedItem).ProductId <= 0)
            {
                MessageBox.Show("Chọn sản phẩm", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbProductForReceipt.Focus();
            }
            else if (txtQuantity.Value < 0)
            {
                MessageBox.Show("Nhập số lượng", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtQuantity.Focus();
            }
            else if (txtUnitPrice.Value < 0)
            {
                MessageBox.Show("Nhập giá nhập hàng", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtUnitPrice.Focus();
            }
            else
            {
                CurrentReceipt.ProductId = ((Product)cbbProductForReceipt.SelectedItem).ProductId;
                CurrentReceipt.Quantity = Decimal.ToInt32(txtQuantity.Value);
                CurrentReceipt.RemainAfterDone = CurrentReceipt.RemainAfterDone + offsetQuantity;
                CurrentReceipt.PriceOfAllForReceipting = Decimal.ToInt32(txtUnitPrice.Value);
                CurrentReceipt.Note = txtNote.Text;
                CurrentReceipt.DatedReceipt = MethodHelpers.ConvertDateTimeToCorrectString(dtReceiptedDate.Value);

                if (ReceiptProcesser.SaveReceipt(CurrentReceipt))
                {
                    LoadReceipts();
                    LoadReceiptUI(null);
                }
                else
                {
                    MessageBox.Show("Có lỗi khi nhập hàng", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }

        private void btViewReceipt_Click(object sender, EventArgs e)
        {
            LoadReceipts();
        }

        private void btDeleteReceipt_Click(object sender, EventArgs e)
        {
            if (grdReceipts.CurrentRow != null && grdReceipts.CurrentRow.DataBoundItem != null)
            {
                var receipt = (Receipt)grdReceipts.CurrentRow.DataBoundItem;
                if(receipt.Quantity > receipt.RemainAfterDone)
                {
                    MessageBox.Show("Hàng Đã Xuất, Không Thể Xóa", "Xóa Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }
                else if (MessageBox.Show("Bạn Đang Xóa #" + receipt.ReceiptId + ". Điều Này Có Thể Làm Một Số Đơn Hàng Thiếu Hàng.\nBạn Muốn Tiếp Tục", "Xóa Hàng", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                {
                    if (ReceiptProcesser.DeleteReceipt(receipt.ReceiptId))
                    {
                        LoadReceipts();
                    }
                    else
                    {
                        MessageBox.Show("Có lỗi khi xóa hàng", "Xóa Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
            }
        }

        private void grdReceipts_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (grdReceipts.CurrentRow != null && grdReceipts.CurrentRow.DataBoundItem != null)
            {
                LoadReceiptUI((Receipt)grdReceipts.CurrentRow.DataBoundItem);
            }
        }

        private void btCancelReceipt_Click(object sender, EventArgs e)
        {
            LoadReceiptUI(null);
        }

        private void txtUnitPrice_ValueChanged(object sender, EventArgs e)
        {
            txtTotalPrice.Value = txtUnitPrice.Value * txtQuantity.Value;
        }

        private void txtQuantity_ValueChanged(object sender, EventArgs e)
        {
            txtTotalPrice.Value = txtUnitPrice.Value * txtQuantity.Value;
        }
    }
}

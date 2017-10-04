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

        public ReceiptsManager()
        {
            InitializeComponent();
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
            grdReceipts.DataSource = receipts;
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
            Dictionary<string, string> quickView = new Dictionary<string, string>();
            quickView.Add("1", "Hôm Nay");
            quickView.Add("2", "Hôm Qua");
            quickView.Add("3", "Tuần Này");
            quickView.Add("4", "Tuần Trước");
            quickView.Add("5", "Tháng Này");
            quickView.Add("6", "Tháng Trước");
            quickView.Add("7", "Năm Này");
            quickView.Add("8", "Năm Trước");
            cbbQuickView.DataSource = new BindingSource(quickView, null);
            cbbQuickView.DisplayMember = "Value";
            cbbQuickView.ValueMember = "Key";
            cbbQuickView.SelectedIndex = 0;
            UpdateView();
            loadProducts();
            LoadReceipts();
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
                    dtMinForView.Value = now.AddDays(-(int)now.DayOfWeek);
                    dtMaxForView.Value = now;
                    break;
                case 4:
                    dtMinForView.Value = now.AddDays(-(int)now.DayOfWeek - 6);
                    dtMaxForView.Value = dtMinForView.Value.AddDays(6);
                    break;
                case 5:
                    dtMinForView.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxForView.Value = now;
                    break;
                case 6:
                    dtMinForView.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxForView.Value = (new DateTime(now.Year, now.Month + 1, 1)).AddDays(-1);
                    break;
                case 7:
                    dtMinForView.Value = new DateTime(now.Year, 1, 1);
                    dtMaxForView.Value = now;
                    break;
                case 8:
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
            if (cbbProductForReceipt.SelectedItem == null && ((Product)cbbProductForReceipt.SelectedItem).ProductId <= 0)
            {
                MessageBox.Show("Chọn sản phẩm", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbProductForReceipt.Focus();
            }
            else if (txtQuantity.Value < 0)
            {
                MessageBox.Show("Nhập số lượng", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtQuantity.Focus();
            }
            else if (txtTotalPrice.Value < 0)
            {
                MessageBox.Show("Nhập giá nhập hàng", "Nhập Hàng", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtTotalPrice.Focus();
            }
            else
            {
                var receipt = new Receipt()
                {
                    ProductId = ((Product)cbbProductForReceipt.SelectedItem).ProductId,
                    Quantity = Decimal.ToInt32(txtQuantity.Value),
                    PriceOfAllForReceipting = Decimal.ToInt32(txtTotalPrice.Value),
                    Note = txtNote.Text,
                    IsSellAll = 0,
                    DatedReceipt = MethodHelpers.ConvertDateTimeToCorrectString(dtReceiptedDate.Value)
                };
                if (ReceiptProcesser.SaveReceipt(receipt))
                {
                    cbbProductForReceipt.SelectedIndex = -1;
                    txtQuantity.Value = 0;
                    txtTotalPrice.Value = 0;
                    txtNote.Text = string.Empty;
                    LoadReceipts();
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
    }
}

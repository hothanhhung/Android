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
    public partial class ReceiptsManager : UserControl
    {
        List<Product> Products = null;

        public ReceiptsManager()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {

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
                    dtMinForView.Value = now;
                    dtMaxForView.Value = now;
                    break;
                case 4: break;
                case 5: break;
                case 6: break;
                case 7: break;
                case 8: break;
            }
        }

        private void cbbQuickView_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateView();
        }
    }
}

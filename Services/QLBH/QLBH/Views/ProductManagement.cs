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
    public partial class ProductManagement : UserControl
    {
        List<Category> Categories = null;
        List<Product> Products = null;
        Product _currentProduct = null;
        Product CurrentProduct{
            get
            {
                if (_currentProduct == null)
                {
                    _currentProduct = new Product();
                }
                return _currentProduct;
            }
            set
            {
                _currentProduct = value;
                if (_currentProduct == null)
                {
                    txtProductName.Text = string.Empty;
                    txtPriceForSelling.Value = 0;
                    txtProductUnit.Text = string.Empty;
                    txtProductNote.Text = string.Empty;
                    cbbCategories.SelectedIndex = -1;
                }
                else
                {
                    cbbCategories.SelectedIndex = -1;
                    txtProductName.Text = _currentProduct.ProductName;
                    txtPriceForSelling.Value = _currentProduct.PriceForSelling;
                    txtProductUnit.Text = _currentProduct.Unit;
                    txtProductNote.Text = _currentProduct.Note;
                    foreach (var item in cbbCategories.Items)
                    {
                        if(((Category)item).CategoryId == _currentProduct.CategoryId)
                        {
                            cbbCategories.SelectedItem = item;
                            break;
                        }
                    }
                }
            }
        }
        Category _currentCategory;
        Category CurrentCategory
        {
            get {
                if (_currentCategory == null)
                {
                    _currentCategory = new Category();
                }
                return _currentCategory;
            }
            set
            {
                _currentCategory = value;
                if (_currentCategory == null)
                {
                    txtCategoryName.Text = string.Empty;
                    txtCategoryNote.Text = string.Empty;
                }
                else
                {
                    txtCategoryName.Text = _currentCategory.CategoryName;
                    txtCategoryNote.Text = _currentCategory.Note;
                }
            }
        }

        public ProductManagement()
        {
            InitializeComponent();
        }

        private void ProductManagement_Load(object sender, EventArgs e)
        {
            if (tabProductManager.SelectedIndex == 0)
            {
                loadProducts();
                loadCbbCategories(true);
            }
            else if (tabProductManager.SelectedIndex == 1)
            {
                loadCategories();
            }
        }

        private void tabProductManager_Selecting(object sender, TabControlCancelEventArgs e)
        {
            if(tabProductManager.SelectedIndex == 0)
            {
                loadProducts();
                loadCbbCategories(true);
            }
            else if (tabProductManager.SelectedIndex == 1)
            {
                loadCategories();
            }
        }

        private void btSaveCategory_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtCategoryName.Text))
            {
                MessageBox.Show("Tên Danh Mục chưa được nhập", "Lưu Danh Mục", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                CurrentCategory.CategoryName = txtCategoryName.Text;
                CurrentCategory.Note = txtCategoryNote.Text;
                CategoryProcesser.SaveCategory(CurrentCategory);
                loadCategories();
            }
        }

        private void loadCbbCategories(bool isReload = true)
        {
            if (Categories == null || isReload)
            {
                Categories = CategoryProcesser.GetCategories();
            }
            cbbCategories.Items.Clear();
            cbbCategories.Items.Add(new Category() { CategoryId = -1, CategoryName = "Tạo mới Danh Mục" });
            foreach(var item in Categories){
                cbbCategories.Items.Add(item);
            }
            cbbCategories.DisplayMember = "CategoryId";
            cbbCategories.DisplayMember = "CategoryName";
            cbbCategories.SelectedIndex = -1;
            if (_currentProduct != null && _currentProduct.CategoryId > 0)
            {
                foreach (var item in cbbCategories.Items)
                {
                    if (((Category)item).CategoryId == _currentProduct.CategoryId)
                    {
                        cbbCategories.SelectedItem = item;
                        break;
                    }
                }
            }
        }

        private void loadCategories()
        {
            Categories = CategoryProcesser.GetCategories();
            grvCategories.DataSource = Categories;

        }

        private void loadProducts(bool isReload = true, string filter = "")
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
                if (Categories != null)
                {
                    foreach (var product in Products)
                    {
                        var category = Categories.Where(c => c.CategoryId == product.CategoryId).FirstOrDefault();
                        if (category != null)
                        {
                            product.CategoryName = category.CategoryName;
                        }
                    }
                }
            }
            if (string.IsNullOrWhiteSpace(filter))
            {
                grdProducts.DataSource = Products; }
            else
            {
                filter = filter.Trim().ToLower();
                grdProducts.DataSource = Products.Where(p => MethodHelpers.RemoveSign4VietnameseString(p.ProductName).ToLower().Contains(filter)).ToList();
           
            }

        }
        private void btAddCategory_Click(object sender, EventArgs e)
        {
            CurrentCategory = null;
        }

        private void grvCategories_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (Categories != null && e.RowIndex > -1 && Categories.Count >= e.RowIndex)
            {
                CurrentCategory = Categories[e.RowIndex];
            }
        }

        private void btAddProduct_Click(object sender, EventArgs e)
        {
            CurrentProduct = null;
        }

        private void btSaveProduct_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtProductName.Text))
            {
                MessageBox.Show("Tên Sản Phẩm chưa được nhập", "Lưu Sản Phẩm", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtProductName.Focus();
            }
            else if (txtPriceForSelling.Value<0)
            {
                MessageBox.Show("Giá bán chưa được nhập", "Lưu Sản Phẩm", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtPriceForSelling.Focus();
            }
            else if (string.IsNullOrWhiteSpace(cbbCategories.Text) || cbbCategories.SelectedItem == null)
            {
                MessageBox.Show("Danh Mục chưa được nhập", "Lưu Sản Phẩm", MessageBoxButtons.OK, MessageBoxIcon.Error);
                cbbCategories.Focus();
            }
            else
            {
                CurrentProduct.ProductName = txtProductName.Text;
                CurrentProduct.PriceForSelling = Decimal.ToInt32(txtPriceForSelling.Value);
                CurrentProduct.CategoryId = ((Category)cbbCategories.SelectedItem).CategoryId;
                CurrentProduct.Unit = txtProductUnit.Text;
                CurrentProduct.Note = txtProductNote.Text;
                ProductProcesser.SaveProduct(CurrentProduct);
                loadProducts();
            }
        }

        private void txtProductPriceForSelling_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = !char.IsDigit(e.KeyChar) && !char.IsControl(e.KeyChar);
        }

        private void grdProducts_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (Products != null && e.RowIndex >-1 && Products.Count >= e.RowIndex)
            {
                CurrentProduct = Products[e.RowIndex];
            }
        }

        private void cbbCategories_SelectedIndexChanged(object sender, EventArgs e)
        {
            if(cbbCategories.SelectedItem != null)
            {
                if(((Category) cbbCategories.SelectedItem).CategoryId == -1)
                {
                    cbbCategories.SelectedIndex = -1;
                    tabProductManager.SelectedIndex = 1;
                }
            }
        }

        private void txtSmartSearchProduct_TextChanged(object sender, EventArgs e)
        {
            loadProducts(false, txtSmartSearchProduct.Text);
        }
        
    }
}

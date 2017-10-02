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
    public partial class ProductManagement : UserControl
    {
        Product CurrentProduct = null;
        Category CurrentCategory = null;

        public ProductManagement()
        {
            InitializeComponent();
        }

        private void tabProductManager_Selecting(object sender, TabControlCancelEventArgs e)
        {
            if(tabProductManager.SelectedIndex == 0)
            {

            }
            else if (tabProductManager.SelectedIndex == 1)
            {
                loadCategories();
            }
        }

        private void btSaveCategory_Click(object sender, EventArgs e)
        {
            if (CurrentCategory == null)
            {
                CurrentCategory = new Category();
            }
            CurrentCategory.CategoryName = txtCategoryName.Text;
            CurrentCategory.NOTE = txtCategoryNote.Text;
            CategoryProcesser.SaveCategory(CurrentCategory);
            loadCategories();
        }

        private void loadCategories()
        {
            grvCategories.DataSource = CategoryProcesser.GetCategories();

        }
    }
}

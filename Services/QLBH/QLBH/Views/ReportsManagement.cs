using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using QLBH.Commons;
using QLBH.Businesses;
using QLBH.Models;
using System.Windows.Forms.DataVisualization.Charting;

namespace QLBH.Views
{
    public partial class ReportsManagement : UserControl
    {
        List<Product> Products = null;
        List<Category> Categories = null;

        public ReportsManagement()
        {
            InitializeComponent();
        }

        public void ReloadData()
        {
            loadcbbProducts(true);
            cbbGroupOn.SelectedIndex = 0;
        }

        private void ReportsManagement_Load(object sender, EventArgs e)
        {
            cbbSelectKindForProducts.SelectedIndex = 0;
            cbbSelectKindsForReceipts.SelectedIndex = 0;
            ReloadData();
            cbbQuickSelection.SelectedIndex = 2;
            UpdateView();
            cbbGroupOn.SelectedIndex = 0;
            cbbGroupProducts.SelectedIndex = 0;
            cbbGroupOnReceipts.SelectedIndex = 0;
            cbbGroupOnCustomer.SelectedIndex = 0;
            ViewReportOnGridView();
            cbbQuickSelectionProducts.SelectedIndex = 2;
            cbbQuickSelectionCustomer.SelectedIndex = 2;
            UpdateViewReportCustomer();
            cbbQuickSelectionReceipts.SelectedIndex = 2;
            UpdateViewReportReceipt();
        }
        private void cbbQuickSelection_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateView();
        }

        private void UpdateView()
        {
            MethodHelpers.UpdateDateFieldsBasedOnQuickSelection(dtMinDate, dtMaxDate, cbbQuickSelection.SelectedIndex);
        }

        private void UpdateViewReportProducts()
        {
            MethodHelpers.UpdateDateFieldsBasedOnQuickSelection(dtMinDateProducts, dtMaxDateProducts, cbbQuickSelectionProducts.SelectedIndex);
            
        }
        private void UpdateViewReportCustomer()
        {
            MethodHelpers.UpdateDateFieldsBasedOnQuickSelection(dtMinDateCustomer, dtMaxDateCustomer, cbbQuickSelectionCustomer.SelectedIndex);

        }
        private void UpdateViewReportReceipt()
        {
            MethodHelpers.UpdateDateFieldsBasedOnQuickSelection(dtMinDateReceipts, dtMaxDateReceipts, cbbQuickSelectionReceipts.SelectedIndex);

        }
        private void loadProducts(bool isReload = false)
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
            }

        }
        private void loadCategories(bool isReload = false)
        {
            if (Products == null || isReload)
            {
                Categories = CategoryProcesser.GetCategories();
            }

        }
        private void loadcbbProducts(bool isReload = false)
        {
            loadProducts(isReload);
            loadCategories(isReload);
            if(cbbSelectKindForProducts.SelectedIndex == 1)
            {
                clbProducts.Items.Clear();
                foreach (var category in Categories)
                {
                    clbProducts.Items.Add(category, true);
                }
                clbProducts.DisplayMember = "CategoryName";
                clbProducts.ValueMember = "CategoryId";
            }
            else
            {
                clbProducts.Items.Clear();
                foreach (var product in Products)
                {
                    clbProducts.Items.Add(product, true);
                }
                clbProducts.DisplayMember = "ProductName";
                clbProducts.ValueMember = "ProductId";
            }

            if (cbbSelectKindsForReceipts.SelectedIndex == 1)
            {
                clbReceiptsProducts.Items.Clear();
                foreach (var category in Categories)
                {
                    clbReceiptsProducts.Items.Add(category, true);
                }
                clbReceiptsProducts.DisplayMember = "CategoryName";
                clbReceiptsProducts.ValueMember = "CategoryId";
            }
            else
            {
                clbReceiptsProducts.Items.Clear();
                foreach (var product in Products)
                {
                    clbReceiptsProducts.Items.Add(product, true);
                }
                clbReceiptsProducts.DisplayMember = "ProductName";
                clbReceiptsProducts.ValueMember = "ProductId";

            }
        }


        private void btViewReport_Click(object sender, EventArgs e)
        {
            ViewReportOnGridView();
        }

        private void ViewReportOnGridView()
        {
            string from = MethodHelpers.ConvertDateToCorrectString(dtMinDate.Value);
            string to = MethodHelpers.ConvertDateToCorrectString(dtMaxDate.Value.AddDays(1));
            var reports = ReportsProcesser.GenerateReportOfProfit(null, from, to, cbbGroupOn.SelectedIndex, cbIncludeFeeOperator.Checked);
            var reportItemInTotal = new ReportItem()
            {
                DateForReport = string.Empty,
                Name = "TỔNG KẾT",
                InCome = reports.Sum(r => r.InCome),
                OutCome = reports.Sum(r => r.OutCome),
                FeeForShip = reports.Sum(r => r.FeeForShip),
                Fee = reports.Sum(r => r.Fee),
            };
            reports.Insert(0, reportItemInTotal);
            grdReports.DataSource = new SortableBindingList<ReportItem>(reports, true);

            reports = reports.GroupBy(r => r.DateForReport).Select(gp => new ReportItem()
            {
                Name = "Bán Hàng",
                DateForReport = gp.Key,
                Fee = gp.Sum(g => g.Fee),
                FeeForShip = gp.Sum(g => g.FeeForShip),
                InCome = gp.Sum(g => g.InCome),
                OutCome = gp.Sum(g => g.OutCome)
            }).ToList();

            foreach (var series in chartProfit.Series)
            {
                series.Points.Clear();
            }

            chartProfit.ChartAreas[0].AxisY.LabelStyle.Format = "N0";

            foreach(var report in reports)
            {
                string name = string.IsNullOrWhiteSpace(report.DateForReport) ? "Tổng Kết" : report.DateForReport;
                chartProfit.Series["SeriesInCome"].Points.AddXY(name, report.InCome);
                chartProfit.Series["SeriesOutCome"].Points.AddXY(name, report.OutCome);
                chartProfit.Series["SeriesFee"].Points.AddXY(name, report.Fee);
                chartProfit.Series["SeriesProfit"].Points.AddXY(name, report.Profit);
            }
        }

        private void ViewReportForProductOnGridView()
        {
            if (clbProducts.CheckedItems == null || clbProducts.CheckedItems.Count == 0)
            {
                MessageBox.Show("Vui lòng chọn ít nhất một sản phẩm", "Tạo Báo Cáo", MessageBoxButtons.OK, MessageBoxIcon.Error);
                clbProducts.Focus();
                return;
            }
            List<Product> selectedProducts = new List<Product>();
            List<int> productIds = new List<int>();

            foreach (var item in clbProducts.CheckedItems)
            {
                if (item is Product)
                {
                    var product = (Product)item;
                    if (product.ProductId > 0)
                    {
                        productIds.Add(product.ProductId);
                        selectedProducts.Add(product);
                    }
                }
                else
                {
                    var category = (Category)item;
                    if (category.CategoryId > 0)
                    {
                        var productlist = Products.Where(p=>p.CategoryId == category.CategoryId).ToList();
                        if(productlist!=null && productlist.Count > 0){
                            productIds.AddRange(productlist.Select(p => p.ProductId).ToArray());
                            selectedProducts.AddRange(productlist);
                        }
                    }
                }
            }
            selectedProducts = selectedProducts.Distinct().ToList();
            productIds = productIds.Distinct().ToList();

            string from = MethodHelpers.ConvertDateToCorrectString(dtMinDateProducts.Value);
            string to = MethodHelpers.ConvertDateToCorrectString(dtMaxDateProducts.Value.AddDays(1));
            var reports = ReportsProcesser.GenerateReportOfProfitOnProducts(productIds, from, to, cbbGroupProducts.SelectedIndex);

            var reportItem = new ReportItem()
            {
                ProductId = 0,
                Name = "Tổng Kết",
            };

            loadProducts(false);
            foreach(var report in reports)
            {
                var product = Products.FirstOrDefault(p=>p.ProductId == report.ProductId);
                if(product!= null)
                {
                    report.Name = product.ProductName;
                }

                reportItem.OutCome += report.OutCome;
                reportItem.InCome += report.InCome;
            }


            var reportsForChart = reports.GroupBy(r => r.ProductId).Select(gp => new ReportItem()
            {
                ProductId = gp.Key,
                Name = gp.First().Name,
                DateForReport = gp.First().DateForReport,
                Fee = gp.Sum(g => g.Fee),
                FeeForShip = gp.Sum(g => g.FeeForShip),
                InCome = gp.Sum(g => g.InCome),
                OutCome = gp.Sum(g => g.OutCome)
            }).ToList();

            chartForProducts.Series.Clear();
            foreach (var product in selectedProducts)
            {
                Series series = new Series(product.ProductName);
                series.IsValueShownAsLabel = true;
                series.LabelAngle = -90;
                series.LabelFormat = "N0";
                chartForProducts.Series.Add(series);
            }

            chartForProducts.ChartAreas[0].AxisY.LabelStyle.Format = "N0";

            foreach (var report in reportsForChart)
            {
                if (selectedProducts.Any(p => p.ProductName == report.Name))
                {
                    chartForProducts.Series[report.Name].Points.AddXY("Tiền Bán Hàng", report.InCome);
                    chartForProducts.Series[report.Name].Points.AddXY("Tiền Nhập Hàng", report.OutCome);
                    chartForProducts.Series[report.Name].Points.AddXY("Lợi Nhận", report.Profit);
                    chartForProducts.Series[report.Name].ToolTip = report.Name;
                }
            }



            reports.Insert(0, reportItem);
            grdReportForProducts.DataSource = new SortableBindingList<ReportItem>(reports, true);

            
        }

        private void ViewReportOnGridViewCustomer()
        {
           // List<Customer> customers = CustomerProcesser.GetCustomers();
            string from = MethodHelpers.ConvertDateToCorrectString(dtMinDateCustomer.Value);
            string to = MethodHelpers.ConvertDateToCorrectString(dtMaxDateCustomer.Value.AddDays(1));
            var reports = ReportsProcesser.GenerateReportOfProfit(null, from, to, cbbGroupOnCustomer.SelectedIndex, false, txtCustomerPhone.Text.Trim());

            reports = reports.GroupBy(r => new { Date = r.DateForReport, Phone = r.PhoneNumber }).Select(gp => new ReportItem()
            {
                Name = "Bán Hàng",
                CustomerName = gp.Last().CustomerName,
                PhoneNumber = gp.Key.Phone,
                DateForReport = gp.Key.Date,
                FeeForShip = gp.Sum(g => g.FeeForShip),
                InCome = gp.Sum(g => g.InCome),
                OutCome = gp.Sum(g => g.OutCome)
            }).ToList();

            grdReportCustomers.DataSource = new SortableBindingList<ReportItem>(reports);
        }

        private void ViewReportOnGridViewReceipts()
        {
            if (clbReceiptsProducts.CheckedItems == null || clbReceiptsProducts.CheckedItems.Count == 0)
            {
                MessageBox.Show("Vui lòng chọn ít nhất một sản phẩm", "Tạo Báo Cáo", MessageBoxButtons.OK, MessageBoxIcon.Error);
                clbProducts.Focus();
                return;
            }
            List<Product> selectedProducts = new List<Product>();
            List<int> productIds = new List<int>();

            foreach (var item in clbProducts.CheckedItems)
            {
                if (item is Product)
                {
                    var product = (Product)item;
                    if (product.ProductId > 0)
                    {
                        productIds.Add(product.ProductId);
                        selectedProducts.Add(product);
                    }
                }
                else
                {
                    var category = (Category)item;
                    if (category.CategoryId > 0)
                    {
                        var productlist = Products.Where(p => p.CategoryId == category.CategoryId).ToList();
                        if (productlist != null && productlist.Count > 0)
                        {
                            productIds.AddRange(productlist.Select(p => p.ProductId).ToArray());
                            selectedProducts.AddRange(productlist);
                        }
                    }
                }
            }
            selectedProducts = selectedProducts.Distinct().ToList();
            productIds = productIds.Distinct().ToList();

            string from = MethodHelpers.ConvertDateToCorrectString(dtMinDateReceipts.Value);
            string to = MethodHelpers.ConvertDateToCorrectString(dtMaxDateReceipts.Value.AddDays(1));
            var reports = ReportsProcesser.GenerateReportOfReceipts(productIds, from, to, cbbGroupOnReceipts.SelectedIndex, cbRemainReceipts.Checked);

            var reportItem = new ReportItem()
            {
                ProductId = 0,
                Name = "Tổng Kết",
            };

            loadProducts(false);
            foreach (var report in reports)
            {
                var product = Products.FirstOrDefault(p => p.ProductId == report.ProductId);
                if (product != null)
                {
                    report.Name = product.ProductName;
                }

                reportItem.OutCome += report.OutCome;
                reportItem.InCome += report.InCome;
                reportItem.Fee += report.Fee;
            }
            reports.Insert(0, reportItem);

            grdReceipts.DataSource = new SortableBindingList<ReportItem>(reports, true);
        }

        private void tabReports_SelectedIndexChanged(object sender, EventArgs e)
        {
            switch (tabReports.SelectedIndex)
            {
                case 0:
                    break;
                case 1:
                    loadcbbProducts(false);
                    if (grdReportForProducts.DataSource == null)
                    {
                        ViewReportForProductOnGridView();
                    }
                    break;
                case 2:
                    if (grdReceipts.DataSource == null)
                    {
                        ViewReportOnGridViewReceipts();
                    }
                    break;
                case 3:
                    if (grdReportCustomers.DataSource == null)
                    {
                        ViewReportOnGridViewCustomer();
                    }
                    break;
            }
        }

        private void cbbQuickSelectionProducts_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateViewReportProducts();
        }

        private void btViewReportProducts_Click(object sender, EventArgs e)
        {
            ViewReportForProductOnGridView();
        }

        private void cbbQuickSelectionCustomer_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateViewReportCustomer();
        }

        private void btViewReportCustomer_Click(object sender, EventArgs e)
        {
            ViewReportOnGridViewCustomer();
        }

        private void cbbQuickSelectionReceipts_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateViewReportReceipt();
        }

        private void btReportReceipts_Click(object sender, EventArgs e)
        {
            ViewReportOnGridViewReceipts();
        }

        private void cbbSelectKindForProducts_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadcbbProducts(false);
        }

        private void cbbSelectKindsForReceipts_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadcbbProducts(false);
        }

    }
}

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
            ReloadData();
            cbbQuickSelection.SelectedIndex = 2;
            UpdateView();
            cbbGroupOn.SelectedIndex = 0;
            ViewReportOnGridView();
        }
        private void cbbQuickSelection_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateView();
        }

        private void UpdateView()
        {
            DateTime now = DateTime.Now;
            switch (cbbQuickSelection.SelectedIndex + 1)
            {
                case 1:
                    dtMinDate.Value = now;
                    dtMaxDate.Value = now;
                    break;
                case 2:
                    dtMinDate.Value = now.AddDays(-1);
                    dtMaxDate.Value = now.AddDays(-1);
                    break;
                case 3:
                    dtMinDate.Value = now.AddDays(-7);
                    dtMaxDate.Value = now;
                    break;
                case 4:
                    dtMinDate.Value = now.AddDays(-(int)now.DayOfWeek);
                    dtMaxDate.Value = now;
                    break;
                case 5:
                    dtMinDate.Value = now.AddDays(-(int)now.DayOfWeek - 6);
                    dtMaxDate.Value = dtMinDate.Value.AddDays(6);
                    break;
                case 6:
                    dtMinDate.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxDate.Value = now;
                    break;
                case 7:
                    dtMinDate.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxDate.Value = (new DateTime(now.Year, now.Month + 1, 1)).AddDays(-1);
                    break;
                case 8:
                    dtMinDate.Value = new DateTime(now.Year, 1, 1);
                    dtMaxDate.Value = now;
                    break;
                case 9:
                    dtMinDate.Value = new DateTime(now.Year - 1, 1, 1);
                    dtMaxDate.Value = new DateTime(now.Year - 1, 12, 31);
                    break;
            }
        }

        private void loadProducts(bool isReload = false)
        {
            if (Products == null || isReload)
            {
                Products = ProductProcesser.GetProducts();
            }

        }
        private void loadcbbProducts(bool isReload = false)
        {
            loadProducts(isReload);
            cbbProducts.DataSource = Products;
            cbbProducts.DisplayMember = "ProductName";
            cbbProducts.ValueMember = "ProductId";
            cbbProducts.Text = string.Empty;
            cbbProducts.SelectedIndex = -1;
        }
        private void btViewReport_Click(object sender, EventArgs e)
        {
            ViewReportOnGridView();
        }

        private void ViewReportOnGridView()
        {
            int? productId = null;
            if (cbbProducts.SelectedIndex >= 0)
            {
                productId = (int)cbbProducts.SelectedValue;
            }
            string from = MethodHelpers.ConvertDateToCorrectString(dtMinDate.Value);
            string to = MethodHelpers.ConvertDateToCorrectString(dtMaxDate.Value.AddDays(1));
            var reports = ReportsProcesser.GenerateReportOfProfit(productId, from, to, cbbGroupOn.SelectedIndex);
            grdReports.DataSource = reports;

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

            foreach(var report in reports)
            {   
                chartProfit.Series["SeriesInCome"].Points.AddXY(report.DateForReport, report.InCome);
                chartProfit.Series["SeriesOutCome"].Points.AddXY(report.DateForReport, report.OutCome);
                chartProfit.Series["SeriesFee"].Points.AddXY(report.DateForReport, report.Fee);
                chartProfit.Series["SeriesProfit"].Points.AddXY(report.DateForReport, report.Profit);
            }
        }

    }
}

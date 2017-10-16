namespace QLBH.Views
{
    partial class ReportsManagement
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.Windows.Forms.DataVisualization.Charting.ChartArea chartArea1 = new System.Windows.Forms.DataVisualization.Charting.ChartArea();
            System.Windows.Forms.DataVisualization.Charting.Legend legend1 = new System.Windows.Forms.DataVisualization.Charting.Legend();
            System.Windows.Forms.DataVisualization.Charting.Series series1 = new System.Windows.Forms.DataVisualization.Charting.Series();
            System.Windows.Forms.DataVisualization.Charting.Series series2 = new System.Windows.Forms.DataVisualization.Charting.Series();
            System.Windows.Forms.DataVisualization.Charting.Series series3 = new System.Windows.Forms.DataVisualization.Charting.Series();
            System.Windows.Forms.DataVisualization.Charting.Series series4 = new System.Windows.Forms.DataVisualization.Charting.Series();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle3 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle4 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle5 = new System.Windows.Forms.DataGridViewCellStyle();
            this.tabReports = new System.Windows.Forms.TabControl();
            this.tabReportCommom = new System.Windows.Forms.TabPage();
            this.panel2 = new System.Windows.Forms.Panel();
            this.grdReports = new System.Windows.Forms.DataGridView();
            this.panel1 = new System.Windows.Forms.Panel();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.panel4 = new System.Windows.Forms.Panel();
            this.cbbProducts = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.panel5 = new System.Windows.Forms.Panel();
            this.dtMinDate = new System.Windows.Forms.DateTimePicker();
            this.label2 = new System.Windows.Forms.Label();
            this.panel6 = new System.Windows.Forms.Panel();
            this.dtMaxDate = new System.Windows.Forms.DateTimePicker();
            this.label3 = new System.Windows.Forms.Label();
            this.panel7 = new System.Windows.Forms.Panel();
            this.cbbQuickSelection = new System.Windows.Forms.ComboBox();
            this.label4 = new System.Windows.Forms.Label();
            this.panel8 = new System.Windows.Forms.Panel();
            this.cbbGroupOn = new System.Windows.Forms.ComboBox();
            this.label5 = new System.Windows.Forms.Label();
            this.panel3 = new System.Windows.Forms.Panel();
            this.btViewReport = new System.Windows.Forms.Button();
            this.tabReportForProduct = new System.Windows.Forms.TabPage();
            this.Column1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.panel9 = new System.Windows.Forms.Panel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.panel10 = new System.Windows.Forms.Panel();
            this.chartProfit = new System.Windows.Forms.DataVisualization.Charting.Chart();
            this.dateForReportDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.inComeDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.outComeDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.feeDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.feeForShipDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.profitDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.reportItemBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.tabReports.SuspendLayout();
            this.tabReportCommom.SuspendLayout();
            this.panel2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grdReports)).BeginInit();
            this.panel1.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel4.SuspendLayout();
            this.panel5.SuspendLayout();
            this.panel6.SuspendLayout();
            this.panel7.SuspendLayout();
            this.panel8.SuspendLayout();
            this.panel3.SuspendLayout();
            this.panel9.SuspendLayout();
            this.tableLayoutPanel2.SuspendLayout();
            this.panel10.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.chartProfit)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.reportItemBindingSource)).BeginInit();
            this.SuspendLayout();
            // 
            // tabReports
            // 
            this.tabReports.Controls.Add(this.tabReportCommom);
            this.tabReports.Controls.Add(this.tabReportForProduct);
            this.tabReports.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tabReports.Location = new System.Drawing.Point(0, 0);
            this.tabReports.Name = "tabReports";
            this.tabReports.SelectedIndex = 0;
            this.tabReports.Size = new System.Drawing.Size(762, 442);
            this.tabReports.TabIndex = 0;
            // 
            // tabReportCommom
            // 
            this.tabReportCommom.Controls.Add(this.panel9);
            this.tabReportCommom.Controls.Add(this.panel1);
            this.tabReportCommom.Location = new System.Drawing.Point(4, 22);
            this.tabReportCommom.Name = "tabReportCommom";
            this.tabReportCommom.Padding = new System.Windows.Forms.Padding(3);
            this.tabReportCommom.Size = new System.Drawing.Size(754, 416);
            this.tabReportCommom.TabIndex = 0;
            this.tabReportCommom.Text = "Xem Báo Cáo Doanh Thu";
            this.tabReportCommom.UseVisualStyleBackColor = true;
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.grdReports);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel2.Location = new System.Drawing.Point(3, 174);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(742, 166);
            this.panel2.TabIndex = 1;
            // 
            // grdReports
            // 
            this.grdReports.AllowUserToAddRows = false;
            this.grdReports.AllowUserToDeleteRows = false;
            this.grdReports.AutoGenerateColumns = false;
            this.grdReports.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grdReports.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.dateForReportDataGridViewTextBoxColumn,
            this.Column1,
            this.inComeDataGridViewTextBoxColumn,
            this.outComeDataGridViewTextBoxColumn,
            this.feeDataGridViewTextBoxColumn,
            this.feeForShipDataGridViewTextBoxColumn,
            this.profitDataGridViewTextBoxColumn});
            this.grdReports.DataSource = this.reportItemBindingSource;
            this.grdReports.Dock = System.Windows.Forms.DockStyle.Fill;
            this.grdReports.Location = new System.Drawing.Point(0, 0);
            this.grdReports.Name = "grdReports";
            this.grdReports.ReadOnly = true;
            this.grdReports.Size = new System.Drawing.Size(742, 166);
            this.grdReports.TabIndex = 0;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.groupBox1);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(3, 3);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(748, 67);
            this.panel1.TabIndex = 0;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.tableLayoutPanel1);
            this.groupBox1.Controls.Add(this.panel3);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(748, 67);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Xem Lợi Nhận";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 5;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.Controls.Add(this.panel4, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel5, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel6, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel7, 3, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel8, 4, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(3, 16);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(572, 48);
            this.tableLayoutPanel1.TabIndex = 1;
            // 
            // panel4
            // 
            this.panel4.Controls.Add(this.cbbProducts);
            this.panel4.Controls.Add(this.label1);
            this.panel4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel4.Location = new System.Drawing.Point(3, 3);
            this.panel4.Name = "panel4";
            this.panel4.Size = new System.Drawing.Size(108, 42);
            this.panel4.TabIndex = 0;
            // 
            // cbbProducts
            // 
            this.cbbProducts.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbProducts.FormattingEnabled = true;
            this.cbbProducts.Location = new System.Drawing.Point(0, 13);
            this.cbbProducts.Name = "cbbProducts";
            this.cbbProducts.Size = new System.Drawing.Size(108, 21);
            this.cbbProducts.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Top;
            this.label1.Location = new System.Drawing.Point(0, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(56, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Sản Phẩm";
            // 
            // panel5
            // 
            this.panel5.Controls.Add(this.dtMinDate);
            this.panel5.Controls.Add(this.label2);
            this.panel5.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel5.Location = new System.Drawing.Point(117, 3);
            this.panel5.Name = "panel5";
            this.panel5.Size = new System.Drawing.Size(108, 42);
            this.panel5.TabIndex = 1;
            // 
            // dtMinDate
            // 
            this.dtMinDate.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtMinDate.Location = new System.Drawing.Point(0, 13);
            this.dtMinDate.Name = "dtMinDate";
            this.dtMinDate.Size = new System.Drawing.Size(108, 20);
            this.dtMinDate.TabIndex = 1;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Top;
            this.label2.Location = new System.Drawing.Point(0, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(48, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Từ Ngày";
            // 
            // panel6
            // 
            this.panel6.Controls.Add(this.dtMaxDate);
            this.panel6.Controls.Add(this.label3);
            this.panel6.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel6.Location = new System.Drawing.Point(231, 3);
            this.panel6.Name = "panel6";
            this.panel6.Size = new System.Drawing.Size(108, 42);
            this.panel6.TabIndex = 2;
            // 
            // dtMaxDate
            // 
            this.dtMaxDate.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtMaxDate.Location = new System.Drawing.Point(0, 13);
            this.dtMaxDate.Name = "dtMaxDate";
            this.dtMaxDate.Size = new System.Drawing.Size(108, 20);
            this.dtMaxDate.TabIndex = 1;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Dock = System.Windows.Forms.DockStyle.Top;
            this.label3.Location = new System.Drawing.Point(0, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(55, 13);
            this.label3.TabIndex = 0;
            this.label3.Text = "Đến Ngày";
            // 
            // panel7
            // 
            this.panel7.Controls.Add(this.cbbQuickSelection);
            this.panel7.Controls.Add(this.label4);
            this.panel7.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel7.Location = new System.Drawing.Point(345, 3);
            this.panel7.Name = "panel7";
            this.panel7.Size = new System.Drawing.Size(108, 42);
            this.panel7.TabIndex = 3;
            // 
            // cbbQuickSelection
            // 
            this.cbbQuickSelection.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbQuickSelection.FormattingEnabled = true;
            this.cbbQuickSelection.Items.AddRange(new object[] {
            "Hôm Nay",
            "Hôm Qua",
            "Trong 7 Ngày",
            "Tuần Này",
            "Tuần Trước",
            "Tháng Này",
            "Tháng Trước",
            "Năm Này",
            "Năm Trước"});
            this.cbbQuickSelection.Location = new System.Drawing.Point(0, 13);
            this.cbbQuickSelection.Name = "cbbQuickSelection";
            this.cbbQuickSelection.Size = new System.Drawing.Size(108, 21);
            this.cbbQuickSelection.TabIndex = 1;
            this.cbbQuickSelection.SelectedIndexChanged += new System.EventHandler(this.cbbQuickSelection_SelectedIndexChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Dock = System.Windows.Forms.DockStyle.Top;
            this.label4.Location = new System.Drawing.Point(0, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(67, 13);
            this.label4.TabIndex = 0;
            this.label4.Text = "Chọn Nhanh";
            // 
            // panel8
            // 
            this.panel8.Controls.Add(this.cbbGroupOn);
            this.panel8.Controls.Add(this.label5);
            this.panel8.Location = new System.Drawing.Point(459, 3);
            this.panel8.Name = "panel8";
            this.panel8.Size = new System.Drawing.Size(110, 42);
            this.panel8.TabIndex = 4;
            // 
            // cbbGroupOn
            // 
            this.cbbGroupOn.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbGroupOn.FormattingEnabled = true;
            this.cbbGroupOn.Items.AddRange(new object[] {
            "Ngày",
            "Tháng",
            "Năm"});
            this.cbbGroupOn.Location = new System.Drawing.Point(0, 13);
            this.cbbGroupOn.Name = "cbbGroupOn";
            this.cbbGroupOn.Size = new System.Drawing.Size(110, 21);
            this.cbbGroupOn.TabIndex = 1;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Dock = System.Windows.Forms.DockStyle.Top;
            this.label5.Location = new System.Drawing.Point(0, 0);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(63, 13);
            this.label5.TabIndex = 0;
            this.label5.Text = "Nhóm Theo";
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.btViewReport);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel3.Location = new System.Drawing.Point(575, 16);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(170, 48);
            this.panel3.TabIndex = 0;
            // 
            // btViewReport
            // 
            this.btViewReport.Dock = System.Windows.Forms.DockStyle.Left;
            this.btViewReport.Location = new System.Drawing.Point(0, 0);
            this.btViewReport.Name = "btViewReport";
            this.btViewReport.Size = new System.Drawing.Size(75, 48);
            this.btViewReport.TabIndex = 0;
            this.btViewReport.Text = "Xem";
            this.btViewReport.UseVisualStyleBackColor = true;
            this.btViewReport.Click += new System.EventHandler(this.btViewReport_Click);
            // 
            // tabReportForProduct
            // 
            this.tabReportForProduct.Location = new System.Drawing.Point(4, 22);
            this.tabReportForProduct.Name = "tabReportForProduct";
            this.tabReportForProduct.Padding = new System.Windows.Forms.Padding(3);
            this.tabReportForProduct.Size = new System.Drawing.Size(754, 416);
            this.tabReportForProduct.TabIndex = 1;
            this.tabReportForProduct.Text = "Báo Cáo Theo Sản Phẩm";
            this.tabReportForProduct.UseVisualStyleBackColor = true;
            // 
            // Column1
            // 
            this.Column1.DataPropertyName = "Name";
            this.Column1.HeaderText = "Tên";
            this.Column1.Name = "Column1";
            this.Column1.ReadOnly = true;
            // 
            // panel9
            // 
            this.panel9.Controls.Add(this.tableLayoutPanel2);
            this.panel9.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel9.Location = new System.Drawing.Point(3, 70);
            this.panel9.Name = "panel9";
            this.panel9.Size = new System.Drawing.Size(748, 343);
            this.panel9.TabIndex = 2;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 1;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel2.Controls.Add(this.panel2, 0, 1);
            this.tableLayoutPanel2.Controls.Add(this.panel10, 0, 0);
            this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel2.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 2;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(748, 343);
            this.tableLayoutPanel2.TabIndex = 0;
            // 
            // panel10
            // 
            this.panel10.Controls.Add(this.chartProfit);
            this.panel10.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel10.Location = new System.Drawing.Point(3, 3);
            this.panel10.Name = "panel10";
            this.panel10.Size = new System.Drawing.Size(742, 165);
            this.panel10.TabIndex = 2;
            // 
            // chartProfit
            // 
            chartArea1.Name = "ChartArea1";
            this.chartProfit.ChartAreas.Add(chartArea1);
            this.chartProfit.Dock = System.Windows.Forms.DockStyle.Fill;
            legend1.Name = "Legend1";
            this.chartProfit.Legends.Add(legend1);
            this.chartProfit.Location = new System.Drawing.Point(0, 0);
            this.chartProfit.Name = "chartProfit";
            series1.ChartArea = "ChartArea1";
            series1.Legend = "Legend1";
            series1.Name = "SeriesInCome";
            series2.ChartArea = "ChartArea1";
            series2.Legend = "Legend1";
            series2.Name = "SeriesOutCome";
            series3.ChartArea = "ChartArea1";
            series3.Legend = "Legend1";
            series3.Name = "SeriesFee";
            series4.ChartArea = "ChartArea1";
            series4.Legend = "Legend1";
            series4.Name = "SeriesProfit";
            this.chartProfit.Series.Add(series1);
            this.chartProfit.Series.Add(series2);
            this.chartProfit.Series.Add(series3);
            this.chartProfit.Series.Add(series4);
            this.chartProfit.Size = new System.Drawing.Size(742, 165);
            this.chartProfit.TabIndex = 0;
            this.chartProfit.Text = "chart1";
            // 
            // dateForReportDataGridViewTextBoxColumn
            // 
            this.dateForReportDataGridViewTextBoxColumn.DataPropertyName = "DateForReport";
            this.dateForReportDataGridViewTextBoxColumn.HeaderText = "Thời Gian";
            this.dateForReportDataGridViewTextBoxColumn.Name = "dateForReportDataGridViewTextBoxColumn";
            this.dateForReportDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // inComeDataGridViewTextBoxColumn
            // 
            this.inComeDataGridViewTextBoxColumn.DataPropertyName = "InCome";
            dataGridViewCellStyle1.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle1.Format = "N0";
            dataGridViewCellStyle1.NullValue = null;
            this.inComeDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle1;
            this.inComeDataGridViewTextBoxColumn.HeaderText = "Tiền Bán Hàng";
            this.inComeDataGridViewTextBoxColumn.Name = "inComeDataGridViewTextBoxColumn";
            this.inComeDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // outComeDataGridViewTextBoxColumn
            // 
            this.outComeDataGridViewTextBoxColumn.DataPropertyName = "OutCome";
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle2.Format = "N0";
            dataGridViewCellStyle2.NullValue = null;
            this.outComeDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle2;
            this.outComeDataGridViewTextBoxColumn.HeaderText = "Tiền Nhập Hàng";
            this.outComeDataGridViewTextBoxColumn.Name = "outComeDataGridViewTextBoxColumn";
            this.outComeDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // feeDataGridViewTextBoxColumn
            // 
            this.feeDataGridViewTextBoxColumn.DataPropertyName = "Fee";
            dataGridViewCellStyle3.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle3.Format = "N0";
            dataGridViewCellStyle3.NullValue = null;
            this.feeDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle3;
            this.feeDataGridViewTextBoxColumn.HeaderText = "Chi Phí";
            this.feeDataGridViewTextBoxColumn.Name = "feeDataGridViewTextBoxColumn";
            this.feeDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // feeForShipDataGridViewTextBoxColumn
            // 
            this.feeForShipDataGridViewTextBoxColumn.DataPropertyName = "FeeForShip";
            dataGridViewCellStyle4.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle4.Format = "N0";
            dataGridViewCellStyle4.NullValue = null;
            this.feeForShipDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle4;
            this.feeForShipDataGridViewTextBoxColumn.HeaderText = "Tiền Vận Chuyển";
            this.feeForShipDataGridViewTextBoxColumn.Name = "feeForShipDataGridViewTextBoxColumn";
            this.feeForShipDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // profitDataGridViewTextBoxColumn
            // 
            this.profitDataGridViewTextBoxColumn.DataPropertyName = "Profit";
            dataGridViewCellStyle5.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle5.Format = "N0";
            dataGridViewCellStyle5.NullValue = null;
            this.profitDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle5;
            this.profitDataGridViewTextBoxColumn.HeaderText = "Lợi Nhuận";
            this.profitDataGridViewTextBoxColumn.Name = "profitDataGridViewTextBoxColumn";
            this.profitDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // reportItemBindingSource
            // 
            this.reportItemBindingSource.DataSource = typeof(QLBH.Models.ReportItem);
            // 
            // ReportsManagement
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.tabReports);
            this.Name = "ReportsManagement";
            this.Size = new System.Drawing.Size(762, 442);
            this.Load += new System.EventHandler(this.ReportsManagement_Load);
            this.tabReports.ResumeLayout(false);
            this.tabReportCommom.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.grdReports)).EndInit();
            this.panel1.ResumeLayout(false);
            this.groupBox1.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.panel4.ResumeLayout(false);
            this.panel4.PerformLayout();
            this.panel5.ResumeLayout(false);
            this.panel5.PerformLayout();
            this.panel6.ResumeLayout(false);
            this.panel6.PerformLayout();
            this.panel7.ResumeLayout(false);
            this.panel7.PerformLayout();
            this.panel8.ResumeLayout(false);
            this.panel8.PerformLayout();
            this.panel3.ResumeLayout(false);
            this.panel9.ResumeLayout(false);
            this.tableLayoutPanel2.ResumeLayout(false);
            this.panel10.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.chartProfit)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.reportItemBindingSource)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tabReports;
        private System.Windows.Forms.TabPage tabReportCommom;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Panel panel4;
        private System.Windows.Forms.Panel panel5;
        private System.Windows.Forms.Panel panel6;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.TabPage tabReportForProduct;
        private System.Windows.Forms.ComboBox cbbProducts;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.DateTimePicker dtMinDate;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.DateTimePicker dtMaxDate;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Panel panel7;
        private System.Windows.Forms.ComboBox cbbQuickSelection;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Panel panel8;
        private System.Windows.Forms.ComboBox cbbGroupOn;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.DataGridView grdReports;
        private System.Windows.Forms.Button btViewReport;
        private System.Windows.Forms.BindingSource reportItemBindingSource;
        private System.Windows.Forms.DataGridViewTextBoxColumn dateForReportDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column1;
        private System.Windows.Forms.DataGridViewTextBoxColumn inComeDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn outComeDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn feeDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn feeForShipDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn profitDataGridViewTextBoxColumn;
        private System.Windows.Forms.Panel panel9;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private System.Windows.Forms.Panel panel10;
        private System.Windows.Forms.DataVisualization.Charting.Chart chartProfit;
    }
}

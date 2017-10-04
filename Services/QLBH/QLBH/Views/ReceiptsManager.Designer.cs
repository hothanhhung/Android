namespace QLBH.Views
{
    partial class ReceiptsManager
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
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.panel2 = new System.Windows.Forms.Panel();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.panel4 = new System.Windows.Forms.Panel();
            this.grdReceipts = new System.Windows.Forms.DataGridView();
            this.receiptIdDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.productIdDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.quantityDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.isSellAllDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.datedReceiptDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.noteDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.receiptBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.panel3 = new System.Windows.Forms.Panel();
            this.panel6 = new System.Windows.Forms.Panel();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.cbbProductsForView = new System.Windows.Forms.ComboBox();
            this.dtMinForView = new System.Windows.Forms.DateTimePicker();
            this.dtMaxForView = new System.Windows.Forms.DateTimePicker();
            this.cbbQuickView = new System.Windows.Forms.ComboBox();
            this.cbGroupProduct = new System.Windows.Forms.CheckBox();
            this.panel5 = new System.Windows.Forms.Panel();
            this.btViewReceipt = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.panel7 = new System.Windows.Forms.Panel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.cbbProductForReceipt = new System.Windows.Forms.ComboBox();
            this.txtQuantity = new System.Windows.Forms.NumericUpDown();
            this.txtTotalPrice = new System.Windows.Forms.NumericUpDown();
            this.dtReceiptedDate = new System.Windows.Forms.DateTimePicker();
            this.txtNote = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.panel8 = new System.Windows.Forms.Panel();
            this.btReceipt = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.panel4.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grdReceipts)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.receiptBindingSource)).BeginInit();
            this.panel3.SuspendLayout();
            this.panel6.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel5.SuspendLayout();
            this.panel1.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.panel7.SuspendLayout();
            this.tableLayoutPanel2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtQuantity)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.txtTotalPrice)).BeginInit();
            this.panel8.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.panel2);
            this.groupBox1.Controls.Add(this.panel1);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(660, 335);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Quản Lý Nhập Hàng";
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.groupBox2);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel2.Location = new System.Drawing.Point(3, 79);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(654, 253);
            this.panel2.TabIndex = 1;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.panel4);
            this.groupBox2.Controls.Add(this.panel3);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox2.Location = new System.Drawing.Point(0, 0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(654, 253);
            this.groupBox2.TabIndex = 0;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Xem hàng đã nhập";
            // 
            // panel4
            // 
            this.panel4.Controls.Add(this.grdReceipts);
            this.panel4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel4.Location = new System.Drawing.Point(3, 56);
            this.panel4.Name = "panel4";
            this.panel4.Size = new System.Drawing.Size(648, 194);
            this.panel4.TabIndex = 1;
            // 
            // grdReceipts
            // 
            this.grdReceipts.AllowUserToAddRows = false;
            this.grdReceipts.AllowUserToDeleteRows = false;
            this.grdReceipts.AutoGenerateColumns = false;
            this.grdReceipts.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grdReceipts.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.receiptIdDataGridViewTextBoxColumn,
            this.productIdDataGridViewTextBoxColumn,
            this.quantityDataGridViewTextBoxColumn,
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn,
            this.isSellAllDataGridViewTextBoxColumn,
            this.datedReceiptDataGridViewTextBoxColumn,
            this.noteDataGridViewTextBoxColumn});
            this.grdReceipts.DataSource = this.receiptBindingSource;
            this.grdReceipts.Dock = System.Windows.Forms.DockStyle.Fill;
            this.grdReceipts.Location = new System.Drawing.Point(0, 0);
            this.grdReceipts.Name = "grdReceipts";
            this.grdReceipts.ReadOnly = true;
            this.grdReceipts.Size = new System.Drawing.Size(648, 194);
            this.grdReceipts.TabIndex = 0;
            // 
            // receiptIdDataGridViewTextBoxColumn
            // 
            this.receiptIdDataGridViewTextBoxColumn.DataPropertyName = "ReceiptId";
            this.receiptIdDataGridViewTextBoxColumn.HeaderText = "ReceiptId";
            this.receiptIdDataGridViewTextBoxColumn.Name = "receiptIdDataGridViewTextBoxColumn";
            this.receiptIdDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // productIdDataGridViewTextBoxColumn
            // 
            this.productIdDataGridViewTextBoxColumn.DataPropertyName = "ProductName";
            this.productIdDataGridViewTextBoxColumn.HeaderText = "Tên Sản Phẩm";
            this.productIdDataGridViewTextBoxColumn.Name = "productIdDataGridViewTextBoxColumn";
            this.productIdDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // quantityDataGridViewTextBoxColumn
            // 
            this.quantityDataGridViewTextBoxColumn.DataPropertyName = "Quantity";
            dataGridViewCellStyle1.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle1.Format = "N0";
            dataGridViewCellStyle1.NullValue = null;
            this.quantityDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle1;
            this.quantityDataGridViewTextBoxColumn.HeaderText = "Số Lượng";
            this.quantityDataGridViewTextBoxColumn.Name = "quantityDataGridViewTextBoxColumn";
            this.quantityDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // priceOfAllForReceiptingDataGridViewTextBoxColumn
            // 
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn.DataPropertyName = "PriceOfAllForReceipting";
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle2.Format = "N0";
            dataGridViewCellStyle2.NullValue = null;
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle2;
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn.HeaderText = "Tổng Giá Nhập";
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn.Name = "priceOfAllForReceiptingDataGridViewTextBoxColumn";
            this.priceOfAllForReceiptingDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // isSellAllDataGridViewTextBoxColumn
            // 
            this.isSellAllDataGridViewTextBoxColumn.DataPropertyName = "IsSellAll";
            this.isSellAllDataGridViewTextBoxColumn.HeaderText = "Tình Trạng Hàng";
            this.isSellAllDataGridViewTextBoxColumn.Name = "isSellAllDataGridViewTextBoxColumn";
            this.isSellAllDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // datedReceiptDataGridViewTextBoxColumn
            // 
            this.datedReceiptDataGridViewTextBoxColumn.DataPropertyName = "DatedReceipt";
            this.datedReceiptDataGridViewTextBoxColumn.HeaderText = "Ngày Nhập";
            this.datedReceiptDataGridViewTextBoxColumn.Name = "datedReceiptDataGridViewTextBoxColumn";
            this.datedReceiptDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // noteDataGridViewTextBoxColumn
            // 
            this.noteDataGridViewTextBoxColumn.DataPropertyName = "Note";
            this.noteDataGridViewTextBoxColumn.HeaderText = "Ghi Chú";
            this.noteDataGridViewTextBoxColumn.Name = "noteDataGridViewTextBoxColumn";
            this.noteDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // receiptBindingSource
            // 
            this.receiptBindingSource.DataSource = typeof(QLBH.Models.Receipt);
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.panel6);
            this.panel3.Controls.Add(this.panel5);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel3.Location = new System.Drawing.Point(3, 16);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(648, 40);
            this.panel3.TabIndex = 0;
            // 
            // panel6
            // 
            this.panel6.Controls.Add(this.tableLayoutPanel1);
            this.panel6.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel6.Location = new System.Drawing.Point(0, 0);
            this.panel6.Name = "panel6";
            this.panel6.Size = new System.Drawing.Size(519, 40);
            this.panel6.TabIndex = 5;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 5;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.Controls.Add(this.cbbProductsForView, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.dtMinForView, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.dtMaxForView, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.cbbQuickView, 3, 0);
            this.tableLayoutPanel1.Controls.Add(this.cbGroupProduct, 4, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(519, 40);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // cbbProductsForView
            // 
            this.cbbProductsForView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbProductsForView.FormattingEnabled = true;
            this.cbbProductsForView.Location = new System.Drawing.Point(3, 3);
            this.cbbProductsForView.Name = "cbbProductsForView";
            this.cbbProductsForView.Size = new System.Drawing.Size(97, 21);
            this.cbbProductsForView.TabIndex = 0;
            // 
            // dtMinForView
            // 
            this.dtMinForView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtMinForView.Location = new System.Drawing.Point(106, 3);
            this.dtMinForView.Name = "dtMinForView";
            this.dtMinForView.Size = new System.Drawing.Size(97, 20);
            this.dtMinForView.TabIndex = 1;
            // 
            // dtMaxForView
            // 
            this.dtMaxForView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtMaxForView.Location = new System.Drawing.Point(209, 3);
            this.dtMaxForView.Name = "dtMaxForView";
            this.dtMaxForView.Size = new System.Drawing.Size(97, 20);
            this.dtMaxForView.TabIndex = 2;
            // 
            // cbbQuickView
            // 
            this.cbbQuickView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbQuickView.FormattingEnabled = true;
            this.cbbQuickView.Location = new System.Drawing.Point(312, 3);
            this.cbbQuickView.Name = "cbbQuickView";
            this.cbbQuickView.Size = new System.Drawing.Size(97, 21);
            this.cbbQuickView.TabIndex = 3;
            this.cbbQuickView.Text = "Xem Nhanh";
            this.cbbQuickView.SelectedIndexChanged += new System.EventHandler(this.cbbQuickView_SelectedIndexChanged);
            // 
            // cbGroupProduct
            // 
            this.cbGroupProduct.AutoSize = true;
            this.cbGroupProduct.Location = new System.Drawing.Point(415, 3);
            this.cbGroupProduct.Name = "cbGroupProduct";
            this.cbGroupProduct.Size = new System.Drawing.Size(101, 17);
            this.cbGroupProduct.TabIndex = 4;
            this.cbGroupProduct.Text = "Nhóm sản phẩm";
            this.cbGroupProduct.UseVisualStyleBackColor = true;
            // 
            // panel5
            // 
            this.panel5.Controls.Add(this.btViewReceipt);
            this.panel5.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel5.Location = new System.Drawing.Point(519, 0);
            this.panel5.Name = "panel5";
            this.panel5.Size = new System.Drawing.Size(129, 40);
            this.panel5.TabIndex = 4;
            // 
            // btViewReceipt
            // 
            this.btViewReceipt.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btViewReceipt.Location = new System.Drawing.Point(0, 0);
            this.btViewReceipt.Name = "btViewReceipt";
            this.btViewReceipt.Size = new System.Drawing.Size(129, 40);
            this.btViewReceipt.TabIndex = 3;
            this.btViewReceipt.Text = "Xem";
            this.btViewReceipt.UseVisualStyleBackColor = true;
            this.btViewReceipt.Click += new System.EventHandler(this.btViewReceipt_Click);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.groupBox3);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(3, 16);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(654, 63);
            this.panel1.TabIndex = 0;
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.panel7);
            this.groupBox3.Controls.Add(this.panel8);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox3.Location = new System.Drawing.Point(0, 0);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(654, 63);
            this.groupBox3.TabIndex = 0;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Nhập Hàng";
            // 
            // panel7
            // 
            this.panel7.Controls.Add(this.tableLayoutPanel2);
            this.panel7.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel7.Location = new System.Drawing.Point(3, 16);
            this.panel7.Name = "panel7";
            this.panel7.Size = new System.Drawing.Size(519, 44);
            this.panel7.TabIndex = 0;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 5;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel2.Controls.Add(this.cbbProductForReceipt, 0, 1);
            this.tableLayoutPanel2.Controls.Add(this.txtQuantity, 1, 1);
            this.tableLayoutPanel2.Controls.Add(this.txtTotalPrice, 2, 1);
            this.tableLayoutPanel2.Controls.Add(this.dtReceiptedDate, 3, 1);
            this.tableLayoutPanel2.Controls.Add(this.txtNote, 4, 1);
            this.tableLayoutPanel2.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel2.Controls.Add(this.label2, 1, 0);
            this.tableLayoutPanel2.Controls.Add(this.label3, 2, 0);
            this.tableLayoutPanel2.Controls.Add(this.label4, 3, 0);
            this.tableLayoutPanel2.Controls.Add(this.label5, 4, 0);
            this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel2.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 2;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(519, 44);
            this.tableLayoutPanel2.TabIndex = 0;
            // 
            // cbbProductForReceipt
            // 
            this.cbbProductForReceipt.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbProductForReceipt.FormattingEnabled = true;
            this.cbbProductForReceipt.Location = new System.Drawing.Point(3, 25);
            this.cbbProductForReceipt.Name = "cbbProductForReceipt";
            this.cbbProductForReceipt.Size = new System.Drawing.Size(97, 21);
            this.cbbProductForReceipt.TabIndex = 0;
            // 
            // txtQuantity
            // 
            this.txtQuantity.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtQuantity.Location = new System.Drawing.Point(106, 25);
            this.txtQuantity.Maximum = new decimal(new int[] {
            1000000000,
            0,
            0,
            0});
            this.txtQuantity.Name = "txtQuantity";
            this.txtQuantity.Size = new System.Drawing.Size(97, 20);
            this.txtQuantity.TabIndex = 1;
            this.txtQuantity.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.txtQuantity.ThousandsSeparator = true;
            // 
            // txtTotalPrice
            // 
            this.txtTotalPrice.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtTotalPrice.Location = new System.Drawing.Point(209, 25);
            this.txtTotalPrice.Maximum = new decimal(new int[] {
            1000000000,
            0,
            0,
            0});
            this.txtTotalPrice.Name = "txtTotalPrice";
            this.txtTotalPrice.Size = new System.Drawing.Size(97, 20);
            this.txtTotalPrice.TabIndex = 2;
            this.txtTotalPrice.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.txtTotalPrice.ThousandsSeparator = true;
            // 
            // dtReceiptedDate
            // 
            this.dtReceiptedDate.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtReceiptedDate.Location = new System.Drawing.Point(312, 25);
            this.dtReceiptedDate.Name = "dtReceiptedDate";
            this.dtReceiptedDate.Size = new System.Drawing.Size(97, 20);
            this.dtReceiptedDate.TabIndex = 3;
            // 
            // txtNote
            // 
            this.txtNote.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtNote.Location = new System.Drawing.Point(415, 25);
            this.txtNote.Name = "txtNote";
            this.txtNote.Size = new System.Drawing.Size(101, 20);
            this.txtNote.TabIndex = 9;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(56, 13);
            this.label1.TabIndex = 4;
            this.label1.Text = "Sản Phẩm";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(106, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 13);
            this.label2.TabIndex = 5;
            this.label2.Text = "Số Lượng";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(209, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(80, 13);
            this.label3.TabIndex = 6;
            this.label3.Text = "Tổng Giá Nhập";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(312, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(61, 13);
            this.label4.TabIndex = 7;
            this.label4.Text = "Ngày Nhập";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(415, 0);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(45, 13);
            this.label5.TabIndex = 8;
            this.label5.Text = "Ghi Chú";
            // 
            // panel8
            // 
            this.panel8.Controls.Add(this.btReceipt);
            this.panel8.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel8.Location = new System.Drawing.Point(522, 16);
            this.panel8.Name = "panel8";
            this.panel8.Size = new System.Drawing.Size(129, 44);
            this.panel8.TabIndex = 1;
            // 
            // btReceipt
            // 
            this.btReceipt.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btReceipt.Location = new System.Drawing.Point(0, 0);
            this.btReceipt.Name = "btReceipt";
            this.btReceipt.Size = new System.Drawing.Size(129, 44);
            this.btReceipt.TabIndex = 0;
            this.btReceipt.Text = "Nhập Hàng";
            this.btReceipt.UseVisualStyleBackColor = true;
            this.btReceipt.Click += new System.EventHandler(this.btReceipt_Click);
            // 
            // ReceiptsManager
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox1);
            this.Name = "ReceiptsManager";
            this.Size = new System.Drawing.Size(660, 335);
            this.Load += new System.EventHandler(this.ReceiptsManager_Load);
            this.groupBox1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.panel4.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.grdReceipts)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.receiptBindingSource)).EndInit();
            this.panel3.ResumeLayout(false);
            this.panel6.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.panel5.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.groupBox3.ResumeLayout(false);
            this.panel7.ResumeLayout(false);
            this.tableLayoutPanel2.ResumeLayout(false);
            this.tableLayoutPanel2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtQuantity)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.txtTotalPrice)).EndInit();
            this.panel8.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Panel panel4;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.DataGridView grdReceipts;
        private System.Windows.Forms.Button btViewReceipt;
        private System.Windows.Forms.DateTimePicker dtMaxForView;
        private System.Windows.Forms.DateTimePicker dtMinForView;
        private System.Windows.Forms.ComboBox cbbProductsForView;
        private System.Windows.Forms.Panel panel6;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Panel panel5;
        private System.Windows.Forms.Panel panel8;
        private System.Windows.Forms.Panel panel7;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private System.Windows.Forms.ComboBox cbbProductForReceipt;
        private System.Windows.Forms.NumericUpDown txtQuantity;
        private System.Windows.Forms.NumericUpDown txtTotalPrice;
        private System.Windows.Forms.DateTimePicker dtReceiptedDate;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btReceipt;
        private System.Windows.Forms.ComboBox cbbQuickView;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox txtNote;
        private System.Windows.Forms.CheckBox cbGroupProduct;
        private System.Windows.Forms.BindingSource receiptBindingSource;
        private System.Windows.Forms.DataGridViewTextBoxColumn receiptIdDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn productIdDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn quantityDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn priceOfAllForReceiptingDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn isSellAllDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn datedReceiptDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn noteDataGridViewTextBoxColumn;
    }
}

namespace QLBH.Views
{
    partial class IssuesManagement
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
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle3 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            this.tabIssues = new System.Windows.Forms.TabControl();
            this.tabCreateOrder = new System.Windows.Forms.TabPage();
            this.panel2 = new System.Windows.Forms.Panel();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.panel10 = new System.Windows.Forms.Panel();
            this.grdOrderDetail = new System.Windows.Forms.DataGridView();
            this.productIdDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewComboBoxColumn();
            this.Column1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.panel9 = new System.Windows.Forms.Panel();
            this.panel1 = new System.Windows.Forms.Panel();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.panel3 = new System.Windows.Forms.Panel();
            this.txtCustomerName = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.panel4 = new System.Windows.Forms.Panel();
            this.txtCustomerPhone = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.panel5 = new System.Windows.Forms.Panel();
            this.txtCustomerDeliveryAddress = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.panel7 = new System.Windows.Forms.Panel();
            this.txtCustomerShipFee = new System.Windows.Forms.NumericUpDown();
            this.label3 = new System.Windows.Forms.Label();
            this.panel8 = new System.Windows.Forms.Panel();
            this.dtDateForSelling = new System.Windows.Forms.DateTimePicker();
            this.label6 = new System.Windows.Forms.Label();
            this.panel6 = new System.Windows.Forms.Panel();
            this.txtCustomerNote = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.cbUpdateCustomerInfo = new System.Windows.Forms.CheckBox();
            this.linkGetCustomerInfo = new System.Windows.Forms.LinkLabel();
            this.tabManagerOrders = new System.Windows.Forms.TabPage();
            this.quantityDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.priceForUnitDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.noteDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.orderDetailBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.tabIssues.SuspendLayout();
            this.tabCreateOrder.SuspendLayout();
            this.panel2.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.panel10.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grdOrderDetail)).BeginInit();
            this.panel1.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel3.SuspendLayout();
            this.panel4.SuspendLayout();
            this.panel5.SuspendLayout();
            this.panel7.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtCustomerShipFee)).BeginInit();
            this.panel8.SuspendLayout();
            this.panel6.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.orderDetailBindingSource)).BeginInit();
            this.SuspendLayout();
            // 
            // tabIssues
            // 
            this.tabIssues.Controls.Add(this.tabCreateOrder);
            this.tabIssues.Controls.Add(this.tabManagerOrders);
            this.tabIssues.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tabIssues.Location = new System.Drawing.Point(0, 0);
            this.tabIssues.Name = "tabIssues";
            this.tabIssues.SelectedIndex = 0;
            this.tabIssues.Size = new System.Drawing.Size(759, 370);
            this.tabIssues.TabIndex = 0;
            // 
            // tabCreateOrder
            // 
            this.tabCreateOrder.Controls.Add(this.panel2);
            this.tabCreateOrder.Controls.Add(this.panel1);
            this.tabCreateOrder.Location = new System.Drawing.Point(4, 22);
            this.tabCreateOrder.Name = "tabCreateOrder";
            this.tabCreateOrder.Padding = new System.Windows.Forms.Padding(3);
            this.tabCreateOrder.Size = new System.Drawing.Size(751, 344);
            this.tabCreateOrder.TabIndex = 0;
            this.tabCreateOrder.Text = "Tạo Đơn Hàng";
            this.tabCreateOrder.UseVisualStyleBackColor = true;
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.groupBox2);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel2.Location = new System.Drawing.Point(3, 117);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(745, 224);
            this.panel2.TabIndex = 1;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.panel10);
            this.groupBox2.Controls.Add(this.panel9);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox2.Location = new System.Drawing.Point(0, 0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(745, 224);
            this.groupBox2.TabIndex = 0;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Chi Tiết Đơn Hàng";
            // 
            // panel10
            // 
            this.panel10.Controls.Add(this.grdOrderDetail);
            this.panel10.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel10.Location = new System.Drawing.Point(3, 61);
            this.panel10.Name = "panel10";
            this.panel10.Size = new System.Drawing.Size(739, 160);
            this.panel10.TabIndex = 1;
            // 
            // grdOrderDetail
            // 
            this.grdOrderDetail.AutoGenerateColumns = false;
            this.grdOrderDetail.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grdOrderDetail.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.productIdDataGridViewTextBoxColumn,
            this.quantityDataGridViewTextBoxColumn,
            this.priceForUnitDataGridViewTextBoxColumn,
            this.Column1,
            this.noteDataGridViewTextBoxColumn});
            this.grdOrderDetail.DataSource = this.orderDetailBindingSource;
            this.grdOrderDetail.Dock = System.Windows.Forms.DockStyle.Fill;
            this.grdOrderDetail.EditMode = System.Windows.Forms.DataGridViewEditMode.EditOnEnter;
            this.grdOrderDetail.Location = new System.Drawing.Point(0, 0);
            this.grdOrderDetail.Name = "grdOrderDetail";
            this.grdOrderDetail.Size = new System.Drawing.Size(739, 160);
            this.grdOrderDetail.TabIndex = 0;
            this.grdOrderDetail.EditingControlShowing += new System.Windows.Forms.DataGridViewEditingControlShowingEventHandler(this.grdOrderDetail_EditingControlShowing);
            // 
            // productIdDataGridViewTextBoxColumn
            // 
            this.productIdDataGridViewTextBoxColumn.HeaderText = "Sản Phẩm";
            this.productIdDataGridViewTextBoxColumn.Name = "productIdDataGridViewTextBoxColumn";
            this.productIdDataGridViewTextBoxColumn.Resizable = System.Windows.Forms.DataGridViewTriState.True;
            this.productIdDataGridViewTextBoxColumn.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.Automatic;
            // 
            // Column1
            // 
            this.Column1.DataPropertyName = "OrderDetailTotalPrice";
            dataGridViewCellStyle3.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle3.Format = "N0";
            dataGridViewCellStyle3.NullValue = null;
            this.Column1.DefaultCellStyle = dataGridViewCellStyle3;
            this.Column1.HeaderText = "Thành Tiền";
            this.Column1.Name = "Column1";
            this.Column1.ReadOnly = true;
            // 
            // panel9
            // 
            this.panel9.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel9.Location = new System.Drawing.Point(3, 16);
            this.panel9.Name = "panel9";
            this.panel9.Size = new System.Drawing.Size(739, 45);
            this.panel9.TabIndex = 0;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.groupBox1);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(3, 3);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(745, 114);
            this.panel1.TabIndex = 0;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.tableLayoutPanel1);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(745, 114);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Thông Tin Khách Hàng";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 4;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 40F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
            this.tableLayoutPanel1.Controls.Add(this.panel3, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel4, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.panel5, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel7, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel8, 2, 1);
            this.tableLayoutPanel1.Controls.Add(this.panel6, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.cbUpdateCustomerInfo, 3, 1);
            this.tableLayoutPanel1.Controls.Add(this.linkGetCustomerInfo, 3, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(3, 16);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(739, 95);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.txtCustomerName);
            this.panel3.Controls.Add(this.label1);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel3.Location = new System.Drawing.Point(3, 3);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(141, 41);
            this.panel3.TabIndex = 0;
            // 
            // txtCustomerName
            // 
            this.txtCustomerName.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtCustomerName.Location = new System.Drawing.Point(0, 13);
            this.txtCustomerName.Name = "txtCustomerName";
            this.txtCustomerName.Size = new System.Drawing.Size(141, 20);
            this.txtCustomerName.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Top;
            this.label1.Location = new System.Drawing.Point(0, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(89, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Tên Khách Hàng";
            // 
            // panel4
            // 
            this.panel4.Controls.Add(this.txtCustomerPhone);
            this.panel4.Controls.Add(this.label4);
            this.panel4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel4.Location = new System.Drawing.Point(3, 50);
            this.panel4.Name = "panel4";
            this.panel4.Size = new System.Drawing.Size(141, 42);
            this.panel4.TabIndex = 1;
            // 
            // txtCustomerPhone
            // 
            this.txtCustomerPhone.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtCustomerPhone.Location = new System.Drawing.Point(0, 13);
            this.txtCustomerPhone.Name = "txtCustomerPhone";
            this.txtCustomerPhone.Size = new System.Drawing.Size(141, 20);
            this.txtCustomerPhone.TabIndex = 1;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Dock = System.Windows.Forms.DockStyle.Top;
            this.label4.Location = new System.Drawing.Point(0, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(75, 13);
            this.label4.TabIndex = 0;
            this.label4.Text = "Số Điện Thoại";
            // 
            // panel5
            // 
            this.panel5.Controls.Add(this.txtCustomerDeliveryAddress);
            this.panel5.Controls.Add(this.label2);
            this.panel5.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel5.Location = new System.Drawing.Point(150, 3);
            this.panel5.Name = "panel5";
            this.panel5.Size = new System.Drawing.Size(289, 41);
            this.panel5.TabIndex = 2;
            // 
            // txtCustomerDeliveryAddress
            // 
            this.txtCustomerDeliveryAddress.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtCustomerDeliveryAddress.Location = new System.Drawing.Point(0, 13);
            this.txtCustomerDeliveryAddress.Name = "txtCustomerDeliveryAddress";
            this.txtCustomerDeliveryAddress.Size = new System.Drawing.Size(289, 20);
            this.txtCustomerDeliveryAddress.TabIndex = 1;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Top;
            this.label2.Location = new System.Drawing.Point(0, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(95, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Địa Chỉ Giao Hàng";
            // 
            // panel7
            // 
            this.panel7.Controls.Add(this.txtCustomerShipFee);
            this.panel7.Controls.Add(this.label3);
            this.panel7.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel7.Location = new System.Drawing.Point(445, 3);
            this.panel7.Name = "panel7";
            this.panel7.Size = new System.Drawing.Size(141, 41);
            this.panel7.TabIndex = 4;
            // 
            // txtCustomerShipFee
            // 
            this.txtCustomerShipFee.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtCustomerShipFee.Location = new System.Drawing.Point(0, 13);
            this.txtCustomerShipFee.Maximum = new decimal(new int[] {
            -1294967296,
            0,
            0,
            0});
            this.txtCustomerShipFee.Name = "txtCustomerShipFee";
            this.txtCustomerShipFee.Size = new System.Drawing.Size(141, 20);
            this.txtCustomerShipFee.TabIndex = 1;
            this.txtCustomerShipFee.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.txtCustomerShipFee.ThousandsSeparator = true;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Dock = System.Windows.Forms.DockStyle.Top;
            this.label3.Location = new System.Drawing.Point(0, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(85, 13);
            this.label3.TabIndex = 0;
            this.label3.Text = "Phí Vận Chuyển";
            // 
            // panel8
            // 
            this.panel8.Controls.Add(this.dtDateForSelling);
            this.panel8.Controls.Add(this.label6);
            this.panel8.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel8.Location = new System.Drawing.Point(445, 50);
            this.panel8.Name = "panel8";
            this.panel8.Size = new System.Drawing.Size(141, 42);
            this.panel8.TabIndex = 5;
            // 
            // dtDateForSelling
            // 
            this.dtDateForSelling.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtDateForSelling.Location = new System.Drawing.Point(0, 13);
            this.dtDateForSelling.Name = "dtDateForSelling";
            this.dtDateForSelling.Size = new System.Drawing.Size(141, 20);
            this.dtDateForSelling.TabIndex = 1;
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Dock = System.Windows.Forms.DockStyle.Top;
            this.label6.Location = new System.Drawing.Point(0, 0);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(57, 13);
            this.label6.TabIndex = 0;
            this.label6.Text = "Ngày Tạo ";
            // 
            // panel6
            // 
            this.panel6.Controls.Add(this.txtCustomerNote);
            this.panel6.Controls.Add(this.label5);
            this.panel6.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel6.Location = new System.Drawing.Point(150, 50);
            this.panel6.Name = "panel6";
            this.panel6.Size = new System.Drawing.Size(289, 42);
            this.panel6.TabIndex = 3;
            // 
            // txtCustomerNote
            // 
            this.txtCustomerNote.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtCustomerNote.Location = new System.Drawing.Point(0, 13);
            this.txtCustomerNote.Name = "txtCustomerNote";
            this.txtCustomerNote.Size = new System.Drawing.Size(289, 20);
            this.txtCustomerNote.TabIndex = 1;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Dock = System.Windows.Forms.DockStyle.Top;
            this.label5.Location = new System.Drawing.Point(0, 0);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(45, 13);
            this.label5.TabIndex = 0;
            this.label5.Text = "Ghi Chú";
            // 
            // cbUpdateCustomerInfo
            // 
            this.cbUpdateCustomerInfo.AutoSize = true;
            this.cbUpdateCustomerInfo.Location = new System.Drawing.Point(592, 50);
            this.cbUpdateCustomerInfo.Name = "cbUpdateCustomerInfo";
            this.cbUpdateCustomerInfo.Size = new System.Drawing.Size(144, 17);
            this.cbUpdateCustomerInfo.TabIndex = 6;
            this.cbUpdateCustomerInfo.Text = "Lưu Thông Tin Khách Hàng";
            this.cbUpdateCustomerInfo.UseVisualStyleBackColor = true;
            // 
            // linkGetCustomerInfo
            // 
            this.linkGetCustomerInfo.AutoSize = true;
            this.linkGetCustomerInfo.Location = new System.Drawing.Point(592, 0);
            this.linkGetCustomerInfo.Name = "linkGetCustomerInfo";
            this.linkGetCustomerInfo.Size = new System.Drawing.Size(114, 13);
            this.linkGetCustomerInfo.TabIndex = 7;
            this.linkGetCustomerInfo.TabStop = true;
            this.linkGetCustomerInfo.Text = "Lấy Thông Tin Đã Lưu";
            // 
            // tabManagerOrders
            // 
            this.tabManagerOrders.Location = new System.Drawing.Point(4, 22);
            this.tabManagerOrders.Name = "tabManagerOrders";
            this.tabManagerOrders.Padding = new System.Windows.Forms.Padding(3);
            this.tabManagerOrders.Size = new System.Drawing.Size(751, 344);
            this.tabManagerOrders.TabIndex = 1;
            this.tabManagerOrders.Text = "Danh Sách Đơn Hàng";
            this.tabManagerOrders.UseVisualStyleBackColor = true;
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
            // 
            // priceForUnitDataGridViewTextBoxColumn
            // 
            this.priceForUnitDataGridViewTextBoxColumn.DataPropertyName = "PriceForUnit";
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle2.Format = "N0";
            dataGridViewCellStyle2.NullValue = null;
            this.priceForUnitDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle2;
            this.priceForUnitDataGridViewTextBoxColumn.HeaderText = "Giá Bán";
            this.priceForUnitDataGridViewTextBoxColumn.Name = "priceForUnitDataGridViewTextBoxColumn";
            // 
            // noteDataGridViewTextBoxColumn
            // 
            this.noteDataGridViewTextBoxColumn.DataPropertyName = "Note";
            this.noteDataGridViewTextBoxColumn.HeaderText = "Note";
            this.noteDataGridViewTextBoxColumn.Name = "noteDataGridViewTextBoxColumn";
            // 
            // orderDetailBindingSource
            // 
            this.orderDetailBindingSource.DataSource = typeof(QLBH.Models.OrderDetail);
            // 
            // IssuesManagement
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.tabIssues);
            this.Name = "IssuesManagement";
            this.Size = new System.Drawing.Size(759, 370);
            this.Load += new System.EventHandler(this.IssuesManagement_Load);
            this.tabIssues.ResumeLayout(false);
            this.tabCreateOrder.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.panel10.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.grdOrderDetail)).EndInit();
            this.panel1.ResumeLayout(false);
            this.groupBox1.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.panel3.ResumeLayout(false);
            this.panel3.PerformLayout();
            this.panel4.ResumeLayout(false);
            this.panel4.PerformLayout();
            this.panel5.ResumeLayout(false);
            this.panel5.PerformLayout();
            this.panel7.ResumeLayout(false);
            this.panel7.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtCustomerShipFee)).EndInit();
            this.panel8.ResumeLayout(false);
            this.panel8.PerformLayout();
            this.panel6.ResumeLayout(false);
            this.panel6.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.orderDetailBindingSource)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tabIssues;
        private System.Windows.Forms.TabPage tabCreateOrder;
        private System.Windows.Forms.TabPage tabManagerOrders;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.TextBox txtCustomerName;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel panel4;
        private System.Windows.Forms.TextBox txtCustomerPhone;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Panel panel5;
        private System.Windows.Forms.TextBox txtCustomerDeliveryAddress;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Panel panel7;
        private System.Windows.Forms.NumericUpDown txtCustomerShipFee;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Panel panel8;
        private System.Windows.Forms.DateTimePicker dtDateForSelling;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Panel panel6;
        private System.Windows.Forms.TextBox txtCustomerNote;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.CheckBox cbUpdateCustomerInfo;
        private System.Windows.Forms.LinkLabel linkGetCustomerInfo;
        private System.Windows.Forms.Panel panel10;
        private System.Windows.Forms.DataGridView grdOrderDetail;
        private System.Windows.Forms.Panel panel9;
        private System.Windows.Forms.BindingSource orderDetailBindingSource;
        private System.Windows.Forms.DataGridViewComboBoxColumn productIdDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn quantityDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn priceForUnitDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column1;
        private System.Windows.Forms.DataGridViewTextBoxColumn noteDataGridViewTextBoxColumn;
    }
}

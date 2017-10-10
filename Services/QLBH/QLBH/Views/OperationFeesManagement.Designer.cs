namespace QLBH.Views
{
    partial class OperationFeesManagement
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
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.panel2 = new System.Windows.Forms.Panel();
            this.panel1 = new System.Windows.Forms.Panel();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.panel4 = new System.Windows.Forms.Panel();
            this.txtFeeName = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.panel5 = new System.Windows.Forms.Panel();
            this.txtFeeNumber = new System.Windows.Forms.NumericUpDown();
            this.label2 = new System.Windows.Forms.Label();
            this.panel6 = new System.Windows.Forms.Panel();
            this.txtFeeNote = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.panel7 = new System.Windows.Forms.Panel();
            this.dtFeeDate = new System.Windows.Forms.DateTimePicker();
            this.label4 = new System.Windows.Forms.Label();
            this.panel3 = new System.Windows.Forms.Panel();
            this.btFeeSave = new System.Windows.Forms.Button();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.panel8 = new System.Windows.Forms.Panel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.panel9 = new System.Windows.Forms.Panel();
            this.panel10 = new System.Windows.Forms.Panel();
            this.panel11 = new System.Windows.Forms.Panel();
            this.panel12 = new System.Windows.Forms.Panel();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.txtViewName = new System.Windows.Forms.TextBox();
            this.dtViewMin = new System.Windows.Forms.DateTimePicker();
            this.dtViewMax = new System.Windows.Forms.DateTimePicker();
            this.cbbViewQuick = new System.Windows.Forms.ComboBox();
            this.btView = new System.Windows.Forms.Button();
            this.btCreateFee = new System.Windows.Forms.Button();
            this.btDeleteFee = new System.Windows.Forms.Button();
            this.grdOperationFees = new System.Windows.Forms.DataGridView();
            this.operationFeeIdDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.operationFeeNameDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.feeDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.noteDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.createdDateDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.operationFeeBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.groupBox1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.panel1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel4.SuspendLayout();
            this.panel5.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtFeeNumber)).BeginInit();
            this.panel6.SuspendLayout();
            this.panel7.SuspendLayout();
            this.panel3.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.panel8.SuspendLayout();
            this.tableLayoutPanel2.SuspendLayout();
            this.panel9.SuspendLayout();
            this.panel10.SuspendLayout();
            this.panel11.SuspendLayout();
            this.panel12.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grdOperationFees)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.operationFeeBindingSource)).BeginInit();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.panel2);
            this.groupBox1.Controls.Add(this.panel1);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(703, 334);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Quản Lý Chi Phí Vận Hành";
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.grdOperationFees);
            this.panel2.Controls.Add(this.groupBox3);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel2.Location = new System.Drawing.Point(3, 80);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(697, 251);
            this.panel2.TabIndex = 1;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.groupBox2);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(3, 16);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(697, 64);
            this.panel1.TabIndex = 0;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.tableLayoutPanel1);
            this.groupBox2.Controls.Add(this.panel3);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox2.Location = new System.Drawing.Point(0, 0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(697, 64);
            this.groupBox2.TabIndex = 0;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Chi Tiết Chi Phí";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 4;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.Controls.Add(this.panel4, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel5, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel6, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel7, 3, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(3, 16);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(597, 45);
            this.tableLayoutPanel1.TabIndex = 1;
            // 
            // panel4
            // 
            this.panel4.Controls.Add(this.txtFeeName);
            this.panel4.Controls.Add(this.label1);
            this.panel4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel4.Location = new System.Drawing.Point(3, 3);
            this.panel4.Name = "panel4";
            this.panel4.Size = new System.Drawing.Size(143, 39);
            this.panel4.TabIndex = 0;
            // 
            // txtFeeName
            // 
            this.txtFeeName.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtFeeName.Location = new System.Drawing.Point(0, 13);
            this.txtFeeName.Name = "txtFeeName";
            this.txtFeeName.Size = new System.Drawing.Size(143, 20);
            this.txtFeeName.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Top;
            this.label1.Location = new System.Drawing.Point(0, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(64, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Tên Chi Phí";
            // 
            // panel5
            // 
            this.panel5.Controls.Add(this.txtFeeNumber);
            this.panel5.Controls.Add(this.label2);
            this.panel5.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel5.Location = new System.Drawing.Point(152, 3);
            this.panel5.Name = "panel5";
            this.panel5.Size = new System.Drawing.Size(143, 39);
            this.panel5.TabIndex = 1;
            // 
            // txtFeeNumber
            // 
            this.txtFeeNumber.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtFeeNumber.Location = new System.Drawing.Point(0, 13);
            this.txtFeeNumber.Maximum = new decimal(new int[] {
            -1294967296,
            0,
            0,
            0});
            this.txtFeeNumber.Name = "txtFeeNumber";
            this.txtFeeNumber.Size = new System.Drawing.Size(143, 20);
            this.txtFeeNumber.TabIndex = 1;
            this.txtFeeNumber.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.txtFeeNumber.ThousandsSeparator = true;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Top;
            this.label2.Location = new System.Drawing.Point(0, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(42, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Chi Phí";
            // 
            // panel6
            // 
            this.panel6.Controls.Add(this.txtFeeNote);
            this.panel6.Controls.Add(this.label3);
            this.panel6.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel6.Location = new System.Drawing.Point(301, 3);
            this.panel6.Name = "panel6";
            this.panel6.Size = new System.Drawing.Size(143, 39);
            this.panel6.TabIndex = 2;
            // 
            // txtFeeNote
            // 
            this.txtFeeNote.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtFeeNote.Location = new System.Drawing.Point(0, 13);
            this.txtFeeNote.Name = "txtFeeNote";
            this.txtFeeNote.Size = new System.Drawing.Size(143, 20);
            this.txtFeeNote.TabIndex = 1;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Dock = System.Windows.Forms.DockStyle.Top;
            this.label3.Location = new System.Drawing.Point(0, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(45, 13);
            this.label3.TabIndex = 0;
            this.label3.Text = "Ghi Chú";
            // 
            // panel7
            // 
            this.panel7.Controls.Add(this.dtFeeDate);
            this.panel7.Controls.Add(this.label4);
            this.panel7.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel7.Location = new System.Drawing.Point(450, 3);
            this.panel7.Name = "panel7";
            this.panel7.Size = new System.Drawing.Size(144, 39);
            this.panel7.TabIndex = 3;
            // 
            // dtFeeDate
            // 
            this.dtFeeDate.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtFeeDate.Location = new System.Drawing.Point(0, 13);
            this.dtFeeDate.Name = "dtFeeDate";
            this.dtFeeDate.Size = new System.Drawing.Size(144, 20);
            this.dtFeeDate.TabIndex = 1;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Dock = System.Windows.Forms.DockStyle.Top;
            this.label4.Location = new System.Drawing.Point(0, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(50, 13);
            this.label4.TabIndex = 0;
            this.label4.Text = "Ngày Chi";
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.btFeeSave);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel3.Location = new System.Drawing.Point(600, 16);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(94, 45);
            this.panel3.TabIndex = 0;
            // 
            // btFeeSave
            // 
            this.btFeeSave.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btFeeSave.Location = new System.Drawing.Point(0, 0);
            this.btFeeSave.Name = "btFeeSave";
            this.btFeeSave.Size = new System.Drawing.Size(94, 45);
            this.btFeeSave.TabIndex = 0;
            this.btFeeSave.Text = "Lưu Chi Phí";
            this.btFeeSave.UseVisualStyleBackColor = true;
            this.btFeeSave.Click += new System.EventHandler(this.btFeeSave_Click);
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.tableLayoutPanel2);
            this.groupBox3.Controls.Add(this.panel8);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox3.Location = new System.Drawing.Point(0, 0);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(697, 69);
            this.groupBox3.TabIndex = 0;
            this.groupBox3.TabStop = false;
            // 
            // panel8
            // 
            this.panel8.Controls.Add(this.btDeleteFee);
            this.panel8.Controls.Add(this.btCreateFee);
            this.panel8.Controls.Add(this.btView);
            this.panel8.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel8.Location = new System.Drawing.Point(469, 16);
            this.panel8.Name = "panel8";
            this.panel8.Size = new System.Drawing.Size(225, 50);
            this.panel8.TabIndex = 0;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 4;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.Controls.Add(this.panel9, 0, 0);
            this.tableLayoutPanel2.Controls.Add(this.panel10, 1, 0);
            this.tableLayoutPanel2.Controls.Add(this.panel11, 2, 0);
            this.tableLayoutPanel2.Controls.Add(this.panel12, 3, 0);
            this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel2.Location = new System.Drawing.Point(3, 16);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 1;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(466, 50);
            this.tableLayoutPanel2.TabIndex = 1;
            // 
            // panel9
            // 
            this.panel9.Controls.Add(this.txtViewName);
            this.panel9.Controls.Add(this.label5);
            this.panel9.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel9.Location = new System.Drawing.Point(3, 3);
            this.panel9.Name = "panel9";
            this.panel9.Size = new System.Drawing.Size(110, 44);
            this.panel9.TabIndex = 0;
            // 
            // panel10
            // 
            this.panel10.Controls.Add(this.dtViewMin);
            this.panel10.Controls.Add(this.label6);
            this.panel10.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel10.Location = new System.Drawing.Point(119, 3);
            this.panel10.Name = "panel10";
            this.panel10.Size = new System.Drawing.Size(110, 44);
            this.panel10.TabIndex = 1;
            // 
            // panel11
            // 
            this.panel11.Controls.Add(this.dtViewMax);
            this.panel11.Controls.Add(this.label7);
            this.panel11.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel11.Location = new System.Drawing.Point(235, 3);
            this.panel11.Name = "panel11";
            this.panel11.Size = new System.Drawing.Size(110, 44);
            this.panel11.TabIndex = 2;
            // 
            // panel12
            // 
            this.panel12.Controls.Add(this.cbbViewQuick);
            this.panel12.Controls.Add(this.label8);
            this.panel12.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel12.Location = new System.Drawing.Point(351, 3);
            this.panel12.Name = "panel12";
            this.panel12.Size = new System.Drawing.Size(112, 44);
            this.panel12.TabIndex = 3;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Dock = System.Windows.Forms.DockStyle.Top;
            this.label5.Location = new System.Drawing.Point(0, 0);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(64, 13);
            this.label5.TabIndex = 0;
            this.label5.Text = "Tên Chi Phí";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Dock = System.Windows.Forms.DockStyle.Top;
            this.label6.Location = new System.Drawing.Point(0, 0);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(48, 13);
            this.label6.TabIndex = 0;
            this.label6.Text = "Từ Ngày";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Dock = System.Windows.Forms.DockStyle.Top;
            this.label7.Location = new System.Drawing.Point(0, 0);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(55, 13);
            this.label7.TabIndex = 0;
            this.label7.Text = "Đến Ngày";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Dock = System.Windows.Forms.DockStyle.Top;
            this.label8.Location = new System.Drawing.Point(0, 0);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(67, 13);
            this.label8.TabIndex = 0;
            this.label8.Text = "Chọn Nhanh";
            // 
            // txtViewName
            // 
            this.txtViewName.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtViewName.Location = new System.Drawing.Point(0, 13);
            this.txtViewName.Name = "txtViewName";
            this.txtViewName.Size = new System.Drawing.Size(110, 20);
            this.txtViewName.TabIndex = 1;
            // 
            // dtViewMin
            // 
            this.dtViewMin.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtViewMin.Location = new System.Drawing.Point(0, 13);
            this.dtViewMin.Name = "dtViewMin";
            this.dtViewMin.Size = new System.Drawing.Size(110, 20);
            this.dtViewMin.TabIndex = 1;
            // 
            // dtViewMax
            // 
            this.dtViewMax.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dtViewMax.Location = new System.Drawing.Point(0, 13);
            this.dtViewMax.Name = "dtViewMax";
            this.dtViewMax.Size = new System.Drawing.Size(110, 20);
            this.dtViewMax.TabIndex = 1;
            // 
            // cbbViewQuick
            // 
            this.cbbViewQuick.Dock = System.Windows.Forms.DockStyle.Fill;
            this.cbbViewQuick.FormattingEnabled = true;
            this.cbbViewQuick.Items.AddRange(new object[] {
            "Hôm Nay",
            "Hôm Qua",
            "Trong 7 Ngày",
            "Tuần Này",
            "Tuần Trước",
            "Tháng Này",
            "Tháng Trước",
            "Năm Này",
            "Năm Trước"});
            this.cbbViewQuick.Location = new System.Drawing.Point(0, 13);
            this.cbbViewQuick.Name = "cbbViewQuick";
            this.cbbViewQuick.Size = new System.Drawing.Size(112, 21);
            this.cbbViewQuick.TabIndex = 1;
            this.cbbViewQuick.SelectedIndexChanged += new System.EventHandler(this.cbbViewQuick_SelectedIndexChanged);
            // 
            // btView
            // 
            this.btView.Dock = System.Windows.Forms.DockStyle.Left;
            this.btView.Location = new System.Drawing.Point(0, 0);
            this.btView.Name = "btView";
            this.btView.Size = new System.Drawing.Size(69, 50);
            this.btView.TabIndex = 0;
            this.btView.Text = "Xem";
            this.btView.UseVisualStyleBackColor = true;
            this.btView.Click += new System.EventHandler(this.btView_Click);
            // 
            // btCreateFee
            // 
            this.btCreateFee.Dock = System.Windows.Forms.DockStyle.Left;
            this.btCreateFee.Location = new System.Drawing.Point(69, 0);
            this.btCreateFee.Name = "btCreateFee";
            this.btCreateFee.Size = new System.Drawing.Size(69, 50);
            this.btCreateFee.TabIndex = 1;
            this.btCreateFee.Text = "Tạo Mới";
            this.btCreateFee.UseVisualStyleBackColor = true;
            this.btCreateFee.Click += new System.EventHandler(this.btCreateFee_Click);
            // 
            // btDeleteFee
            // 
            this.btDeleteFee.Dock = System.Windows.Forms.DockStyle.Left;
            this.btDeleteFee.Location = new System.Drawing.Point(138, 0);
            this.btDeleteFee.Name = "btDeleteFee";
            this.btDeleteFee.Size = new System.Drawing.Size(68, 50);
            this.btDeleteFee.TabIndex = 2;
            this.btDeleteFee.Text = "Xóa";
            this.btDeleteFee.UseVisualStyleBackColor = true;
            this.btDeleteFee.Click += new System.EventHandler(this.btDeleteFee_Click);
            // 
            // grdOperationFees
            // 
            this.grdOperationFees.AllowUserToAddRows = false;
            this.grdOperationFees.AllowUserToDeleteRows = false;
            this.grdOperationFees.AutoGenerateColumns = false;
            this.grdOperationFees.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grdOperationFees.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.operationFeeIdDataGridViewTextBoxColumn,
            this.operationFeeNameDataGridViewTextBoxColumn,
            this.feeDataGridViewTextBoxColumn,
            this.noteDataGridViewTextBoxColumn,
            this.createdDateDataGridViewTextBoxColumn});
            this.grdOperationFees.DataSource = this.operationFeeBindingSource;
            this.grdOperationFees.Dock = System.Windows.Forms.DockStyle.Fill;
            this.grdOperationFees.Location = new System.Drawing.Point(0, 69);
            this.grdOperationFees.Name = "grdOperationFees";
            this.grdOperationFees.ReadOnly = true;
            this.grdOperationFees.Size = new System.Drawing.Size(697, 182);
            this.grdOperationFees.TabIndex = 1;
            this.grdOperationFees.CellDoubleClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.grdOperationFees_CellDoubleClick);
            // 
            // operationFeeIdDataGridViewTextBoxColumn
            // 
            this.operationFeeIdDataGridViewTextBoxColumn.DataPropertyName = "OperationFeeId";
            this.operationFeeIdDataGridViewTextBoxColumn.HeaderText = "Id";
            this.operationFeeIdDataGridViewTextBoxColumn.Name = "operationFeeIdDataGridViewTextBoxColumn";
            this.operationFeeIdDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // operationFeeNameDataGridViewTextBoxColumn
            // 
            this.operationFeeNameDataGridViewTextBoxColumn.DataPropertyName = "OperationFeeName";
            this.operationFeeNameDataGridViewTextBoxColumn.HeaderText = "Tên Chi Phí";
            this.operationFeeNameDataGridViewTextBoxColumn.Name = "operationFeeNameDataGridViewTextBoxColumn";
            this.operationFeeNameDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // feeDataGridViewTextBoxColumn
            // 
            this.feeDataGridViewTextBoxColumn.DataPropertyName = "Fee";
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleRight;
            dataGridViewCellStyle2.Format = "N0";
            dataGridViewCellStyle2.NullValue = null;
            this.feeDataGridViewTextBoxColumn.DefaultCellStyle = dataGridViewCellStyle2;
            this.feeDataGridViewTextBoxColumn.HeaderText = "Chi Phí";
            this.feeDataGridViewTextBoxColumn.Name = "feeDataGridViewTextBoxColumn";
            this.feeDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // noteDataGridViewTextBoxColumn
            // 
            this.noteDataGridViewTextBoxColumn.DataPropertyName = "Note";
            this.noteDataGridViewTextBoxColumn.HeaderText = "Ghi Chú";
            this.noteDataGridViewTextBoxColumn.Name = "noteDataGridViewTextBoxColumn";
            this.noteDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // createdDateDataGridViewTextBoxColumn
            // 
            this.createdDateDataGridViewTextBoxColumn.DataPropertyName = "CreatedDate";
            this.createdDateDataGridViewTextBoxColumn.HeaderText = "Ngày Chi";
            this.createdDateDataGridViewTextBoxColumn.Name = "createdDateDataGridViewTextBoxColumn";
            this.createdDateDataGridViewTextBoxColumn.ReadOnly = true;
            // 
            // operationFeeBindingSource
            // 
            this.operationFeeBindingSource.DataSource = typeof(QLBH.Models.OperationFee);
            // 
            // OperationFeesManagement
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox1);
            this.Name = "OperationFeesManagement";
            this.Size = new System.Drawing.Size(703, 334);
            this.Load += new System.EventHandler(this.OperationFeesManagement_Load);
            this.groupBox1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.panel4.ResumeLayout(false);
            this.panel4.PerformLayout();
            this.panel5.ResumeLayout(false);
            this.panel5.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.txtFeeNumber)).EndInit();
            this.panel6.ResumeLayout(false);
            this.panel6.PerformLayout();
            this.panel7.ResumeLayout(false);
            this.panel7.PerformLayout();
            this.panel3.ResumeLayout(false);
            this.groupBox3.ResumeLayout(false);
            this.panel8.ResumeLayout(false);
            this.tableLayoutPanel2.ResumeLayout(false);
            this.panel9.ResumeLayout(false);
            this.panel9.PerformLayout();
            this.panel10.ResumeLayout(false);
            this.panel10.PerformLayout();
            this.panel11.ResumeLayout(false);
            this.panel11.PerformLayout();
            this.panel12.ResumeLayout(false);
            this.panel12.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grdOperationFees)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.operationFeeBindingSource)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.Panel panel4;
        private System.Windows.Forms.TextBox txtFeeName;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel panel5;
        private System.Windows.Forms.NumericUpDown txtFeeNumber;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Panel panel6;
        private System.Windows.Forms.TextBox txtFeeNote;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Panel panel7;
        private System.Windows.Forms.DateTimePicker dtFeeDate;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btFeeSave;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private System.Windows.Forms.Panel panel8;
        private System.Windows.Forms.Panel panel9;
        private System.Windows.Forms.TextBox txtViewName;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Panel panel10;
        private System.Windows.Forms.DateTimePicker dtViewMin;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Panel panel11;
        private System.Windows.Forms.DateTimePicker dtViewMax;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Panel panel12;
        private System.Windows.Forms.ComboBox cbbViewQuick;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Button btDeleteFee;
        private System.Windows.Forms.Button btCreateFee;
        private System.Windows.Forms.Button btView;
        private System.Windows.Forms.DataGridView grdOperationFees;
        private System.Windows.Forms.DataGridViewTextBoxColumn operationFeeIdDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn operationFeeNameDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn feeDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn noteDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn createdDateDataGridViewTextBoxColumn;
        private System.Windows.Forms.BindingSource operationFeeBindingSource;
    }
}

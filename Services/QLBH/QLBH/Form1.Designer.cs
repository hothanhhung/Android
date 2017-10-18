namespace QLBH
{
    partial class Form1
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

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.btReceipt = new System.Windows.Forms.Button();
            this.btOrder = new System.Windows.Forms.Button();
            this.btProduct = new System.Windows.Forms.Button();
            this.btFeeToSell = new System.Windows.Forms.Button();
            this.btReport = new System.Windows.Forms.Button();
            this.btCustomers = new System.Windows.Forms.Button();
            this.pnlMain = new System.Windows.Forms.Panel();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.tableLayoutPanel1);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.pnlMain);
            this.splitContainer1.Size = new System.Drawing.Size(696, 404);
            this.splitContainer1.SplitterDistance = 131;
            this.splitContainer1.TabIndex = 0;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 1;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Controls.Add(this.btReceipt, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.btOrder, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.btProduct, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.btFeeToSell, 0, 3);
            this.tableLayoutPanel1.Controls.Add(this.btReport, 0, 4);
            this.tableLayoutPanel1.Controls.Add(this.btCustomers, 0, 5);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 7;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(131, 404);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // btReceipt
            // 
            this.btReceipt.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btReceipt.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btReceipt.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btReceipt.Location = new System.Drawing.Point(3, 3);
            this.btReceipt.Name = "btReceipt";
            this.btReceipt.Size = new System.Drawing.Size(125, 44);
            this.btReceipt.TabIndex = 0;
            this.btReceipt.Text = "Nhập Hàng";
            this.btReceipt.UseVisualStyleBackColor = true;
            this.btReceipt.Click += new System.EventHandler(this.btReceipt_Click);
            // 
            // btOrder
            // 
            this.btOrder.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btOrder.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btOrder.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btOrder.Location = new System.Drawing.Point(3, 53);
            this.btOrder.Name = "btOrder";
            this.btOrder.Size = new System.Drawing.Size(125, 44);
            this.btOrder.TabIndex = 2;
            this.btOrder.Text = "Đơn Hàng";
            this.btOrder.UseVisualStyleBackColor = true;
            this.btOrder.Click += new System.EventHandler(this.btOrder_Click);
            // 
            // btProduct
            // 
            this.btProduct.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btProduct.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btProduct.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btProduct.Location = new System.Drawing.Point(3, 103);
            this.btProduct.Name = "btProduct";
            this.btProduct.Size = new System.Drawing.Size(125, 44);
            this.btProduct.TabIndex = 3;
            this.btProduct.Text = "Sản Phẩm";
            this.btProduct.UseVisualStyleBackColor = true;
            this.btProduct.Click += new System.EventHandler(this.btProduct_Click);
            // 
            // btFeeToSell
            // 
            this.btFeeToSell.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btFeeToSell.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btFeeToSell.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btFeeToSell.Location = new System.Drawing.Point(3, 153);
            this.btFeeToSell.Name = "btFeeToSell";
            this.btFeeToSell.Size = new System.Drawing.Size(125, 44);
            this.btFeeToSell.TabIndex = 4;
            this.btFeeToSell.Text = "Chi Phí Bán Hàng";
            this.btFeeToSell.UseVisualStyleBackColor = true;
            this.btFeeToSell.Click += new System.EventHandler(this.btFeeToSell_Click);
            // 
            // btReport
            // 
            this.btReport.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btReport.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btReport.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btReport.Location = new System.Drawing.Point(3, 203);
            this.btReport.Name = "btReport";
            this.btReport.Size = new System.Drawing.Size(125, 44);
            this.btReport.TabIndex = 5;
            this.btReport.Text = "Báo Cáo Doanh Thu";
            this.btReport.UseVisualStyleBackColor = true;
            this.btReport.Click += new System.EventHandler(this.btReport_Click);
            // 
            // btCustomers
            // 
            this.btCustomers.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btCustomers.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.btCustomers.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btCustomers.Location = new System.Drawing.Point(3, 253);
            this.btCustomers.Name = "btCustomers";
            this.btCustomers.Size = new System.Drawing.Size(125, 44);
            this.btCustomers.TabIndex = 6;
            this.btCustomers.Text = "Khách Hàng";
            this.btCustomers.UseVisualStyleBackColor = true;
            this.btCustomers.Click += new System.EventHandler(this.btCustomers_Click);
            // 
            // pnlMain
            // 
            this.pnlMain.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pnlMain.Location = new System.Drawing.Point(0, 0);
            this.pnlMain.Name = "pnlMain";
            this.pnlMain.Size = new System.Drawing.Size(561, 404);
            this.pnlMain.TabIndex = 0;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(696, 404);
            this.Controls.Add(this.splitContainer1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = "Quản Lý Bán Hàng";
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Button btReceipt;
        private System.Windows.Forms.Button btOrder;
        private System.Windows.Forms.Button btProduct;
        private System.Windows.Forms.Button btFeeToSell;
        private System.Windows.Forms.Button btReport;
        private System.Windows.Forms.Panel pnlMain;
        private System.Windows.Forms.Button btCustomers;
    }
}


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
using QLBH.Commons;
using QLBH.Businesses;

namespace QLBH.Views
{
    public partial class OperationFeesManagement : UserControl
    {
        private List<OperationFee> OperationFees = null;
        public BindingSource OperationFeesBinding = new BindingSource();
        public OperationFee CurrentOperationFee = null;

        public OperationFeesManagement()
        {
            InitializeComponent();
        }

        private void cbbViewQuick_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateMinMaxDateView();
        }

        private void UpdateMinMaxDateView()
        {
            DateTime now = DateTime.Now;
            switch (cbbViewQuick.SelectedIndex + 1)
            {
                case 1:
                    dtViewMin.Value = now;
                    dtViewMax.Value = now;
                    break;
                case 2:
                    dtViewMin.Value = now.AddDays(-1);
                    dtViewMax.Value = now.AddDays(-1);
                    break;
                case 3:
                    dtViewMin.Value = now.AddDays(-7);
                    dtViewMax.Value = now;
                    break;
                case 4:
                    dtViewMin.Value = now.AddDays(-(int)now.DayOfWeek);
                    dtViewMax.Value = now;
                    break;
                case 5:
                    dtViewMin.Value = now.AddDays(-(int)now.DayOfWeek - 6);
                    dtViewMax.Value = dtViewMin.Value.AddDays(6);
                    break;
                case 6:
                    dtViewMin.Value = new DateTime(now.Year, now.Month, 1);
                    dtViewMax.Value = now;
                    break;
                case 7:
                    dtViewMin.Value = new DateTime(now.Year, now.Month, 1);
                    dtViewMax.Value = (new DateTime(now.Year, now.Month + 1, 1)).AddDays(-1);
                    break;
                case 8:
                    dtViewMin.Value = new DateTime(now.Year, 1, 1);
                    dtViewMax.Value = now;
                    break;
                case 9:
                    dtViewMin.Value = new DateTime(now.Year - 1, 1, 1);
                    dtViewMax.Value = new DateTime(now.Year - 1, 12, 31);
                    break;
            }
        }

        private void LoadOperationFees(bool isReload = false)
        {
            if (OperationFees == null || isReload)
            {
                string from = MethodHelpers.ConvertDateToCorrectString(dtViewMin.Value);
                string to = MethodHelpers.ConvertDateToCorrectString(dtViewMax.Value.AddDays(1));
                if (OperationFees == null)
                {
                    OperationFees = new List<OperationFee>();
                }
                OperationFees.Clear();
                OperationFees.AddRange(OperationFeeProcesser.GetOperationFees(txtViewName.Text.Trim(), from, to));
            }
            OperationFeesBinding.ResetBindings(true);

        }

        private void OperationFeesManagement_Load(object sender, EventArgs e)
        {
            cbbViewQuick.SelectedIndex = 2;
            UpdateMinMaxDateView();
            grdOperationFees.DataSource = OperationFeesBinding;
            LoadOperationFees(false);
            OperationFeesBinding.DataSource = OperationFees;
            LoadOperationFeeForUI(null);
        }

        private void btView_Click(object sender, EventArgs e)
        {
            LoadOperationFees(true);
        }

        private void btCreateFee_Click(object sender, EventArgs e)
        {
            LoadOperationFeeForUI(null);
        }

        private void btDeleteFee_Click(object sender, EventArgs e)
        {
            if (grdOperationFees.CurrentRow != null && grdOperationFees.CurrentRow.DataBoundItem != null)
            {
                var operationFee = (OperationFee)grdOperationFees.CurrentRow.DataBoundItem;
                if (MessageBox.Show(string.Format("Bạn Muốn Xóa Chi Phí: {0} #{1}?", operationFee.OperationFeeName, operationFee.OperationFeeId), "Quản Lý Chi Phí", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                {
                    if (OperationFeeProcesser.DeleteOperationFee(operationFee.OperationFeeId))
                    {
                        LoadOperationFees(true);
                    }
                    else
                    {
                        MessageBox.Show("Có Lỗi Khi Xóa", "Quản Lý Chi Phí", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
            }
        }

        private void btFeeSave_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtFeeName.Text))
            {
                MessageBox.Show("Nhập Tên Chi Phí", "Quản Lý Chi Phí", MessageBoxButtons.OK, MessageBoxIcon.Error);
                txtFeeName.Focus();
            }
            else
            {
                if (CurrentOperationFee == null)
                {
                    CurrentOperationFee = new OperationFee();
                }
                CurrentOperationFee.OperationFeeName = txtFeeName.Text.Trim();
                CurrentOperationFee.Fee = decimal.ToInt32(txtFeeNumber.Value);
                CurrentOperationFee.Note = txtFeeNote.Text.Trim();
                CurrentOperationFee.CreatedDate = MethodHelpers.ConvertDateTimeToCorrectString(dtFeeDate.Value);
                CurrentOperationFee = OperationFeeProcesser.SaveCustomer(CurrentOperationFee);
                if (CurrentOperationFee.OperationFeeId > 0)
                {
                    LoadOperationFees(true);
                }
                else
                {
                    MessageBox.Show("Có Lỗi Khi Lưu", "Quản Lý Chi Phí", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }

        private void LoadOperationFeeForUI( OperationFee operationFee)
        {
            CurrentOperationFee = operationFee;
            if (CurrentOperationFee != null)
            {
                txtFeeName.Text = CurrentOperationFee.OperationFeeName;
                txtFeeNumber.Value = CurrentOperationFee.Fee;
                txtFeeNote.Text = CurrentOperationFee.Note;
                dtFeeDate.Value = MethodHelpers.ConvertStringDateTimeToDateTime(CurrentOperationFee.CreatedDate);
                
            }
            else
            {
                CurrentOperationFee = new OperationFee();
                txtFeeName.Text = CurrentOperationFee.OperationFeeName;
                txtFeeNumber.Value = CurrentOperationFee.Fee;
                txtFeeNote.Text = CurrentOperationFee.Note;
                dtFeeDate.Value = DateTime.Now;
            }
        }

        private void grdOperationFees_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0 && grdOperationFees.CurrentRow != null && grdOperationFees.CurrentRow.DataBoundItem != null)
            {
                LoadOperationFeeForUI((OperationFee)grdOperationFees.CurrentRow.DataBoundItem);
            }
        }
    }
}

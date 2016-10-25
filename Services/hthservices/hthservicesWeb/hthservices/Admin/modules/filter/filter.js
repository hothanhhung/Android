'use strict';


function FilterController() {

    var ctrl = this;
    if (typeof (ctrl.fromDate) === 'undefined') {
        ctrl.fromDate = new Date();
    }
    if (typeof (ctrl.toDate) === 'undefined') {
        ctrl.toDate = new Date();
    }
    ctrl.QuickSearchTitle = "Customized";

    ctrl.ToDateMaxDate = new Date();

    ctrl.UpdateSearchDate = function (id) {
        switch (id) {
            case 1:
                ctrl.QuickSearchTitle = "7 Days";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(ctrl.toDate.getFullYear(), ctrl.toDate.getMonth(), ctrl.toDate.getDate() - 7);
                break;
            case 2:
                ctrl.QuickSearchTitle = "30 Days";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(ctrl.toDate.getFullYear(), ctrl.toDate.getMonth(), ctrl.toDate.getDate() - 30);
                break;
            case 3:
                ctrl.QuickSearchTitle = "Today";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date();
                break;
            case 4:
                ctrl.QuickSearchTitle = "Yesterday";
                var td = new Date();
                ctrl.toDate = new Date(td.getFullYear(), td.getMonth(), td.getDate() - 1);
                ctrl.fromDate = ctrl.toDate;
                break;
            case 5:
                ctrl.QuickSearchTitle = "This Week";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(ctrl.toDate.getFullYear(), ctrl.toDate.getMonth(), ctrl.toDate.getDate() - ctrl.toDate.getDay());
                break;
            case 6:
                ctrl.QuickSearchTitle = "This Month";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(ctrl.toDate.getFullYear(), ctrl.toDate.getMonth(), 1);
                break;
            case 7:
                ctrl.QuickSearchTitle = "This Year";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(ctrl.toDate.getFullYear(), 0, 1);
                break;
            case 8:
                ctrl.QuickSearchTitle = "All Time";
                ctrl.toDate = new Date();
                ctrl.fromDate = new Date(2015, 0, 1);
            default:
                ctrl.QuickSearchTitle = "Customized";
        }
    }

    ctrl.ApplyFilter = function ()
    {
        ctrl.onApply({fromDate: ctrl.fromDate, toDate: ctrl.toDate});
    }
};

hthServiceApp.component('filter', {
    templateUrl: '/admin/modules/filter/filter.tpl.html',
    controller: FilterController,
    bindings: {
        onApply: '&',
        fromDate: '=?',
        toDate: '=?'
    },
    bindToController: {
        onApply: '&',
        fromDate: '=?',
        toDate: '=?'
    }
});
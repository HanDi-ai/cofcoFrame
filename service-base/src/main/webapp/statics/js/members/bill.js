var lastrow = '';
var lastcell = '';
var ids = new Array();
var lastIrow = '';

$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/bill/list',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', width: 30, key: true,sortable: false, hidden: true},
            {label: '联盟仓单ID', name: 'wbillId', width: 85, align:'left',sortable: false, hidden: false},
            {label: '联盟成员ID', name: 'whMemberId', width: 30, sortable: false, hidden: true},
            {
                label: '仓单类型',
                name: 'wbillType',
                width: 30,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue == 0) {
                        return '联盟';
                    } else if (cellvalue == 1) {
                        return '非联盟';
                    } else {
                        return '其他';
                    }
                }
            },
            {
                label: '是否标准仓单',
                name: 'isStandard',
                width: 35,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue == 0) {
                        return '标准';
                    } else if (cellvalue == 1) {
                        return '非标准';
                    } else {
                        return '其他';
                    }
                }
            },
            {label: '仓库仓单编号', name: 'whWbillId', align:'right',width: 40, sortable: false},
            {label: '品种代码', name: 'varietyId', width: 60, sortable: false, hidden: true},
            {label: '品种名称', name: 'varietyName', width: 30, sortable: false},
            {
                label: '包装',
                name: 'packageType',
                width: 20,
                hidden:true,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue == 0) {
                        return '0';
                    } else if (cellvalue == 1) {
                        return '1';
                    } else if (cellvalue == 2) {
                        return '2';
                    }
                }
            },
            {label: '数量', name: 'counts', width: 25, align: 'right', sortable: false},
            {label: '重量Check', name: 'weightChk', width: 30, sortable: false, hidden: true},
            {
                label: '可质押重量',
                name: 'weight',
                width: 40,
                align: 'right',
                sortable: false,
                editable: true,
                edittype: 'text',
                editrules: {required: true, number: true},
                editoptions: {
                    dataEvents: [
                        {
                            type: 'blur', fn: function (e) {
                                var rex = "^[0-9]\\d{0,4}$";
                                var re = new RegExp(rex);
                                var res = re.test(this.value);
                                this.value = this.value.replace(/[^\d.]/g, "");
                                this.value = this.value.replace(/^\./g, "");
                                this.value = this.value.replace(/\.{2,}/g, "");
                                this.value = this.value.replace(".", "$#$".replace(/\./g, "").replace("$#$", "."));
                                this.value = this.value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3');
                                $('#jqGrid').jqGrid("saveCell", lastIrow, lastcell);
                            }
                        }
                    ]
                },
                cellattr: function (rowId, val, rawObject, cm, rdata) {
                    return "style='color:red'"
                }
            },
            {label: '是否标记', name: 'isMarked', width: 60, sortable: false, hidden: true},
            {label: '标记内容', name: 'markContent', width: 60, sortable: false, hidden: true},
            {label: '产地', name: 'productArea', width: 60, sortable: false, hidden: true},
            {label: '品质（先做一个品种）', name: 'quality', width: 60, sortable: false, hidden: true},
            {label: '检验检疫是否生效', name: 'isInspected', width: 60, sortable: false, hidden: true},
            {label: '检验机构名称', name: 'inspectionOrgName', width: 60, sortable: false, hidden: true},
            {label: '检验机构组织机构代码', name: 'inspectionOrgNo', width: 60, sortable: false, hidden: true},
            {label: '检验检疫证明', name: 'inspectionAttach', width: 60, sortable: false, hidden: true},
            {label: '检验检疫生效日期', name: 'inspectedBeginDate', width: 60, sortable: false, hidden: true},
            {label: '检验检疫失效日期', name: 'inspectedEndDate', width: 60, sortable: false, hidden: true},
            {label: '垛位信息', name: 'locationInfo', width: 60, sortable: false, hidden: true},
            {label: '仓储起始日期', name: 'storeBeginDate', width: 60, sortable: false, hidden: true},
            {label: '仓储终止日期', name: 'storeEndDate', width: 60, sortable: false, hidden: true},
            {label: '仓储费用', name: 'storageFee', width: 60, sortable: false, hidden: true},
            {label: '损耗标准', name: 'lossStandard', width: 60, hidden: true},
            {label: '仓储机构经办人姓名', name: 'operatorName', width: 60},
            {label: '仓储机构经办人电话', name: 'operatorTel', align:'right',width: 60},
            {label: '货主联盟客户代码', name: 'clientId', width: 60},
            {label: '货主联系人姓名', name: 'clientContactName', width: 50},
            {label: '货主联系人电话', name: 'clientContactTel', align:'right',width: 40},
            {label: '是否有保险', name: 'isInsurance', width: 60, hidden: true},
            {label: '保险公司名称', name: 'insuranceOrg', width: 60, hidden: true},
            {label: '保险公司组织机构代码', name: 'insuranceOrgNo', width: 60, hidden: true},
            {label: '保险公司联系人姓名', name: 'insuranceContact', width: 60, hidden: true},
            {label: '保险公司联系人电话', name: 'insuranceContactTel', width: 60, hidden: true},
            {label: '保险生效日期', name: 'insuranceBeginDate', width: 60, hidden: true},
            {label: '保险失效日期', name: 'insuranceEndDate', width: 60, hidden: true},
            {
                label: '库存状态', name: 'operatorType', width: 30,
                formatter: function (cellvalue, options, rowObject) {
                    if (rowObject.weight == '0') {
                        return '<span class="label label-danger">不可质押</span>';
                    } else {
                        return '<span class="label label-success">可质押</span>';
                    }
                }
            },
        ],
        beforeEditCell: function (rowid, cellname, v, iRow, iCol) {
            lastIrow = iRow;
            lastrow = rowid;
            lastcell = iCol;
        },
        afterSaveCell: function (rowid, name, val, iRow, iCol) {
            var tallyDetail = $("#jqGrid").getRowData(rowid);
            if (parseFloat(val) > parseFloat(tallyDetail.weightChk)) {
                $("#jqGrid").setCell(rowid, "weight", tallyDetail.weightChk);
                alertMsg("质押重量不能大于总重量，请重新录入!");
            }
        },
        cellEdit: true,
        cellsubmit: "clientArray",
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        sortname: 'id',
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        loadComplete: function () {
            var count = 0;
            var ids = $("#jqGrid").jqGrid('getRowData');
            var allCountID = $("#jqGrid").jqGrid('getDataIDs');
            ids.push($("#jqGrid").jqGrid('getRowData', allCountID[allCountID.length - 1]));
            for (var i = 0; i < ids.length; i++) {
                var rowData = ids[i];
                if (rowData.weight == '0') {
                    //$('#jqg_jqGrid_' + rowData.id).attr("disabled", "disabled");
                    $("#jqGrid").jqGrid('setCell', rowData.id, 'weight', '', 'not-editable-cell');
                    count++;
                }
            }
            if (count == ids.length) {
                //$('#cb_jqGrid').attr('disabled', 'disabled');
            }
        },
        onSelectAll: function (rowids, statue) {
            var ids = $("#jqGrid").jqGrid('getRowData');
            var allCountID = $("#jqGrid").jqGrid('getDataIDs');
            ids.push($("#jqGrid").jqGrid('getRowData', allCountID[allCountID.length - 1]));
            for (var i = 0; i < ids.length; i++) {
                var rowData = ids[i];
                if (rowData.weight == '0') {
                    $('#jqg_jqGrid_' + rowData.id).prop('checked', false);
                    $('#' + rowData.id).removeClass('success');
                    $('#' + rowData.id).attr('aria-selected', false);
                }
            }
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    doResize();
});
var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            id: '', //ID
            wbillId: '', //联盟仓单ID
            whMemberId: '', //联盟成员ID
            wbillType: '', //仓单类型
            isStandard: '', //是否标准仓单
            whWbillId: '', //仓库仓单编号
            varietyId: '', //品种代码
            varietyName: '', //品种名称
            counts: '', //数量
            weight: '', //重量
            operatorName: '', //仓储机构经办人姓名
            operatorTel: '', //仓储机构经办人电话
            clientId: '', //货主联盟客户代码
            clientContactName: '', //货主联系人姓名
            clientContactTel: '' //货主联系人电话
        },
        w: {
            dataList: []
        },
        showList: true,
        title: null,
        config: {}
    },
    methods: {
        // 仓单质押预冻结提交
        preFreezing: function (event) {
            debugger
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            for (var i = 0; i < ids.length; i++) {
                var rowData = $("#jqGrid").jqGrid('getRowData', ids[i]);
                vm.w.dataList[i] = rowData;
            }
            debugger
            var url = "../sys/bill/preFreezingSub";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.w.dataList),
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function(index){
                            vm.reload();
                        });
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
        },
        //仓单注销提交
        billCancel: function (event) {
            var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            if (ids.length == 0) {
                alert("请选择一条记录");
                return false;
            }
            for (var i = 0; i < ids.length; i++) {
                var rowData = $("#jqGrid").jqGrid('getRowData', ids[i]);
                vm.w.dataList[i] = rowData;
            }
            var url = "../sys/bill/billCancel";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.w.dataList),
                contentType: 'application/json',
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function(index){
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function (event) {
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                //postData:{'key': vm.q.key},
                page:page
            }).trigger("reloadGrid");
        }
    }
});
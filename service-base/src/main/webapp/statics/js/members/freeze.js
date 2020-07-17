var lastrow = '';
var lastcell = '';
var lastIrow = '';

$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/bill/queryPledgeList',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', width: 30, key: true, sortable: false, sortable: false, hidden: false},
            {
                label: '操作类型',
                name: 'operatorType',
                width: 120,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    //0:质押预冻结已提交，交易所审核中
                    if (cellvalue == '0') {
                        return '<span class="label label-primary">质押预冻结已提交，交易所审核中</span>';
                        //1：质押预冻结撤销已提交，交易所审核中
                    } else if (cellvalue == '1') {
                        return '<span class="label label-danger">质押预冻结撤销已提交，交易所审核中</span>';
                        //2:质押冻结已提交，交易所审核中
                    } else if (cellvalue == '2') {
                        return '<span class="label label-default">质押冻结已提交，交易所审核中</span>';
                        //5:质押预解质已提交，交易所审核中
                    } else if (cellvalue == '5') {
                        return '<span class="label label-success">质押预解质已提交，交易所审核中</span>';
                        //6:质押预解质撤销已提交，交易所审核中
                    } else if (cellvalue == '6') {
                        return '<span class="label label-danger">质押预解质撤销已提交，交易所审核中</span>';
                        //7:质押解质已提交，交易所审核中
                    } else if (cellvalue == '7') {
                        return '<span class="label label-info">质押解质已提交，交易所审核中</span>';
                        //13:质押预冻结中
                    } else if (cellvalue == '13') {
                        return '<span class="label label-primary">质押预冻结已提交，仓库锁货中</span>'
                        //15.质押预解质中
                    } else if (cellvalue == '15') {
                        return '<span class="label label-success">质押解质中</span>';
                        //16.质押预解质撤销中
                    } else if (cellvalue == '16') {
                        return '<span class="label label-success">质押预解质撤销中</span>';
                        //17.质押解质中
                    } else if (cellvalue == '17') {
                        return '<span class="label label-success">质押解质中</span>';
                        //18.预冻结成功
                    }else if (cellvalue == '18') {
                        return '<span class="label label-success">质押预冻结成功</span>';
                        //19.预冻结匹配失败，被交易所驳回
                    }else if (cellvalue == '19') {
                        return '<span class="label label-success">质押预冻结失败</span>';
                        //20.质押预冻结撤销成功
                    }else if (cellvalue == '20') {
                        return '<span class="label label-success">质押预冻结撤销成功</span>';
                        //21.质押预冻结撤销失败，被交易所驳回
                    }else if (cellvalue == '21') {
                        return '<span class="label label-success">质押预冻结撤销失败</span>';
                        //22.质押冻结成功
                    }else if (cellvalue == '22') {
                        return '<span class="label label-success">质押冻结成功</span>';
                        //23.质押冻结失败，被交易所驳回
                    }else if (cellvalue == '23') {
                        return '<span class="label label-success">质押冻结失败</span>';
                        //24.质押预解质成功
                    }else if (cellvalue == '24') {
                        return '<span class="label label-success">质押预解质成功</span>';
                        //25.质押预解质失败，被交易所驳回
                    }else if (cellvalue == '25') {
                        return '<span class="label label-success">质押预解质失败</span>';
                        //26.质押预解质撤销成功
                    }else if (cellvalue == '26') {
                        return '<span class="label label-success">质押预解质撤销成功</span>';
                        //27.质押预解质撤销失败，被交易所驳回
                    }else if (cellvalue == '27') {
                        return '<span class="label label-success">质押预解质撤销失败</span>';
                        //28.质押解质成功
                    }else if (cellvalue == '28') {
                        return '<span class="label label-success">质押解质成功</span>';
                        //29.质押解质失败，被交易所驳回
                    }else if (cellvalue == '29') {
                        return '<span class="label label-success">质押解质失败</span>';
                    }else{
                        return '';
                    }
                }
            },
            {label: '操作类型Code', name: 'operatorTypeCode', width: 60, sortable: false, hidden: true},
            {label: '流水号', name: 'virtualWbillId', width: 100, sortable: false},
            {label: '质押重量', name: 'weight', width: 50, sortable: false},
            {label: '出质人联盟成员ID', name: 'pledgerMemberId', width: 75, sortable: false},
            {
                label: '成员类型',
                name: 'memberType',
                width: 30,
                sortable: false,
                hidden:true,
                formatter: function (cellvalue, options, rowObject) {
                    //0：出质人
                    if (cellvalue == '0') {
                        return '出质人';
                        //1：质权人
                    } else if (cellvalue == '1') {
                        return '质权人';
                    }
                    return '';
                }
            },
            {label: '联盟客户ID', name: 'clientId', width: 55, sortable: false},
            {label: '质押合同ID', name: 'pledgerContractId', width: 55, align: 'right', sortable: false},
            {label: '经办人姓名', name: 'operatorName', width: 40, sortable: false},
            {label: '经办人联系电话', name: 'operatorTel', width: 70, sortable: false},
            {label: '经办人身份证号码', name: 'operatorId', width: 75, sortable: false},
            {label: '经办人身份证复印件', name: 'operatorIdAttach', width: 60, sortable: false, hidden: true},
            {label: '质押融资协议附件', name: 'pledgeContractAttach', width: 60, sortable: false, hidden: true},
            {label: '质押协议生效日期', name: 'pledgeBeginDate', width: 60, sortable: false},
            {label: '质押协议失效日期', name: 'pledgeEndDate', width: 60, sortable: false},
            {label: '仓单操作记录表ID', name: 'billOperatorId', width: 60, sortable: false, hidden: true},
            {label: '流水时间', name: 'timeCreate', width: 60, sortable: false,hidden:true},

        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
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
            wbillTmpId: '', //联盟仓单ID
            whMemberId: '', //联盟成员ID
            weight: '' //重量
        },
        w: {
            dataList: [],
        },
        showList: true,
        title: null,
        config: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        //仓单质押预冻结撤销提交
        revokePreFreezing: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            //vm.w.dataList = [];
            //var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            //for (var i = 0; i < ids.length; i++) {
            //var rowData = $("#jqGrid").jqGrid('getRowData', ids[i]);
            debugger
            var rowData = $("#jqGrid").jqGrid('getRowData', id);
            //if (rowData.operatorTypeCode == '0'||rowData.operatorTypeCode == '13') {
            if(true){
                var url = "../sys/bill/revokePreFreezingss";
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(rowData),
                    success: function (r) {
                        if (r.code === '0') {
                            alert(r, function (index) {
                                vm.reload();
                            });
                        } else {
                            alertMsg(r.msg);
                        }
                    }
                });
            }else{
                alertMsg("操作类型是质押预冻结或预冻结中的才可撤销");
                return false;
            }
            //vm.w.dataList[i] = rowData;
            //}

        },
        //仓单质押冻结提交
        freeze: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //vm.w.dataList = [];
            //var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            //for (var i = 0; i < ids.length; i++) {
            var rowData = $("#jqGrid").jqGrid('getRowData', id);
            /*if (rowData.operatorTypeCode != '0') {
                alertMsg("操作类型是质押预冻结的才可冻结");
                return false;
            }*/
            //vm.w.dataList[i] = rowData;
            //}
            var url = "../sys/bill/freezess";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(rowData),
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function (index) {
                            vm.reload();
                        });
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
        },
        //仓单质押预解质提交
        preUnfreeze: function (event) {
            debugger
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            //vm.w.dataList = [];
            //var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            //for (var i = 0; i < ids.length; i++) {
            var rowData = $("#jqGrid").jqGrid('getRowData', id);
            /*if(rowData.operatorTypeCode != '2'){
                alertMsg("操作类型是质押冻结的才可预解质");
               return false;
             }*/
            //vm.w.dataList[i] = rowData;
            //}
            var url = "../sys/bill/preUnjz";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(rowData),
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function (index) {
                            vm.reload();
                        });
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
        },
        //仓单质押预解质撤销提交
        preUnfreezeCancel: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            //vm.w.dataList = [];
            //var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            //for (var i = 0; i < ids.length; i++) {
            var rowData = $("#jqGrid").jqGrid('getRowData', id);
                /*if(rowData.operatorTypeCode != '5'){
                    alertMsg("操作类型是质押预解质的才可撤销");
                    return false;
                }*/
            //    vm.w.dataList[i] = rowData;
            //}
            var url = "../sys/bill/preUnfreCancel";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(rowData),
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        //仓单质押解质提交
        unfreeze: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            //vm.w.dataList = [];
            //var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            //for (var i = 0; i < ids.length; i++) {
            var rowData = $("#jqGrid").jqGrid('getRowData', id);
               /* if(rowData.operatorTypeCode != '5'){
                    alertMsg("操作类型是质押预解质的才可解质");
                    return false;
                }*/
            //    vm.w.dataList[i] = rowData;
            //}
            var url = "../sys/bill/unfre";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(rowData),
                success: function (r) {
                    if (r.code === '0') {
                        alert(r, function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function (event) {
            var page = 1;
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});
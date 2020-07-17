$(function () {
    $("#jqGrid").jqGrid({
        url: '../vlockmanageregist/list',
        datatype: "json",
        colModel: [
            {label: '锁编号', name: 'lockInitCode', index: 'LOCK_INIT_CODE', width: 60, key: true},
            {label: 'mac地址', name: 'lockInitMac', index: 'LOCK_INIT_MAC', width: 80},
            {label: '创建时间', name: 'createTime', index: 'CREATE_TIME', width: 110},
            {label: '创建人', name: 'createPerson', index: 'CREATE_PERSON', width: 70},
            {label: '老化测试次数', name: 'agingTestCount', index: 'AGING_TEST_COUNT', width: 80},
            {label: '开锁测试次数', name: 'operateCount', index: 'OPERATE_COUNT', width: 80},
            {label: '开锁失败次数', name: 'erroeCount', index: 'ERROE_COUNT', width: 80},
            {label: '获取状态总数', name: 'stateCount', index: 'STATE_COUNT', width: 80},
            {label: '获取状态失败', name: 'stateError', index: 'STATE_ERROR', width: 80},
            {
                label: '注册状态',
                name: 'registerState',
                index: 'REGISTER_STATE',
                width: 80,
                formatter: function (value, options, row) {
                    if (value == '0') {
                        return "未注册";
                    } else if (value == '1') {
                        return "已注册";
                    } else {
                        return "-";
                    }
                }
            },
            {label: '所属组织', name: 'orgName', index: 'ORG_NAME', width: 80}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
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

    layui.use('laydate', function () {
        var laydate = layui.laydate;

        //执行一个laydate实例
        laydate.render({
            elem: '#start_date', //指定元素
            choose: function (dates) {
                vm.q.startTime = dates; //函数回调,把选中的时间赋值给实体(基于vue)
            }
        });
        //执行一个laydate实例
        laydate.render({
            elem: '#end_date', //指定元素
            choose: function (dates) {
                vm.q.endTime = dates; //函数回调,把选中的时间赋值给实体(基于vue)
            }
        });

    });

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        vLockManageRegist: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.vLockManageRegist = {};
        },
        update: function (event) {
            var agingTestCount = getSelectedRow();
            if (agingTestCount == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(agingTestCount)
        },
        saveOrUpdate: function (event) {
            var url = vm.vLockManageRegist.agingTestCount == null ? "../vlockmanageregist/save" : "../vlockmanageregist/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.vLockManageRegist),
                success: function (r) {
                    if (r.code === 0) {
                        alert(r, function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var agingTestCounts = getSelectedRows();
            if (agingTestCounts == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../vlockmanageregist/delete",
                    data: JSON.stringify(agingTestCounts),
                    success: function (r) {
                        if (r.code == 0) {
                            alert(r, function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (agingTestCount) {
            $.get("../vlockmanageregist/info/" + agingTestCount, function (r) {
                if (r.code == 0) {
                    vm.vLockManageRegist = r.vLockManageRegist;
                } else {
                    alert(r.msg);
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page,
                postData: {
                    'sCode': $("#sCode").val(),
                    'eCode': $("#eCode").val(),
                    'stauts': $("#stauts").val(),
                    'startDate': $("#start_date").val(),
                    'endDate': $("#end_date").val(),
                    'filtration': $("#filtration").val(),
                    'organName': $("#organName").val()
                }
            }).trigger("reloadGrid");
        },
        reset: function (event) {
            vm.showList = true;
            $("#sCode").val('');
            $("#eCode").val('');
            $("#stauts").val('');
            $("#start_date").val('');
            $("#end_date").val('');
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page,
                postData: {
                    'sCode': $("#sCode").val(),
                    'eCode': $("#eCode").val(),
                    'stauts': $("#stauts").val(),
                    'startDate': $("#start_date").val(),
                    'endDate': $("#end_date").val()
                }
            }).trigger("reloadGrid");
        }
    }
});
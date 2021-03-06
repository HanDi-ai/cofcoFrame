$(function () {
    $("#jqGrid").jqGrid({
        url: '../vlockfail/list',
        postData: {
            'lockInitCode': parent.$("#hide_scode").val()
        },
        datatype: "json",
        colModel: [
            {label: '锁编号', name: 'lockInitCode', index: 'LOCK_INIT_CODE', width: 60},
            {label: '操作类型', name: 'oprType', index: 'OPR_TYPE', width: 60},
            {label: '操作结果', name: 'oprFlg', index: 'OPR_FLG', width: 60},
            {label: '创建时间', name: 'createTime', index: 'CREATE_TIME', width: 90}
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

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        vLockFail: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.vLockFail = {};
        },
        update: function (event) {
            var createTime = getSelectedRow();
            if (createTime == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(createTime)
        },
        saveOrUpdate: function (event) {
            var url = vm.vLockFail.createTime == null ? "../vlockfail/save" : "../vlockfail/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.vLockFail),
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
            var createTimes = getSelectedRows();
            if (createTimes == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../vlockfail/delete",
                    data: JSON.stringify(createTimes),
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
        getInfo: function (createTime) {
            $.get("../vlockfail/info/" + createTime, function (r) {
                if (r.code == 0) {
                    vm.vLockFail = r.vLockFail;
                } else {
                    alert(r.msg);
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});

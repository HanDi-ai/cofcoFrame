$(function () {
    if($("tr .jqg-third-row-header")){
        $(".ui-jqgrid .ui-jqgrid-htable th").css("padding","0 1px 0 3px");
    }
    $("#jqGrid").jqGrid({
        url: '../sys/bill/queryList',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'markContent', index: "markContent", width: 75,sortable: false},
            { label: '仓库仓单编号', name: 'whWbillId', index: "whWbillId", width: 75,sortable: false},
            { label: '品种代码', name: 'varietyId', index: "varietyId", width: 75,sortable: false},
            { label: '品种名称', name: 'varietyName', index: "varietyName", width: 75,sortable: false },
            { label: '是否包装', name: 'packageType', index: "packageType", width: 75,sortable: false },
            { label: '数量', name: 'counts',index:"counts", width: 100,sortable: false},
            { label: '重量', name: 'weight', index: "weight", width: 80,sortable: false},
            { label: '客户ID',name: 'clientId',index:"clientId",width:80,sortable: false}
        ],
        viewrecords: true,
        height:'auto',
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount",
            id:"page.rn"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
    doResize();
});

//日期时间选择器
layui.use('laydate', function() {
    var laydate = layui.laydate;
    //检验检疫生效日期
    laydate.render({
        elem: '#inspectedBeginDate',
        done: function (value) {
            vm.role.inspectedBeginDate =value
        }
    });
    //检验检疫失效日期
    laydate.render({
        elem: '#inspectedEndDate',
        done: function (value) {
            vm.role.inspectedEndDate =value
        }
    });
    //仓储起始日期
    laydate.render({
        elem: '#storeBeginDate',
        done: function (value) {
            vm.role.storeBeginDate =value
        }
    });
    //仓储终止日期
    laydate.render({
        elem: '#storeEndDate',
        done: function (value) {
            vm.role.storeEndDate =value
        }
    });
    //保险生效日期
    laydate.render({
        elem: '#insuranceBeginDate',
        done: function (value) {
            vm.role.insuranceBeginDate =value
        }
    });
    //保险失效日期
    laydate.render({
        elem: '#insuranceEndDate',
        done: function (value) {
            vm.role.insuranceEndDate =value
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            organId:null,
            roleName: null,
            organName: null,
            varietyName: null
        },
        showList: true,
        title:null,
        role:{
            organName:'',
            organId:'',
            inspectedBeginDate:'',
            inspectedEndDate:'',
            storeBeginDate:'',
            storeEndDate:'',
            insuranceBeginDate:'',
            insuranceEndDate:''
        }

    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.role = {
                status:0,
                remark:'',
                name:'',
                organName:''};
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.role = {};
            vm.showList = false;
            vm.title = "修改";
            var cId = $("#jqGrid").jqGrid('getGridParam','selrow');
            var clientRowData = $("#jqGrid").jqGrid("getRowData",cId);
            var id = clientRowData.markContent;
            debugger;
            vm.getRole(id);
        },
        getRole: function(id){
            $.get("../sys/bill/info/"+id, function(r){
                vm.role = r.role;
            });
        },
        saveOrUpdate: function (event) {
            //var url = vm.role.id == null ? "../sys/role/save" : "../sys/bill/update";
            /*vm.role.inspectedBeginDate=$("#inspectedBeginDate").val();
            vm.role.inspectedEndDate=$("#inspectedEndDate").val();

            vm.role.storeBeginDate=$("#storeBeginDate").val();
            vm.role.storeEndDate=$("#storeEndDate").val();

            vm.role.insuranceBeginDate=$("#insuranceBeginDate").val();
            vm.role.insuranceEndDate=$("#insuranceEndDate").val();*/
            var url ="../sys/bill/save";
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: JSON.stringify(vm.role),
                            success: function(r){
                                vm.reload();
                            }
                        });
        },
        reload: function (event) {
            vm.showList = true;
            var page =1;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'varietyName': vm.q.varietyName},
                page:page
            }).trigger("reloadGrid");
        }
    }
});
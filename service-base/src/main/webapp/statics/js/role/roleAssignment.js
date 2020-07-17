$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/role/list',
        datatype: "json",
        colModel: [
            { label: '角色名称', name: 'name', index: "name", width: 75 ,sortable: false},
            { label: '组织机构', name: 'organName', index: "organName", width: 75 ,sortable: false},
            { label: '备注', name: 'remark', width: 100 ,sortable: false},
            { label: '创建时间', name: 'createTime', index: "create_time", width: 80}
        ],
        viewrecords: true,
        height:'auto',
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        onSelectRow: function(ids) { //单击选择行
            var id= $("#jqGrid").jqGrid('getGridParam','selrow');
            var rowDatas = $("#jqGrid").jqGrid('getRowData', id);
            var roleName=rowDatas.name;
            var organName=rowDatas.organName;
            $("#role_id").val(id);//页面隐藏域赋值
            $("#roleName_div").text('当前角色：  【'+organName+'-'+roleName+'】');//页面展示用名字
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },
    });
    doResize("",144);
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            organId:null,
            organName: null,
            roleName: null,
            userNameYes:null,
            userNameNo:null
        },
        showList: true,
        title:null,
        rowId:'',
        role:{
            id:'',
            userList:[]
        }
    },
    created:function(){
        initTree();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        queryYes: function () {
            vm.reload1();
        },
        queryNo: function () {
            vm.reload2();
        },

        update: function () {
            var id = $("#role_id").val();
            if(id == null|| id ==''){
                alertMsg("请选择角色");
                return ;
            }
            vm.role.id=id;
            vm.showList = false;
            vm.title = "角色分配";
            vm.getUser();
            vm.reload1();
            vm.reload2();
        },
        getUser:function(){
            $("#jqGrid1").jqGrid({
                url: '../sys/roleAssignment/infoYes',
                datatype: "json",
                postData:{"id":vm.role.id},
                colModel: [

                    { label: '用户名', name: 'userName', width: 155 ,sortable: false},
                    { label: '登陆名', name: 'loginName', width: 155 ,sortable: false},
                    { label: '组织机构', name: 'bapName', index: "bapName", width: 165 ,sortable: false}

                ],
                rowNum: 10,
                rownumWidth: 25,
                autowidth:true,
                multiselect: true,
                pager: "#jqGridPager1",
                jsonReader : {
                    root: "page.list",
                    page: "page.currPage",
                    total: "page.totalPage",
                    records: "page.totalCount"
                },
                prmNames : {
                    page:"page",
                    rows:"limit",
                    order: "order"
                },
                gridComplete:function(){
                    var wid = $("#wid").width();
                    $("#jqGrid1").setGridWidth(wid);
                    //隐藏grid底部滚动条
                    $("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                },
                ondblClickRow: function(rowId){
                    vm.rowId=rowId;
                    vm.yesToNo();
                },
            });
            $("#jqGrid2").jqGrid({
                url: '../sys/roleAssignment/infoNo',
                datatype: "json",
                postData:{"id":vm.role.id},
                colModel: [

                    { label: '用户名', name: 'userName', width: 155 ,sortable: false},
                    { label: '登陆名', name: 'loginName', width: 155 ,sortable: false},
                    { label: '组织机构', name: 'bapName', index: "bapName", width: 165 ,sortable: false}
                ],
                rowNum: 10,
                rownumWidth: 25,
                autowidth:true,
                multiselect: true,
                pager: "#jqGridPager2",
                jsonReader : {
                    root: "page.list",
                    page: "page.currPage",
                    total: "page.totalPage",
                    records: "page.totalCount"
                },
                prmNames : {
                    page:"page",
                    rows:"limit",
                    order: "order"
                },
                gridComplete:function(){
                    var wid = $("#wid").width();
                    $("#jqGrid2").setGridWidth(wid);
                    //隐藏grid底部滚动条
                    $("#jqGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                },
                ondblClickRow: function(rowId){
                    vm.rowId=rowId;
                    vm.noToYes();
                }
            });
            doResize('jqGrid1','300');
            doResize('jqGrid2','300');
        },
        yesToNo: function(){
            if(''==vm.rowId || null==vm.rowId){
                vm.role.userList = vm.getSelectedRows1();
                if(null == vm.role.userList || '' == vm.role.userList){
                    alert("请选择左侧用户（需要删除此角色的用户）");
                    return ;
                }
            }else{
                vm.role.userList.length=0;
                vm.role.userList.push(vm.rowId);
                vm.rowId='';
            }
            $.ajax({
                type: "POST",
                url: "../sys/roleAssignment/updateYes",
                data: JSON.stringify(vm.role),
                success: function(r){
                    if(r.code == 0){
                        toast(r, function(index){
                            vm.reload1();
                            vm.reload2();
                        });
                    }else{
                        alertMsg(r.msg);
                    }
                }
            });
        },
        noToYes: function(){
            if(''==vm.rowId || null==vm.rowId){
                vm.role.userList = vm.getSelectedRows2();
                if(null == vm.role.userList || '' == vm.role.userList){
                    alert("请选择右侧用户（需要增加此角色的用户）");
                    return ;
                }
            }else{
                vm.role.userList.length=0;
                vm.role.userList.push(vm.rowId);
                vm.rowId='';
            }
            $.ajax({
                type: "POST",
                url: "../sys/roleAssignment/updateNo",
                data: JSON.stringify(vm.role),
                success: function(r){
                    if(r.code == 0){
                        toast(r, function(index){
                            vm.reload1();
                            vm.reload2();
                        });
                    }else{
                        alertMsg(r.msg);
                    }
                }
            });
        },

        //选择多条记录
        getSelectedRows1: function() {
            var grid = $("#jqGrid1");
            var rowKey = grid.getGridParam("selrow");
            return grid.getGridParam("selarrrow");
        },
        getSelectedRows2: function() {
            var grid = $("#jqGrid2");
            var rowKey = grid.getGridParam("selrow");
            return grid.getGridParam("selarrrow");
        },

        organTree: function(){
            initTree();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择机构",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = organTree.getSelectedNodes();
                    //选择上级菜单
                    vm.role.organId = node[0].id;
                    vm.role.organName = node[0].name;
                    vm.q.organId = node[0].id;
                    vm.q.organName = node[0].name;
                    layer.close(index);
                    vm.reloadTree();
                }
            });
        },
        clear:function (event) {
            vm.role.organId = null;
            vm.role.organName = null;
            vm.q.organId = null;
            vm.q.organName = null;
            vm.reloadTree();
        },
        reload: function (event) {
            vm.showList = true;
            var page =1;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'organId': vm.q.organId,'roleName': vm.q.roleName},
                page:page
            }).trigger("reloadGrid");
        },
        reload1: function (event) {
            vm.showList = false;
            var page = 1;
            $("#jqGrid1").jqGrid('setGridParam',{
                postData:{'userName': vm.q.userNameYes,'id': vm.role.id},
                page:page
            }).trigger("reloadGrid");
        },
        reload2: function (event) {
            vm.showList = false;
            var page = 1;
            $("#jqGrid2").jqGrid('setGridParam',{
                postData:{'userName': vm.q.userNameNo,'id': vm.role.id},
                page:page
            }).trigger("reloadGrid");
        },
        reloadTree: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'organId': vm.q.organId},
                page:page
            }).trigger("reloadGrid");
        }
    }
});

var organTree ="";
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};

/**
 * 初始化机构树
 */
function initTree() {
    $.get("../sys/organ/organTree", function(r){
        //初始化zTree，三个参数一次分别是容器(zTree 的容器 className 别忘了设置为 "ztree")、参数配置、数据源
        organTree = $.fn.zTree.init($("#organTree"), setting, r.organTree);
        var node = organTree.getNodeByParam("id", vm.role.menu_id);//得到当前上级菜单节点
        organTree.selectNode(node);//选中新增加的节点
        //vm.menu.parentName = node.name;
    })
}
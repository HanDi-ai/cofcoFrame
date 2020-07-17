$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/user/enableList',
        datatype: "json",
        colModel: [
			{ label: '用户名', name: 'userName', width: 75,sortable: false},
			{ label: '机构', name: 'bapName', width: 75,sortable: false},
			{ label: '手机号', name: 'phone', width: 100 },
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
        },onSelectRow: function(ids) { //单击选择行
           var id= $("#jqGrid").jqGrid('getGridParam','selrow');
           var rowDatas = $("#jqGrid").jqGrid('getRowData', id);
           var username=rowDatas.userName;
           $("#user_id").val(id);//页面隐藏域赋值
           $("#username_div").text('当前用户：  【'+username+'】');//页面展示用名字
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    doResize();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			userName: null,
            roleNameL: null,
            roleNameR: null
		},
		showList: true,
		title:null,
		roleList:{},
		user:{
            id:'',
			status:1,
			baid:'',
			baName:'',
			roleIdList:[]
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        queryL: function () {

            vm.reload1();
        },
        queryR: function () {
            vm.reload2();
        },
		add: function(){
            var id = $("#user_id").val();
            if(id == null|| id ==''){
                alertMsg("请选择用户");
                return ;
            }
            vm.user.id=id;
			vm.showList = false;
			vm.title = "角色授予";
			vm.getUser();
			vm.reload1();
			vm.reload2();
		},
        getUser:function(){
            $("#jqGrid1").jqGrid({
                url: '../sys/userGrantRoles/notinlist',
                datatype: "json",
                postData:{'userId':vm.user.id},
                colNames : [ '组织机构', '角色名称', '创建时间'],//jqGrid的列显示名字
                colModel: [
                    {name: 'organName', index: "organName"},
                    {name: 'name', index: "name"},
                    {name: 'createTime', index: "create_time",}
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
                ondblClickRow:function(rowId){

                    var id= rowId;
                    var roleIdList = new Array();
                    roleIdList.push(id);
                    vm.user.roleIdList=roleIdList;
                    var sign = 1;
                    vm.branch(sign);

                },
                gridComplete:function(){
                    var wid = $("#wid").width();
                    $("#jqGrid1").setGridWidth(wid);
                    console.log(wid);
                    //隐藏grid底部滚动条
                    $("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
            });
            $("#jqGrid2").jqGrid({
                url: '../sys/userGrantRoles/list',
                datatype: "json",
                postData:{'userId':vm.user.id},
                colNames : [ '组织机构', '角色名称', '创建时间'],//jqGrid的列显示名字
                colModel: [
                    {name: 'organName', index: "organName"},
                    {name: 'name', index: "name"},
                    {name: 'createTime', index: "create_time",}
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
                ondblClickRow:function(rowId){
                    var id= rowId;
                    var roleIdList = new Array();
                    roleIdList.push(id);
                    vm.user.roleIdList=roleIdList;
                    var sign = 1;
                    vm.relieve(sign);

                },
                gridComplete:function(){
                    var wid = $("#wid").width();
                    $("#jqGrid2").setGridWidth(wid);
                    //隐藏grid底部滚动条
                    $("#jqGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
            });
            doResize('jqGrid1','300');
            doResize('jqGrid2','300');
        },
        relieve: function (sign) {
			if(sign!=1){
                vm.rightList();
                var ids = vm.getSelectedRows2();
                if(ids == null || ids==''){
                    alertMsg("请选择已分配角色");
                    return false;
                }
                var roleIdList = new Array();
                for(var i=0; i<ids.length; i++) {
                    roleIdList.push(ids[i]);
                }
                vm.user.roleIdList=roleIdList;
            }

			confirm('确定要取消该用户角色？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/userGrantRoles/relieve",
				    data: JSON.stringify(vm.user),
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
			});
		},branch: function (sign) {
            if(sign!=1){
                vm.leftList();
                var ids = vm.getSelectedRows1();
                if(ids == null || ids==''){
                    alertMsg("请选择未分配角色");
                    return false;
                }
                var roleIdList = new Array();
                for(var i=0; i<ids.length; i++) {
                    roleIdList.push(ids[i]);
                }
                vm.user.roleIdList=roleIdList;
            }

			$.ajax({
				type: "POST",
			    url:  "../sys/userGrantRoles/distribution",
			    data: JSON.stringify(vm.user),
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
        leftList: function(){
            var grid = $("#jqGrid1");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alertMsg("请选择未分配角色");
                return false;
            }

            return grid.getGridParam("selarrrow");
        },
        rightList: function(){
            var grid = $("#jqGrid2");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alertMsg("请选择已分配角色");
                return false;
            }

            return grid.getGridParam("selarrrow");
        },
        getSelectedRows1: function() {
            var grid = $("#jqGrid1");
            var rowKey = grid.getGridParam("selrow");
            return grid.getGridParam("selarrrow");
        },
        getSelectedRows2: function() {
            var grid = $("#jqGrid2");
            var rowKey = grid.getGridParam("selrow");
            return grid.getGridParam("selarrrow");
        },reload1: function () {
            vm.showList = false;
            var page = $("#jqGrid1").jqGrid('getGridParam','page');
            $("#jqGrid1").jqGrid('setGridParam',{
                postData:{'roleName': vm.q.roleNameL,'userId':vm.user.id},
                page:page
            }).trigger("reloadGrid");
        },reload2: function () {
            vm.showList = false;
            var page = $("#jqGrid2").jqGrid('getGridParam','page');
            $("#jqGrid2").jqGrid('setGridParam',{
                postData:{'roleName': vm.q.roleNameR,'userId':vm.user.id},
                page:page
            }).trigger("reloadGrid");
        },reload: function () {
            vm.showList = true;
            var page = 1;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'userName': vm.q.userName},
                page:page
            }).trigger("reloadGrid");
        }
	}
});
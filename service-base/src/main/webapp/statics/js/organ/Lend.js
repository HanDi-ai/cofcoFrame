$(function () {

    $("#jqGrid1").jqGrid({
        url: '../sys/lend/newlist',
        datatype: "json",
        postData:{'new_organ_Id':vm.q.new_organ_Id},
        colModel: [
            { label: '用户名', name: 'userName', width: 155,sortable: false  },
            { label: '登陆名', name: 'loginName', width: 155,sortable: false  },
            { label: '组织机构', name: 'bapName',  width: 165,sortable: false }

        ],
        height: 320,
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
            console.log(id);
            var roleIdList = new Array();
            roleIdList.push(id);
            vm.user.roleIdList=roleIdList;
            var sign = 1;
            vm.branch(sign);

        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
    $("#jqGrid2").jqGrid({
        url: '../sys/lend/list',
        datatype: "json",
        postData:{'old_organ_Id':vm.q.old_organ_Id},
        colModel: [
            { label: '用户名', name: 'userName', width: 155,sortable: false  },
            { label: '登陆名', name: 'loginName', width: 155,sortable: false  },
            { label: '组织机构', name: 'bapName',  width: 165,sortable: false }
        ],
        height: 320,
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
            //隐藏grid底部滚动条
            $("#jqGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
    doResize('jqGrid1','250');
    doResize('jqGrid2','250');
});
var organTreeL ="";
var organTreeR ="";
/**
 * 初始化机构树
 */
function initTree() {
    $.get("../sys/organ/organTree", function(r){
        //初始化zTree，三个参数一次分别是容器(zTree 的容器 className 别忘了设置为 "ztree")、参数配置、数据源

        organTreeR = $.fn.zTree.init($("#organTreeR"), settingR, r.organTree);

    })

    $.get("../sys/organ/organTree", function(r){
        //初始化借调机构树，为当前登录所在机构下所有权限

        organTreeL = $.fn.zTree.init($("#organTreeL"), settingL, r.organTree);

    })

}
var settingR = {
    view: {
        dblClickExpand: false
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        }
    }
};
var settingL = {
    view: {
        dblClickExpand: false
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        }
    }
};
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
            new_organ_Name: null,
            old_organ_Name: null,
            new_organ_Id: null,
            old_organ_Id: null
		},
		showList: true,
		title:null,
		roleList:{},
		lend:{
            new_organ_Id:'',
            old_organ_Id:'',
			userIdList:[]
		}
	},
    created:function(){
        initTree();
    },
	methods: {
        queryL: function () {
            vm.reloadL();
        },
        queryR: function () {
            vm.reloadR();
        },
		relieve: function (sign) {
			if(sign!=1){
                var new_organ_Id=$("#organ_left_id").val();
                vm.lend.new_organ_Id=new_organ_Id;
               // vm.rightList();
                var ids = vm.getSelectedRows1();
                if(ids == null || ids==''){
                    alertMsg("请选择已分配角色");
                    return false;
                }
                var userIdList = new Array();
                for(var i=0; i<ids.length; i++) {
                    userIdList.push(ids[i]);
                }
                vm.lend.userIdList=userIdList;
            }

			confirm('确定要取消该用户角色？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/lend/relieve",
				    data: JSON.stringify(vm.lend),
				    success: function(r){
                        if(r.code == 0){
                            toast(r, function(index){
                                vm.reloadL();
                                vm.reloadR();
                            });
                        }else{
                            alertMsg(r.msg);
                        }
					}
				});
			});
		},
        Toloan: function (sign) {
            if(sign!=1){
              //  vm.rightList();
                var old_organ_Id=$("#organ_right_id").val();
                var new_organ_Id=$("#organ_left_id").val();
                vm.lend.new_organ_Id=new_organ_Id;
                vm.lend.old_organ_Id=old_organ_Id;
                if(old_organ_Id == null || old_organ_Id==''){
                    alertMsg("还未选择现机构");
                    return false;
                }
                if(new_organ_Id == null || new_organ_Id==''){
                    alertMsg("还未选择借调机构");
                    return false;
                }
                var ids = vm.getSelectedRows2();
                if(ids == null || ids==''){
                    alertMsg("还未选择借调人员");
                    return false;
                }
                var userIdList = new Array();
                for(var i=0; i<ids.length; i++) {
                    userIdList.push(ids[i]);
                }
                vm.lend.userIdList=userIdList;
            }

			$.ajax({
				type: "POST",
			    url:  "../sys/lend/toloan",
			    data: JSON.stringify(vm.lend),
			    success: function(r){
                    if(r.code == 0){
                        toast(r, function(index){
                            vm.reloadL();
                            vm.reloadR();
                        });
                    }else{
                        alertMsg(r.msg);
                    }
				}

			});
		},organTreeleft: function(){
            initTree();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择借调机构",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#menuLayerL"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = organTreeL.getSelectedNodes();
                    //选择上级菜单
                    vm.q.old_organ_Name = node[0].id;
                    vm.q.old_organ_Name = node[0].name;
                    $("#organ_left_id").val(node[0].id);
                    $("#organ_left_name").val(node[0].name);
                    layer.close(index);
                }
            });
        },
        organTreeRight: function(){
            initTree();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择原机构",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#menuLayerR"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = organTreeR.getSelectedNodes();
                    //选择上级菜单
                    vm.q.new_organ_Name = node[0].id;
                    vm.q.new_organ_Name = node[0].name;
                    $("#organ_right_id").val(node[0].id);
                    $("#organ_right_name").val(node[0].name);
                    layer.close(index);
                }
            });
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
        },reload: function (event) {
            vm.showList = false;
            var page = $("#jqGrid").jqGrid('getGridPara','page');
            $("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
        },reloadL: function (event) {
            var new_organ_Id=$("#organ_left_id").val();
            var page = $("#jqGrid1").jqGrid('getGridParam','page');
            $("#jqGrid1").jqGrid('setGridParam',{
                postData:{'new_organ_Id': new_organ_Id},
                page:page
            }).trigger("reloadGrid");
        },reloadR: function (event) {
            var old_organ_Id=$("#organ_right_id").val();
            var page = $("#jqGrid2").jqGrid('getGridParam','page');
            $("#jqGrid2").jqGrid('setGridParam',{
                postData:{'old_organ_Id':old_organ_Id},
                page:page
            }).trigger("reloadGrid");
        }
	}
});














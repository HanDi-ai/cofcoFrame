$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/role/list',
        datatype: "json",
        colModel: [
			{ label: '角色名称', name: 'name', index: "name", width: 75,sortable: false },
            { label: '组织机构', name: 'organName', index: "organName", width: 75,sortable: false },
            { label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
                return value == 0 ?
                    '<span class="label label-success">正常</span>' :
                    '<span class="label label-danger">禁用</span>';
            }},
			{ label: '备注', name: 'remark', width: 100,sortable: false},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 80,sortable: false}
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
            records: "page.totalCount"
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
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};
var setting1 = {
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
        organTree = $.fn.zTree.init($("#organTree"), setting1, r.organTree);
        var node = organTree.getNodeByParam("id", vm.role.menu_id);//得到当前上级菜单节点
        organTree.selectNode(node);//选中新增加的节点
        //vm.menu.parentName = node.name;
    })
}
var ztree;
var ztree2;

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
            organId:null,
            roleName: null,
            organName: null
		},
		showList: true,
		title:null,
		role:{
            appmenuIdList:'',
            menuIdList:'',
            organName:'',
            organId:''
		}

	},
    created:function(){
        initTree();
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
			vm.getMenuTree(null);
		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
            vm.role = {};
			vm.showList = false;
            vm.title = "修改";
            vm.getMenuTree(id);
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/role/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
                            toast(r.msg,function(){
                                vm.reload();
                            });
						}else{
                            alertMsg(r.msg);
						}
					}
				});
			});
		},
        enabled: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/role/enabled",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
                            toast(r.msg,function(){
                                vm.reload();
                            });
						}else{
                            alertMsg(r.msg);
						}
					}
				});
			});
		},
		getRole: function(id){
            $.get("../sys/role/info/"+id, function(r){
            	vm.role = r.role;//勾选角色所拥有的菜单
    			var menuIds = vm.role.menuIdList;
    			var appmenuIds= vm.role.appmenuIdList;
    			for(var i=0; i<menuIds.length; i++) {
                    var node = ztree.getNodeByParam("id", menuIds[i]);
                    ztree.checkNode(node, true, false);

    			}
                for(var i=0; i<appmenuIds.length; i++) {
                    var node2 = ztree2.getNodeByParam("id", appmenuIds[i]);
                    ztree2.checkNode(node2, true, false);
                }

    		});
		},
		saveOrUpdate: function (event) {
		    if(vm.role.organName==''){
                alertMsg('请选择所属机构');
                return false;
            }
            if(vm.role.name==''){
                alertMsg('请填写角色名称');
                return false;
            }
            var urluser ="../sys/role/checkrole";
            var url = vm.role.id == null ? "../sys/role/save" : "../sys/role/update";
            $.ajax({
                type: "POST",
                url: urluser,
                data: JSON.stringify(vm.role),
                success: function(r){
                    if (r.total > 0) {
                        alertMsg("本机构已经存在该角色,请修改后注册");
                        return false;
                    }else{
                        //获取选择的菜单
                        var nodes = ztree.getCheckedNodes(true);
                        var nodes2 = ztree2.getCheckedNodes(true);
                        var menuIdList = new Array();
                        var appmenuIdList = new Array();
                        for(var i=0; i<nodes.length; i++) {
                            menuIdList.push(nodes[i].id);
                        }
                        for(var i=0; i<nodes2.length; i++) {
                            appmenuIdList.push(nodes2[i].id);
                        }
                        vm.role.menuIdList = menuIdList;
                        vm.role.appmenuIdList = appmenuIdList;
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: JSON.stringify(vm.role),
                            success: function(r){
                                vm.q.organId = '';
                                vm.q.organName = '';
                                if(r.code == 0){
                                    if(vm.role.id==null){
                                        alert(r,function (index,layer) {
                                            vm.reload();
                                        },function (index) {
                                            vm.role.name='';
                                            vm.role.remark='';
                                        })
                                    }else{
                                        toast(r.msg,function(){
                                            vm.reload();
                                        });
                                    }

                                }else{
                                    alertMsg(r.msg);
                                }
                            }
                        });

                    }

             }
            });
		},
		getMenuTree: function(id) {
			var sign='1';
			//加载菜单树
			$.get("../sys/menu/perms/"+sign,function(r){
				 ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				//展开所有节点
                var nodes = ztree.getNodes();
                // for (var i = 0; i < nodes.length; i++) { //设置节点展开
                //     ztree.expandNode(nodes[i], true, false, true);
                // }

				if(id != null){
					vm.getRole(id);
				}
			});
            var sign='0';
            //加载菜单树
            $.get("../sys/menu/perms/"+sign,function(r){
                ztree2 = $.fn.zTree.init($("#appmenuTree"), setting, r.menuList);
                //展开所有节点
                var nodes = ztree2.getNodes();
                // for (var i = 0; i < nodes.length; i++) { //设置节点展开
                //     ztree2.expandNode(nodes[i], true, false, true);
                // }
                if(id != null){
                    vm.getRole(id);
                }
            });

	    },organTree: function(){
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
                }
            });
        },
        clear:function (event) {
            vm.role.organId = null;
            vm.role.organName = null;
            vm.q.organId = null;
            vm.q.organName = null;
        },
	    reload: function (event) {
	    	vm.showList = true;
			var page =1;
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'organId': vm.q.organId,'roleName': vm.q.roleName},
                page:page
            }).trigger("reloadGrid");
		}
	}
});
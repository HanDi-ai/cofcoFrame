$(function () {
    $("#jqGrid").jqGrid({
        url: '../vlockexception/list',
        datatype: "json",
        colModel: [
            { label: '锁具编号', name: 'lockCode', index: 'LOCK_CODE', width: 80 },
            { label: '锁具物理地址', name: 'lockMac', index: 'LOCK_MAC', width: 80 },
            { label: '异常信息', name: 'exceptionCon', index: 'EXCEPTION_CON', width: 80 },
            { label: '创建时间', name: 'createTime', index: 'CREATE_TIME', width: 80 },
            { label: '组织机构', name: 'orgPath', index: 'ORG_PATH', width: 80 },
            { label: '创建人姓名', name: 'userName', index: 'USER_NAME', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		vLockException: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.vLockException = {};
		},
		update: function (event) {
			var delFlag = getSelectedRow();
			if(delFlag == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(delFlag)
		},
		saveOrUpdate: function (event) {
			var url = vm.vLockException.delFlag == null ? "../vlockexception/save" : "../vlockexception/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.vLockException),
			    success: function(r){
			    	if(r.code === 0){
						alert(r, function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var delFlags = getSelectedRows();
			if(delFlags == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../vlockexception/delete",
				    data: JSON.stringify(delFlags),
				    success: function(r){
						if(r.code == 0){
							alert(r, function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(delFlag){
			$.get("../vlockexception/info/"+delFlag, function(r){
                if(r.code == 0){
                    vm.vLockException = r.vLockException;
                }else{
                    alert(r.msg);
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData: {
                    'sCode': $("#sCode").val(),
                    'eCode': $("#eCode").val(),
                    'startDate': $("#start_date").val(),
                    'endDate': $("#end_date").val()
                }
            }).trigger("reloadGrid");
		},
        reset: function (event) {
            vm.showList = true;
            $("#sCode").val('');
            $("#eCode").val('');
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
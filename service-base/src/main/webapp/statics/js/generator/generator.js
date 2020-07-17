$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/generator/list',
        datatype: "json",
        colModel: [
            { label: '表名', name: 'tableName', width: 100, key: true },
            { label: 'Engine', name: 'engine', width: 70},
            { label: '表备注', name: 'comments', width: 100 },
            // { label: '表备注', name: 'tableComment', width: 100 },
            { label: '创建时间', name: 'createTime', width: 100 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50,100,200],
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
});

$(function () {
    $("#jqGrid1").jqGrid({
        url: '../sys/generator/view',
        datatype: "json",
        colModel: [
            { label: '表名', name: 'tableName', width: 100, key: true },
            { label: 'Engine', name: 'engine', width: 70},
            { label: '表备注', name: 'comments', width: 100 },
            // { label: '表备注', name: 'tableComment', width: 100 },
            { label: '创建时间', name: 'createTime', width: 100 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50,100,200],
        rownumbers: true,
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
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        showList: true,
        title:null,
		q:{
			tableName: null
		}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'tableName': vm.q.tableName},
                page:1 
            }).trigger("reloadGrid");
		},
		generator: function() {
			var tableNames = getSelectedRows();
			if(tableNames == null){
				return ;
			}
			location.href = "../sys/generator/code?tables=" + JSON.stringify(tableNames);
		},
        generator1: function() {
            var grid = $("#jqGrid1");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条记录");
                return ;
            }
            var tableNames = grid.getGridParam("selarrrow");
            if(tableNames == null){
                return ;
            }
            location.href = "../sys/generator/code?tables=" + JSON.stringify(tableNames);
        },
        conversion:function(value){
		    if(value == 1){
                vm.showList=false;
                vm.title="视图列表";
            }else{
                vm.showList=true;
                vm.title="表列表";
            }
        }
	}
});


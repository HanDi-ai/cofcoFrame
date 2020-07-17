$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/log/list',
        datatype: "json",
        postData:{'sign': vm.q.sign},
        colModel: [			
			// { label: 'id', name: 'id', width: 30, key: true },
			{ label: '用户名', name: 'username', width: 50 }, 			
			{ label: '用户操作', name: 'operation', width: 70 }, 			
			{ label: '请求方法', name: 'method', width: 150 }, 			
			{ label: '请求参数', name: 'params', width: 80 },
            { label: '返回值', name: 'result', width: 80 },
            { label: '类型', name: 'sign', width: 20, formatter: function(value, options, row){
                    return value == 0 ?
                        '<span class="label label-danger">applog</span>' :
                        '<span class="label label-success">weblog</span>'
                }},
			{ label: '创建时间', name: 'createDate', width: 90 }			
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
    doResize();
});

//日期时间选择器
layui.use('laydate', function() {
    var laydate = layui.laydate;
    laydate.render({
        elem: '#start'
        , type: 'datetime'
    });
    laydate.render({
        elem: '#end'
        , type: 'datetime'
    });
});
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null,
            sign:1,
            starttime: null,
            startend: null
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
		    vm.q.starttime=$("#start").val();
		    vm.q.startend=$("#end").val();
            vm.q.sign=$("#selectsign option:selected").val();
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'key': vm.q.key,'starttime':vm.q.starttime,'startend':vm.q.startend,'sign':vm.q.sign},
                page:page
            }).trigger("reloadGrid");
		}
	}
});
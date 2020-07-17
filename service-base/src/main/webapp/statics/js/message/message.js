
window.onload = function(){
    auto_height('weizhi',180);
    auto_panel_height(90);
};
$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/message/list',
        datatype: "json",
        colModel: [
            { label: '消息内容', name: 'messageContent', index: "messageContent", width: 120,sortable: false },
            { label: '消息类型', name: 'messageType', index: "messageType", width: 75,sortable: false },
            { label: '消息等级', name: 'messageLevel', index: "messageLevel", width: 55,sortable: false },
            { label: '位置名称', name: 'locationName', index: "locationName", width: 75,sortable: false },
            { label: '消息状态', name: 'messageState', width: 50, formatter: function(value, options, row){
                    return value == 0 ?
                        '<span class="label label-danger">未读</span>' :
                        '<span class="label label-success">已读</span>';
                }},
            { label: '是否处理', name: 'isDispose', width: 50, formatter: function(value, options, row){
                    return value == 0 ?
                        '<span class="label label-danger">未处理</span>' :
                        '<span class="label label-success">已处理</span>';
                }},
            { label: '处理原因', name: 'disposeRemark', index: "disposeRemark", width: 75,sortable: false },
            { label: '创建时间', name: 'createTime', index: "createTime", width: 90,sortable: false},
            { label: '操作', name: '', index: "operate", width: 140,
                formatter: function (cellvalue, options, rowObject) {
                    var messageId = rowObject['messageId'];
                    var isDispose = rowObject['isDispose'];
                    if(isDispose==1){
                        var detail="<button class='layui-btn layui-btn-small' onclick='alertMsg(\"此条报警消息已处理\")' type='button'><i class=\"layui-icon\">&#xe60a;</i>处 理</button><button class='layui-btn layui-btn-small' onclick='readMessage(\""+messageId+"\")' type='button'><i class=\"layui-icon\">&#xe604;</i>查 阅</button>";
                        return detail;
                    }else{
                        var detail="<button class='layui-btn layui-btn-small' onclick='dispose(\""+messageId+"\")' type='button'><i class=\"layui-icon\">&#xe60a;</i>处 理</button><button class='layui-btn layui-btn-small' onclick='readMessage(\""+messageId+"\")' type='button'><i class=\"layui-icon\">&#xe604;</i>查 阅</button>";
                        return detail;
                    }
                    }}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        //multiselect: true,
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
function dispose(id) {
    if (id!=null) {
        vm.messageId=id;
        var messageId=vm.messageId;
    }
    var html='<div class="form-body" style="padding: 25px;">';
    html+=' <div style="min-height: 50px;"><div class="form-group"><div class="col-sm-2 control-label">处理原因：</div><div class="col-sm-10"><textarea class="form-control" v-model="message.disposeRemark" id="disposeRemark" name="disposeRemark"></textarea></div></div></div>';
    html+='</div>';
    layer.open({
        scrollbar: false,
        type: 1,
        title : ["处理报警消息" , true],
        area: ['50%', '43%'], //宽高
        content: html,
        shadeClose : false,
        btn: ['确定'],
        btn1: function (index) {
            vm.message.messageId=vm.messageId;
            vm.message.disposeRemark=document.getElementById("disposeRemark").value;
            var url = "../sys/message/dispose";
            var messageId=vm.messageId;
            var disposeRemark=document.getElementById("disposeRemark").value;
            var url="../sys/message/dispose/"+messageId+"/"+disposeRemark;
            $.get(url,function (r) {
                    var page = $("#jqGrid").jqGrid('getGridParam','page');
                    $("#jqGrid").jqGrid('setGridParam',{
                        postData:{},
                        page:page
                    }).trigger("reloadGrid");
                    alert(r.msg);
                    layer.close(index);
            });

        }
    });
}
function readMessage(id) {
    if (id!=null) {
        vm.messageId=id;
        var messageId=vm.messageId;
    }
    $.get("../sys/message/readMessage/"+messageId, function(r){
        var html='<div class="form-body" style="padding: 25px;">';
        html+=' <div style="min-height: 50px;">'+r.message.messageContent+'</div>';
        html+=' <p style="text-align: right;margin-top: 50px;">查阅时间：'+r.message.operationTime+'</p>';
        html+='</div>';
        layer.open({
            scrollbar: false,
            type: 1,
            title : ["查阅报警消息" , true],
            area: ['50%', '50%'], //宽高
            content: html,
            shadeClose : false,
        });
        var page = $("#jqGrid").jqGrid('getGridParam','page');
        $("#jqGrid").jqGrid('setGridParam',{
            postData:{},
            page:page
        }).trigger("reloadGrid");
    });
}
var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            messageLevel: '',
            isDispose:''
        },
        items:{},
        selected:"消息等级",
        showList: true,
        title:null,
        messageId:null,
        messageLevel:{},
        messageType:{},
        message:{
            messageId:'',
            disposeRemark:''
        }
    },
    created:function(){
        //initTree();
        var url ="../sys/message/code";
        $.post(url,function (r) {
            if(r.code == 0){
                vm.items=r.codeTree;
            }else {
                alertMsg(r.msg)
            }
        });
    },
    methods: {
        queryMesLevel: function () {
            vm.reloadMesLevel();
        },
        queryIsDispose: function () {
            vm.reloadIsDispose();
        },
        reloadIsDispose: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'isDispose': vm.q.isDispose},
                page:page
            }).trigger("reloadGrid");
        },
        dispose: function (id) {
            if (id!=null) {
                vm.messageId=id;
                var messageId=vm.messageId;
            }
            if (IsChecked(vm.messageId)) {
                $.get("../sys/message/dispose/"+messageId, function(r){
                    alert(r.msg);
                });
            }
        },
        reloadMesLevel: function (event) {
            var messageLevel=vm.q.messageLevel;
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'messageLevel': messageLevel},
                page:page
            }).trigger("reloadGrid");
        }
    }
});
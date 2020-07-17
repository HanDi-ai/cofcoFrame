window.onload = function(){
    auto_height('weizhi',110);
};
function jqGrid(bapid,userName) {
    $("#jqGrid").jqGrid({
        url: '../sys/user/enableList',
        datatype: "json",
        postData:{'organId':bapid,'userName':userName},
        colModel: [
            { label: '用户名', name: 'userName', width: 155,sortable: false  },
            { label: '组织机构', name: 'bapName', index: "bapName", width: 165,sortable: false }
        ],
        viewrecords: true,
        height:'auto',
        rowNum: 10,
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
        onSelectRow:
            function(rowId){
                vm.q.id=rowId;
                vm.userLocInfo(rowId);
            },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
    doResize('jqGrid',250);
}
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1,
        },
        key: {
            url:"nourl"
        }
    },
    check:{
        //enable:true,
        nocheckInherit:true
    },
    callback: {
        //onClick: zTreeOnClick
    }
};
var settinglocation = {
    view: {
        dblClickExpand: false
    },
    typeSkins:{
        folder:"diy",
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "locationId",
            pIdKey: "fLocationId",
            rootPId: -1
        },
        key: {
            url:"nourl",
            nameArray:["locationName","locationType"],
            nameFormat:"%s（%s）",
            title:"locationName",
            icon:""
        }
    },
    check:{
        enable:true,
        nocheckInherit:true
    },
    callback: {
        //onClick: zTreelocOnClick
    }
};
/**
 *  获取zTree 节点下的所有节点
 * @param treeNode
 * @param childs
 */
function getAllNodes(treeNode, childs) {
    var childNodes = treeNode.children;
    if(childNodes){
        for (var i=0;i<childNodes.length;i++){
            childs+="," + childNodes[i].locationId;
            if(childNodes){
                childs=getAllNodes(childNodes[i],childs);
            }
        }
    }
    return childs;
}
/**
 * 登陆者组织机构
 */
function initOrgan() {
    $.get("../sys/location/initOrgan", function(r){
        if(r.userorgan){
            vm.q.bName=r.userorgan.SIMPLIFY_NAME;
            vm.loc= true;
            var bapid=r.userorgan.ID;
            var wfpName=$("#yfpName").val();
            jqGrid(bapid,wfpName);
            vm.q.scode=r.userorgan.CODE;
            initTreeloc(r.userorgan.CODE);
            vm.userLocInfo(vm.q.id);
            vm.locationIdList.scode=r.userorgan.CODE;
        }else{
            jqGrid('','');
        }
    });
}
var ztree;
/**
 * 初始化组织机构
 */
function initTree() {
    $.get("../sys/organ/organTree", function(r){
        ztree =$.fn.zTree.init($("#allorganTree"), setting, r.organTree);
        var nodes = ztree.getNodes();
        for (var i = 0; i < nodes.length; i++) { //设置节点展开
            ztree.expandNode(nodes[i], true, false, true);
        }
    });
}
var ztreeloc;
/**
 * 初始化位置信息
 */
function initTreeloc(scode) {
    vm.q.scode=scode;
    //加载位置信息树
    $.get("../sys/location/locTree/"+scode, function(r){
        ztreeloc = $.fn.zTree.init($("#locationTree"), settinglocation, r.locationTree);
        var nodes = ztreeloc.getNodes();
        for (var i = 0; i < nodes.length; i++) { //设置节点展开
            ztreeloc.expandNode(nodes[i], true, false, true);
        }
    });
    vm.userLocInfo(vm.q.id);
}
var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            scode:null,
            userName: null,
            locationId: null,
            locationName: null,
            bapid: null,
            bName:null,
            id:null
        },
        locationIdList:{
            id:'',
            scode:'',
            locationIdList:[]
        },
        showList: true,
        showInfo: false,
        loc: false,
        title: null,
        organ:{
            parentName:null,
            parentId:0,
            sort:'0',
            code:"",
            name:"",
            remark:"",
            type:"1",
            open:"1",
            name:""
        },
    },
    created:function(){
        initTree();
        initOrgan();
    },
    methods: {
        userLocInfo:function(rowId){
            var id= rowId;
            vm.locationIdList.id=id;
            var scode=vm.q.scode;
            if(rowId==''||rowId==null){
                id=null;
            }
            var url="../sys/location/userLocInfo/"+id+"/"+scode;
            $.get(url,function (result) {
                ztreeloc.checkAllNodes(false);
                for(var i=0; i<result.locationIds.length; i++) {
                    node = ztreeloc.getNodeByParam("locationId", result.locationIds[i].locationId);
                    ztreeloc.cancelSelectedNode();//先取消所有的选中状态
                    ztreeloc.selectNode(node,true);
                    ztreeloc.checkNode(node, true, false);
                    ztreeloc.expandAll(true);
                }
            })
        },
        queryL: function () {
            vm.reloadL();
        },
        reloadL: function (event) {
            //initOrgan();
            var locationId=$("#locationId").val();
            var yfpName=$("#yfpName").val();
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'locationId': locationId,'userName':yfpName},
                page:page
            }).trigger("reloadGrid");
        },
        showMenuL: function(){
            initTree();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择组织",
                area: ['300px', '420px'],
                shade: 0,
                shadeClose: false,
                content: $("#menuLayerL"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    vm.loc= true;
                    var zTree = $.fn.zTree.getZTreeObj("allorganTree"),
                        nodes = zTree.getSelectedNodes(),
                        v = "";
                    layer.close(index);
                    var wfpName=$("#yfpName").val();
                    var page = $("#jqGrid").jqGrid('getGridParam','page');
                    $("#jqGrid").jqGrid('setGridParam',{
                        postData:{'organId':nodes[0].id,'userName':wfpName},
                        page:page
                    }).trigger("reloadGrid");
                    initTreeloc(nodes[0].code);
                    vm.q.bapid=nodes[0].id;
                    $("#organId").val(nodes[0].id);
                    $("#organName").val(nodes[0].name);
                    vm.q.bName=nodes[0].name;
                    vm.locationIdList.scode=nodes[0].code;
                }
            });
        },
        save: function (event) {
            if(vm.locationIdList.id==''||vm.locationIdList.id==null){
                alertMsg('请选择用户');
                return false;
            }
            //获取选择的菜单
            var nodes = ztreeloc.getCheckedNodes(true);
            var locationIdList = new Array();
            for(var i=0; i<nodes.length; i++) {
                locationIdList.push(nodes[i].locationId);
            }
            vm.locationIdList.locationIdList = locationIdList;
            if(vm.locationIdList.locationIdList==null||vm.locationIdList.locationIdList==''){
                alertMsg('请选择位置');
                return false;
            }
            var url ="../sys/location/toloan";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.locationIdList),
                success: function(r){
                    if(r.code == 0){
                        toast(r, function(index){
                            vm.reloadL();
                        });
                    }else{
                        alertMsg(r.msg);
                    }
                }
            });
        }
    }
});
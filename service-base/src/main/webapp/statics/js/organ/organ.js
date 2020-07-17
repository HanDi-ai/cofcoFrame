$(function(){
    vm.showInfo=true;
});
window.onload = function(){
    auto_height('ztree_organ',160);
    auto_panel_height(100);
};
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
        // enable:true,
        nocheckInherit:true
    },
    callback: {
        onClick: zTreeOnClick
    }
};
/**
 * zTree点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnClick(event, treeId, treeNode) {
    var url="../sys/organ/info/"+treeNode.id;
    $.get(url,function (result) {
        vm.organ=result.organ;
        var longAndlat=result.organ.longitude;
        var nodes=ztree.getSelectedNodes();
        var parentId=nodes[0].bapid;
        document.getElementById("parentName").value ="";
        if(parentId =='0'){
            document.getElementById("parentName").value ="根节点"
        }else{
            var parentname=treeNode.getParentNode().name;
            document.getElementById("parentName").value = parentname;
        }
        if(longAndlat!=null){
            document.getElementById("lonlatCopy").value = result.organ.latitude+ "," + result.organ.longitude;
        }else{
            document.getElementById("lonlatCopy").value = "";
        }
        vm.showInfo=true;
        vm.title='修改组织';
    })
};
var ztree;

/**
 * 初始化菜单
 */
function initTree() {
    //加载菜单树
    $.get("../sys/organ/organTree", function(r){
        ztree = $.fn.zTree.init($("#allorganTree"), setting, r.organTree);
        //展开所有节点
        // ztree.expandAll(true);
    });
}

var vm = new Vue({
    el:'#rrapp',
    data:{
        showInfo: false,
        title: null,
        organ:{
            parentId:0,
            sort:'0',
            code:"",
            name:"",
            remark:"",
            type:"1",
            open:"1",
            fullName:""
        },
    },
    created:function(){
        initTree();
    },
    methods: {
        add: function(){
            document.getElementById("lonlatCopy").value = "";
            var nodes=ztree.getSelectedNodes();
            if(nodes.length<=0){
                alertMsg("请选择组织机构！");
            }else{
                vm.organ.parentId=nodes[0].id;
                document.getElementById("parentName").value = nodes[0].name;
            }
            vm.showInfo = true;
            vm.title = "新增组织";
            vm.organ.name="";
            vm.organ.fullName="";
            vm.organ.sort="";
            vm.organ.code="";
            vm.organ.remark="";
            vm.organ.type="1";
            vm.organ.id = null;
            vm.organ.longitude="" ;
            vm.organ.latitude="";
        },
        del: function (event) {
            var nodes=ztree.getSelectedNodes();
            if(nodes.length<=0){
                alertMsg("请选择要删除的组织");
            }
            confirm('确定要删除【'+nodes[0].name+'】，及下面的组织么？', function(){
                var url="../sys/organ/delete";
                var childs=nodes[0].id;
                childs=getAllNodes(nodes[0],childs);
                $.post(url,JSON.stringify(childs),function (r) {
                    if(r.code == '-1'){
                        alertMsg("删除失败！请先删除当前机构下的用户、角色、及子节点");
                    }else{
                        toast(r.msg,function(){
                            ztree.removeNode(nodes[0]);
                        });
                    }
                });
            });
        },
        controlOpen: function(lonlat){
            //window.open('map.html');
            var l=(screen.availWidth-500)/2;
            var t=(screen.availHeight-520)/2;
            window.open('map.html','container','width=615,height=560,top='+t+',left='+l+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');


        },
        saveOrUpdate: function (event) {
            var parentName=$("#parentName").val();
            if(parentName==""){
                alertMsg("请在左侧选择组织机构！");
            }else{
                $.ajax({
                    type: "POST",
                    url: "../sys/organ/queryRepetition",
                    data: JSON.stringify(vm.organ),
                    success: function(r){
                        if(r.code == '-1'){
                            alertMsg("当前机构下，组织简称或者组织全称不能重名，请您检查后重新填写");
                        }else{
                            var sigon='';
                            var lat=$('#lonlatCopy').val();
                            var lonlatCopy=lat.split(",");
                            vm.organ.latitude=lonlatCopy[0];
                            vm.organ.longitude=lonlatCopy[1];
                            if(vm.organ.fullName != null && vm.organ.fullName !="" && vm.organ.name!="" && vm.organ.name!=null){
                                var url = vm.organ.id == null ? "../sys/organ/save" : "../sys/organ/update";
                                var cleck=vm.organ.name;
                                if(cleck.length>10){
                                    alertMsg("组织机构简称不能超过10位，请您检查后重新填写！");
                                }else{
                                    $.ajax({
                                        type: "POST",
                                        url: url,
                                        data: JSON.stringify(vm.organ),
                                        success: function(r){
                                            if(r.code == '0'){
                                                var organInfo=r.organInfo;
                                                var nodes = ztree.getSelectedNodes();
                                                var icon = "";
                                                if(organInfo.type == 1){
                                                    //机构
                                                    icon='../statics/plugins/ztree/css/zTreeStyle/img/diy/8.png';
                                                }
                                                if(organInfo.type == 2){
                                                    //部门
                                                    icon='../statics/plugins/ztree/css/zTreeStyle/img/diy/2.png';
                                                }
                                                if(vm.organ.id==null || vm.organ.id==''){
                                                    toast(r.msg,function(){
                                                        ztree.addNodes(nodes[0],{id:organInfo.id,parentId:organInfo.parentId,name:organInfo.name,icon:icon});
                                                    });
                                                }else{
                                                    toast(r.msg,function(){
                                                        nodes[0].name=organInfo.name;
                                                        nodes[0].icon=icon;
                                                        ztree.updateNode(nodes[0]);
                                                    });
                                                }
                                            }else{
                                                alertMsg(r.msg);
                                            }
                                        }
                                    });
                                }
                            }else{
                                alertMsg("请检查您的组织机构、组织机构简称的填写情况！");
                            }
                        }
                    }
                });
            }
        },
        reload: function (event) {
            $("#organFrom")[0].reset();
        }
    }
});

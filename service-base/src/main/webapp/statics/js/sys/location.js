window.onload = function(){
    auto_height('weizhi',180);
    auto_panel_height(90);
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
            rootPId: -1,
        },
        key: {
            url:"nourl",
            name : "locationName",
            icon:""
        }
    },
    check:{
        // enable:true,
        nocheckInherit:true
    },
    callback: {
        onClick: zTreelocOnClick
    }
};
function zTreelocOnClick(event, treeId, treeNode) {
    var url="../sys/location/info/"+treeNode.locationId;
    $.get(url,function (result) {
        if(result.location.fLocationName==null||result.location.fLocationName==''){
            vm.organName=vm.q.old_organ_Name;
            vm.fLocationName=vm.organName;
        }else{
            vm.fLocationName=result.location.fLocationName;
        }
        vm.location=result.location;
        vm.createP=result.location.createP;
        vm.showInfo=true;
    })
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
        vm.q.old_organ_Name=nodes[0].name;
        initTreeloc(nodes[0].code);
        vm.loc= true;
        vm.scode=nodes[0].code;
    });
}
var ztreeloc;
/**
 * 初始化位置信息
 */
function initTreeloc(scode) {
    $.get("../sys/location/locationTree/"+scode, function(r){
       // ztreeloc.expandAll(false);
        ztreeloc = $.fn.zTree.init($("#locationTree"), settinglocation, r.locationTree);
        /*var nodes = ztreeloc.getNodes();
        for (var i = 0; i < nodes.length; i++) { //设置节点展开
            ztreeloc.expandNode(nodes[i], true, false, true);
        }*/
        ztreeloc.expandAll(true);
    });
}
function init() {
    $.get("../sys/location/code", function(r){
        vm.items.items=r.codeTree;
    });
}
var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            new_organ_Name: null,
            old_organ_Name: null,
            new_organ_Id: null,
            old_organ_Id: null
        },
        showInfo: false,
        loc: false,
        fWorkflowId:true,
        title: "位置信息编辑",
        items:{
            items:[]
        },
        organName:"",
        fLocationName:"",
        scode:"",
        createP:"",
        selected: "",
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
        location:{
            locationId:"",
            fLocationId:"",
            fWorkflowId:"1",
            lockNo:"1",
            locationType:"",
            locationKind:"",
            scode:"",
            sfcode:"",
            operationTime:"",
            operationPerson:"",
            createPerson:"",
            createTime:"",
            dataFlag:"",
            delFlag:"",
            inDbTime:"",
            version:""
        },
    },
    created:function(){
        initTree();
        initTreeloc();
        init();
    },
    methods: {
        addLocation: function(){
            vm.location.createTime="";
            vm.createP="";
            vm.location.locationType=vm.selected;
            vm.fWorkflowId= true;
            var nodes=ztree.getSelectedNodes();
            var nodes1=ztreeloc.getSelectedNodes();
            if(nodes1.length<=0){
                vm.location.fLocationId='';
                vm.location.locationId=null;
                vm.items.items.value='';
                vm.items.items.name='';
                vm.fLocationName=$("#organName").val();
            }
            if(nodes.length<=0){
                vm.location.scode=vm.scode;
                vm.location.sfcode=vm.scode;
                vm.location.locationId=null;
            }
            vm.showInfo = true;
            vm.location.locationName="";
            vm.location.fLocationId=nodes1[0].locationId;
            vm.fLocationName=nodes1[0].locationName;

            vm.location.scode=nodes[0].code;
            vm.location.sfcode=nodes[0].code;
            vm.location.locationId = null;
        },
        locationType: function (event) {
            var type=vm.location.locationType;
            var url="../sys/location/findLocationType/"+type;
            $.get(url,function (result) {
                var str=result.mark;
                var mark=str.substr(str.length-3);
                if(mark>=100){
                    vm.fWorkflowId= false;
                    vm.location.fWorkflowId=-1;
                }else{
                    vm.fWorkflowId= true;
                    vm.location.fWorkflowId=1;
                }
            })
        },
        delLocation: function (event) {
            var nodes=ztreeloc.getSelectedNodes();
            if(nodes.length<=0){
                alertMsg("请选择要删除的位置信息");
            }
            confirm('确定要删除【'+nodes[0].locationName+'】么？', function(){
                var url="../sys/location/delete";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: JSON.stringify(vm.location),
                    success: function(r){
                        if(r.code == 0){
                            toast(r.msg,function(){
                                ztreeloc.removeNode(nodes[0]);
                                vm.showInfo=false;
                                vm.addLocation();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            if(vm.location.locationName==''){
                alertMsg('请填写位置名称');
                return false;
            }
            if(vm.location.locationType==''||vm.location.locationType==null){
                alertMsg('请选择位置类型');
                return false;
            }
            var url = vm.location.locationId == null||vm.location.locationId == "" ? "../sys/location/save" : "../sys/location/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.location),
                success: function(r){
                    if(r.code == '0'){
                        var locationInfo=r.locationInfo;
                        var node = ztree.getSelectedNodes();
                        var organId=$("#organId").val();
                        var nodes = ztreeloc.getSelectedNodes();
                        var icon = "";
                        if(vm.location.locationId==null || vm.location.locationId==''){
                            toast(r.msg,function(){
                                if(node.length<=0){
                                    vm.organName=vm.q.old_organ_Name;
                                    initTreeloc(vm.scode);
                                }else{
                                    vm.organName=vm.q.old_organ_Name;
                                    initTreeloc(node[0].code);
                                }
                                if(result.location.fLocationName==null||result.location.fLocationName==''){
                                    vm.fLocationName=vm.organName;
                                }else{
                                    vm.fLocationName=nodes[0].locationName;
                                }
                            });
                        }else{
                            toast(r.msg,function(){
                                initTreeloc(vm.location.scode);
                                ztreeloc.updateNode(nodes[0]);
                            });
                        }
                    }else{
                        alertMsg(r.msg);
                    }
                }
            });
        },
        showMenuL: function(){
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
                    vm.showInfo=false;
                    var zTree = $.fn.zTree.getZTreeObj("allorganTree"),
                        nodes = zTree.getSelectedNodes(),
                        v = "";
                    initTreeloc(nodes[0].code);
                    vm.location.scode=nodes[0].code;
                    vm.location.sfcode=nodes[0].code;
                    $("#organId").val(nodes[0].code);
                    $("#organName").val(nodes[0].name);
                    vm.q.old_organ_Name=nodes[0].name;
                    vm.q.old_organ_Id=nodes[0].id;
                    vm.organName=vm.q.old_organ_Name;
                    vm.organCode=nodes[0].code;
                    layer.close(index);
                }
            });
        },
        indexSelect(){
            console.log(this.id);
            selected=this.id;
        }
    }
});
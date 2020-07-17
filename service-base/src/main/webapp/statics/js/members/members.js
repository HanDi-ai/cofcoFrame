$(function () {
    if($("tr .jqg-third-row-header")){
        $(".ui-jqgrid .ui-jqgrid-htable th").css("padding","0 1px 0 3px");
    }
    $("#jqGrid").jqGrid({
        url: '../sys/memberst/queryList',
        datatype: "json",
        colModel: [
            {label:'ID',name:'id',index:'id',width:'45',sortable:'false'},
            {label:'库名称',name:'whName',index:'whName',width:'95',sortable:'false'},
            {label:'联系电话',name:'tel',index:'tel',width:'75',sortable:'false',hidden:true},
            {label:'组织机构代码',name:'orgNo',index:'orgNo',width:'75',sortable:'false'},
            {label:'法人代表姓名',name:'corporateName',index:'corporateName',width:'55',sortable:'false'},
            {label:'法人代表电话',name:'corporateTel',index:'corporateTel',width:'45',sortable:'false',align:'right'},
            {label:'联盟成员ID',name:'memberId',index:'memberId',width:'75',sortable:'false'},
            {
                label: '状态', name: 'status', align: "left", width:50, sortable: true,
                formatter: function (value, options, row) {
                    if (row.status === '1') {
                        return "<span class='label label-success'>成员注册审核中</span>";
                    } else if (row.status === '2') {
                        return "<span class='label label-success'>成员注册成功</span>";
                    } else if (row.status === '3') {
                        return "<span class='label label-success'>成员注册失败</span>";
                    } else if (row.status === '4') {
                        return "<span class='label label-success'>成员修改审核中</span>";
                    } else if (row.status === '5') {
                        return "<span class='label label-success'>成员修改成功</span>";
                    } else if (row.status === '6') {
                        return "<span class='label label-success'>成员修改失败</span>";
                    } else if (row.status === '7') {
                        return "<span class='label label-success'>成员注销审核中</span>";
                    } else if (row.status === '8') {
                        return "<span class='label label-success'>成员注销成功</span>";
                    } else if (row.status === '9') {
                        return "<span class='label label-success'>成员注销失败</span>";
                    } else {
                        return '';
                    }
                }
            }
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

//图片上传
layui.use('upload', function () {
    var upload = layui.upload;
    //---------------------
     //执行实例
     upload.render({
         elem: '#orgAttach', //绑定元素
         url: '../upload/image/jpg', //上传接口
         accept: 'images',
         acceptMime: 'image/!*', //只显示图片文件
         before: function (obj) {
             obj.preview(function (index, file, result) {
                 vm.e.imgSrc = result;
             });
         },
         done: function (res) {
             vm.role.orgAttach = res.image;
             if (res.code == '0') {
                 $('#orgAttachImg').attr('src', vm.e.imgSrc);
                 alert("图片上传成功");
             } else {
                 $("#orgAttachImg").removeAttr("src");
                 alert("图片上传失败,请重新上传");
             }
         },
         error: function () {
             //请求异常回调
             alertMsg("图片上传失败")
         }
     });
     //法人代表身份证附件
     upload.render({
         elem: '#corporateIdAttach', //绑定元素
         url: '../upload/image/jpg', //上传接口
         accept: 'images',
         acceptMime: 'image/!*', //只显示图片文件
         before: function (obj) {
             obj.preview(function (index, file, result) {
                 vm.e.imgSrc = result;
             });
         },
         done: function (res) {
             vm.role.corporateIdAttach = res.image;
             if (res.code == '0') {
                 $('#corporateIdAttachImg').attr('src', vm.e.imgSrc);
                 alert("图片上传成功");
             } else {
                 $("#corporateIdAttachImg").removeAttr("src");
                 alert("图片上传失败,请重新上传");
             }
         },
         error: function () {
             //请求异常回调
             alertMsg("图片上传失败")
         }
     });
     //联系人身份证附件
     upload.render({
         elem: '#contactIdAttach', //绑定元素
         url: '../upload/image/jpg', //上传接口
         accept: 'images',
         acceptMime: 'image/!*', //只显示图片文件
         before: function (obj) {
             obj.preview(function (index, file, result) {
                 vm.e.imgSrc = result;
             });
         },
         done: function (res) {
             vm.role.contactIdAttach = res.image;
             if (res.code == '0') {
                 $('#contactIdAttachImg').attr('src', vm.e.imgSrc);
                 alert("图片上传成功");
             } else {
                 $("#contactIdAttachImg").removeAttr("src");
                 alert("图片上传失败,请重新上传");
             }
         },
         error: function () {
             //请求异常回调
             alertMsg("图片上传失败")
         }
     });
     //库平面图
     upload.render({
         elem: '#positionDiagram', //绑定元素
         url: '../upload/image/jpg', //上传接口
         accept: 'images',
         acceptMime: 'image/!*', //只显示图片文件
         before: function (obj) {
             obj.preview(function (index, file, result) {
                 vm.e.imgSrc = result;
             });
         },
         done: function (res) {
             vm.role.positionDiagram = res.image;
             if (res.code == '0') {
                 $('#positionDiagramImg').attr('src', vm.e.imgSrc);
                 alert("图片上传成功");
             } else {
                 $("#positionDiagramImg").removeAttr("src");
                 alert("图片上传失败,请重新上传");
             }
         },
         error: function () {
             //请求异常回调
             alertMsg("图片上传失败")
         }
     });
     //上级仓库营业执照
     upload.render({
         elem: '#upperOrgAttach', //绑定元素
         url: '../upload/image/jpg', //上传接口
         accept: 'images',
         acceptMime: 'image/!*', //只显示图片文件
         before: function (obj) {
             obj.preview(function (index, file, result) {
                 vm.e.imgSrc = result;
             });
         },
         done: function (res) {
             vm.role.upperOrgAttach = res.image;
             if (res.code == '0') {
                 $('#upperOrgAttachImg').attr('src', vm.e.imgSrc);
                 alert("图片上传成功");
             } else {
                 $("#upperOrgAttachImg").removeAttr("src");
                 alert("图片上传失败,请重新上传");
             }
         },
         error: function () {
             //请求异常回调
             alertMsg("图片上传失败")
         }
     });
});

//日期时间选择器
layui.use('laydate', function() {
    var laydate = layui.laydate;
    //检验检疫生效日期
    laydate.render({
        elem: '#leaseBeginDate',
        done: function (value) {
            vm.role.leaseBeginDate =value
        }
    });
    //检验检疫失效日期
    laydate.render({
        elem: '#leaseEndDate',
        done: function (value) {
            vm.role.leaseEndDate =value
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            organId:null,
            varietyName: null
        },
        w: {
            dataList: [],
            image: ''
        },
        e: {
            imgSrc: ''
        },
        showList: true,
        title:null,
        role:{
            isDelivery:'',//是否指定交割仓库
            whType:'',//仓库类型
            whName:'',//库名称
            whAbbr:'',//库简称
            regAddr:'',//注册地址
            officeAddr:'',//办公地址
            tel:'',//联系电话
            orgNo:'',//组织机构代码
            orgAttach:'',//营业执照
            property:'',//经营性质
            productType:'',//产品类型
            productCategory:'',//产品种类
            bankName:'',//开户银行
            bankAccount:'',//银行账号
            regMoney:'',//注册资金
            totalAsset:'',//总资产
            fixedAsset:'',//固定资产
            netAsset:'',//净资产
            corporateName:'',//法人代表姓名
            corporateTel:'',//法人代表电话
            corporateId:'',//法人代表身份证号码
            corporateIdAttach:'',//法人代表身份证附件
            contactName:'',//联系人姓名
            contactTel:'',//联系人电话
            contactId:'',//联系人身份证号码
            contactIdAttach:'',//联系人身份证附件
            isLease:'',//是否为租赁库
            leaseBeginDate:'',//租赁起始日期
            leaseEndDate:'',//租赁终止日期
            regionArea:'',//库区面积
            houseArea:'',//库房面积
            cementArea:'',//水泥硬化地面面积
            totHeapQty:'',//总堆储能力
            ftrHeapQty:'',//期货堆储能力
            bulkHeapQty:'',//散粮堆储能力
            loadometerTon:'',//地磅吨数
            positionDiagram:'',//库平面图
            upperType:'',//上级仓库类型
            upperName:'',//上级仓库名称
            upperAbbr:'',//上级仓库简称
            upperTel:'',//上级仓库联系电话
            upperAddr:'',//上级仓库地址
            upperOrgNo:'',//上级仓库组织机构代码
            upperOrgAttach:'',//上级仓库营业执照
            upperCorporateName:''//上级仓库法人代表姓名
        }

    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            $("#orgAttachImg").removeAttr("src");
            vm.showList = false;
            vm.title = "新增";
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.role = {};
            vm.showList = false;
            vm.title = "修改";
            var url = "../sys/members/info/" + id;
            $.ajax({
                type: "GET",
                url: url,
                success: function (r) {
                    if (r.code == '0') {
                        vm.role = r.role;
                        setImage();
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
            //vm.getRole(id);
        },
        del: function () {
            //debugger
            var ids = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            if (ids.length == 0) {
                alert("请选择一条记录");
                return false;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData', ids);
            vm.w.dataList = rowData;

            confirm('确定要删除选中的记录？', function(){
                var numId = $("#jqGrid").jqGrid('getGridParam','selrow');
                var numRowData = $("#jqGrid").jqGrid("getRowData",numId);
                var memberId = numRowData.memberId;

                //先判断当前联盟成员下是否包含联盟客户信息
                var containNumsUrl = "../sys/members/containNums/" + memberId;
                $.ajax({
                    type: "GET",
                    url: containNumsUrl,
                    success: function (r) {
                        if (r.code == '0') {
                            /*if(r.countNums != 0){
                                alert("当前联盟成员下包含联盟客户信息,无法删除!");
                                return false;
                            }*/
                            var url = "../sys/members/delete";
                            $.ajax({
                                type: "POST",
                                url: url,
                                contentType: "application/json",
                                data: JSON.stringify(vm.w.dataList),
                                success: function (r) {
                                    if (r.code === '0') {
                                        alert(r, function(index){
                                            vm.reload();
                                        });
                                    } else {
                                        alertMsg(r.msg);
                                    }
                                }
                            });
                        } else {
                            alertMsg(r.msg);
                        }
                    }
                });


           /* var url = "../sys/members/delete";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.w.dataList),
                success: function (r) {
                        if (r.code === '0') {
                            alert(r, function(index){
                                vm.reload();
                            });
                        } else {
                            alertMsg(r.msg);
                            }
                        }
                });*/
            });
        },
        /*getRole: function(id){
            $.get("../sys/members/info/"+id, function(r){
                vm.role = r.role;

            });
        },*/
        saveOrUpdate: function (event) {
            var url = vm.role.id == null ? "../sys/members/save" : "../sys/members/update";
            //var url ="../sys/members/save";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.role),
                success: function(r){
                    alertMsg(r.msg);
                    vm.reload();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page =1;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'varietyName': vm.q.varietyName},
                page:page
            }).trigger("reloadGrid");
        }
    }
});

function setImage(){
    $("#orgAttachImg").attr("src","data:image/png;base64," + vm.role.orgAttach);
    $("#corporateIdAttachImg").attr("src","data:image/png;base64," + vm.role.corporateIdAttach);
    $("#contactIdAttachImg").attr("src","data:image/png;base64," + vm.role.contactIdAttach);
    $("#positionDiagramImg").attr("src","data:image/png;base64," + vm.role.positionDiagram);
    $("#upperOrgAttachImg").attr("src","data:image/png;base64," + vm.role.upperOrgAttach);
}
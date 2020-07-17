var idArray = [0];

layui.use('upload', function () {
    var upload = layui.upload;
    upload.render({
        elem: '#clientOrgAttach',
        url: '../upload/image/jpg',
        exts: 'jpeg|png|gif',
        accept: 'images',
        acceptMime: 'image/*',
        before: function (obj) {
            obj.preview(function (index, file, result) {
                vm.e.imgSrc = result;
            });
        },
        done: function (res) {
            vm.q.clientOrgAttach = res.image;
            if (res.code == '0') {
                $('#clientOrgAttachImg').attr('src', vm.e.imgSrc);
                alert("图片上传成功");
            } else {
                $("#clientOrgAttachImg").removeAttr("src");
                alert("图片上传失败,请重新上传");
            }
        },
        error: function () {
            alert("图片上传失败"); ""
        }
    });
    upload.render({
        elem: '#corporateIdAttach',
        url: '../upload/image/jpg',
        exts: 'jpeg|png|gif',
        accept: 'images',
        acceptMime: 'image/*',
        before: function (obj) {
            obj.preview(function (index, file, result) {
                vm.e.imgSrc = result;
            });
        },
        done: function (res) {
            vm.q.corporateIdAttach = res.image;
            if (res.code == '0') {
                $('#corporateIdAttachImg').attr('src', vm.e.imgSrc);
                alert("图片上传成功");
            } else {
                $("#corporateIdAttachImg").removeAttr("src");
                alert("图片上传失败,请重新上传");
            }
        },
        error: function () {
            alert("图片上传失败");
        }
    });
    upload.render({
        elem: '#contactIdAttach',
        url: '../upload/image/jpg',
        exts: 'jpeg|png|gif',
        accept: 'images',
        acceptMime: 'image/*',
        before: function (obj) {
            obj.preview(function (index, file, result) {
                vm.e.imgSrc = result;
            });
        },
        done: function (res) {
            vm.q.contactIdAttach = res.image;
            if (res.code == '0') {
                $('#contactIdAttachImg').attr('src', vm.e.imgSrc);
                alert("图片上传成功");
            } else {
                $("#contactIdAttachImg").removeAttr("src");
                alert("图片上传失败,请重新上传");
            }
        },
        error: function () {
            alert("图片上传失败");
        }
    });
});

$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/client/query',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', width: 30, key: true, sortable: false, sortable: false, hidden: true},
            {label: '联盟客户号', name: 'clientId', width: 55, sortable: false},
            {label: '联盟成员ID', name: 'memberId', width: 55, sortable: false},
            {label: '公司名称', name: 'clientName', width: 40, sortable: false},
            {label: '公司地址', name: 'clientAddr', width: 50, sortable: false},
            {label: '公司电话', name: 'clientTel', width: 50, sortable: false},
            {label: '公司组织机构代码', name: 'clientOrgNo', width: 60, sortable: false},
            {label: '公司营业执照附件', name: 'clientOrgAttach', width: 60, sortable: false, hidden: true},
            {label: '注册资金', name: 'regMoney', width: 35, sortable: false},
            {label: '开户银行信息', name: 'bankInfo', width: 60, sortable: false},
            {label: '单位性质', name: 'property', width: 30, sortable: false, hidden: true},
            {label: '单位性质', name: 'propertyName', width: 30, sortable: false,formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue == '0') {
                        return '国有';
                    } else if (cellvalue == '1') {
                        return '集体';
                    } else if (cellvalue == '2') {
                        return '私营';
                    } else if (cellvalue == '3') {
                        return '联营';
                    } else if (cellvalue == '4') {
                        return '其他';
                    }
                    return '';
                }},
            {label: '法人代表姓名', name: 'corporateName', width: 40, sortable: false},
            {label: '法人代表电话', name: 'corporateTel', width: 45, sortable: false},
            {label: '法人代表身份证号码', name: 'corporateId', width: 30, sortable: false},
            {label: '法人代表身份证附件', name: 'corporateIdAttach', width: 30, sortable: false, hidden: true},
            {label: '联系人姓名', name: 'contactName', width: 30, sortable: false},
            {label: '联系人电话', name: 'contactTel', width: 30, sortable: false},
            {label: '联系人身份证号码', name: 'contactId', width: 30, sortable: false},
            {label: '联系人身份证附件', name: 'contactIdAttach', width: 30, sortable: false, hidden: true},
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    doResize();
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            id: '',
            clientId: '',
            memberId: '',
            clientName: '',
            clientAddr: '',
            clientTel: '',
            clientOrgNo: '',
            clientOrgAttach: '',
            regMoney: '',
            bankInfo: '',
            property: '',
            corporateName: '',
            corporateTel: '',
            corporateId: '',
            corporateIdAttach: '',
            contactName: '',
            contactTel: '',
            contactId: '',
            contactIdAttach: ''
        },
        w: {
            image: ''
        },
        e: {
            imgSrc: ''
        },
        r: {
            count: 1
        },
        showList: true,
        title: null,
        role: {
            appmenuIdList: '',
            menuIdList: '',
            organName: '',
            organId: ''
        }

    },
    methods: {
        add: function () {
            $("#clientOrgAttachImg").removeAttr("src");
            $("#corporateIdAttachImg").removeAttr("src");
            $("#contactIdAttachImg").removeAttr("src");
            $("#bankName0").val("");
            $("#cardNum0").val("");
            $("#addInfo").empty()
            vm.showList = false;
            vm.q = {};
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            var url = "../sys/client/info/" + id;
            $.ajax({
                type: "GET",
                url: url,
                success: function (r) {
                    if (r.code == '0') {
                        vm.q = r.client;
                        setImage();
                        addBankInfo();
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../sys/client/delete/" + id,
                    data: JSON.stringify(id),
                    success: function(r){
                        if(r.code == 0){
                            alert(r, function(index){
                                vm.reload();
                            });
                        }else{
                            alertMsg(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            debugger

           /* if (vm.q.clientName == '') {
                alertMsg('请输入公司名称');
                return false;
            } else {
                if (getBlen(vm.q.clientName) > 200) {
                    alertMsg('请输入200字节以内的公司名称(汉子:2字节,英文:1字节)');
                    return false;
                }
            }
            if (vm.q.clientAddr == '') {
                alertMsg('请输入公司地址');
                return false;
            } else {
                if (getBlen(vm.q.clientName) > 100) {
                    alertMsg('请输入100字节以内的公司地址(汉子:2字节,英文:1字节)');
                    return false;
                }
            }
            if (vm.q.clientTel == '') {
                alertMsg('请输入公司电话');
                return false;
            } else {
                if (!checkTel(vm.q.clientTel)) {
                    alertMsg('请输入正确的公司电话');
                    return false;
                }
            }
            if (vm.q.clientOrgNo == '') {
                alertMsg('请输入公司组织机构代码');
                return false;
            }
            if (vm.q.clientOrgAttach == '') {
                alertMsg('请输入公司营业执照附件');
                return false;
            }
            if (vm.q.regMoney == '') {
                alertMsg('请输入注册资金');
                return false;
            } else {
                if (!checkNum(vm.q.regMoney)) {
                    alertMsg('请输入正确的注册资金');
                    return false;
                }
            }*/
            var bankInfo = {};
            for (var i = 0; i < idArray.length; i++) {
                var bankName = $("#bankName" + idArray[i] + " option:selected").val();
                var cardNum = $("#cardNum" + idArray[i]).val();
                /*if (bankName == '' || cardNum == '') {
                    alertMsg('请输入完整的开户银行和开户银行卡号,不要的请删除');
                    return false;
                }*/
                bankInfo[bankName] = cardNum;
            }
            vm.q.bankInfo = JSON.stringify(bankInfo);
            /*if (vm.q.property == '') {
                alertMsg('请输入单位性质');
                return false;
            }
            if (vm.q.corporateName == '') {
                alertMsg('请输入法人代表姓名');
                return false;
            }
            if (vm.q.corporateTel == '') {
                alertMsg('请输入法人代表电话');
                return false;
            } else {
                if (!checkPhone(vm.q.corporateTel)) {
                    alertMsg('请输入正确的法人代表电话');
                    return false;
                }
            }
            if (vm.q.corporateId == '') {
                alertMsg('请输入法人代表身份证号码');
                return false;
            } else {
                if (!checkIdcard(vm.q.corporateId)) {
                    alertMsg('请输入正确的法人代表身份证号码');
                    return false;
                }
            }
            if (vm.q.corporateIdAttach == '') {
                alertMsg('请输入法人代表身份证附件');
                return false;
            }
            if (vm.q.contactName == '') {
                alertMsg('请输入联系人姓名');
                return false;
            }
            if (vm.q.contactTel == '') {
                alertMsg('请输入联系人电话');
                return false;
            } else {
                if (!checkPhone(vm.q.contactTel)) {
                    alertMsg('请输入正确的联系人电话');
                    return false;
                }
            }
            if (vm.q.contactId == '') {
                alertMsg('请输入联系人身份证号码');
                return false;
            } else {
                if (!checkIdcard(vm.q.corporateId)) {
                    alertMsg('请输入正确的联系人身份证号码');
                    return false;
                }
            }
            if (vm.q.contactIdAttach == '') {
                alertMsg('请输入联系人身份证附件');
                return false;
            }*/
            var url = vm.q.id == undefined ? "../sys/client/save" : "../sys/client/update";
            $.ajax({
                type: "POST",
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(vm.q),
                success: function (r) {
                    if (r.code == '0') {
                        alert(r, function(index){
                            vm.reload();
                        });
                    } else {
                        alertMsg(r.msg);
                    }
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = 1;
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});

function setImage(){
    $("#clientOrgAttachImg").attr("src","data:image/jpg;base64," + vm.q.clientOrgAttach);
    $("#corporateIdAttachImg").attr("src","data:image/jpg;base64," + vm.q.corporateIdAttach);
    $("#contactIdAttachImg").attr("src","data:image/jpg;base64," + vm.q.contactIdAttach);
}

function addBankInfo(){
    debugger
    var jsObject = JSON.parse(vm.q.bankInfo);
    var keys = Object.keys(jsObject);
    var values = Object.values(jsObject)
    $("#bankName0").val(keys[0]);
    $("#cardNum0").val(values[0]);

    $("#addInfo").empty()
    for (var i = 1; i < keys.length; i++) {
        var val = $(
            "<div class='form-group'>" +
            "<div class='col-sm-4 control-label'></div>" +
            "<div>" +
            "<div class='col-sm-2' style='padding-right: 0px;'>" +
            "<select id=bankName" + i + " class='form-control' style='padding-left: 6px;'>" +
            "<option value=''></option>" +
            "<option value='中国银行'>中国银行</option>" +
            "<option value='工商银行'>工商银行</option>" +
            "<option value='农业银行'>农业银行</option>" +
            "<option value='建设银行'>建设银行</option>" +
            "<option value='民生银行'>民生银行</option>" +
            "<option value='交通银行'>交通银行</option>" +
            "</select>" +
            "</div>" +
            "<div class='col-sm-4' style='padding-left: 2px; padding-right: 2px;'>" +
            "<input class='form-control' id=cardNum" + i + " maxlength='19' value=" + values[i] + " placeholder='开户银行信息'/>" +
            "</div>" +
            //"<button class='layui-btn layui-btn-small' type='button' onclick='addRow();'><i class='layui-icon'></i></button>" +
            "<button class='layui-btn layui-btn-danger layui-btn-small' type='button' onclick=delRow(this," + i + "); style='margin-top:2px;margin-left:0px;'><i class='layui-icon'></i></button>" +
            "</div>" +
            "</div>");
        $("#addInfo").append(val);
        $("#bankName" + i).val(keys[i]);
        idArray.push(vm.r.count);
        vm.r.count++;
    }
}

function addRow() {
    var val = $(
        "<div class='form-group'>" +
        "<div class='col-sm-4 control-label'></div>" +
        "<div>" +
        "<div class='col-sm-2' style='padding-right: 0px;'>" +
        "<select id=bankName" + vm.r.count + " class='form-control' style='padding-left: 6px;'>" +
        "<option value=''></option>" +
        "<option value='中国银行'>中国银行</option>" +
        "<option value='工商银行'>工商银行</option>" +
        "<option value='农业银行'>农业银行</option>" +
        "<option value='建设银行'>建设银行</option>" +
        "<option value='民生银行'>民生银行</option>" +
        "<option value='交通银行'>交通银行</option>" +
        "</select>" +
        "</div>" +
        "<div class='col-sm-4' style='padding-left: 2px; padding-right: 2px;'>" +
        "<input class='form-control' id=cardNum" + vm.r.count + " maxlength='19' placeholder='开户银行信息'/>" +
        "</div>" +
        //"<button class='layui-btn layui-btn-small' type='button' onclick='addRow();'><i class='layui-icon'></i></button>" +
        "<button class='layui-btn layui-btn-danger layui-btn-small' type='button' onclick=delRow(this," + vm.r.count + "); style='margin-top:2px;margin-left:0px;'><i class='layui-icon'></i></button>" +
        "</div>" +
        "</div>");
    $("#addInfo").append(val);
    idArray.push(vm.r.count);
    vm.r.count++;
}

function delRow(val, index) {
    for (var i = 1; i < idArray.length; i++) {
        if (idArray[i] == index) {
            idArray.splice(i, 1);
            i--;
        }
    }
    $(val).parent().parent().remove();
}

function getBlen(val) {
    return val.replace(/[^\u0000-\u00ff]/g, "aa").length;
}

function checkTel(val) {
    var reg = /\d{3}-\d{8}|\d{4}-\d{7}/;
    if (reg.test(val)) {
        return true;
    }
    return false;
}

function checkNum(val) {
    var reg = /^(0|[1-9][0-9]*)$/;
    if (reg.test(val)) {
        return true;
    }
    return false;
}

function checkPhone(val) {
    var reg = /^(1[3-9][0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
    if (reg.test(val)) {
        return true;
    }
    return false;
}

function checkIdcard(val) {
    var reg = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (reg.test(val)) {
        return true;
    }
    return false;
}
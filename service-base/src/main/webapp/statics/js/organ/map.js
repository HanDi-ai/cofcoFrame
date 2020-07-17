

var map;
var smap = function() {
    return {
        init : function() {
            map = new BMap.Map("container");
            map.setDefaultCursor("crosshair");//设置地图默认的鼠标指针样式
            map.enableScrollWheelZoom();//启用滚轮放大缩小，默认禁用。
            map.addEventListener("click", function(e){//地图单击事件
                map.clearOverlays();
                document.getElementById("lonlat").value = e.point.lng + "," + e.point.lat;
                smap.doLocate(e.point.lng, e.point.lat);
            });
        },
        doLocate:function (jd, wd) {//根据经纬度定位
            var point = new BMap.Point(jd, wd);
            var marker = new BMap.Marker(point);
            map.addOverlay(marker);
            map.centerAndZoom(point, 20);//初始化地图，设置中心点坐标和地图级别

        },
        sear:function(result){
            debugger;
            var local = new BMap.LocalSearch(map, {
                renderOptions:{map: map}
            });
            local.search(result);
        }
    };
}();

function getparam(){
    debugger;
    if (window.opener != null && !window.opener.closed) {
        debugger;
        var txtName = window.opener.document.getElementById("lonlatCopy");//获取父窗口中元素，也可以获取父窗体中的值
        var lonlatValue=txtName.value;
        if(lonlatValue!=null&&lonlatValue!=""){
            debugger;
            var lonlatdata=lonlatValue.split(",");
            var lon=lonlatdata[0];
            var lat=lonlatdata[1];
            smap.doLocate(lon,lat);
        }else{
            smap.doLocate(116.437058,39.913121);
        }
    }

}



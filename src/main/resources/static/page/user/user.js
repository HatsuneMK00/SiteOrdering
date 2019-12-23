var areaData = address;
var $form;
var form;
var $;
var url;
// layui.config({
// 	base : "../../js/"
// }).use(['form','layer','upload'],function(){

    // var baseUrl = getRootPath_web();
    // layui.upload({
    //     mehtod:"post",
    //     url : baseUrl+'file/uploadImage',
    //     success: function(res){
    //         url=res.responseMap.url;
    //         $("#userFace").attr("src",url);
    //         alert(url);
    //     }
    // });

    // form = layui.form();
    // var layer = parent.layer === undefined ? layui.layer : parent.layer;
    // $ = layui.jquery;
    //
    // $form = $('form');

    // //添加验证规则
    // form.verify({
    //     oldPwd : function(value, item){
    //         if(value != "123456"){
    //             return "密码错误，请重新输入！";
    //         }
    //     },
    //     newPwd : function(value, item){
    //         if(value.length < 6){
    //             return "密码长度不能小于6位";
    //         }
    //     },
    //     confirmPwd : function(value, item){
    //         if(!new RegExp($("#oldPwd").val()).test(value)){
    //             return "两次输入密码不一致，请重新输入！";
    //         }
    //     }
    // });

    // //提交个人资料
    // form.on("submit(changeUser)",function(data){
    //     var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
    //     setTimeout(function(){
    //         layer.close(index);
    //         layer.msg("提交成功！");
    //     },2000);
    //     return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    // });



    // //修改密码
    // form.on("submit(changePwd)",function(data){
    // 	var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
    //     setTimeout(function(){
    //         layer.close(index);
    //         layer.msg("密码修改成功！");
    //         $(".pwd").val('');
    //     },2000);
    // 	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    // })
// });

//  //加载省数据
// function loadProvince() {
//     var proHtml = '';
//     for (var i = 0; i < areaData.length; i++) {
//         proHtml += '<option value="' + areaData[i].provinceCode + '_' + areaData[i].mallCityList.length + '_' + i + '">' + areaData[i].provinceName + '</option>';
//     }
//     //初始化省数据
//     $form.find('select[name=province]').append(proHtml);
//     form.render();
//     form.on('select(province)', function(data) {
//         $form.find('select[name=area]').html('<option value="">请选择县/区</option>');
//         var value = data.value;
//         var d = value.split('_');
//         var code = d[0];
//         var count = d[1];
//         var index = d[2];
//         if (count > 0) {
//             loadCity(areaData[index].mallCityList);
//         } else {
//             $form.find('select[name=city]').attr("disabled","disabled");
//         }
//     });
// }
//  //加载市数据
// function loadCity(citys) {
//     var cityHtml = '<option value="">请选择市</option>';
//     for (var i = 0; i < citys.length; i++) {
//         cityHtml += '<option value="' + citys[i].cityCode + '_' + citys[i].mallAreaList.length + '_' + i + '">' + citys[i].cityName + '</option>';
//     }
//     $form.find('select[name=city]').html(cityHtml).removeAttr("disabled");
//     form.render();
//     form.on('select(city)', function(data) {
//         var value = data.value;
//         var d = value.split('_');
//         var code = d[0];
//         var count = d[1];
//         var index = d[2];
//         if (count > 0) {
//             loadArea(citys[index].mallAreaList);
//         } else {
//             $form.find('select[name=area]').attr("disabled","disabled");
//         }
//     });
// }
//  //加载县/区数据
// function loadArea(areas) {
//     var areaHtml = '<option value="">请选择县/区</option>';
//     for (var i = 0; i < areas.length; i++) {
//         areaHtml += '<option value="' + areas[i].areaCode + '">' + areas[i].areaName + '</option>';
//     }
//     $form.find('select[name=area]').html(areaHtml).removeAttr("disabled");
//     form.render();
//     form.on('select(area)', function(data) {
//         //console.log(data);
//     });
// }
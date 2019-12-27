/*用来显示我已经租进来的车子，并且具有还车的功能*/
/**
 * Created by 蛟川小盆友 on 2017/12/6.
 */
layui.config({
    base: "js/"
}).use(['form', 'layer', 'jquery', 'laypage'], function () {
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();
    //加载页面数据
    var newsData = [];
    refreshList();

    var user_id = 0;

    //删除
    $("body").on("click",".preorder_del",function(){
        var _this = $(this);
        layer.confirm('删除此订单？',{icon:3, title:'提示信息'},function(index){
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].preOrderId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'order/'+newsData[i].preOrderId,
                        type : "delete",
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                newsData.splice(i,1);
                                layer.msg("删除成功");
                                newsList(newsData);

                            }else{
                                layer.msg("删除失败");
                                newsList(newsData);
                            }
                        },
                        error:function () {
                            layer.msg("检查一下网络吧");
                            window.location.reload();
                        }
                    });
                    newsList(newsData);
                }
            }

            layer.close(index);
        });
    });


    //渲染数据函数
    function newsList(that) {

        function renderDate(data, curr) {
            var dataHtml = '';
            var currData = data.concat().splice(curr * nums - nums, nums);
            if (currData.length !== 0) {
                for (var i = 0; i < currData.length; i++) {
                    var check = (currData[i].checked === 0)?"通过审核":"未通过审核";
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td >' + currData[i].preOrderId + '</td>'
                        + '<td >' + currData[i].groundId + '</td>'
                        + '<td >' + currData[i].userId + '</td>'
                        + '<td >' + currData[i].orderTime + '</td>'
                        + '<td >' + currData[i].price + '</td>'
                        + '<td >' + currData[i].startTime + '</td>'
                        + '<td >' + currData[i].duration + '</td>'
                        + '<td >' + check + '</td>'
                        + '<td>'
                            + '<a class="layui-btn layui-btn-danger layui-btn-mini preorder_del" data-id="'+currData[i].preOrderId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                        + '</td>'
                        + '</tr>';
                }
            } else {
                dataHtml = '<tr><td colspan="10">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 10; //每页出现的数据量
        laypage({
            cont: "page",
            pages: Math.ceil(that.length / nums),
            jump: function (obj) {
                $(".cars_content").html(renderDate(that, obj.curr));
                $('.cars_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }

    //加载页面数据——所有该用户的预约信息
    function refreshList(){
        $.ajax({
            url: baseUrl + "user/getByName",
            type: "get",
            dataType: "json",
            data: {userName: $.cookie('userName')},
            success: function (data) {
                if (data.status === 200) {
                    user_id = data.responseMap.result.userId;
                    //加载页面数据
                    $.ajax({
                        url: baseUrl + "order/user/" + user_id + "/preOrder",
                        type: "get",
                        dataType: "json",
                        data: {userId: user_id},
                        success: function (data) {
                            if(data.status === 200){
                                layer.msg("获取订单");
                                newsData=data.responseMap.result;
                                newsList(newsData);
                            }
                            else{
                                layer.msg("获取订单失败");
                                newsList([]);
                            }

                        },
                        error: function () {
                            layer.msg("检查一下网络吧");
                            newsList([]);
                        }
                    });
                }
                else {
                    layer.msg("用户不存在");
                    newsList([]);
                }
            },
            error: function () {
                layer.msg("检查一下网络吧");
                newsList([]);
            }
        });
    }
});

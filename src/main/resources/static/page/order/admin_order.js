/**
 * Created by 蛟川小盆友 on 2017/12/19.
 */
/**
 * Created by 蛟川小盆友 on 2017/12/6.
 */
layui.config({
    base : "js/"
}).use(['form','layer','jquery','laypage'],function(){
    var form = layui.form(),
    layer = parent.layer === undefined ? layui.layer : parent.layer,
    laypage = layui.laypage,
    $ = layui.jquery;

    var baseUrl = getRootPath_web();
    //加载页面数据
    var newsData = [];
    refreshList();

    //查询
    $(".search_btn").click(function(){
        var newArray1 = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                $.ajax({
                    url :  baseUrl+"order/search",
                    type : "post",
                    dataType : "json",
                    contentType : 'application/json;charset=UTF-8',
                    data: JSON.stringify({content:$(".search_input").val()}),
                    success : function(data){
                        newsData = data.responseMap.result;
                        newsList(newsData);
                    }
                });
                layer.close(index);
            },1000);
        }else{
            newsList(newsData);
        }
    });

    //全选
    form.on('checkbox(allChoose)', function(data){
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //通过判断文章是否全部选中来确定全选按钮是否选中
    form.on("checkbox(choose)",function(data){
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        var childChecked = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"]):checked')
        if(childChecked.length == child.length){
            $(data.elem).parents('table').find('thead input#allChoose').get(0).checked = true;
        }else{
            $(data.elem).parents('table').find('thead input#allChoose').get(0).checked = false;
        }
        form.render('checkbox');
    })

    //通过
    $("body").on("click",".news_pass",function(){

        var _this = $(this);

        layer.confirm('通过此订单？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].preOrderId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'order/check/'+newsData[i].preOrderId,
                        type : "PUT",
                        dataType : "json",
                        contentType : 'application/json;charset=UTF-8',
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                console.log(newsData[i]);
                                newsData[i].checked="1";
                                layer.msg("审核成功");
                                newsList(newsData);

                            }else{
                                layer.msg("审核失败");
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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });

    });

    //不通过
    $("body").on("click",".news_reject",function(){

        var _this = $(this);

        layer.confirm('拒绝此订单？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].preOrderId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'order/uncheck/'+newsData[i].preOrderId,
                        type : "PUT",
                        dataType : "json",
                        contentType : 'application/json;charset=UTF-8',
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                console.log(newsData[i]);
                                newsData[i].checked="-1";
                                layer.msg("审核成功");
                                newsList(newsData);

                            }else{
                                layer.msg("审核失败");
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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });

    });

    //删除
    $("body").on("click",".news_del",function(){

        var _this = $(this);

        layer.confirm('删除此订单？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });
    });

    function newsList(that){
        //渲染数据
        function renderDate(data,curr){
            var dataHtml = '';
            var currData;
            currData = that.concat().splice(curr*nums-nums, nums);

            if(currData.length !== 0){
                for(var i=0;i<currData.length;i++){
                    var startTime=new Date(currData[i].startTime);
                    var interVal=startTime.getTime();
                    interVal+=3600000*currData[i].duration;
                    var endTime=new Date(interVal);
                    var orderTime=new Date(currData[i].orderTime);
                    var payed= (currData[i].payed===0)?"未支付":"已支付"
                    dataHtml += '<tr>'
                        +'<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        +'<td>'+currData[i].preOrderId+'</td>'
                        +'<td>'+currData[i].groundId+'</td>'
                        +'<td>'+currData[i].groundName+'</td>'
                        +'<td>'+currData[i].userId+'</td>'
                        +'<td>'+currData[i].userName+'</td>'
                        // 时间格式化
                        +  '<td>'+orderTime.getFullYear()+"年"+orderTime.getMonth()+"月"+orderTime.getDay()+"日 "
                        +   orderTime.getHours()+":"+orderTime.getMinutes()+'</td>'

                        +'<td>'+currData[i].price+'</td>'

                        //时间格式化
                        +  '<td>'+startTime.getFullYear()+"年"+startTime.getMonth()+"月"+startTime.getDay()+"日 "
                        +   startTime.getHours()+":"+startTime.getMinutes()+"<br>|<br>"+
                        //时间格式化
                        +  endTime.getFullYear()+"年"+endTime.getMonth()+"月"+endTime.getDay()+"日 "
                        +   endTime.getHours()+":"+endTime.getMinutes()+'</td>'
                        +'<td>'+payed+'</td>'
                    ;

                    if(currData[i].checked == "0"){
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-btn-normal layui-btn-mini news_pass" data-id="'+data[i].preOrderId+'"><i class="layui-icon">&#xe600;</i> 通过</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_reject" data-id="'+data[i].preOrderId+'"><i class="layui-icon">&#xe640;</i> 不通过</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+data[i].preOrderId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                            +'</td>'
                            +'</tr>';
                    }else{
                        var txt= '审核不通过';
                        if(currData[i].checked == "0"){
                            txt= '未审核';
                        }else if(currData[i].checked == "1"){
                            txt= '已通过';
                        }else if(currData[i].checked == "-1"){
                            txt= '未通过';
                        }
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-btn-disabled layui-btn-mini " data-id="'+data[i].preOrderId+'"><i class="layui-icon">&#xe600;</i>'+ txt+'</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+data[i].preOrderId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                            +'</td>'
                            +'</tr>';
                    }

                }
            }else{
                dataHtml = '<tr><td colspan="11">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 7; //每页出现的数据量
        laypage({
            cont : "page",
            pages : Math.ceil(that.length/nums),
            jump : function(obj){
                $(".news_content").html(renderDate(that,obj.curr));
                $('.news_list thead input[type="checkbox"]').prop("checked",false);
                form.render();
            }
        })
    }

    function refreshList(){
        $.ajax({
            url: baseUrl + "order",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status === 200) {
                    newsData=data.responseMap.result;
                    newsList(newsData);
                }
                else{
                    alert("获取订单失败");
                    newsList([]);
                }
            },
            error: function () {
                alert("检查一下网络吧");
                newsList([]);
            }
        });
    }
})
<%--
  Created by IntelliJ IDEA.
  User: 蛟川小盆友
  Date: 2017/12/6
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>文章添加--layui后台管理模板</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="../../layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="../../css/font_eolqem241z66flxr.css" media="all" />
</head>
<body class="childrenBody">
<form class="layui-form">
    <div class="layui-form-item">
        <label class="layui-form-label">租车信息</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input carTitle" lay-verify="required" placeholder="请输入用户想要租的车型">
        </div>
    </div>
    <div class="layui-form-item">

        <div class="layui-inline">
            <label class="layui-form-label">用户信息</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input carUser" lay-verify="required" placeholder="请输入租车用户姓名">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">发布时间</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input carTime" lay-verify="date" onclick="layui.laydate({elem:this})">
            </div>
        </div>

    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">预期价格</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input carCost" placeholder="请输入用户预期的租车价格">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="addNews">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="../../layui/layui.js"></script>
<script type="text/javascript" src="addCarRentIn.js"></script>
</body>
</html>

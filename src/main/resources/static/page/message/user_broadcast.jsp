<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>来自管理员的公告</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="../../layui/css/layui.css" media="all"/>
    <link rel="stylesheet" href="../../css/font_eolqem241z66flxr.css" media="all"/>
    <link rel="stylesheet" href="../../css/news.css" media="all"/>
</head>
<body class="childrenBody">
<blockquote class="layui-elem-quote news_search">
    <p align="left"><input type="button" onclick="window.location.reload()" value="刷新"/></p>
</blockquote>
<div class="layui-form cars_list">

    <table class="layui-table">
        <colgroup>
            <%--<col width="50">
            <col>
            <col width="9%">
            <col width="9%">
            <col width="9%">
            <col width="9%">
            <col width="9%">
            <col width="15%">--%>
            <col width="3%">
            <col width="9%">
            <col width="75%">
            <col width="25%">
            <col width="9%">
            <col width="9%">
            <col width="9%">
            <col width="9%">
            <col width="5%">
        </colgroup>
        <thead>
        <tr>
            <th><input type="checkbox" name="" lay-skin="primary" lay-filter="allChoose" id="allChoose"></th>
            <th style="text-align:left;">公告ID</th>
            <th style="text-align:left;">公告内容</th>
            <th style="text-align:left;">公告发布时间</th>
        </tr>
        </thead>
        <tbody class="cars_content"></tbody>
    </table>
</div>
<div id="page"></div>
<script type="text/javascript" src="../../layui/layui.js"></script>
<script type="text/javascript" src="../../js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="../../js/jquery.cookie.js"></script>
<!--获取根路径-->
<script type="text/javascript" src="../../js/getRootPath.js"></script>
<script type="text/javascript" src="user_broadcast.js"></script>
</body>
</html>

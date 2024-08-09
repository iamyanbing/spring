<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新闻推送</title>
</head>
<body>
<h1>每日头条</h1>
<div>
    <div>
        <h2>今日头条新闻实时看</h2>
        <!-- 通过 longLoop() 中代码设置值  -->
        <div style="color:#F00"><b><p id="realTimeNews"></p></b></div>
    </div>
    <hr>
    <div>
        2024年巴黎奥运会(第33届夏季奥林匹克运动会)
    </div>
    <hr>
    <div>
        [巴黎奥运会紧急声明] 本届奥运会期间，塞纳河的水质问题备受关注。依据赛事安排，马拉松游泳比赛将于当地时间8日和9日在塞纳河...
    </div>
</div>
<script type="text/javascript" src="assets/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

    longLoop();

    function longLoop() {
        $.get("ajaxLongPolling", function (data) {
            console.log(data);
            <!-- 设置上面  p标签 id="realTimeNews" 值 -->
            $("#realTimeNews").html(data);
            longLoop();
        })
    }

</script>
</body>
</html>
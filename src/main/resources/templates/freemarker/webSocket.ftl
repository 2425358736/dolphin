<html xmlns="http://www.w3.org/1999/html">
    <script>
        let ws;
        function open () {
            if ("WebSocket" in window)
            {

                // 打开一个 web socket
                ws = new WebSocket("ws://localhost:6533/websocket?${userId}");

                ws.onopen = function()
                {
                    alert("websocket连接成功");
                }
            }
            else
            {
                // 浏览器不支持 WebSocket
                alert("您的浏览器不支持 WebSocket!");
            }
        }

        open ();

        ws.onmessage = function (evt)
        {
            var received_msg = evt.data;
            document.getElementById("content").innerHTML = document.getElementById("content").innerHTML + "<span>" + received_msg + "</span></br>";
            //alert("接受到来自服务端消息：" + received_msg);
        };

        ws.onclose = function()
        {
            alert("WebSocket连接关闭");
        };

        ws.onerror = function()
        {
            alert("WebSocket连接异常，发起重新连接");
            open ();
        };
        send = () => {
            const userId  = document.getElementById("userId").value;
            const message  = document.getElementById("message").value;
            if (message === '' && userId === '') {
                alert("消息和接收人id不可以为空");
            } else {
                ws.send(${userId!""} + ":" + userId + ":" + message);
            }
        }
        ajaxSend = () => {
            const userId  = document.getElementById("userId").value;
            const message  = document.getElementById("message").value;
            if (message === '' && userId === '') {
                alert("消息和接收人id不可以为空");
            } else {
                const httpRequest = new XMLHttpRequest();//第一步：建立所需的对象
                httpRequest.open('GET', 'http://localhost:6533/webSocket/send?sendId=' + ${userId!""} + '&userId='+ userId + "&message=" + message, true);//第二步：打开连接  将请求参数写在url中  ps:"./Ptest.php?name=test&nameone=testone"
                httpRequest.send();//第三步：发送请求  将请求参数写在URL中
                /**
                 * 获取数据后的处理程序
                 */
                httpRequest.onreadystatechange = function () {
                    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                        const json = httpRequest.responseText;//获取到json字符串，还需解析
                        console.log(json);
                        // alert(json);
                    }
                };
            }
        }
    </script>
    <body>
        <h1>作者：${作者!""}</h1>
        <h3>当前登录人userId： ${userId!""}</h3>
        <div>
            <h3>聊天内容</h3>
            <div id="content" />
        </div>
        <span>
            接收人id：<input id="userId" />
            发送的消息：<input id="message" />
            <button onclick="send()">js发送</button>
        </span>
        <span>
            <button onclick="ajaxSend()">ajax发送</button>
    </body>
</html>
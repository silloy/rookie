var stompClient = null;
var host=window.location.host

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  }
  else {
    $("#conversation").hide();
  }
  $("#response").html("");
}


// SendUser ***********************************************
function connect() {
  var userId = $('#userId').val();

  console.log('ws://' + host + '/limi-websocket' + '?token=' + userId)
  var socket = new WebSocket('ws://' + host + '/limi-websocket' + '?token=' + userId);
  // var socket = new SockJS('/limi-websocket' + '?token=' + userId);
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
    setConnected(true);
    console.log('Connected:' + frame);
    stompClient.subscribe('/user/queue/sendUser', function(response) {
      showResponse(JSON.parse(response.body).message);
    });

    stompClient.subscribe('/topic/sendTopic', function(response) {
      showResponse(JSON.parse(response.body).message);
    });

    stompClient.subscribe('/topic/abc/def', function(response) {
      showResponse(JSON.parse(response.body).message);
    });
  });
}



function disconnect() {
  if (stompClient != null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}


function send() {
  var name = $('#name').val();
  var message = $('#messgae').val();
  /*//发送消息的路径,由客户端发送消息到服务端
  stompClient.send("/sendServer", {}, message);
*/
  /*// 发送给所有广播sendTopic的人,客户端发消息，大家都接收，相当于直播说话 注：连接需开启 /topic/sendTopic
  stompClient.send("/sendAllUser", {}, message);
  */
  /* 这边需要注意，需要启动不同的前端html进行测试，需要改不同token ，例如 token=1234，token=4567
  * 然后可以通过写入name 为 token  进行指定用户发送
  */
  stompClient.send("/app/sendMyUser", {}, JSON.stringify({name:name, message:message}));
}



function showResponse(message) {
  $("#response").append("<tr><td>" + message + "</td></tr>");
}


$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $( "#connect" ).click(function() { connect(); });
  $( "#disconnect" ).click(function() { disconnect(); });
  $( "#send" ).click(function() { send(); });
});
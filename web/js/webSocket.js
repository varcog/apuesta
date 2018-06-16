var webSocket;

function openSocket() {
    if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
        writeResponse("WebSocket is already opened.");
        return;
    }    
    var idUsuario = $("input[name=idUsuario]").val();
    webSocket = new WebSocket("ws://200.58.174.203:10000/wsNotificacion/" + idUsuario);

    webSocket.onopen = function (event) {
        if (event.data === undefined)
            return;
        writeResponse(event.data);
    };

    webSocket.onclose = function (event) {
        writeResponse("Connection closed");
    };
    webSocket.onmessage = function (event) {
        var json = $.parseJSON(event.data);
        switch (json.tipo) {
            case 0:                
                notificacion(json);
                break;            
        }
    };
}

function notificacion(obj) {
    var cuerpo = "<li class='bg-warning' title='"+obj.descripcion+"'><a href='#'><i class='fa fa-users text-aqua'></i>"+obj.descripcion+"</a></li>";
    $("#navNovedades").prepend(cuerpo);
    $("#cantNot").text(parseInt($("#cantNot").text())+1);
    alertar(obj.descripcion,obj.foto);
}
/**
 * Sends the value of the text input to the server
 */
function send(mensaje) {
    webSocket.send(mensaje);
}

function closeSocket() {
    webSocket.close();
}

function writeResponse(text) {
    console.log(text);
}

function alertar(mensaje, foto) {
    if (Notification) {
        if (Notification.permission !== "granted") {
            Notification.requestPermission();
        }
        var title = "Apuesta";
        var extra = {
            icon: foto,
            body: mensaje
        };
        var noti = new Notification(title, extra);
        noti.onclick = function(event){
            event.preventDefault();            
        };
        noti.onclose = {
// Al cerrar
        };
        setTimeout(function () {
            noti.close();
        }, 10000);
    }
}
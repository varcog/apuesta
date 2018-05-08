var url = "IngresoController";

$(document).ready(function () {
    $.post(url, {evento: "obtenerIngreso"}, function (resp) {
        if (resp === "false")
            location.href = "index.html";
        else {
            $(".elmenu").remove();
            var json = $.parseJSON(resp);
            var html = "";
            $.each(json.menu, function (menu, subMenus) {
                html += "<li class='treeview'>";
                html += "<a>";
                // fa-asterisk
                // fa-certificate
                // fa-circle
                html += "<i class='fa fa-asterisk'></i>";
                html += "<span>" + menu + "</span>";
                html += "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>";
                html += "</a>";
                html += "<ul class='treeview-menu'>";
                $.each(subMenus, function (i, subMenu) {
                    html += "<li><a onclick='cambiarMenu(\"" + subMenu.url + "\")'><i class='fa fa-circle-o'></i> " + subMenu.nombre + "</a></li>";
                });
                html += "</ul>";
                html += "</li>";
            });
            $("#menu").append(html);
            $(".nombre_usuario").text((json.usuario || ""));
            $(".cargo").text((json.perfil || ""));
        }
    });
});

function cambiarMenu(url) {
    $("#contentFrame").attr("src", url);
}


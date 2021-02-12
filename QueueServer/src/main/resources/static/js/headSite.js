var statusHTML = 'Статус: ';
var lastStatus;
var userId;

var csrf_token = '';
var name_user;
var menu_struct;

var errorAuth = false;


//Обращение на сервер
function ajax_get(url, callback) {
    if (errorAuth) {
        callback(null, true);
        return;
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            console.log('responseText:' + xmlhttp.responseText);
            try {
                var data = JSON.parse(xmlhttp.responseText);
            } catch(err) {
                console.log(err.message + " in " + xmlhttp.responseText);
                alert(err.message);
                return;
            }
            callback(data);
        }else{
            if (xmlhttp.readyState == 4 && xmlhttp.status == 401)
                errorAuth = true;
            if (xmlhttp.readyState == 4) {
                console.log('responseText:' + xmlhttp.responseText);
                callback(xmlhttp, true);
            }
        }
    };

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function ajax_post(url, body, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var data;
            if (xmlhttp.responseType == 'json') {
                var responseText = JSON.stringify(xmlhttp.response);
                console.log('responseText:' + responseText);
                try {
                    data = xmlhttp.response;
                } catch(err) {
                    console.log(err.message + " in " + responseText);
                    alert(err.message);
                    return;
                }
            }else {
                console.log('responseText:' + xmlhttp.responseText);
                try {
                    data = JSON.parse(xmlhttp.responseText);
                } catch(err) {
                    console.log(err.message + " in " + xmlhttp.responseText);
                    alert(err.message);
                    return;
                }
            }
            callback(data, false);
        }else if (xmlhttp.readyState == 4 && xmlhttp.status != 200) {
            callback(xmlhttp, true);
        }
    };

    xmlhttp.responseType =	"json";
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    //xmlhttp.setRequestHeader('X-CSRF-TOKEN', csrf_token);
    xmlhttp.send(body);
}

function draw_head() {
    var htmlMenu = '<div style="margin-top: 10px;' +
        'margin-left: 40px;' +
        'position: absolute;' +
        'height: 60px;" id="menuHead"></div>';
    var htmlFormLogout = '<form id="logoutForm" action="/logout" method="post">' +
        '<input type="hidden" name="_csrf" value="' + csrf_token + '"/>\n' +
        '        <button type="submit" name="Выйти" value="Выйти">Выйти</button>' +
        '    </form>';
    var htmlEmployee = '<div id="headEmployee" style="position: absolute;' +
        'margin-top: 10px;' +
        'right: 0;' +
        'font: 20px SFProDisplay-Regular">' +
        '<div id="head_employee_name"' +
        'style="position: absolute;' +
        'margin-right: 10px;' +
        'width: 400px;' +
        'right: 0;' +
        'height: 20px;' +
        'text-align: right;' +
        '">' + name_user + '</div>' +
        '<div id="statusEmployee"' +
        'style="margin-top: 0px;' +
        'margin-right: 405px;' +
        'right: 0;' +
        'width: 400px;' +
        'height: 20px;' +
        'text-align: right;"></div>' +
        '<div id="form_logout" ' +
        'style="margin-top: 5px;' +
        'position: absolute;' +
        'margin-right: 10px;' +
        'right: 0;">' + htmlFormLogout + '</div></div>';
    var htmlLine = '<hr style="margin-top: 66px;' +
        'width: 95%;' +
        'position: absolute;' +
        'margin-left: 2.5%;" >';
    var headSite = document.getElementById('headSite');
    headSite.innerHTML = htmlMenu + htmlEmployee + htmlLine;
    headSite.style.height = '70px';
    draw_menu();
}

function draw_menu() {
    var html_menu = '';
    for (let i = 0; i < menu_struct.length; i++) {
        html_menu += '<button class="button_menu" style="position: absolute;' +
            'margin-left: ' + (i * 165) + 'px;" onclick="clickMenu(\'' + menu_struct[i].URL + '\')">' + menu_struct[i].name + '</button>';
    }

    document.getElementById('menuHead').innerHTML = html_menu;
}

function clickMenu(URL) {
    document.location.href = URL;
}

function updateStatus(userID) {
    var htmlStatus = statusHTML + 'NaN' + '</h4>';
    userId = userID;
    var parameters = 'idUser=' + userID;
    ajax_get('/rest/work_station/getStatus?' + parameters, function (data, error) {
        lastStatus = data['value'];
        htmlStatus = statusHTML + data['name'] + '.';
        document.getElementById("statusEmployee").innerHTML = htmlStatus;
        if (!error)
            setTimeout(updateStatus, 2 * 1000, userID);
    });

}
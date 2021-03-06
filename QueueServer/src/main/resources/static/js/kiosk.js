var PAGE = 'Services';
var idBSService = null;
var errorMes = '';
var widthLogo = 571;
var xyLogo = widthLogo * 90 / 1713;
var rLogo = xyLogo * 4 / 9;

var bodyKioskHTML = '<div id="bodyKiosk" width="' +
    screen.width + 'px" ' +
    'heigth="' +
    (screen.height - 60) + 'px" style="position: relative;"></div>';
var logoHTML = '<div id="logo" style="margin-left: ' + ((screen.width / 2) - (571 / 2)) + 'px"><img src="/img/logo.png" width="571px" height="89px"' +
    'usemap="#BSService">' +
    '<map name="BSService">' +
    '<area shape="circle" coords="' + xyLogo + ',' + xyLogo + ',' + rLogo +
    '" onclick="clickBSService();">' +
    '</map></div>';

var dateTimeHTML = '<div id="datetime" style="position: absolute; height: 60px; width: '+
    screen.width +'px; left: 0px; right: 0px; bottom: 0px; top: ' + (screen.height - 60) +
    'px;"></div>';
dateTimeHTML += '<div id="date" style="position: absolute; top: ' + (screen.height - 60 + 13) +
    'px; left: 31px;"></div>'
dateTimeHTML += '<div id="time" style="position: absolute; top: ' + (screen.height - 60 + 13) +
    'px; left: ' + (screen.width - 72 - 29) + 'px;"></div>'

var servicesBodyHTML = '<div id="services" style="position: absolute; display: table;" ></div>';

var servicesInnerHTML = '';

var questionYesNoHTML = '<div id="questionYesNo" style="margin-left: ' +
    ((screen.width / 2) - (699/2)) +'px;">' +
    '<p class="questionYesNo">Вы будете сейчас оплачивать услуги?</p>' +
    '</div>';

var imgPayHTML = '<div class="imgPay" style="position: absolute; margin-left: ' +
    ((screen.width / 2) - (502 / 2)) + 'px; margin-top: ' +
    ((screen.height / 2) - (373 / 2)) + 'px;">' +
    '<img src="/img/pay.png" class="imgPay" width="502px" height="373px">' +
    '</div>';

var formYesNoHTML = '<div id="formYesNo" style="position: absolute; width: 870px; height: 227px;' +
    'margin-left: ' +
    ((screen.width / 2) - (870 / 2)) + 'px;' +
    'margin-top: ' +
    (screen.height - (97 + 227)) + 'px;">' +
    '</div>';

var labelPrintingHTML = '<div id="labelPrinting" style="margin-left: ' +
    ((screen.width / 2) - (611/2)) +'px;">' +
    '<p class="labelPrinting"><b>Спасибо!</b><br>' +
    'Пожалуйста, возьмите ваш талон.</p>' +
    '</div>';

var imgPrintingHTML = '<div class="imgPrinting" style="position: absolute; margin-left: ' +
    ((screen.width / 2) - (502 / 2)) + 'px; margin-top: ' +
    ((screen.height / 2) - (389 / 2)) + 'px;">' +
    '<img src="/img/talon.png" class="imgPay" width="502px" height="389px">' +
    '</div>';
// var imgErrorHTML = '<div class="imgError" style="position: absolute; margin-left: ' +
//     ((screen.width / 2) - (428 / 2)) + 'px; margin-top: ' +
//     ((screen.height / 2) - (416 / 2)) + 'px;">' +
//     '<img src="/img/ThisIsError.png" class="imgError" width="428px" height="416px">' +
//     '</div>';
var imgErrorHTML = '<div class="imgError" style="position: absolute; margin-left: ' +
    ((screen.width / 2) - (510 / 2)) + 'px; margin-top: ' +
    ((screen.height / 2) - (416 / 2)) + 'px;' +
    'width: 510px;">' +
    '<span style="font: 50px Arial;' +
    'color: #5d6f91;' +
    '">Электронная очередь времено не работает</span>' +
    '</div>';

//Обращение на сервер
function ajax_get(url, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            console.log('responseText:' + xmlhttp.responseText);
            try {
                var data = JSON.parse(xmlhttp.responseText);
            } catch(err) {
                console.log(err.message + " in " + xmlhttp.responseText);
                return;
            }
            callback(data);
        }
    };

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

//Функции отрисовки
////////////////////////
function updateTime() {
    ajax_get('/kioskt/get_time', function(data) {
        document.getElementById("date").innerHTML = data["date"];
        document.getElementById("time").innerHTML = data["time"];
    });
}

function drawPage(data) {
    if (data["type"] == 'List'){
        var html = logoHTML + servicesBodyHTML;
        document.getElementById("bodyKiosk").innerHTML = html;
        document.getElementById("bodyFull").style = 'background-image: url("/img/background.png");' +
            '    background-size: cover;';
        drawServices(data);
    }else if (data["type"] == 'YESNO') {
        var html = questionYesNoHTML + imgPayHTML + formYesNoHTML;
        document.getElementById("bodyKiosk").innerHTML = html;
        document.getElementById("bodyFull").style = 'background-image: url("/img/background2.png");' +
            '    background-size: cover;';
        drawYesNo(data);
    }else if (data["type"] == 'Print') {
        document.getElementById("bodyFull").style = 'background-image: url("/img/background2.png");' +
            '    background-size: cover;';
        drawPrinting(data);
    }else {
        document.getElementById("bodyFull").style = 'background-image: url("/img/background2.png");' +
            '    background-size: cover;';
        drawError(data);
    }
    PAGE = data["type"];
    idBSService = data['idBSService'];
    document.getElementById("body").style.width = '' +  screen.width + 'px';
    document.getElementById("body").style.height = '' + screen.height + 'px';
}

function drawPrinting(data) {
    //alert('drawPrinting : ' + data['message']);
    var printingHTML = labelPrintingHTML + imgPrintingHTML;
    document.getElementById("bodyKiosk").innerHTML = printingHTML;
    setTimeout(isPrinted, 5 * 1000, data);
}

function isPrinted(data) {
    var url = '/kioskt/isPrinted';
    var body = 'id=' + data['message'];
    ajax_get(url + '?' + body, function (data) {

        if (data['printed'] == 'ok') {
            updateServices();
        } else if (data['printed'] == 'wait'){
            setTimeout(isPrinted, 5 * 1000, UUID);
        } else {
            drawError(data);
        }
    });
}

function drawError(data) {
    console.log('Error:' + data['message']);
    var html = logoHTML + imgErrorHTML;
    document.getElementById("bodyKiosk").innerHTML = html;
    setTimeout(updateServices, 30 * 1000);
}

function drawYesNo(data) {

    var buttonsHTML = '<div class="button_YesNo" ' +
        'style="position: absolute;' +
        'margin-left: 0px;' +
        'margin-top: 0px;" ' +
        ' onclick="clickYesNo('+ ("'id=" + data['idMenu'] + "&yesno=yes'") + ')"><table style="padding: 0px;"><tr><td style="padding: 0px; ' +
        'width: 412px; height: 106px; vertical-align: middle; text-align: center;">Да' +
        '</td></tr></table></div></div>' +
        '<div class="button_YesNo" ' +
        'style="position: absolute;' +
        'margin-left: 458px;' +
        'margin-top: 0px;" ' +
        ' onclick="clickYesNo('+ ("'id=" + data['idMenu'] + "&yesno=no'") + ')"><table style="padding: 0px;"><tr><td style="padding: 0px; ' +
        'width: 412px; height: 106px; vertical-align: middle; text-align: center;">Нет' +
        '</td></tr></table></div></div>' +
        '<div class="button_Back" ' +
        'style="position: absolute;' +
        'margin-left: 229px;' +
        'margin-top: 147px;" ' +
        ' onclick="clickBack()"><table style="padding: 0px;"><tr><td style="padding: 0px; ' +
        'width: 412px; height: 80px; vertical-align: middle; text-align: center;">Назад' +
        '</td></tr></table></div></div>';
    document.getElementById("formYesNo").innerHTML = buttonsHTML;
}

function drawServices(data) {
    var servicesHTML = '';
    var centerW = screen.width / 2; //Центр экрана по гризонтали
    var centerH = 197 + 89 + ((screen.height - 197 - 89 - 60) / 2); //Центр экрана по вертикали с учетом логотипа и меню
    var heightMenu = (data['list'].length * 106) + ((data['list'].length - 1)* 18); //Высота меню с учетом разрывов
    var position = centerH - (heightMenu / 2); //Начальная позиция отступа сверху элемента меню
    var step = 106 + 18; //шаг увеличения позиции элемента меню

    data["list"].forEach(function(item, i, arr) {
        servicesHTML += '<div class="button_service" ' +
            'style="position: absolute;' +
            'margin-left: ' + (centerW - (680 / 2)) + 'px;' +
            'margin-top: ' + position + 'px;" ' +
            ' onclick="clickButton(' + item.id + ')"><table style="padding: 0px;"><tr><td style="padding: 0px; ' +
            'width: 680px; height: 106px; vertical-align: middle; text-align: center;">' + item.name + '</td></tr></table></div>';
        position += step;
    });
    document.getElementById("services").innerHTML = servicesHTML;

}

function updateServices() {
    ajax_get('/kioskt/get_services', function (data) {
        drawPage(data);
    });
}

//Функции обработки событий
////////////////////////
function clickButton(id) {
    if (id != 0) {
        var url = '/kioskt/clickservice';
        var body = 'id=' + id;
        ajax_get(url + '?' + body, function (data) {
            drawPage(data);
        });
    }
}
function clickBSService() {
    if (PAGE == 'List') {
        if (idBSService != null) {
            var url = '/kioskt/clickservice';
            var body = 'idService=' + idBSService;
            ajax_get(url + '?' + body, function (data) {
                drawPage(data);
            });
        }
    }
}

function clickBack() {
    updateServices();
}

function clickYesNo(yesNo) {
    if (yesNo != '') {
        var url = '/kioskt/clickservice';
        var body = yesNo;
        ajax_get(url + '?' + body, function (data) {
            drawPage(data);
        });
    }
}

var lines = 12;
var orientation = 0;

var speedTextLine = 1; //Скорость движения текста (пиксели за обновление)
var fpsTextLine = 150; // Количество обновлений в секунду
var runLineWorked;
var widthText;
var currentPXText;
var endPXText;
var drawerRunLine;
var runLineActive = false;
var runLines = [];
var currentRunLine = 0;
var runLineUpdater;

var screenWidth = screen.width;
var screenHeight = screen.height;
var angle = 0;

var badQuery = 0;
var errorConnection = false;

var arr_ticketsToCasement = new Array(lines); //Массив текущих талонов
var length_lines = 0; //Количество актуальных элетементов в массиве
var sound = false; //Флаг воспроизведения звука
var object_sound = null;

var versionTabloPage;


//параметрические переменные
var body_padding; //Отступы у body
var body_padding_2x; //Двойной отступ body
var lines_padding; //Отступы между линиями
var lines_padding_2x; //Двойной отступ между линиями
var height_line; //Высота строки назначенных талонов
var head_borderRadius; //радиус закругления заголовков
var width_talon_to_casement;
var width_talon_in_queue;
var width_bigPic;
var width_bigPicImg
var height_logo;
var height_bicPicImg;
var height_bigPic;
var width_runLine;
var height_runLine;
var height_talon_to_casement;
var height_talon_in_queue;
var width_talon_to_casement_column2; // ширина второго столбца в заголовке назначенных талонов
var width_talon_to_casement_column1;// ширина первого столбца в заголовке назначенных талонов
var font_size_HeadTalonToCasement1; //Размер шрифта в заголовке
var font_size_HeadTalonToCasement2; //Размер шрифта в заголовке (слово окно)
var font_size_LinesToCasement; //Размер в строке назначенный талон
var font_size_runLine; //Размер текста в бегущей строке
var marginLeft_talon_to_casement; // Отсуп слева у списка назначенных талонов
var marginLeft_talon_in_queue; //Отступ слева у списка таловнов очереди
var marginTop_talon_to_casement; // Отсуп сверху у списка назначенных талонов
var marginTop_talon_in_queue; //Отступ сверху у списка таловнов очереди
var marginLeft_bicPic;
var marginLeft_bicPicImg;
var marginLeft_runLine;
var marginTop_bicPic;
var marginTop_bicPicImg;
var marginTop_runLine;
var marginLeft_logo;
var marginTop_logo;
var marginTop_spanRunLineText;


var color_backgroundHead = '#007ab3'; //Фон заголовков
var color_backgroundHCasement = '#028ccc'; //Фон у слова окно
var color_backLine1 = '#0291d3';
var color_backLine2 = '#35a7dc';
var color_backLineInQueue1 = '#80c8e9';
var color_backLineInQueue2 = '#d8eef8';
var color_fontInQueue = '#5d6f91';
var color_fontRunLine = '#007ab3'; //Цвет текста бегузей строки

//Обращение на сервер
function ajax_get(url, return_false, callback) {
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
            badQuery = 0;
            callback(data);
        } else {
            if (badQuery == 5)
                setTimeout(init_page, 5 * 1000);

            if (xmlhttp.readyState == 4) {
                badQuery++;
                if (return_false) {
                    callback(false);
                    badQuery++;
                }
            }

        }
    };

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

//Обеспечение анимации
var r = 255;
var g = 255;
var b = 255;
var iterator = false;

function checkLines(talons) {
    var result = false;
    //var startIndex = Math.max(0, talons.length - lines);
    var startIndex = 0;
    for (var i = startIndex; i < talons.length; i++) {
        if (!(isTicket(talons[i]))) {
            add_ticket(talons[i]);
            result = true;
        }
    }

    for (var i = 0; i < length_lines; i++) {
        var del = true;
        for (var j = 0; j < talons.length; j++) {
            if (arr_ticketsToCasement[i].id == talons[j].id){
                del = false;
                break;
            }
        }
        if (del){
            del_ticket(i);
            i--;
            result = true;
        }
    }

    return result;
}

function del_ticket(index) {
    for (var i = index; i < length_lines - 1; i++) {
        arr_ticketsToCasement[i] = arr_ticketsToCasement[i + 1];
    }
    length_lines--;
}

function animation_render() {

    var iter = false;
    var value_iter = 10;

    if (iterator){
        g += value_iter;
        b += value_iter;
    }else {
        g -= value_iter;
        b -= value_iter;
    }

    if (r > 255 || r < 0
        || g > 255 || g < 0
        || b > 255 || b < 0){
        iterator = !iterator;
        if (g < 0){
            g = 1;
            b = 1;
        }
        if (g > 255){
            g = 255;
            b = 255;
            iter = true;
        }

    }

    for (var i = 0; i < length_lines; i++) {
        if (arr_ticketsToCasement[i].animation > 0) {
            var column1 = document.getElementById('column1_' + arr_ticketsToCasement[i].id);
            var column2 = document.getElementById('column2_' + arr_ticketsToCasement[i].id);

            if (iter)
                arr_ticketsToCasement[i].animation -= 1;
            if (column1 != null) {
                column1.style.color = 'rgb('+ r + ',' + g + ',' + b + ')';
                column2.style.color = 'rgb('+ r + ',' + g + ',' + b + ')';
            }

        }
    }
}

function add_ticket(ticket) {
    var new_ticket = {
        'id':ticket.id,
        'name':ticket.name,
        'casement':ticket.casement,
        'animation':10
    }
    if (length_lines + 1 > arr_ticketsToCasement.length)
        add_space();
    // if(length_lines + 1 > lines)
    //     del_ticket(0);
    arr_ticketsToCasement[length_lines] = new_ticket;
    length_lines++;
    sound = true;
}

function add_space() {
    var arr = [arr_ticketsToCasement.length * 1.5];
    for (var i = 0; i < arr_ticketsToCasement.length; i++) {
        arr[i] = arr_ticketsToCasement[i];
    }
    arr_ticketsToCasement = arr;
}

function isTicket(ticket) {
    for (var i = 0; i < length_lines; i++) {
        if (ticket.id == arr_ticketsToCasement[i].id)
            return true;
    }
    return false;
}

function play_sound() {
    if (object_sound == null)
        object_sound = document.getElementById('sound_');

    if (object_sound == null)
        return;

    if (sound){
        object_sound.play();
        sound = false;
    }

}
//Конец обеспечение анимации

function draw_page() {
    var marginLeft_body = 0;
    var marginTop_body = 0;
    if (orientation == 0) {
        screenHeight = screen.height;
        screenWidth = screen.width;
        marginLeft_body = 0;
        marginTop_body = 0;
        angle = 0;
    }else if (orientation == 1) {
        screenHeight = screen.width;
        screenWidth = screen.height;
        marginLeft_body = screen.width;
        marginTop_body = 0;
        angle = 90;
    }else if (orientation == 2) {
        screenHeight = screen.height;
        screenWidth = screen.width;
        marginLeft_body = screen.width;
        marginTop_body = screen.height;
        angle = 180;
    }else if (orientation == 3) {
        screenHeight = screen.width;
        screenWidth = screen.height;
        marginLeft_body = 0;
        marginTop_body = screen.height;
        angle = 270;
    }

    if (versionTabloPage == 1)
        drawPage1(marginLeft_body, marginTop_body);
    if (versionTabloPage == 2)
        drawPage2(marginLeft_body, marginTop_body);
}

function drawPage1(marginLeft_body, marginTop_body) {
    var html = '<div id="talon_to_casement">' +
        '<table cellpadding="0px" cellspacing="0px">' +
        '<tr>' +
        '<td id="head_talon_to_casement_1">В работе' +
        '</td>' +
        '<td id="head_talon_to_casement_2" align="center">' +
        '<div id="head_casement">окно</div>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '<div id="lines_to_casement"></div>' +
        '</div>' +

        '<div id="talon_in_queue">' +
        '<table cellpadding="0px" cellspacing="0px">' +
        '<tr>' +
        '<td id="head_talon_in_queue">В очереди' +
        '</tr>' +
        '</table>' +
        '<div id="lines_in_queue"></div>' +
        '</div>' +
        '<audio hidden controls id="sound_">' +
        '  <source src="/img/Ringtone.mp3" type="audio/mp3" >' +
        '</audio>';
    var body = document.getElementById('body');
    body.style.transform = 'rotate(' + angle + 'deg)';
    body.style.marginLeft = marginLeft_body + 'px';
    body.style.marginTop = marginTop_body + 'px';
    body.innerHTML = html;
}

function drawPage2(marginLeft_body, marginTop_body) {
    var html = '<div id="talon_to_casement">' +
        '<table cellpadding="0px" cellspacing="0px">' +
        '<tr>' +
        '<td id="head_talon_to_casement_1">Клиент' +
        '</td>' +
        '<td id="head_talon_to_casement_2" align="center">' +
        '<div id="head_casement">окно</div>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '<div id="lines_to_casement"></div>' +
        '</div>' +

        '<div id="bigPic">' +
        '<img src="img/bigPic.jpg" id="bigPicImg">' +
        '<img src="img/logo.png" id="logo">' +
        '</div>' +

        '<div id="runLine">' +
        '<span id="runLineText"></span>' +
        '</div>' +

        '<audio hidden controls id="sound_">' +
        '  <source src="/img/Ringtone.mp3" type="audio/mp3" >' +
        '</audio>';
    var body = document.getElementById('body');
    body.style.transform = 'rotate(' + angle + 'deg)';
    body.style.marginLeft = marginLeft_body + 'px';
    body.style.marginTop = marginTop_body + 'px';
    body.innerHTML = html;
}

function calculateParametricParameters1(landscape) {
    //параметрические переменные
    body_padding = screen.height * 0.01;
    body_padding_2x = body_padding * 2;
    lines_padding = body_padding;
    lines_padding_2x = lines_padding * 2;

    if (landscape) {
        height_line = ((screenHeight - body_padding_2x) / (lines + 1));
        head_borderRadius = height_line * 0.2;
        width_talon_to_casement = ((screenWidth - (body_padding * 3)) / 2);
        width_talon_in_queue = width_talon_to_casement;
        height_talon_to_casement = (screenHeight - body_padding_2x);
        height_talon_in_queue = height_talon_to_casement;
        width_talon_to_casement_column2 = ((height_line / 3) * (200/68));
        width_talon_to_casement_column1 = (width_talon_to_casement - width_talon_to_casement_column2);
        font_size_HeadTalonToCasement1 = height_line * 0.6;
        font_size_HeadTalonToCasement2 = (height_line / 3);
        marginLeft_talon_to_casement = 0 + body_padding;
        marginLeft_talon_in_queue = width_talon_to_casement + body_padding_2x;
        marginTop_talon_to_casement = 0 + body_padding;
        marginTop_talon_in_queue = 0 + body_padding;
        font_size_LinesToCasement = font_size_HeadTalonToCasement1 ;
    } else {
        height_line = (((screenHeight - (body_padding * 3)) / 2) / (lines + 1));
        head_borderRadius = height_line * 0.2;
        width_talon_to_casement = (screenWidth - body_padding_2x);
        width_talon_in_queue = width_talon_to_casement;
        height_talon_to_casement = ((screenHeight - (body_padding * 3)) / 2);
        height_talon_in_queue = height_talon_to_casement;
        width_talon_to_casement_column2 = ((height_line / 3) * (200/68));
        width_talon_to_casement_column1 = (width_talon_to_casement - width_talon_to_casement_column2);
        font_size_HeadTalonToCasement1 = height_line * 0.6;
        font_size_HeadTalonToCasement2 = (height_line / 3);
        marginLeft_talon_to_casement = 0 + body_padding;
        marginLeft_talon_in_queue = marginLeft_talon_to_casement;
        marginTop_talon_to_casement = 0 + body_padding;
        marginTop_talon_in_queue = height_talon_to_casement + body_padding_2x;
        font_size_LinesToCasement = font_size_HeadTalonToCasement1 ;
    }
}

function calculateParametricParameters2(landscape) {
    //параметрические переменные
    body_padding = screen.height * 0.01;
    body_padding_2x = body_padding * 2;
    lines_padding = body_padding;
    lines_padding_2x = lines_padding * 2;

    if (landscape) {
        height_line = ((screenHeight - body_padding_2x) / (lines + 1));
        height_runLine = height_line;
        height_bigPic = screenHeight - (body_padding * 3) - height_runLine;
        width_bigPic = 4*height_bigPic/3;
        width_runLine = width_bigPic;

        height_logo = height_bigPic * 0.07;
        height_bicPicImg = height_bigPic;

        head_borderRadius = height_line * 0.2;
        width_talon_to_casement = ((screenWidth - width_bigPic - (body_padding * 3)));
        height_talon_to_casement = (screenHeight - body_padding_2x);
        width_talon_to_casement_column2 = ((height_line / 3) * (200/68));
        width_talon_to_casement_column1 = (width_talon_to_casement - width_talon_to_casement_column2);
        font_size_HeadTalonToCasement1 = height_line * 0.6;
        font_size_HeadTalonToCasement2 = (height_line / 3);


        marginLeft_talon_to_casement = width_bigPic + body_padding_2x;
        marginTop_talon_to_casement = body_padding;
        marginLeft_bicPic = body_padding;
        marginLeft_runLine = body_padding;
        marginTop_bicPic = body_padding;
        marginTop_runLine = height_bigPic + body_padding_2x;
        marginTop_bicPicImg = 0;
        marginLeft_bicPicImg = (width_bigPic / 2) - (width_bigPicImg / 2);
        marginLeft_logo = body_padding_2x;
        marginTop_logo = body_padding_2x;
        marginTop_spanRunLineText = (height_runLine / 2) - (font_size_runLine / 2);

        font_size_LinesToCasement = font_size_HeadTalonToCasement1 ;
        font_size_runLine = font_size_LinesToCasement;
    } else {
        width_bigPic = screenWidth - (body_padding_2x);
        height_bigPic = width_bigPic * 3 / 4;
        height_bicPicImg = height_bigPic;
        height_logo = height_bigPic * 0.07;
        height_line = (((screenHeight - (body_padding * 4)) - height_bigPic) / (lines + 2));
        height_runLine = height_line;
        width_runLine = width_bigPic;
        head_borderRadius = height_line * 0.2;
        width_talon_to_casement = width_bigPic;
        height_talon_to_casement = ((screenHeight - (body_padding * 4)) - height_bigPic - height_runLine);

        width_talon_to_casement_column2 = ((height_line / 3) * (200/68));
        width_talon_to_casement_column1 = (width_talon_to_casement - width_talon_to_casement_column2);
        font_size_HeadTalonToCasement1 = height_line * 0.6;
        font_size_HeadTalonToCasement2 = (height_line / 3);

        marginLeft_talon_to_casement = body_padding;
        marginTop_talon_to_casement = body_padding;
        marginTop_bicPic = body_padding_2x + height_talon_to_casement;
        marginLeft_bicPic = body_padding;
        marginTop_bicPicImg = 0;
        marginLeft_bicPicImg = 0;
        marginLeft_logo = body_padding;
        marginTop_logo = body_padding;
        marginTop_runLine = (body_padding * 3) + height_talon_to_casement + height_bigPic;
        marginLeft_runLine = body_padding;

        font_size_LinesToCasement = font_size_HeadTalonToCasement1 ;
        font_size_runLine = font_size_LinesToCasement;
    }
}

function set_styles() {
    var body = document.getElementById('body');
    var div_talon_to_casement = document.getElementById('talon_to_casement');
    var head_talon_to_casement1 = document.getElementById('head_talon_to_casement_1');
    var head_talon_to_casement2 = document.getElementById('head_talon_to_casement_2');
    var div_head_casement = document.getElementById('head_casement');

    if (versionTabloPage == 1) {
        var div_talon_in_queue = document.getElementById('talon_in_queue');
        var div_lines_in_queue = document.getElementById('lines_in_queue');
        var head_talon_in_queue = document.getElementById('head_talon_in_queue');
    }

    if (versionTabloPage == 2) {
        var div_bigPic = document.getElementById('bigPic');
        var div_runLine = document.getElementById('runLine');
        var img_bigPicImg = document.getElementById('bigPicImg');
        var img_logo = document.getElementById('logo');
        var span_runLineText = document.getElementById('runLineText');
    }
    var landscape = screenWidth > screenHeight;

    //Вычисление параметрических переменных
    if (versionTabloPage == 1)
        calculateParametricParameters1(landscape);
    if (versionTabloPage == 2)
        calculateParametricParameters2(landscape);


    //body
    //body.style.padding = '' + body_padding + 'px';

    //Спиок назначенных талонов
    div_talon_to_casement.style.width = '' + width_talon_to_casement + 'px';
    div_talon_to_casement.style.height = '' + height_talon_to_casement + 'px';
    div_talon_to_casement.style.position = 'absolute';
    div_talon_to_casement.style.marginLeft = '' + marginLeft_talon_to_casement + 'px';
    div_talon_to_casement.style.marginTop = '' + marginTop_talon_to_casement + 'px';

    //Заголовок списка назначенных талонов первая колонка
    head_talon_to_casement1.style.width = '' + (width_talon_to_casement_column1 - lines_padding_2x) + 'px';
    head_talon_to_casement1.style.height = '' + (height_line - lines_padding_2x) + 'px';
    head_talon_to_casement1.style.font = '' + font_size_HeadTalonToCasement1 + 'px SFProDisplay-Regular';
    head_talon_to_casement1.style.backgroundColor = color_backgroundHead;
    head_talon_to_casement1.style.borderRadius = '' + head_borderRadius + 'px' + ' 0 0 0';
    head_talon_to_casement1.style.padding = '' + lines_padding + 'px';

    //Заголовок списка назначенных талонов вторая колонка
    head_talon_to_casement2.style.width = '' + width_talon_to_casement_column2 + 'px';
    head_talon_to_casement2.style.height = '' + (height_line - lines_padding_2x) + 'px';
    head_talon_to_casement2.style.font = '' + font_size_HeadTalonToCasement2 + 'px SFProDisplay-Regular';
    head_talon_to_casement2.style.backgroundColor = color_backgroundHead;
    head_talon_to_casement2.style.borderRadius = '0 ' + head_borderRadius + 'px 0 0';
    head_talon_to_casement2.style.padding = '0 px';

    //Надпись окно в заголовке
    div_head_casement.style.backgroundColor = color_backgroundHCasement;
    div_head_casement.style.width = '' + (width_talon_to_casement_column2) + 'px';

    if (versionTabloPage == 1) {

        //Спиок талонов в очереди
        div_talon_in_queue.style.width = '' + width_talon_in_queue + 'px';
        div_talon_in_queue.style.height = '' + height_talon_to_casement + 'px';
        div_talon_in_queue.style.position = 'absolute';
        div_talon_in_queue.style.marginLeft = '' + marginLeft_talon_in_queue + 'px';
        div_talon_in_queue.style.marginTop = '' + marginTop_talon_in_queue + 'px';

        //Заголовок списка талонов в очереди
        head_talon_in_queue.style.width = '' + (width_talon_in_queue - lines_padding_2x) + 'px';
        head_talon_in_queue.style.height = '' + (height_line - lines_padding_2x) + 'px';
        head_talon_in_queue.style.font = '' + font_size_HeadTalonToCasement1 + 'px SFProDisplay-Regular';
        head_talon_in_queue.style.backgroundColor = color_backgroundHead;
        head_talon_in_queue.style.borderRadius = '' + head_borderRadius + 'px ' + head_borderRadius + 'px 0 0';
        head_talon_in_queue.style.padding = '' + lines_padding + 'px';

        //Список талонов в очереди
        div_lines_in_queue.style.marginTop = '' + lines_padding + 'px';
    }

    if (versionTabloPage == 2) {
        //Блок картинки
        div_bigPic.style.width = width_bigPic + 'px';
        div_bigPic.style.height = height_bigPic + 'px';
        div_bigPic.style.marginTop = marginTop_bicPic + 'px';
        div_bigPic.style.marginLeft = marginLeft_bicPic + 'px';

        //Большая картинка
        //img_bigPicImg.style.width = width_bigPicImg;
        img_bigPicImg.style.height = height_bicPicImg + 'px';
        img_bigPicImg.style.marginLeft = marginTop_bicPicImg + 'px';
        img_bigPicImg.style.marginTop = marginTop_bicPicImg + 'px';

        //Логтип
        img_logo.style.height = height_logo + 'px';
        img_logo.style.marginLeft = marginLeft_logo + 'px';
        img_logo.style.marginTop = marginTop_logo + 'px';

        //Бегущаяя строка
        div_runLine.style.height = height_runLine + 'px';
        div_runLine.style.width = width_runLine + 'px';
        div_runLine.style.marginTop = marginTop_runLine + 'px';
        div_runLine.style.marginLeft = marginLeft_runLine + 'px';
        div_runLine.style.font = '' + font_size_runLine + 'px SF Pro Display';
        div_runLine.style.color = color_fontRunLine;
        span_runLineText.style.marginTop = marginTop_spanRunLineText + 'px';
        updateRunLine();
    }
}

function draw_talon_to_casement(talons) {

    var update = checkLines(talons);
    if (!update)
        return;
    var html = '';
    //for (var i = Math.min(length_lines, lines) - 1; i >= 0; i--) {
    for (var i = 0; i < Math.min(length_lines, lines); i++) {
        html +=
            '<div style="height: ' + (height_line - lines_padding) + 'px; ' +
            'background-color: ' + (i % 2 == 0 ? color_backLine1 : color_backLine2) + '; ' +
            'width: ' + (width_talon_to_casement_column1 - (lines_padding / 2)) + 'px; ' +
            'font: ' + font_size_LinesToCasement + 'px SFProDisplay-Regular; ' +
            'border-radius: ' + (height_line * 0.1) + 'px 0 0 ' + (height_line * 0.1) +'px; ' +
            'position: absolute; ' +
            'margin-left: ' + 0 + 'px; ' +
            'margin-top: ' + (lines_padding + (height_line * i)) + 'px;' +
            '"><div style="position: absolute;' +
            'margin-top: ' + ((height_line - lines_padding) * 0.08) + 'px; ' +
            'margin-left: ' + ((height_line - lines_padding) * 0.15) + 'px; ' +
            '"' +
            'id="column1_' + arr_ticketsToCasement[length_lines - 1 - i]['id'] + '">' +
            arr_ticketsToCasement[length_lines - 1 - i]['name'] +
            '</div>' +
            '<img src="/img/arrow2.png" style="position: absolute;' +
            'margin-top: ' + ((height_line - lines_padding) * 0.1) + 'px; ' +
            'margin-left: ' + ((width_talon_to_casement_column1 - (lines_padding / 2)) -
                                ((height_line - lines_padding) * 0.15) -
                                (((height_line - lines_padding) * 0.8) * 1.28)) + 'px; ' +
            'height: ' + ((height_line - lines_padding) * 0.8) + 'px; ' +
            '">' +
            '</div>' +
            '<div style="height: ' + (height_line - lines_padding) + 'px; ' +
            'background-color: ' + (i % 2 == 0 ? color_backLine1 : color_backLine2) + '; ' +
            'width: ' + (width_talon_to_casement_column2) + 'px; ' +
            'font: ' + font_size_LinesToCasement + 'px SFProDisplay-Regular; ' +
            'border-radius: 0 ' + (height_line * 0.1) + 'px ' + (height_line * 0.1) +'px 0; ' +
            'position: absolute; ' +
            'margin-left: ' + (width_talon_to_casement - width_talon_to_casement_column2) +  'px; ' +
            'margin-top: ' + (lines_padding + (height_line * i)) + 'px;' +
            '" align="center"><div style="position: absolute;' +
            'margin-top: ' + ((height_line - lines_padding) * 0.08) + 'px; ' +
            'margin-left: ' + ((width_talon_to_casement_column2 / 2) -
                (font_size_LinesToCasement * 0.58 * (String(arr_ticketsToCasement[length_lines - 1 - i]['casement'])).length / 2)) +  'px; ' +
            '" align="center"' +
            'id="column2_' + arr_ticketsToCasement[length_lines - 1 - i]['id'] + '">' +
            arr_ticketsToCasement[length_lines - 1 - i]['casement'] +
            '</div>' +
             '</div>';
    }
    document.getElementById('lines_to_casement').innerHTML = html;
}


function draw_talon_in_queue(talons) {
    var linesPadding = lines_padding / 2;
    var heightLine = (height_line / 1.5) - linesPadding;
    var widthLine = width_talon_in_queue / 2;
    var countTalonsInColumn = Math.floor((height_talon_in_queue - height_line) / heightLine);
    var html = '';
    for (var i = 0; i < Math.min(talons.length, countTalonsInColumn); i++) {
        html += '<div style="width: ' + (widthLine - linesPadding) + 'px; ' +
            'height: ' + (heightLine - linesPadding) + 'px; ' +
            'margin-top: ' + (heightLine * i)  + 'px; ' +
            'position: absolute;' +
            'color: ' + color_fontInQueue + '; ' +
            '">' +
            '<div style="width: ' + (heightLine * 0.1) + 'px; ' +
            'height: ' + (heightLine - linesPadding) + 'px; ' +
            'border-radius: ' + (heightLine * 0.1) + 'px 0 0 ' + (heightLine * 0.1) + 'px ; ' +
            'background-color: ' + (i % 2 == 0 ? color_backLineInQueue1 : color_backLineInQueue2) + '; ' +
            'margin-top: 0px; ' +
            'margin-left: 0px; ' +
            'position: absolute;' +
            '"></div>' +
            '<div style="font: ' + (heightLine * 0.6) + 'px SFProDisplay-Regular; ' +
            'position: absolute; ' +
            'margin-top: ' + (heightLine * 0.1) + 'px; ' +
            'margin-left: ' + ((heightLine * 0.1) + (linesPadding * 2)) + 'px; ' +
            '">   ' + talons[i]['name'] + '</div>' +
            '</div>';
    }

    if (talons.length > countTalonsInColumn) {
        for (var i = 0; i < Math.min((talons.length - countTalonsInColumn), countTalonsInColumn); i++) {
            html += '<div style="width: ' + (widthLine - linesPadding) + 'px; ' +
                'height: ' + (heightLine - linesPadding) + 'px; ' +
                'margin-top: ' + (heightLine * i) + 'px; ' +
                'margin-left: ' + (widthLine + linesPadding) + 'px; ' +
                'position: absolute;' +
                'color: ' + color_fontInQueue + '; ' +
                '">' +
                '<div style="width: ' + (heightLine * 0.1) + 'px; ' +
                'height: ' + (heightLine - linesPadding) + 'px; ' +
                'border-radius: ' + (heightLine * 0.1) + 'px 0 0 ' + (heightLine * 0.1) + 'px ; ' +
                'background-color: ' + (i % 2 == 0 ? color_backLineInQueue2 : color_backLineInQueue1) + '; ' +
                'margin-top: 0px; ' +
                'margin-left: 0px; ' +
                'position: absolute;' +
                '"></div>' +
                '<div style="font: ' + (heightLine * 0.6) + 'px SFProDisplay-Regular; ' +
                'position: absolute; ' +
                'margin-top: ' + (heightLine * 0.1) + 'px; ' +
                'margin-left: ' + ((heightLine * 0.1) + (linesPadding * 2)) + 'px; ' +
                '">   ' + talons[i]['name'] + '</div>' +
                '</div>';
        }
    }

    document.getElementById('lines_in_queue').innerHTML = html;
}

function update_data() {
    ajax_get('/rest/tablo/getListTickets', false, function (talons) {

        if (!talons.active) {
            wait_active();
            return
        }

        draw_talon_to_casement(talons['talonsToCasement']);
        if (versionTabloPage == 1) {
            draw_talon_in_queue(talons['talonsInQueue']);
        }


        if (talons.update) {
            window.location.reload();
        }

    });
    setTimeout(update_data,5 * 1000);
}

function startRunLine() {
    runLineWorked = true;
    drawerRunLine = setInterval(drawRunLine, 1000 / fpsTextLine);
}

function stopRunLine() {
    runLineWorked = false;
    if (drawerRunLine != null) {
        clearInterval(drawerRunLine);
    }
}

function getRunLine() {
    ajax_get('/rest/tablo/getRunLines', true, function (data) {

        var oldRunLineActive = runLineActive;
        runLineActive = data['active'];
        runLines = data['runLines'];

        if (oldRunLineActive != runLineActive)
            updateRunLine();
    });
}

function updateRunLine() {
    stopRunLine();
    if (runLineUpdater == null) {
        getRunLine();
        runLineUpdater = setInterval(getRunLine, 1000 * 60 * 20);
    }
    if (runLineActive == true && runLines.length > 0) {
        if (currentRunLine >= runLines.length)
            currentRunLine = 0;
        var runLineText = runLines[currentRunLine];
        var span_runLineText = document.getElementById('runLineText');
        span_runLineText.innerText = runLineText;
        widthText = span_runLineText.offsetWidth;
        currentPXText = 0;
        endPXText = widthText + width_runLine;
        span_runLineText.style.marginLeft = (width_runLine + 10) + 'px';
        currentRunLine++;
        startRunLine();
    }

}

function drawRunLine() {
    if (runLineWorked == true) {
        currentPXText += speedTextLine;
        document.getElementById('runLineText').style.marginLeft = (width_runLine - currentPXText) + 'px';
        if (currentPXText > endPXText) {
            updateRunLine();
        }
    }
}

function draw_notWorking() {
    var html = '<div align="center" style="position: absolute;' +
        'margin-top: ' + ((screenHeight / 2) - (screenHeight * 0.1)) + 'px; ' +
        'width: ' + (screenWidth) + 'px;' +
        'color: ' + color_fontInQueue + '; ' +
        'font: ' + (screenHeight * 0.1) + 'px SFProDisplay-Regular;">' +
        'Электронная очередь временно не работает.' +
        '</div>';
    document.getElementById('body').innerHTML = html;
}

function wait_active() {
    draw_notWorking();
    setTimeout(init_page, 30 * 1000);
}

function init_page() {
    //alert('init');
    ajax_get('/rest/tablo/initTablo', true, function (data) {

        if (data == false || data['active'] == false){
            wait_active();
            return;
        }

        lines = data['countLines'];
        orientation = data['landscape'];
        arr_ticketsToCasement = new Array(lines);
        versionTabloPage = data['versionTabloPage'];
        draw_page();
        set_styles();
        update_data();
        setInterval(animation_render, 33);
        setInterval(play_sound, 500);
    });

}
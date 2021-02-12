var currentStatus;
var currentTicket;

var changeStatus = {
    'userId':'',
    'status':'',
    'casement':''
}

function isTalon(userId) {
    ajax_get('/rest/work_station/isTicket?idUser=' + userId, function (data) {
        if (data['ticket'] == true)
            drawClient(data['id']);
        else
            document.getElementById('client').style.display = 'none';
    });
}

function clickOptionStatus() {

    var select = document.getElementById('status_');
    var casement = document.getElementById('casement_').value;
    var changedStatus = select.options[select.selectedIndex].value;
    if (currentStatus != changedStatus) {
        changeStatus['idUser'] = userId;
        changeStatus['valueNewStatus'] = changedStatus;
        changeStatus['casement'] = casement;
        var json = JSON.stringify(changeStatus);
        ajax_post('/rest/work_station/setNewStatus', json, function (data) {
            if (data['status'] == 'ok')
                currentStatus = changedStatus;
        });
    }
}

function drawCasementStatus(userId) {
    var htmlCasementStatus = '';
    ajax_get('/rest/work_station/getStatuses?idUser=' + userId, function (data) {
            htmlCasementStatus += '<form id="CasementStatus"></form><table><tr>' +
                '<td class="casementStatus" id="labelCasement">Окно</td>' +
                '<td class="casementStatus" id="labelFullName">Ф. И. О. сотрудника</td>' +
                '<td class="casementStatus" id="labelStatus">Статус</td>' +
                '</tr><tr>';
            currentStatus = data['currentStatus']['value'];
            htmlCasementStatus += '<td class="casementStatus" id="casement"><input id="casement_"' +
                (currentStatus != 'NOT_WORKING_TIME' ? 'disabled' : '') +
                ' type="number" name="casement" value="' + data['casement'] + '"/></td>';
            htmlCasementStatus += '<td class="casementStatus" id="fullName"><input id="fullName_" disabled type="text" value="' +
                'nameManager' + '"/></td>';
            htmlCasementStatus += '<td class="casementStatus" id="status"><select name="status" id="status_" onchange="clickOptionStatus()"> ';
            data['statuses'].forEach(function (status, i) {
                htmlCasementStatus += '<option ' +
                    (status['value'] == data['currentStatus']['value'] ? 'selected' : '') +
                    ' value="' + status['value'] + '">' +
                    status['name'] + '</option>';
            });
            htmlCasementStatus += '</select></td>';
            htmlCasementStatus += '</tr></table></<form>'
            document.getElementById("casement_status").innerHTML = htmlCasementStatus;

    });
}

function startServiceClient(userID) {
    var req = {
        'idUser':userID
    }

    var body = JSON.stringify(req);

    ajax_post('/rest/work_station/startServiceClient', body, function (data) {
        if (data['status'] != 'ok')
            alert('Ошибка выполнения.');
    })

}

function endServiceClient(userID) {
    var req = {
        'idUser':userID
    }

    var body = JSON.stringify(req);

    ajax_post('/rest/work_station/endServiceClient', body, function (data) {
        if (data['status'] != 'ok')
            alert('Ошибка выполнения.');
    })

}

function drawElQueue(userId) {
    var htmlElQueue = '';
    ajax_get('/rest/work_station/getElQueue?idUser=' + userId, function (data) {
        htmlElQueue += '<div id="elQueueParent_"><span class="elQueue" id="elQueueText_">' +
            'Электронная очередь: ' + data['name'] +
            '</span><br/>' +
            '<span class="elQueue">В очереди ' + data['clientsInQueue'] + ' клиентов.</span></div>';
        document.getElementById("elQueue_").innerHTML = htmlElQueue;
    });
}

function drawClient(TicketId) {
    document.getElementById('client').style.display = 'block';
    var html = '';
    ajax_get('/rest/work_station/getTicket?idTicket=' + TicketId, function (data) {
        currentTicket = data;
        html += '<form>';

        html += 'Талон № <input disabled type="text" id="numberTalon" value="' + data['name'] + '"/>';
        html += '&nbsp; Услуга: <input disabled type="text" id="nameService" value="' + data['service'] + '"/>';
        html += '&nbsp; Контрагент: <input disabled type="text" id="nameContractor" value="' + (data['contractor'] == null ?
            '--' : data['contractor']['name']) + '"/>';
        html += '<br/>';
        html += '<input class="buttonClient" type="button" id="button_1" name="Посетитель пришел" value="Посетитель пришел"' +
            ' onclick="startServiceClient(\'' + userId + '\');"/>';
        html += '<input class="buttonClient" type="button" id="button_2" name="Посетитель ушел" value="Посетитель ушел"' +
            ' onclick="endServiceClient(\'' + userId + '\')"/>';
        html += '<input class="buttonClient" type="button" id="button_3" name="Поставить на паузу" value="Поставить на паузу"/>';
        //html += '<input class="buttonClient" type="button" id="button_4" name="Направить в другое окно" value="Направить в другое окно"/>';

        html += '</form>';
        document.getElementById('client').innerHTML = html;
    });
}

function draw_info(userId) {
    var html = '<h4>Талоны на паузе:</h4>';
    html += '<div id="table_paused_tickets"></div>';
    ajax_get('/rest/work_station/getTicketsPaused?idUser=' + userId, function (data) {
        html += '<table id="tickets_paused" border="2px" width="300px">';
        data.forEach(function (ticket, i) {
           html += '<tr>' +
               '<td>' + data['name'] + '</td>' +
               '</tr>';
        });
        if (data.size == 0) {
            html += '<tr><td>Талонов на паузе нет</td></tr>'
        }
        html += '</table>';

        document.getElementById('table_paused_tickets').innerHTML = html;
    });
    html += '<div id="buttons_paused">' +
        '<input type="button" class="buttonClient" id="button_4" name="Создать талон" value="Создать талон">' +
        '<input type="button" class="buttonClient" id="button_5" name="Вернуть в работу" value="Вернуть в работу">' +
        '</div>';
    document.getElementById('info_queue').innerHTML = html;
}

function updatePage(userId) {
    if (currentStatus != lastStatus) {
        drawElQueue(userId);
        drawCasementStatus(userId);
        isTalon(userId);
        draw_info(userId);
    }
    setTimeout(updatePage, 1*1000, userId);
}




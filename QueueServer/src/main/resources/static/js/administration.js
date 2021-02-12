var current_page = -1;
var current_queue_id;

var current_manager_data = null;
var current_queue_data = null;

var current_manager_lists = null;
var last_value_found;
var timerId;

var legendHTML = '<table id="legend" cellspacing="0" cellpadding="0">' +
    '<tr>' +
    '<td id="legend_queue" class="cell_legend" align="center">В очереди' +
    '<span id="countQueue"></span></td>' +
    '<td id="legend_wait" class="cell_legend" align="center">Ждет приема' +
    '<span id="countWait"></span></td>' +
    '<td id="legend_servicing" class="cell_legend" align="center">Выполняется прием' +
    '<span id="countService"></span></td>' +
    '</tr></table>';
var head_table_talonsHTML = '<table id="head_table_talons">' +
    '<thead>' +
    '<tr>' +
    '<td class="talons_casement">Р/м' +
    '</td>' +
    '<td class="talons_talon">Посититель' +
    '</td>' +
    '<td class="talons_status">Статус' +
    '</td>' +
    '<td class="talons_manager">Сотрудник' +
    '</td>' +
    '<td class="talons_date_queue">Постановка в очередь' +
    '</td>' +
    '<td class="talons_date_distrib">Ожидание приема' +
    '</td>' +
    '<td class="talons_date_service">Начало приема' +
    '</td>' +
    '</tr></thead>' +
    '<tbody id="ticket_list"></tbody>' +
    '</table>';
var head_table_managersHTML = '<table id="head_table_managers">' +
    '<thead>' +
    '<tr>' +
    '<td class="managers_casement">Р/м' +
    '</td>' +
    '<td class="managers_status">Статус' +
    '</td>' +
    '<td class="managers_manager">Сотрудник' +
    '</td>' +
    '<td class="managers_time">Время' +
    '</td>' +
    '</tr>' +
    '</thead>' +
    '<tbody id="manager_list"></tbody></table>';
var head_table_managersListHTML = '<table id="head_table_managersList">' +
    '<thead>' +
    '<tr>' +
    '<td class="managers_count">№' +
    '</td>' +
    '<td class="managers_managerName">Сотрудник' +
    '</td>' +
    '</tr></thead>' +
    '<tbody id="manager_listWithQueue"></tbodyid>' +
    '</table>';
var head_table_managersWithoutQueueListHTML = '<table id="head_table_managersList">' +
    '<thead>' +
    '<tr>' +
    '<td class="managers_count">№' +
    '</td>' +
    '<td class="managers_managerName">Сотрудник' +
    '</td>' +
    '</tr></thead>' +
    '<tbody id="manager_listWithoutQueue"></tbody> ' +
    '</table>';
var form_manager_settings = '<form id="form_manager_settings">' +
    '<table id="form_table">' +
    '<tr>' +
    '<td>Сотрудник</td>' +
    '<td id="name_manager">-</td>' +
    '</tr>' +
    '<tr>' +
    '<td>Текущий статус</td>' +
    '<td><select class="select_form_manager_settings" id="current_status" name="current_status"></select></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Рабочее окно</td>' +
    '<td><input type="number" name="casement" id="form_casement"></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Электронная очередь</td>' +
    '<td><select class="select_form_manager_settings" id="current_queue" name="current_queue"></select></td>' +
    '</tr>' +
    '<tr>' +
    '<td></td>' +
    '<td>Услуги:</td>' +
    '</tr>' +
    '<tr>' +
    '<td colspan="2">' +
    '<table id="table_services">' +
    '' +
    '</table> ' +
    '</td>' +
    '</tr>' +
    '<tr>' +
    '<td></td>' +
    '<td>' +
    '<input type="text" id="form_managerID" name="managerID" hidden>' +
    '<input type="button" name="save" value="Сохранить" onclick="save_data_manager()"></td></tr>' +
    '<tr id="form_tr_error" hidden>' +
    '<td class="error_mes" >Ошибка:</td>' +
    '<td class="error_mes" id="form_td_error"></td>' +
    '</tr>' +
    '</table> ' +
    '</form>';

var form_queue_settings = '<form id="form_queue_settings">' +
    '<table id="form_table">' +
    '<tr>' +
    '<td>Очередь активна:</td>' +
    '<td><input type="checkbox" name="queue_active"></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Количество талонов для информирования:</td>' +
    '<td><input type="number" name="quantityInform"></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Периодичность информирования:</td>' +
    '<td><input type="number" name="timeInform"></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Киоски:</td>' +
    '<td><table id="queue_settings_kiosks"></table></td>' +
    '</tr>' +
    '<tr>' +
    '<td>Табло:</td>' +
    '<td><table id="queue_settings_tablos"></table></td>' +
    '</tr>' +
    '<tr>' +
    '<td></td>' +
    '<td>' +
    '<input type="button" name="save" value="Сохранить" onclick="save_data_queue()"></td></tr>' +
    '<tr id="form_tr_error" hidden>' +
        '<td class="error_mes" >Ошибка:</td>' +
        '<td class="error_mes" id="form_td_error"></td>' +
    '</tr>' +
    '</table> ' +
    '</form>';

function draw_page_0() {
    var html = '<div id="page_0_left">';
    html += legendHTML;
    html += head_table_talonsHTML;
    //html += '<div id="ticket_list"></div>';
    html += '<div id="devices_list"></div>'
    html += '</div>';
    html += '<div id="page_0_right" align="center">';
    html += head_table_managersHTML;
    //html += '<div id="manager_list"></div>';
    html += '</div>';

    document.getElementById('body_page').innerHTML = html;

    var left = document.getElementById('page_0_left');
    var right = document.getElementById('page_0_right');

    left.style.width = (screen.width * 0.65) + 'px';
    left.style.height = (screen.height - 440) + 'px';

    right.style.width = (screen.width * 0.34) + 'px';
    right.style.height = (screen.height - 440) + 'px';
    right.style.marginLeft = (screen.width * 0.65) + 'px';
}

function draw_page_1() {
    var html = '<div id="page_0_left">';
    html += form_manager_settings;
    html += '</div>';
    html += '<div id="page_0_right" align="center">';
    html += '<div id="div_find_user">' +
        '<table>' +
        '<tr>' +
        '<td>' +
        '<img src="/img/find.png" id="png_find">' +
        '</td>' +
        '<td>' +
        '<input type="text" name="name" id="nameUserFind" onblur="focusFindUser(false)" onfocus="focusFindUser(true)">' +
        '</td>' +
        '<td>' +
        '<img src="/img/clear.png" id="png_clear" onclick="clear_findInput()">' +
        '</td></tr></table>' +
        '</div>'
    html += '<span class="header3">Сотрудники выбранной очереди</span>';
    html += head_table_managersListHTML;
    html += '<div id="manager_listWithQueue"></div>';
    html += '<br/><br/><br/>';
    html += '<span class="header3">Сотрудники без очереди</span>';
    html += head_table_managersWithoutQueueListHTML;
    html += '<div id="manager_listWithoutQueue"></div>';
    html += '</div>';

    document.getElementById('body_page').innerHTML = html;
    var left = document.getElementById('page_0_left');
    var right = document.getElementById('page_0_right');

    left.style.width = ((screen.width * 0.65)-50) + 'px';
    left.style.height = (screen.height - 440) + 'px';
    left.style.marginLeft = '50px';

    right.style.width = (screen.width * 0.34) + 'px';
    right.style.height = (screen.height - 440) + 'px';
    right.style.marginLeft = ((screen.width * 0.65)-50) + 'px';
    update_page_1();
}

function draw_page_2() {
    var html = form_queue_settings;

    document.getElementById('body_page').innerHTML = html;
    update_page_2();
}

function getClassLine(statusId) {
    if (statusId == 0)
        return 'legend_queue';
    if (statusId == 1)
        return 'legend_wait';
    if (statusId == 2)
        return 'legend_servicing';
    if (statusId == 'NOT_WORKING_TIME')
        return 'legend_queue';
    if (statusId == 'INDIVIDUAL_TIME' || statusId == 'WAIT_CLIENT')
        return 'legend_wait';
    if (statusId == 'SERVICING_CLIENT' || statusId == 'SERVICING_REGULAR_CLIENT' || statusId == 'WORKING_TIME' || statusId == 'RECEPTION_EXPEDITION')
        return 'legend_servicing';
}

function update_page() {
    if (current_page == 0)
        update_page_0();
}

function update_page_0() {
    var ticket_list = document.getElementById('ticket_list');
    var manager_list = document.getElementById('manager_list');
    ajax_get("/rest/administration_page/get_status_page?queue_id="+current_queue_id, function (statusData) {
        // var htmlTickets = '<table id="tickets_data">';
        // var htmlManagers = '<table id="managers_data">';
        var htmlTickets = '';
        var htmlManagers = '';
        var countQueue = 0;
        var countWait = 0;
        var countService = 0;
        for (let i = 0; i < statusData['tickets'].length; i++) {
            var line = statusData['tickets'][i];
            htmlTickets += '<tr class="' + getClassLine(line.status) + ' line_admin">' +
                '<td class="talons_casement">' + line.casement +
                '</td>' +
                '<td class="talons_talon">' + line.ticket +
                '</td>' +
                '<td class="talons_status">' + line.statusName +
                '</td>' +
                '<td class="talons_manager">' + line.manager +
                '</td>' +
                '<td class="talons_date_queue">' + line.dateQueue +
                '</td>' +
                '<td class="talons_date_distrib">' + line.dateDistrib +
                '</td>' +
                '<td class="talons_date_service">' + line.dateService +
                '</td>' +
                '</tr>';
            if (line.status == 0)
                countQueue++;
            if (line.status == 1)
                countWait++;
            if (line.status == 2)
                countService++;

        }
        //htmlTickets += '</table>';

        for (let i = 0; i < statusData['managers'].length; i++) {
            var line = statusData['managers'][i];
            htmlManagers += '<tr class="' + getClassLine(line.status) + ' line_admin">' +
                '<td class="managers_casement">' + line.casement +
                '</td>' +
                '<td class="managers_status">' + line.statusName +
                '</td>' +
                '<td class="managers_manager">' + line.name +
                '</td>' +
                '<td class="managers_time">' + line.time +
                '</td>' +
                '</tr>';
        }
        //htmlManagers += '</table>';

        var deviceHTML = '<table id="devices">';
        var lineDevices = '';
        var lineNames = '';


        let devices = new Array();
        for (let i = 0; i < statusData.kiosks.length; i++) {
            var device = {
                'type': 'kiosk',
                'name':'',
                'statusDevice':'',
                'message':''
            }
            device.name = statusData.kiosks[i].name;
            device.statusDevice = statusData.kiosks[i].statusDevice;
            device.message = statusData.kiosks[i].message;
            devices.push(device);
        }
        for (let i = 0; i < statusData.tablos.length; i++) {
            var device = {
                'type': 'tablo',
                'name':'',
                'statusDevice':'',
                'message':''
            }
            device.name = statusData.tablos[i].name;
            device.statusDevice = statusData.tablos[i].statusDevice;
            device.message = statusData.tablos[i].message;
            devices.push(device);
        }

        var countDevices = devices.length;
        var width = screen.width * 0.7;
        var columns = parseInt(width / 102);
        var rows = countDevices / columns;
        rows = parseInt(rows) == rows ? rows : parseInt(rows) + 1;

        for (let i = 0; i < rows; i++) {
            //deviceHTML += '<tr>';
            lineDevices = '<tr>';
            lineNames = '<tr>';

            for (let j = 0; j < Math.min(columns, devices.length - (columns * i)); j++) {
                var index = i * columns + j;
                lineDevices += '<td class="td_device" align="center">' +
                    '<div class="div_device_' + devices[index].statusDevice + ' block_device"  align="center">' +
                    '<img class="device_logo" src="' + (devices[index].type == 'kiosk' ? 'img/kiosk_pic.png' : 'img/tablo_pic.png') + '"' +
                    'title="' + devices[index].message + '"></div></td>';
                lineNames += '<td class="td_device_name" align="center"><div  align="center">' + devices[index].name + '</div>' +
                    '</td>'
            }

            lineDevices += '</tr>';
            lineNames += '</tr>';

            deviceHTML += lineDevices + lineNames;
        }

        deviceHTML += '</table>';

        document.getElementById('countQueue').innerText = ' - ' + countQueue + ' чел.';
        document.getElementById('countWait').innerText = ' - ' + countWait + ' чел.';
        document.getElementById('countService').innerText = ' - ' + countService + ' чел.';

        document.getElementById('devices_list').innerHTML = deviceHTML;
        ticket_list.innerHTML = htmlTickets;
        manager_list.innerHTML = htmlManagers;
    });
}

function update_page_1() {

    ajax_get("/rest/administration_page/get_lists_managers?queue_id="+current_queue_id, function (managersData) {
        current_manager_lists = managersData;
        updateListUsers('');
    });
}

function updateListUsers(otbor) {
    if (current_manager_lists == null)
        return;
    var managers_queue = [];
    var mangers_withoutQueue = [];
    var manager_listWithQueue = document.getElementById('manager_listWithQueue');
    var manager_listWithoutQueue = document.getElementById('manager_listWithoutQueue');
    if (otbor == '') {
        managers_queue = current_manager_lists['withQueueList'];
        mangers_withoutQueue = current_manager_lists['withoutQueueList'];
    }else {
        for (let i = 0; i < current_manager_lists['withQueueList'].length; i++) {
            if (current_manager_lists['withQueueList'][i].name.toLowerCase().indexOf(otbor.toLowerCase()) == 0) {
                managers_queue.push(current_manager_lists['withQueueList'][i]);
            }
        }
        for (let i = 0; i < current_manager_lists['withoutQueueList'].length; i++) {
            if (current_manager_lists['withoutQueueList'][i].name.toLowerCase().indexOf(otbor.toLowerCase()) == 0) {
                mangers_withoutQueue.push(current_manager_lists['withoutQueueList'][i]);
            }
        }
    }

    var htmlManagersWithQueue = '';
    var htmlManagersWithoutQueue = '';
    for (let i = 0; i < managers_queue.length; i++) {
        var line = managers_queue[i];
        htmlManagersWithQueue += '<tr onclick="draw_manager(' + line.id + ')">' +
            '<td class="managers_count">' +
            line.count +
            '</td>' +
            '<td class="managers_managerName">' +
            line.name +
            '</td>' +
            '</tr>';
    }
    //htmlManagersWithQueue += '</table>';

    for (let i = 0; i < mangers_withoutQueue.length; i++) {
        var line = mangers_withoutQueue[i];
        htmlManagersWithoutQueue += '<tr onclick="draw_manager(' + line.id + ')">' +
            '<td class="managers_count">' +
            line.count +
            '</td>' +
            '<td class="managers_managerName">' +
            line.name +
            '</td>' +
            '</tr>';
    }
    //htmlManagersWithoutQueue += '</table>';
    manager_listWithQueue.innerHTML = htmlManagersWithQueue;
    manager_listWithoutQueue.innerHTML = htmlManagersWithoutQueue;
}

function update_page_2() {
    ajax_get("/rest/administration_page/get_queue_data?queue_id="+current_queue_id, function (queueData){
        var form = document.getElementById('form_queue_settings');
        form.queue_active.checked = queueData.active;
        form.quantityInform.value = queueData.quantityInform;
        form.timeInform.value = queueData.timeInform;

        var kiosksHTML = '<tr>' +
            '<td class="kiosk_table_1">Имя кисока</td>' +
            '<td class="kiosk_table_2">Комментарий</td>' +
            '<td class="kiosk_table_3">Активен</td>' +
            '</tr>';
        for (let i = 0; i < queueData.kiosks.length; i++) {
            var kiosk = queueData.kiosks[i];
            kiosksHTML += '<tr>' +
                '<td class="kiosk_table_1">' +
                kiosk.name +
                '</td>' +
                '<td class="kiosk_table_2">' +
                kiosk.comment +
                '</td>' +
                '<td class="kiosk_table_3" align="center">' +
                '<input type="checkbox" name="kiosk_active_' + kiosk.id + '" ' +
                (kiosk.active ? 'checked' : '') + '>' +
                '</td>' +
                '</tr>';
        }

        var tablosHTML = '<tr>' +
            '<td class="tablo_table_1">Имя кисока</td>' +
            '<td class="tablo_table_2">Комментарий</td>' +
            '<td class="tablo_table_3">Количество строк</td>' +
            '<td class="tablo_table_4">Активен</td>' +
            '</tr>';
        for (let i = 0; i < queueData.tablos.length; i++) {
            var tablo = queueData.tablos[i];
            tablosHTML += '<tr>' +
                '<td class="tablo_table_1">' +
                tablo.name +
                '</td>' +
                '<td class="tablo_table_2">' +
                tablo.comment +
                '</td>' +
                '<td class="tablo_table_3" align="center">' +
                '<input type="number" name="tablo_countLinesOnPage_' + tablo.id + '"' +
                'value="' + tablo.countLinesOnPage + '">' +
                '</td>' +
                '<td class="tablo_table_4" align="center">' +
                '<input type="checkbox" name="tablo_active_' + tablo.id + '" ' +
                (tablo.active ? 'checked' : '') + '>' +
                '</td>' +
                '</tr>';
        }

        document.getElementById('queue_settings_kiosks').innerHTML = kiosksHTML;
        document.getElementById('queue_settings_tablos').innerHTML = tablosHTML;
        current_queue_data = queueData;
    });
}

function draw_manager(id_manager) {
    draw_error(false, '');
    ajax_get("/rest/administration_page/get_manager_data?manager_id="+id_manager+
        "&currentManager=" + userId, function (managersData) {
        var name_managerHTML = managersData.nameManager;
        var select_statusHTML = '';
        var current_status = managersData.statuses.currentStatus.value;
        for (let i = 0; i < managersData.statuses.statuses.length; i++) {
            var status = managersData.statuses.statuses[i];
            select_statusHTML += '<option value="' +
                status.value + '"' +
                (status.value == current_status ? ' selected' : '') + '>' +
                status.name +
                '</option>';
        }

        var current_queue = managersData.queues.current_queue;
        var select_queueHTML = '<option value="' +
            -1 + '"' +
            (current_queue == -1 ? ' selected' : '') + '>' +
            '---' +
            '</option>';

        for (let i = 0; i < managersData.queues.queues.length; i++) {
            var queue = managersData.queues.queues[i];
            select_queueHTML += '<option value="' +
                queue.value + '"' +
                (queue.value == current_queue ? ' selected' : '') + '>' +
                queue.name +
                '</option>';
        }

        var table_servicesHTML = '';
        for (let i = 0; i < managersData.services.length; i++) {
            var service = managersData.services[i];
            table_servicesHTML += '<tr>' +
                '<td>' +
                '<input type="checkbox" name="check_' + service.id + '" ' +
                (service.selected ? ' checked' : '') + '>' +
                '</td>' +
                '<td>' + service.name + '</td>' +
                '</tr>';
        }
        document.getElementById('form_casement').value = managersData.statuses.casement;
        document.getElementById('form_managerID').value = managersData.managerID;
        document.getElementById('name_manager').innerHTML = name_managerHTML;
        document.getElementById('current_status').innerHTML = select_statusHTML;
        document.getElementById('current_queue').innerHTML = select_queueHTML;
        document.getElementById('table_services').innerHTML = table_servicesHTML;
        current_manager_data = managersData;
    });
}

function save_data_manager() {
    var form = document.getElementById('form_manager_settings');

    var save_data = {
        'managerID':'',
        'currentStatus':'',
        'casement':'',
        'current_queue':'',
        'services':''
    }
    save_data.managerID = current_manager_data.managerID;
    save_data.currentStatus = form.current_status.value;
    save_data.casement = form.casement.value;
    save_data.current_queue = form.current_queue.value;
    save_data.services = current_manager_data.services;

    for (let i = 0; i < save_data.services.length; i++) {
        save_data.services[i].selected = form['check_' + save_data.services[i].id].checked;
    }
    var body = JSON.stringify(save_data);
    ajax_post('/rest/administration_page/saveManagersData', body, function (data) {
        if (data == 'ok' || data == null) {
            draw_error(true, 'Данные сохранены');
            update_page_1();
        }else {
            var error = 'Ошибка ' + data.status + '.\n';
            error += data.response.message;
            draw_error(true, error);
        }
    });
}

function save_data_queue() {
    var form = document.getElementById('form_queue_settings');

    var save_data = {
        'queueId':'',
        'active':'',
        'quantityInform':'',
        'timeInform':'',
        'kiosks':'',
        'tablos':''
    }
    save_data.queueId = current_queue_id;
    save_data.active = form.queue_active.checked;
    save_data.quantityInform = form.quantityInform.value;
    save_data.timeInform = form.timeInform.value;

    save_data.kiosks = current_queue_data.kiosks;
    save_data.tablos = current_queue_data.tablos;

    for (let i = 0; i < save_data.kiosks.length; i++) {
        save_data.kiosks[i].active = form['kiosk_active_' + save_data.kiosks[i].id].checked;
    }

    for (let i = 0; i < save_data.tablos.length; i++) {
        save_data.tablos[i].countLinesOnPage = form['tablo_countLinesOnPage_' + save_data.tablos[i].id].value;
        save_data.tablos[i].active = form['tablo_active_' + save_data.tablos[i].id].checked;
    }
    var body = JSON.stringify(save_data);
    ajax_post('/rest/administration_page/saveQueueData', body, function (data) {
        if (data == 'ok' || data == null) {
            draw_error(true, 'Данные сохранены');
            update_page_1();
        }else {
            var error = 'Ошибка ' + data.status + '.\n';
            error += data.response.message;
            draw_error(true, error);
        }
    });
}

function draw_error(visible, error) {
    var tr_error = document.getElementById('form_tr_error');
    var td_error = document.getElementById('form_td_error');
    tr_error.hidden = !visible;
    td_error.innerText = error;
}

function all_page_non_target() {
    var page_0 = document.getElementById('page_0');
    var page_1 = document.getElementById('page_1');
    var page_2 = document.getElementById('page_2');

    var color = 'var(--all-color-text)';
    var font = '15px Arial';
    var background_color = 'var(--non-target-background)';

    page_0.style.color = color;
    page_0.style.font = font;
    page_0.style.backgroundColor = background_color;

    page_1.style.color = color;
    page_1.style.font = font;
    page_1.style.backgroundColor = background_color;

    page_2.style.color = color;
    page_2.style.font = font;
    page_2.style.backgroundColor = background_color;
}

function activate_page(code_page) {
    if (current_page == code_page)
        return;
    all_page_non_target();
    var page = document.getElementById('page_' + code_page);
    current_page = code_page;

    var color = 'var(--target-color-text)';
    var font = '15px Arial Black';
    var background_color = 'var(--target-background)';

    page.style.color = color;
    page.style.font = font;
    page.style.backgroundColor = background_color;

    window['draw_page_' + code_page]();
}

function focusFindUser(focus) {
    if (focus)
        timerId = setInterval(foundUsers, 100);
    else
        clearInterval(timerId);
}

function clear_findInput() {
    document.getElementById('nameUserFind').value = '';
    foundUsers();
}

function foundUsers() {
    var input = document.getElementById('nameUserFind');
    if (last_value_found != input.value)
        updateListUsers(input.value);
}

function changeQueue() {
    current_queue_id = document.getElementById('select_current_queue').value;
    activate_page(0);
    update_page();
}

function init_administration() {
    ajax_get("/rest/administration_page/get_queues?user_id="+userId, function (data) {
        current_queue_id = data['current_queue'];
        var select_current_queue = document.getElementById('select_current_queue');
        var html = '';
        for (let i = 0; i < data['queues'].length; i++) {
            html += '<option value="'+
                data['queues'][i].value +
                '"' +
                (data['queues'][i].value == data['current_queue'] ? 'selected' : '') +
                '>' +
                data['queues'][i].name +
                '</option>';
        }
        select_current_queue.innerHTML = html;
        activate_page(0);
        update_page();
        setInterval(update_page, 5 * 1000);

        var body_page = document.getElementById('body_page');
        body_page.style.height = (screen.height - 20 - 30 - 20 - 50 - 50 - 260 - 16) + 'px';

    });
}
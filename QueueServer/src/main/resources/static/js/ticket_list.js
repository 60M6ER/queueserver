let countPages = 0;
let currentPage = 0;

function getServices() {
    ajax_get('/rest/get_services', function (data) {
        let html = '<option value="---">---</option>'
        if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].name + '</option>'
            }
        }else {
            console('Не найдео услуг.');
        }
        document.getElementById('service_find').innerHTML = html;
    });
}

function getQueues() {
    ajax_get('/rest/get_queues?user_id=' + userId, function (data) {
        let html = '<option value="---">---</option>'
        if (data.queues.length > 1) {
            for (let i = 0; i < data.queues.length; i++) {
                html += '<option value="' + data.queues[i].value + '">' + data.queues[i].name + '</option>'
            }
        }else if (data.queues.length == 1){
            html = '<option value="' + data.queues[0].value + '" selected>' + data.queues[0].name + '</option>';
        }else {
            console('Не найдео очередей.');
        }
        document.getElementById('queue_find').innerHTML = html;
    });
}

function getDates() {
    let date = new Date();
    let mouth = date.getMonth() + 1;
    if (mouth < 10) {
        mouth = '0' + mouth;
    }
    let day = date.getDate();
    if (day < 10) {
        day = '0' + day;
    }
    let dateStr = date.getFullYear() + '-' + mouth + '-' + day;
    document.getElementById('start_date').value = dateStr;
    document.getElementById('end_date').value = dateStr;
}

function clear_report_ticket_list() {
    document.getElementById('ticket_list_report').innerHTML = '';
}

function add_page(iter) {
    if (countPages != 0) {
        currentPage += iter;
        let update = true;
        if (currentPage < 1){
            currentPage = 1;
            update = false;
        }
        if (currentPage > countPages) {
            currentPage = countPages;
            update = false;
        }
        document.getElementById('current_page').value = currentPage;
        if (update)
            toFormTicketList();
    }
}

function click_find(){
    let page = document.getElementById('current_page').value;
    if (page == '') page = 0;
    currentPage = page;
    toFormTicketList();
}

function drawTicketList(result) {
    document.getElementById('ticket_list_report').innerHTML = '<table id="table_ticket_list" hidden>\n' +
        '            <thead>\n' +
        '                <tr>\n' +
        '                    <td class="name headField">Талон</td>\n' +
        '                    <td class="dateCreate headField">Дата создания</td>\n' +
        '                    <td class="date_print headField">Дата печати</td>\n' +
        '                    <td class="date_distrib headField">Дата распределения</td>\n' +
        '                    <td class="date_start headField">Дата начала обслуживания</td>\n' +
        '                    <td class="date_end headField">Дата завершения обслуживания</td>\n' +
        '                    <td class="status_field headField">Статус</td>\n' +
        '                    <td class="service_name headField">Услуга</td>\n' +
        '                    <td class="queue_field headField">Очередь</td>\n' +
        '                    <td class="name_manager headField">Менеджер</td>\n' +
        '                    <td class="contractor_field headField">Контрагент</td>\n' +
        '                    <td class="selling headField">Квитанции</td>\n' +
        '                </tr>\n' +
        '            </thead>\n' +
        '            <tbody id="body_report_ticket_list">\n' +
        '\n' +
        '            </tbody>\n' +
        '        </table>';
    document.getElementById('count_pages').innerText ='/ ' +  result.countPages;
    document.getElementById('current_page').value = result.currentPage;
    countPages = result.countPages;
    currentPage = result.currentPage;

    document.getElementById('table_ticket_list').hidden = false;
     let html = '';

    for (let i = 0; i < result.linesPOJO.length; i++) {
        html += '<tr>' +
            '<td class="name center field">' + result.linesPOJO[i].nameTalonField + '</td>' +
            '<td class="dateCreate center field">' + result.linesPOJO[i].dateCreateField + '</td>' +
            '<td class="date_print center field">' + result.linesPOJO[i].datePrintedField + '</td>' +
            '<td class="date_distrib center field">' + result.linesPOJO[i].dateDistribField + '</td>' +
            '<td class="date_start center field">' + result.linesPOJO[i].dateStartServiceField + '</td>' +
            '<td class="date_end center field">' + result.linesPOJO[i].dateEndServiceField + '</td>' +
            '<td class="status_field field">' + result.linesPOJO[i].statusField + '</td>' +
            '<td class="service_name field">' + result.linesPOJO[i].serviceField + '</td>' +
            '<td class="queue_field field">' + result.linesPOJO[i].queueField + '</td>' +
            '<td class="name_manager field">' + result.linesPOJO[i].name_managerField + '</td>' +
            '<td class="contractor_field field smallFont">' + result.linesPOJO[i].contractorField + '</td>' +
            '<td class="selling field smallFont">' + result.linesPOJO[i].ticketSellingsField + '</td>' +
            '</tr>';
    }
    document.getElementById('body_report_ticket_list').innerHTML = html;
}



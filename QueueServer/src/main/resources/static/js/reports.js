
var settings_hide = false;
var idReport;
class ToFormPojo{
    startDate;
    endDate;
    norm;
    overNorm;
    critNorm;
    overCritNorm;
}
class ParametersPOJO{
    dateOf;
    dateTo;
    number;
    service;
    queue;
    currentPage;
}

var htmlLoadingLogo = '<img src="/img/loading_logo.gif" id="loading_logo">';

function init_reportsPage() {
    var work_space = document.getElementById('work_space');
    var headSite = document.getElementById('headSite');
    work_space.style.height = (window.innerHeight - headSite.offsetHeight - (8*3) - 6) + 'px'; //Высота страницы - высота шапки - три отступа - две границы
    work_space.style.width = (window.innerWidth - (8 * 2) - 6) + 'px';
    //setCookie('parameters_report', '123', {'max-age': 99999});
    let parameters = getCookie('parameters_report', true);
    if (parameters != undefined && parameters != null) {
        let form = document.getElementById('settings_form');

        form.start_date.value = parameters.startDate;
        form.end_date.value = parameters.endDate;
        form.norm.value = parameters.norm;
        form.over_norm.value = parameters.overNorm;
        form.crit_norm.value = parameters.critNorm;
    }
    //div_settings.style.marginLeft = (screen.width - 16 - 400) + 'px';
}

function showLoadingLogo() {
    let report_body = document.getElementById('report_body');
    report_body.innerHTML = htmlLoadingLogo;
    let loading_logo = document.getElementById('loading_logo');
    loading_logo.style.marginLeft = ((report_body.offsetWidth / 2) - (loading_logo.offsetWidth / 2)) + 'px';
}

function clearReportBody() {
    let report_body = document.getElementById('report_body');
    report_body.innerHTML = '';
}

function update_settings_block() {
    var div_settings_body = document.getElementById('settings_body');
    div_settings_body.hidden = settings_hide;
}

function click_settings() {
    settings_hide = !settings_hide;
    update_settings_block();
}


function toFormTicketList() {
    clear_report_ticket_list();
    let error = false;
    let error_message = '';
    let form = document.getElementById('settings_form');
    let parametersPojo = new ParametersPOJO();
    parametersPojo.dateOf = form.start_date.value == '' ? null : form.start_date.value;
    parametersPojo.dateTo = form.end_date.value == '' ? null : form.end_date.value;
    parametersPojo.number = form.numberTicket.value == '' ? null : form.numberTicket.value;
    parametersPojo.service = form.service.value == '---' ? null : form.service.value;
    parametersPojo.queue = form.queue.value == '---' ? null : form.queue.value;
    parametersPojo.currentPage = currentPage;

    if (parametersPojo.dateTo == null && parametersPojo.dateOf == null && parametersPojo.number == null) {
        error_message += '* Необходимо заполнить отбор либо по дате либо по номеру';
        error = true;
    }

    if (parametersPojo.dateTo != null && parametersPojo.dateOf != null) {
        if (!(Date.parse(form.end_date.value) >= Date.parse(form.start_date.value))) {
            error_message += '* Дата окончания должна быть больше даты начала';
            error = true;
        }
    }

    if (error) {
        document.getElementById('error_field').innerText = error_message;
    } else {
        ajax_post("/rest/reports/toformticketlist", JSON.stringify(parametersPojo), function (data, error) {
            if (error) {
                let html = '<div class="error_ajax"><h3>Ошибка: ' + data.status + '</h3><br>';
                html += data.response.message;
                html += '</div>';
                document.getElementById('report_body').innerHTML = html;
            }else {
                idReport = data.id;
                setTimeout(getResultReportTicketList, 1000);
            }
        });
    }
}

function click_execute() {

    showLoadingLogo();
    let error = false;
    let form = document.getElementById('settings_form');
    let start_dateError = document.getElementById('start_date_error');
    let end_dateError = document.getElementById('end_date_error');
    let normError = document.getElementById('norm_error');
    let over_normError = document.getElementById('over_norm_error');
    let crit_normError = document.getElementById('crit_norm_error');
    //let over_crit_normError = document.getElementById('over_crit_norm_error');
    let toForm = new ToFormPojo();

    if (form.start_date.value == '') {
        start_dateError.innerText = '* Необходимо указать дату начала';
        error = true;
    }
    else{
        toForm.startDate = form.start_date.value;
        start_dateError.innerText = '';
    }


    if (form.end_date.value == '') {
        end_dateError.innerText = '* Необходимо указать дату окончания';
        error = true;
    }
    else{
        toForm.endDate = form.end_date.value;
        end_dateError.innerText = '';
    }

    if (!(Date.parse(form.end_date.value) > Date.parse(form.start_date.value))) {
        end_dateError.innerText = '* Дата окончания должна быть больше даты начала';
        error = true;
    }

    if (form.norm.value == '') {
        normError.innerText = '* Необходимо указать параметр нормы';
        error = true;
    }
    else{
        normError.innerText = '';
        toForm.norm = form.norm.value;
    }


    if (form.over_norm.value == '') {
        over_normError.innerText = '* Необходимо указать параметр сверхнормы';
        error = true;
    }
    else{
        toForm.overNorm = form.over_norm.value;
        over_normError.innerText = '';
    }


    if (form.crit_norm.value == '') {
        crit_normError.innerText = '* Необходимо указать параметр критической нормы';
        error = true;
    }
    else{
        toForm.critNorm = form.crit_norm.value;
        crit_normError.innerText = '';
    }


    if (error) {
        clearReportBody();
    } else {
        setCookie('parameters_report', toForm, {'max-age': 99999999});
        ajax_post("/rest/reports/toformreport", JSON.stringify(toForm), function (data, error) {
            if (error) {
                let html = '<div class="error_ajax"><h3>Ошибка: ' + data.status + '</h3><br>';
                html += data.response.message;
                html += '</div>';
                document.getElementById('report_body').innerHTML = html;
            }else {
                idReport = data.id;
                setTimeout(getResultReport, 1000);
            }
        });
    }
}

function getResultReport() {
    ajax_get("/rest/reports/getResultReport?id=" + idReport, function (result, error) {

        if (error) {
            result = JSON.parse(result.responseText);
            let message = result.message;
            if (message == 'Задание выполняется') {
                setTimeout(getResultReport, 1000);
            }else {
                let html = '<div class="error_ajax"><h3>Ошибка: ' + result.status + '</h3><br>';
                html += message;
                html += '</div>';
                document.getElementById('report_body').innerHTML = html;
            }
        }else {
            executeReport(result);

            settings_hide = true;
            update_settings_block();
        }
    });
}

function getResultReportTicketList() {
    ajax_get("/rest/reports/getResultTicketList?id=" + idReport, function (result, error) {

        if (error) {
            result = JSON.parse(result.responseText);
            let message = result.message;
            if (message == 'Задание выполняется') {
                setTimeout(getResultReportTicketList, 1000);
            }else {
                let html = '<div class="error_ajax"><h3>Ошибка: ' + result.status + '</h3><br>';
                html += message;
                html += '</div>';
                document.getElementById('ticket_list_report').innerHTML = html;
            }
        }else {
            drawTicketList(result);

            settings_hide = true;
            update_settings_block();
        }
    });
}



function executeReport(dataReport) {
    let htmlTable;
    if (dataReport.lines.lenght == 0)
        htmlTable = '<span id="not_data">Не найденно данных по заданым параметрам поиска.</span>';
    else
        htmlTable = '<table id="table_report" cellpadding="3px" cellspacing="0">';

    htmlTable += '<thead id="table_report_head">' +
        '<tr>' +
        '<td class="folder"></td>' +
        '<td class="nameGroup tableBorder">Электронная очередь</td>' +
        '<td rowspan="3" class="data tableBorder">Количество талонов</td>' +
        '<td rowspan="3" class="data tableBorder">% Норматив, ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">% Сверх нормы, ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">% Критическая норма, ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">% Сверхкритическая норма, ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">Норма ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">Сверхнорма ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">Критическая норма ожидание клиент</td>' +
        '<td rowspan="3" class="data tableBorder">Сверхкритическая норма, ожидание клиент</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="folder"></td>' +
        '<td class="nameGroup tableBorder">&nbsp;&nbsp;&nbsp;&nbsp;Итоги в разрезе талонов (неделя)</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="folder"></td>' +
        '<td class="nameGroup tableBorder">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Дата</td>' +
        '</tr>' +
        '</thead>';

    //Итоги
    htmlTable += '<tfoot id="table_results">' +
        '<tr>' +
        '<td class="folder"></td>' +
        '<td class="resultsName tableBorder"> ИТОГО:</td>' +
        '<td class="data tableBorder">' + dataReport.countTickets +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.percentNorm.toFixed(2) +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.percentOverNorm.toFixed(2) +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.percentCritNorm.toFixed(2) +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.percentOverCritNorm.toFixed(2) +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.countTicketsNorm +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.countTicketsOverNorm +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.countTicketsCritNorm +
        '</td>' +
        '<td class="data tableBorder">' + dataReport.countTicketsOverCritNorm +
        '</td>' +
        '</tr>' +
        '</tfoot>'

    htmlTable += getHtmlTableLines(dataReport.lines, 0, []);

    htmlTable += '</table>';
    let reportBody = document.getElementById('report_body');
    reportBody.innerHTML = htmlTable;

}

function clickFold(idButton, elements) {
    let currentLevel = Number(idButton.substring(0, 1));
    let arr = elements.split(',');
    let hidden = document.getElementById('button' + idButton).innerText == '-';
    for (let i = 0; i < arr.length; i++) {
        if (hidden) {
            document.getElementById(arr[i]).hidden = hidden;
        }else {
            let underLevel = Number(arr[i].substring(0, 1));
            if (underLevel == currentLevel + 1) {
                document.getElementById(arr[i]).hidden = hidden;
                let button = document.getElementById('button' + arr[i]);
                if (button != null) {
                    button.innerText = '+';
                }
            }
        }
    }
    document.getElementById('button' + idButton).innerText = document.getElementById('button' + idButton).innerText == '-' ? '+' : '-';
}

var map = new Map();

function getStringFromArr(arr) {
    let str = '';
    for (let i = 0; i < arr.length; i++) {
        if (str != '')
            str += ',';
        str += arr[i];
    }
    return str;
}

function getHtmlTableLines(lines, iter, underElements) {
    let htmlResult = '';
    let indent = '&nbsp;&nbsp;&nbsp;&nbsp;';
    for (let i = 0; i < lines.length; i++) {
        let html = '';
        let newUnderElements = [];
        if (lines[i].lines.length > 0)
            html = getHtmlTableLines(lines[i].lines, iter + 1, newUnderElements);
        let idBlock = '' + iter + 'block' + uuidv4();
        map.set(idBlock, true);
        //newUnderElements.unshift(idBlock);
        let currentHtml = '<tbody id="' + idBlock + '">';
        currentHtml += '<tr class="' + (iter == 0 || iter % 2 == 0 ? 'line2' : 'line1') + '">' +
            '<td class="folder">' + (newUnderElements.length > 0 ? '<button class="folderButton" id="button' + idBlock + '" onclick="clickFold(\'' + idBlock+ '\',\'' + getStringFromArr(newUnderElements) + '\')">-</button>' : '') + '</td>' +
            '<td class="nameGroup tableBorder">' + (getIndent(indent, iter) + lines[i].nameGroup) + '</td>' +
            '<td class="data tableBorder">' + lines[i].countTickets +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].percentNorm.toFixed(2) +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].percentOverNorm.toFixed(2) +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].percentCritNorm.toFixed(2) +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].percentOverCritNorm.toFixed(2) +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].countTicketsNorm +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].countTicketsOverNorm +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].countTicketsCritNorm +
            '</td>' +
            '<td class="data tableBorder">' + lines[i].countTicketsOverCritNorm +
            '</td>' +
            '</tr>';
        currentHtml += '</tbody>';
        htmlResult += currentHtml + html;
        underElements.push(idBlock)
        for (let j = 0; j < newUnderElements.length; j++) {
            underElements.push(newUnderElements[j]);
        }
    }
    return htmlResult;
}

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function getIndent(indent, inter) {
    let text = '';
    for (let i = 0; i < inter; i++) {
        text += indent;
    }
    return text;
}


class HoursPOJO {
    value;
}

function saveHoursWorkingTime() {
    let hoursint = document.getElementById('hours').value;

    if (hoursint < 1) {
        alert('Время должно быть больше 0');
        return;
    }

    let hours = new HoursPOJO();
    hours.value = hoursint;

    ajax_post("/rest/setting_program/setHoursWorkingTime", JSON.stringify(hours), function (data, error) {
        if (error) {
            let html = '<div class="error_ajax"><h3>Ошибка: ' + data.status + '</h3><br>';
            html += data.response.message;
            html += '</div>';
            document.getElementById('error_setting').innerHTML = html;
        }else {
            alert('Сохранено');
        }
    });
}
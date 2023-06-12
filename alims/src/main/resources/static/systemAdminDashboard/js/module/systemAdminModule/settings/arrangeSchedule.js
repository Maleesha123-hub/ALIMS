$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restScheduleBtn").click(function (e) {
        clearScheduleFields();
    });

    $("#saveUpdateSchedule").click(function (e) {
        saveUpdateSchedule();
    });

    $("#country").change(function (e) {
        getAllActiveAirportsByCountry();
    });
    getAllActiveCountries();
    getAllActiveInactiveSchedules();
    getAllActiveAirPlanes();
    getAllActivePilots();
    getAllActiveCrews();

});

function clearScheduleFields() {
    $("#pilot_div").empty();
    $("#crew_div").empty();
    $('#schedule_id').val('');
    $('#departure_date').val('');
    $('#departure_time').val('');
    $('#arrival_time').val('');
    $('#airplane').val('NOT_SELECTED');
    $('#country').val('NOT_SELECTED');
    $('#start_point').val('NOT_SELECTED');
    $('#landing_point').val('NOT_SELECTED');
    $('#schedule_status').val('NOT_SELECTED');
    getAllActiveInactiveSchedules();
    getAllActiveAirPlanes();
    getAllActivePilots();
    getAllActiveCrews();
    getAllActiveCountries();
}

function saveUpdateSchedule() {

    var id = $.trim($('#schedule_id').val());
    var scheduleName = $.trim($('#schedule_name').val());
    var departureDate = $.trim($('#departure_date').val());
    var arrivalDate = $.trim($('#arrival_date').val());
    var departureTime = $.trim($('#departure_time').val());
    var arrivalTime = $.trim($('#arrival_time').val());
    var airplane = $.trim($('#airplane').val());
    var country = $.trim($('#country').val());
    var startPoint = $.trim($('#start_point').val());
    var landingPoint = $.trim($('#landing_point').val());
    var scheduleStatus = $.trim($('#schedule_status').val());
    var pilots = [];
    $.each($("input[name='pilot']:checked"), function () {
        pilots.push({
            id: $(this).val()
        });
    });
    var crews = [];
    $.each($("input[name='crew']:checked"), function () {
        crews.push({
            id: $(this).val()
        });
    });

    var scheduleObj = {
        "id": id,
        "scheduleName": scheduleName,
        "departureDate": departureDate + " 00:00",
        "arrivalDate": arrivalDate + " 00:00",
        "departureTime": departureTime + ":00",
        "arrivalTime": arrivalTime + ":00",
        "airplaneId": airplane,
        "countryId": country,
        "startPoint": startPoint,
        "landingPoint": landingPoint,
        "status": scheduleStatus,
        "pilots": pilots,
        "crews": crews
    }
    $.ajax({
        url: BASE_URL + '/airline/arrangeSchedule/saveUpdate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(scheduleObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                toastr.success("Schedule Saved Successfully.");
                clearScheduleFields();
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for Schedule failed.");
        }
    });
}

function getAllActiveAirPlanes() {
    $(".airplaneClass").remove();
    $.ajax({
        url: BASE_URL + '/airline/airplane/getAllActive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $.each(data, function (key, value) {
                    $('#airplane').append('<option class="airplaneClass" value="' + value.id + '">' + value.airplaneName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airplane failed.");
        }
    });
}

function getAllActiveCountries() {
    $(".countryClass").remove();
    $.ajax({
        url: BASE_URL + '/airline/country/getAllActive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $.each(data, function (key, value) {
                    $('#country').append('<option class="countryClass" value="' + value.id + '">' + value.countryName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for country failed.");
        }
    });
}

function getAllActiveInactiveSchedules() {
    $.ajax({
        url: BASE_URL + '/airline/arrangeSchedule/getAllActiveInactive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setScheduleTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for schedule failed.");
        }
    });
}

function setScheduleTable(data) {
    $('#schedule_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.countryName + '</td><td>' + value.departureDate + '</td><td>' + value.departureTime + '</td>' +
            '<td>' + value.arrivalTime + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#schedule_table tbody').append(tableRows);
}

function getAllActiveAirportsByCountry() {
    $(".starting").remove();
    $(".landing").remove();
    var countryId = $.trim($('#country').val());
    $.ajax({
        url: BASE_URL + '/airline/countryAirport/getAllActiveAirportsByCountry/' + countryId,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $.each(data, function (key, value) {
                    $('#start_point').append('<option class="starting" value="' + value.id + '">' + value.airportName + '</option>');
                    $('#landing_point').append('<option class="landing" value="' + value.id + '">' + value.airportName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airport failed.");
        }
    });
}

function getAllActivePilots() {
    $("#pilot_div").empty();
    $.ajax({
        url: BASE_URL + '/airline/pilot/getAllActive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $('#pilot_div').append('<div class="col-md-12">\n' +
                    '    <div class="form-group">\n' +
                    '        <div class="form-check">\n' +
                    '            <input class="form-check-input" onclick="togglePilots(this);"\n' +
                    '                type="checkbox">\n' +
                    '            <label class="form-check-label">All</label>\n' +
                    '        </div>\n' +
                    '    </div>\n' +
                    '</div>');
                $.each(data, function (key, value) {
                    $('#pilot_div').append('<div class="col-md-10">');
                    $('#pilot_div').append('<div class="col-md-4"><div class="form-group"><div class="form-check"><input type="checkbox" class="form-check-input" name="pilot" value=' + value.id + '></div></div></div>');
                    $('#pilot_div').append('<div class="col-md-4"><div class="form-group"><div class="form-check"><label class="form-check-label">' + value.fullName + '</label></div></div></div>');
                    $('#pilot_div').append('</div>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for pilots failed.");
        }
    });
}

function togglePilots(source) {
    var pilotCheckboxes = document.querySelectorAll('input[name="pilot"]');
    for (var i = 0; i < pilotCheckboxes.length; i++) {
        if (pilotCheckboxes[i] != source) {
            pilotCheckboxes[i].checked = source.checked;
        }
    }
}

function getAllActiveCrews() {
    $("#crew_div").empty();
    $.ajax({
        url: BASE_URL + '/airline/crew/getAllActive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $('#crew_div').append('<div class="col-md-12">\n' +
                    '    <div class="form-group">\n' +
                    '        <div class="form-check">\n' +
                    '            <input class="form-check-input" onclick="toggleCrews(this);"\n' +
                    '                type="checkbox">\n' +
                    '            <label class="form-check-label">All</label>\n' +
                    '        </div>\n' +
                    '    </div>\n' +
                    '</div>');
                $.each(data, function (key, value) {
                    $('#crew_div').append('<div class="col-md-10">');
                    $('#crew_div').append('<div class="col-md-4"><div class="form-group"><div class="form-check"><input type="checkbox" class="form-check-input" name="crew" value=' + value.id + '></div></div></div>');
                    $('#crew_div').append('<div class="col-md-4"><div class="form-group"><div class="form-check"><label class="form-check-label">' + value.fullName + '</label></div></div></div>');
                    $('#crew_div').append('</div>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for crews failed.");
        }
    });
}

function toggleCrews(source) {
    var pilotCheckboxes = document.querySelectorAll('input[name="crew"]');
    for (var i = 0; i < pilotCheckboxes.length; i++) {
        if (pilotCheckboxes[i] != source) {
            pilotCheckboxes[i].checked = source.checked;
        }
    }
}
$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restAirplaneBtn").click(function (e) {
        clearAirplaneFields();
    });

    $("#saveUpdateAirplane").click(function (e) {
        saveUpdateAirplane();
    });
    getAllActiveInactiveAirplanes();

});

function clearAirplaneFields() {
    $('#airplane_id').val('');
    $('#airplane_name').val('');
    $('#airplane_status').val('NOT_SELECTED');
    getAllActiveInactiveAirplanes();
}

function saveUpdateAirplane() {

    var id = $.trim($('#airplane_id').val());
    var airplaneName = $.trim($('#airplane_name').val());
    var airplaneStatus = $.trim($('#airplane_status').val());

    var airplaneObj = {
        "id": id,
        "airplaneName": airplaneName,
        "status": airplaneStatus
    }
    console.log(airplaneObj);
    $.ajax({
        url: BASE_URL + '/airline/airplane/saveUpdate/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(airplaneObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearAirplaneFields();
                toastr.success("Airplane Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for airplane failed.");
        }
    });
}

function getAllActiveInactiveAirplanes() {
    $.ajax({
        url: BASE_URL + '/airline/airplane/getAllActiveInactive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirplaneTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airplane failed.");
        }
    });
}

function setAirplaneTable(data) {
    $('#airplane_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.airplaneName + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#airplane_table tbody').append(tableRows);
}
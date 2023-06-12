$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restYearBtn").click(function (e) {
        clearYearFields();
    });

    $("#saveUpdateYearBtn").click(function (e) {
        saveUpdate();
    });
    getAllActiveInactiveYears();
});

function clearYearFields() {
    $('#year_id').val('');
    $('#year').val('');
    $('#year_semester').val('');
    $('#semester_start_date').val('');
    $('#semester_end_date').val('');
    $('#year_status').val('NOT_SELECTED');
    $('#saveUpdateYearBtn').val("Create");
    getAllActiveInactiveYears();
}

function saveUpdate() {

    var id = $.trim($('#year_id').val());
    var yearName = $.trim($('#year').val());
    var startDate = $.trim($('#year_start_date').val());
    var endDate = $.trim($('#year_end_date').val());
    var yearStatus = $.trim($('#year_status').val());

    var yearObj = {
        "id": id,
        "year": yearName,
        "startDate": startDate,
        "endDate": endDate,
        "status": yearStatus
    }
    console.log(yearObj);
    $.ajax({
        url: BASE_URL + '/financialYear/saveUpdateYear/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(yearObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearYearFields();
                getAllActiveInactiveYears();
                toastr.success("Year Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for year failed.");
        }
    });
}

function getAllActiveInactiveYears() {
    $.ajax({
        url: BASE_URL + '/financialYear/getAllActiveInactive/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setYearTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for year failed.");
        }
    });
}

function setYearTable(data) {
    $('#year_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.year + '</td>' + '</td><td>' + value.startDate + '</td>' +
            '</td><td>' + value.endDate + '</td>' + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="userEdit(value)">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#year_table tbody').append(tableRows);
}
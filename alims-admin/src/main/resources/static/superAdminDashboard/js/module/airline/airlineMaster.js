$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restAirlineBtn").click(function (e) {
        clearAirlineFields();
    });

    $("#saveUpdateAirlineBtn").click(function (e) {
        saveUpdate();
    });
    getAllActiveInactiveAirlines();

});

function clearAirlineFields() {
    $('#airline_id').val('');
    $('#airline_name').val('');
    $('#airline_code').val('');
    $('#airline_contact_1').val('');
    $('#airline_contact_2').val('');
    $('#airline_email').val('');
    $('#airline_web').val('');
    $('#contact_person_name').val('');
    $('#country').val('');
    $('#airline_status').val('NOT_SELECTED');
    $('#airline_comment').val('');
    $('#airline_address').val('');
    $('#saveUpdateAirlineBtn').val("Create");
    getAllActiveInactiveAirlines();
}

function saveUpdate() {

    var id = $.trim($('#airline_id').val());
    var name = $.trim($('#airline_name').val());
    var code = $.trim($('#airline_code').val());
    var contactNo1 = $.trim($('#airline_contact_1').val());
    var contactNo2 = $.trim($('#airline_contact_2').val());
    var email = $.trim($('#airline_email').val());
    var web = $.trim($('#airline_web').val());
    var contactPerson = $.trim($('#contact_person_name').val());
    var country = $.trim($('#country').val());
    var comment = $.trim($('#airline_comment').val());
    var address = $.trim($('#airline_address').val());
    var airlineStatus = $.trim($('#airline_status').val());

    var airlineObj = {
        "id": id,
        "name": name,
        "code": code,
        "contactNo1": contactNo1,
        "contactNo2": contactNo2,
        "email": email,
        "web": web,
        "contactPerson": contactPerson,
        "country": country,
        "comment": comment,
        "address": address,
        "status": airlineStatus,
    }
    console.log(airlineObj);
    $.ajax({
        url: BASE_URL + '/airlineMaster/saveUpdateAirline/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(airlineObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearAirlineFields();
                getAllActiveInactiveAirlines();
                toastr.success("Airline Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for airline failed.");
        }
    });
}

function getAllActiveInactiveAirlines() {
    $.ajax({
        url: BASE_URL + '/airlineMaster/getAllActiveInactive/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirlineTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline failed.");
        }
    });
}

function setAirlineTable(data) {
    $('#airline_master_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.name + '</td><td>' + value.code + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#airline_master_table').append(tableRows);
}
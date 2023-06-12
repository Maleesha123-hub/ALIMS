$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restAirportBtn").click(function (e) {
        clearCountryAirportFields();
    });

    $("#saveUpdateAirportBtn").click(function (e) {
        saveUpdateCountryAirport();
    });
    getAllActiveInactiveAirportDTOs();
    getAllActiveCountries();

});

function clearCountryAirportFields() {
    $('#airport_name').val('');
    $('#contact_no').val('');
    $('#country_id').val('NOT_SELECTED');
    $('#airport_status').val('NOT_SELECTED');
    getAllActiveInactiveAirportDTOs();
    getAllActiveCountries();
}

function saveUpdateCountryAirport() {

    var id = $.trim($('#airport_id').val());
    var airportName = $.trim($('#airport_name').val());
    var contactNo = $.trim($('#contact_no').val());
    var countryId = $.trim($('#countryId').val());
    var airportStatus = $.trim($('#airport_status').val());

    var countryAirportObj = {
        "id": id,
        "airportName": airportName,
        "contactNo": contactNo,
        "countryId": countryId,
        "status": airportStatus
    }
    console.log(countryAirportObj);
    $.ajax({
        url: BASE_URL + '/airline/countryAirport/saveUpdate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(countryAirportObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearCountryAirportFields();
                toastr.success("Country Airport Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for Airport failed.");
        }
    });
}

function getAllActiveInactiveAirportDTOs() {
    $.ajax({
        url: BASE_URL + '/airline/countryAirport/getAllActiveInactive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirportTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airport failed.");
        }
    });
}

function setAirportTable(data) {
    $('#airport_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.airportName + '</td><td>' + value.country + '</td>' +
            '<td>' + value.contactNo + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#airport_table').append(tableRows);
}

function getAllActiveCountries() {
    $(".country").remove();
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
                console.log(data);
                $.each(data, function (key, value) {
                    $('#countryId').append('<option class="country" value="' + value.id + '">' + value.countryName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for country failed.");
        }
    });
}


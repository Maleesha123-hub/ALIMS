$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restCountryBtn").click(function (e) {
        clearCountryFields();
    });

    $("#saveUpdateCountryBtn").click(function (e) {
        saveUpdateCountry();
    });
    getAllActiveInactiveCountries();

});

function clearCountryFields() {
    $('#country_name').val('');
    $('#country_status').val('NOT_SELECTED');
    getAllActiveInactiveCountries();
}

function saveUpdateCountry() {

    var id = $.trim($('#country_id').val());
    var countryName = $.trim($('#country_name').val());
    var countryStatus = $.trim($('#country_status').val());

    var countryObj = {
        "id": id,
        "countryName": countryName,
        "status": countryStatus
    }
    console.log(countryObj);
    $.ajax({
        url: BASE_URL + '/airline/country/saveUpdate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(countryObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearCountryFields();
                toastr.success("Country Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for country failed.");
        }
    });
}

function getAllActiveInactiveCountries() {
    $.ajax({
        url: BASE_URL + '/airline/country/getAllActiveInactive/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setCountryTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline failed.");
        }
    });
}

function setCountryTable(data) {
    $('#country_master_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.countryName + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#country_master_table').append(tableRows);
}


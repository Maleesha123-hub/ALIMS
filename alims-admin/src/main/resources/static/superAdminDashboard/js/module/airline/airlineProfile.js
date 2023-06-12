$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restProfileBtn").click(function (e) {
        clearAirlineProfileFields();
    });

    $("#saveUpdateProfileBtn").click(function (e) {
        saveUpdate();
    });
    getAllActiveYears();
    getAllActiveBranches();
    getAllActiveUsers();
    getAllActiveInactiveAirlineProfiles();

});

function clearAirlineProfileFields() {
    $('#profile_id').val('');
    $('#financial_year_id').val('NOT_SELECTED');
    $('#airline_branch_id').val('NOT_SELECTED');
    $('#airline_profile_system_admin').val('NOT_SELECTED');
    $('#airline_profile_name').val('');
    $('#airline_profile_status').val('NOT_SELECTED');
    getAllActiveInactiveAirlineProfiles();
}

function saveUpdate() {

    var id = $.trim($('#profile_id').val());
    var yearId = $.trim($('#financial_year_id').val());
    var branchId = $.trim($('#airline_branch_id').val());
    var systemAdminId = $.trim($('#airline_profile_system_admin').val());
    var profileName = $.trim($('#airline_profile_name').val());
    var profileStatus = $.trim($('#airline_profile_status').val());

    var profileObj = {
        "id": id,
        "profileName": profileName,
        "airlineSystemAdminId": systemAdminId,
        "airlineBranchId": branchId,
        "financialYearId": yearId,
        "status": profileStatus,
    }
    console.log(profileObj);
    $.ajax({
        url: BASE_URL + '/airlineProfileController/saveUpdateAirlineProfile/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(profileObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                getAllActiveInactiveAirlineProfiles();
                clearAirlineProfileFields();
                toastr.success("Airline Profile Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for airline profile failed.");
        }
    });
}

function getAllActiveYears() {
    $(".financial_year").remove();
    $.ajax({
        url: BASE_URL + '/financialYear/getAllActive/',
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
                    $('#financial_year_id').append('<option class="financial_year" value="' + value.id + '">' + value.year + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for year failed.");
        }
    });
}

function getAllActiveBranches() {
    $(".airline_branch").remove();
    $.ajax({
        url: BASE_URL + '/airlineBranch/getAllActive/',
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
                    $('#airline_branch_id').append('<option class="airline_branch" value="' + value.id + '">' + value.name + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline branches failed.");
        }
    });
}

function getAllActiveUsers() {
    $(".airline_profile_sys_admin").remove();
    $.ajax({
        url: BASE_URL + '/userAccount/getAllActive/',
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
                    $('#airline_profile_system_admin').append('<option class="airline_profile_sys_admin" value="' + value.id + '">' + value.userName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for profile system admin failed.");
        }
    });
}

function getAllActiveInactiveAirlineProfiles() {
    $.ajax({
        url: BASE_URL + '/airlineProfileController/getAllActiveInactiveAirlineProfiles/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirlineProfileTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for active inactive airline profile failed.");
        }
    });
}

function setAirlineProfileTable(data) {
    $('#airline_profile_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.financialYear + '</td><td>' + value.airlineBranch + '</td><td>' + value.profileName + '</td>' +
            '<td>' + value.airlineSystemAdmin + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#airline_profile_table').append(tableRows);
}
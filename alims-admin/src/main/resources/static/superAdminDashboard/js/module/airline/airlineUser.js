$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#airline_user_sync_btn").click(function (e) {

    });
    getAllActiveProfiles();
    getAllUsersByProfileFromAirlineSystem();

});

function getAllActiveProfiles() {
    $(".airline_profile").remove();
    $.ajax({
        url: BASE_URL + '/airlineProfileController/getAllActive/',
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
                    $('#airline_profile_id').append('<option class="airline_profile" value="' + value.id + '">' + value.profileName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline profiles failed.");
        }
    });
}

function getSystemAdminByProfile() {

    var profileId = $.trim($('#airline_profile_id').val());

    $.ajax({
        url: BASE_URL + '/userAccount/getSystemAdminByProfile/' + profileId,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $('#system_admin_user_name').val(data.userName);
                $('#system_admin_role_name').val(data.userRole);
                $('#system_admin_user_status').val(data.userStatus);
                $('#system_admin_role_status').val(data.userRoleStatus);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for system admin failed.");
        }
    });
}

function syncAdminData() {
    var profileId = $.trim($('#airline_profile_id').val());
    if (profileId == "NOT_SELECTED") {
        toastr.error("Please Select a Profile");
        return;
    }
    $.ajax({
        url: BASE_URL + '/dataSync/syncUsersAndRoles/' + profileId,
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            alert(data.SyncTemporaryUnavailable);
            toastr.error(data.SyncTemporaryUnavailable);
        },
        error: function (xhr) {
            toastr.success(data.returnMsg);
            //toastr.error("Sync data for system admin failed.");
        }
    });
}

function getAllUsersByProfileFromAirlineSystem() {

    var profileId = $.trim($('#airline_profile_id').val());

    $.ajax({
        url: BASE_URL + '/dataSync/getAllUsersByProfileFromAirlineSystem/' + profileId,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirlineUserTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline system users failed.");
        }
    });
}

function setAirlineUserTable(data) {
    $('#airline_user_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.userName + '</td><td>' + value.rolName + '</td><td>' + value.createdOn +
            '</td><td>' + value.updatedOn + '</td><td>' + value.lastAccess + '</td><td>';
    });
    $('#airline_user_table').append(tableRows);
}


$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restUserBtn").click(function (e) {
        clearUserFields();
    });

    $("#saveUpdateUserBtn").click(function (e) {
        saveUpdate();
    });

    getAllActiveInactiveUsers();
    getAllActiveUserRoles();

});

function clearUserFields() {
    $('#confirm_password').val('');
    $('#user_id').val('');
    $('#username').val('');
    $('#user_password').val('');
    $('#user_role_id').val('NOT_SELECTED');
    $('#user_status').val('NOT_SELECTED');
    $('#saveUpdateUserBtn').val("Create");
    getAllActiveUserRoles();
    getAllActiveInactiveUsers();
}

function saveUpdate() {

    var id = $.trim($('#user_id').val());
    var userName = $.trim($('#username').val());
    var userPassword = $.trim($('#user_password').val());
    var confirmPassword = $.trim($('#confirm_password').val());
    var userRoleId = $.trim($('#user_role_id').val());
    var userStatus = $.trim($('#user_status').val());

    if (id.length == 0 && userPassword.length == 0) {
        toastr.error("Please Enter a Password");
        return;
    }

    if (id.length == 0 && confirmPassword.length == 0) {
        toastr.error('Please Enter a Confirm Password');
        return;
    }

    if (id.length == 0 && userPassword.localeCompare(confirmPassword) != 0) {
        toastr.error('Passwords are mismatched');
        return;
    }

    var userObj = {
        "id": id,
        "userName": userName,
        "password": userPassword,
        "userRoleId": userRoleId,
        "status": userStatus
    }
    console.log(userObj);
    $.ajax({
        url: BASE_URL + '/airline/userAccount/saveUpdateUser/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(userObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearUserFields();
                getAllActiveUserRoles();
                getAllActiveInactiveUsers();
                toastr.success("User Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for user failed.");
        }
    });
}

function getAllActiveUserRoles() {
    $(".user_role_id").remove();
    $.ajax({
        url: BASE_URL + '/airline/userRole/getAllActive/',
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
                    $('#user_role_id').append('<option class="user_role" value="' + value.id + '">' + value.roleName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for user role failed.");
        }
    });
}

function getAllActiveInactiveUsers() {
    $.ajax({
        url: BASE_URL + '/airline/userAccount/getAllActiveInactive/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setUserTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for user role failed.");
        }
    });
}

function setUserTable(data) {
    $('#user_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.userName + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="userEdit(value)">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#user_table tbody').append(tableRows);
}
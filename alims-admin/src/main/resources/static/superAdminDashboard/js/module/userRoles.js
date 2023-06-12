$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    getAllActiveInactiveUserRoles();

    $("#restRoleBtn").click(function (e) {
        clearRoleFields();
    });

    $("#saveUpdateRoleBtn").click(function (e) {
        saveUpdate();
    });

});

function clearRoleFields() {
    $('#user_role_id').val('');
    $('#role_name').val('');
    $('#role_description').val('');
    $('#role_status').val('NOT_SELECTED');
    $('#saveUpdateRoleBtn').val("Create");
    getAllActiveInactiveUserRoles();
}

function saveUpdate() {

    var id = $.trim($('#user_role_id').val());
    var name = $.trim($('#role_name').val());
    var description = $.trim($('#role_description').val());
    var status = $.trim($('#role_status').val());

    var userRoleObj = {
        "id": id,
        "roleName": name,
        "description": description,
        "status": status
    }

    $.ajax({
        url: BASE_URL + '/userRole/saveUpdateUserRole/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(userRoleObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearRoleFields();
                toastr.success("User Role Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for user role failed.");
        }
    });
}

function getAllActiveInactiveUserRoles() {
    $.ajax({
        url: BASE_URL + '/userRole/getAllActiveInactive/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setUserRoleTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for user role failed.");
        }
    });
}

function setUserRoleTable(data) {
    $('#user_role_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.roleName + '</td><td>' + value.description + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="userRoleEdit(value)">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="delete_role(value)">Delete</button></td></tr>';
    });
    $('#user_role_table').append(tableRows);
}
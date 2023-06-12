$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restPilotBtn").click(function (e) {
        clearPilotFields();
    });

    $("#saveUpdatePilotBtn").click(function (e) {
        saveUpdatePilot();
    });
    getAllActiveUsers();
    getAllActiveInactivePilots();

});

function clearPilotFields() {
    $(':radio').each(function () {
        this.checked = false;
        $('#pilot_id').val('');
    });
    $('#initials').val('');
    $('#first_name').val('');
    $('#last_name').val('');
    $('#full_name').val('');
    $('#nic').val('');
    $('#emergency_contact_no').val('');
    $('#email').val('');
    $('#dob').val('');
    $('#religion').val('NOT_SELECTED');
    $('#race').val('NOT_SELECTED');
    $('#pilot_user').val('NOT_SELECTED');
    $('#pilot_reg_date').val('');
    $('#pilot_status').val('NOT_SELECTED');
    $('#address').val('');
    getAllActiveUsers();
    getAllActiveInactivePilots();
}

function saveUpdatePilot() {

    var id = $.trim($('#pilot_id').val());
    var initials = $.trim($('#initials').val());
    var firstName = $.trim($('#first_name').val());
    var lastName = $.trim($('#last_name').val());
    var fullName = $.trim($('#full_name').val());
    var nic = $.trim($('#nic').val());
    var emergencyContactNo = $.trim($('#emergency_contact_no').val());
    var email = $.trim($('#email').val());
    var dob = $.trim($('#dob').val());
    var religion = $.trim($('#religion').val());
    var race = $.trim($('#race').val());
    var pilotUser = $.trim($('#pilot_user').val());
    var pilotRegDate = $.trim($('#pilot_reg_date').val());
    var pilotStatus = $.trim($('#pilot_status').val());
    var address = $.trim($('#address').val());
    var gender = null;

    if (document.getElementById('male').checked) {
        gender = document.getElementById('male').value;
    } else if (document.getElementById('female').checked) {
        gender = document.getElementById('female').value;
    } else {
        toastr.error("Please Select a gender");
        return;
    }

    var pilotObj = {
        "id": id,
        "initials": initials,
        "firstName": firstName,
        "lastName": lastName,
        "fullName": fullName,
        "nic": nic,
        "emergencyContactNo": emergencyContactNo,
        "email": email,
        "dob": dob + " 00:00",
        "religion": religion,
        "race": race,
        "pilotUser": pilotUser,
        "pilotRegDate": pilotRegDate + " 00:00",
        "pilotStatus": pilotStatus,
        "address": address,
        "gender": gender
    }
    $.ajax({
        url: BASE_URL + '/airline/pilot/saveUpdate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(pilotObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                toastr.success("Pilot Saved Successfully.");
                clearPilotFields();
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for pilots failed.");
        }
    });
}

function getAllActiveUsers() {
    $(".pilot_user").remove();
    $.ajax({
        url: BASE_URL + '/airline/userAccount/getAllActiveUsersByPilot',
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
                    $('#pilot_user').append('<option class="pilotUser" value="' + value.id + '">' + value.userName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for user failed.");
        }
    });
}

function getAllActiveInactivePilots() {
    $.ajax({
        url: BASE_URL + '/airline/pilot/getAllActiveInactive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setPilotTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for pilot failed.");
        }
    });
}

function setPilotTable(data) {
    $('#pilot_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.fullName + '</td><td>' + value.gender + '</td><td>' + value.emergencyContactNo + '</td><td>' + value.pilotStatus + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="userEdit(value)">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#pilot_table tbody').append(tableRows);
}

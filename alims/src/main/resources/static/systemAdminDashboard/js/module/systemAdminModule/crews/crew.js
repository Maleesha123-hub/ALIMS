$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restCrewBtn").click(function (e) {
        clearCrewFields();
    });

    $("#saveUpdateCrewBtn").click(function (e) {
        saveUpdateCrew();
    });
    getAllActiveCrewUsers();
    getAllActiveInactiveCrews();

});

function clearCrewFields() {
    $(':radio').each(function () {
        this.checked = false;
        $('#crew_id').val('');
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
    $('#crew_user').val('NOT_SELECTED');
    $('#crew_reg_date').val('');
    $('#crew_status').val('NOT_SELECTED');
    $('#address').val('');
    getAllActiveCrewUsers();
    getAllActiveInactiveCrews();
}

function saveUpdateCrew() {

    var id = $.trim($('#crew_id').val());
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
    var crewUser = $.trim($('#crew_user').val());
    var crewRegDate = $.trim($('#crew_reg_date').val());
    var crewStatus = $.trim($('#crew_status').val());
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

    var crewObj = {
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
        "crewUser": crewUser,
        "crewRegDate": crewRegDate + " 00:00",
        "crewStatus": crewStatus,
        "address": address,
        "gender": gender
    }
    $.ajax({
        url: BASE_URL + '/airline/crew/saveUpdate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(crewObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                toastr.success("Crew Saved Successfully.");
                clearCrewFields();
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for crews failed.");
        }
    });
}

function getAllActiveCrewUsers() {
    $(".crew_user").remove();
    $.ajax({
        url: BASE_URL + '/airline/userAccount/getAllActiveUsersByCrew',
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
                    $('#crew_user').append('<option class="crewUser" value="' + value.id + '">' + value.userName + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for user failed.");
        }
    });
}

function getAllActiveInactiveCrews() {
    $.ajax({
        url: BASE_URL + '/airline/crew/getAllActiveInactive',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setCrewTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for crew failed.");
        }
    });
}

function setCrewTable(data) {
    $('#crew_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.fullName + '</td><td>' + value.gender + '</td><td>' + value.emergencyContactNo + '</td><td>' + value.crewStatus + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#crew_table tbody').append(tableRows);
}

$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#restBranchBtn").click(function (e) {
        clearAirlineBranchFields();
    });

    $("#saveUpdateBranchBtn").click(function (e) {
        saveUpdate();
    });
    getAllActiveInactiveAirlineBranches();
    getAllActiveAirlines();

});

function clearAirlineBranchFields() {
    $('#branch_id').val('');
    $('#airline_id_branch').val('NOT_SELECTED');
    $('#airline_code_branch').val('');
    $('#branch_name').val('');
    $('#branch_code').val('');
    $('#branch_contact_1').val('');
    $('#branch_contact_2').val('');
    $('#branch_email').val('');
    $('#branch_web').val('');
    $('#branch_contact_person_name').val('');
    $('#branch_country').val('');
    $('#branch_type').val('NOT_SELECTED');
    $('#branch_status').val('NOT_SELECTED');
    $('#branch_comment').val('');
    $('#branch_address').val('');
    $('#saveUpdateBranchBtn').val("Create");
    getAllActiveInactiveAirlineBranches();
}

function saveUpdate() {

    var id = $.trim($('#branch_id').val());
    var airlineId = $.trim($('#airline_id_branch').val());
    var airlineCode = $.trim($('#airline_code_branch').val());
    var branchName = $.trim($('#branch_name').val());
    var branchCode = $.trim($('#branch_code').val());
    var contactNo1 = $.trim($('#branch_contact_1').val());
    var contactNo2 = $.trim($('#branch_contact_2').val());
    var email = $.trim($('#branch_email').val());
    var web = $.trim($('#branch_web').val());
    var contactPerson = $.trim($('#branch_contact_person_name').val());
    var country = $.trim($('#branch_country').val());
    var branchType = $.trim($('#branch_type').val());
    var branchStatus = $.trim($('#branch_status').val());
    var comment = $.trim($('#branch_comment').val());
    var address = $.trim($('#branch_address').val());

    var airlineBranchObj = {
        "id": id,
        "airlineId": airlineId,
        "name": branchName,
        "airlineCode": airlineCode,
        "code": branchCode,
        "contactNo1": contactNo1,
        "contactNo2": contactNo2,
        "email": email,
        "web": web,
        "contactPerson": contactPerson,
        "country": country,
        "comment": comment,
        "branchType": branchType,
        "address": address,
        "status": branchStatus,
    }
    console.log(airlineBranchObj);
    $.ajax({
        url: BASE_URL + '/airlineBranch/saveUpdateAirlineBranch/',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        dataType: "json",
        data: JSON.stringify(airlineBranchObj),
        success: function (data) {
            if (!data.status) {
                toastr.error(data.errorMessages);
            } else {
                clearAirlineBranchFields();
                getAllActiveInactiveAirlineBranches();
                toastr.success("Airline Branch Saved Successfully.");
            }
        },
        error: function (xhr) {
            toastr.error("Saving data for airline branch failed.");
        }
    });
}

function getAllActiveInactiveAirlineBranches() {
    $.ajax({
        url: BASE_URL + '/airlineBranch/getAllActiveInactiveBranches/',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                setAirlineBranchTable(data);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for active inactive airline branch failed.");
        }
    });
}

function setAirlineBranchTable(data) {
    $('#airline_branch_table tbody').empty();
    var tableRows = '';
    $.each(data, function (index, value) {
        tableRows += '<tr><td>' + value.name + '</td><td>' + value.code + '</td><td>' + value.status + '</td>' +
            '<td><button value=' + value.id + ' class="btn btn-info btn-sm" onclick="">Edit</button> ' +
            '<button value=' + value.id + ' class="btn btn-danger btn-sm"  onclick="">Delete</button></td></tr>';
    });
    $('#airline_branch_table').append(tableRows);
}

function getAllActiveAirlines() {
    $(".airline_id_branch").remove();
    $.ajax({
        url: BASE_URL + '/airlineMaster/getAllActive/',
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
                    $('#airline_id_branch').append('<option class="airline_id_branch" value="' + value.id + '">' + value.name + '</option>');
                });
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for acttive airline failed.");
        }
    });
}

function getAirlineById() {
    var airlineId = $.trim($('#airline_id_branch').val());

    $.ajax({
        url: BASE_URL + '/airlineMaster/getById/' + airlineId,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data != null) {
                $('#airline_code_branch').val('');
                $('#airline_code_branch').val(data.code);
            }
        },
        error: function (xhr) {
            toastr.error("Getting data for airline by id failed.");
        }
    });
}



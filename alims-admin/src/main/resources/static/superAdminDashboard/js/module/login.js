var token;
$(document).ready(function () {
    token = sessionStorage.getItem('token');
    console.log("Token: " + token);

    $("#loginBtn").click(function (e) {
        authRequest();
    });

});

function authRequest() {
    var userName = $('#user_name').val();
    var password = $('#user_password').val()

    var divE = document.getElementById("loggedOutMssg");
    divE.hidden = true;

    divE = document.getElementById("invalidCredentialsMssg");
    divE.hidden = true;

    divE = document.getElementById("enterCredentialsMssg");
    divE.hidden = true;

    if (userName.length == 0 || password.length == 0) {
        var divE = document.getElementById("enterCredentialsMssg");
        divE.hidden = false;
        return;
    }

    var authObj = {
        "username": userName,
        "password": password
    }
    console.log(userName + password);
    $.ajax({
        url: BASE_URL + '/admin/authenticate',
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType: "json",
        data: JSON.stringify(authObj),
        success: function (data) {
            if (data.errorMessege != null) {
                var divE = document.getElementById("invalidCredentialsMssg");
                divE.hidden = false;
                return;
            }
            sessionStorage.setItem('token', data.jwtToken);
            token = sessionStorage.getItem('token');
            console.log("*** Authentication begin *** " + sessionStorage.getItem('token'));
            saveUpdateUserToken(userName);
        },
        error: function (xhr) {
            toastr.error("Getting data for token failed.");
        }
    });
}

function saveUpdateUserToken(userName) {
    console.log(userName);
    $.ajax({
        url: BASE_URL + '/userAccount/saveUpdateToken/' + userName,
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            if (data.status) {
                console.log("*** User Token saved successful *** " + userName);
                pageRedirection(token);
            }
        },
        error: function (xhr) {
            toastr.error("Saving token for user failed.");
        }
    });
}

function pageRedirection(token) {
    console.log("*** Try to Redirect to the page ***");
    $.ajax({
        url: BASE_URL + '/userAccount/home',
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'AuthToken': 'Bearer ' + token
        },
        data: {},
        success: function (data) {
            console.log("*** Redirection page ***");
            var homePage = data.homePage;
            var user = data.userName;
            sessionStorage.setItem('token', data.token);
            var token = data.token;
            var userRole = data.userRole;
            window.location.href = BASE_URL + "/admin" + "/" + homePage + "/" + token;
        }
    });
}


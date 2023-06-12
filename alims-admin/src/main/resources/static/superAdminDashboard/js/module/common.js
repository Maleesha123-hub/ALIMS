
var BASE_URL = '';

$(document).ready(function () {
    setBaseUrl();
});

var BASE_URL = ''

function setBaseUrl() {
    BASE_URL = "http://localhost:8762";
}

function setupSessionExpiryHandler() {
    var expiresAt = Date.parse(window.sessionStorage.getItem('expiresAt'));
    setTimeout(handleSessionExpiry, (expiresAt - Date.now()));
}

function handleSessionExpiry() {
    logOut();
}

function logOut() {
    //window.sessionStorage.clear();
    navigateToLogin();
}

function navigateToLogin() {
    location.href = "http://localhost:8762/admin/login";
}

setBaseUrl();
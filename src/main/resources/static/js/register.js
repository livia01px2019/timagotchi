/**
 * Front end logic for the login page.
 */
$(document).ready(() => {
    const studentRadio = document.getElementById('student');
    const teacherRadio = document.getElementById('teacher');
    const name = document.getElementById('name');
    const username = document.getElementById('username');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirm-password');
    const matching = document.getElementById('matching');
    const submit = document.getElementById('submit');
    const message = document.getElementById('message');

    confirmPassword.onkeyup = checkMatch;
    function checkMatch()
    {
        if (password.value === confirmPassword.value) {
            matching.innerHTML = 'Passwords match!';
            matching.style.color = "green";
        } else {
            matching.innerHTML = 'Passwords don\'t match!';
            matching.style.color = "DarkRed";
        }
    }

    submit.onclick = register;
    function register()
    {
        const postParameters = {
            student: studentRadio.checked.toString(),
            teacher: teacherRadio.checked.toString(),
            name: name.value,
            username: username.value,
            password: password.value,
            confirm: confirmPassword.value
        };

        $.post("/register-submit", postParameters, response => {
            const success = JSON.parse(response).results;

            if (success === "Success!") {
                message.innerHTML = "Register successful. Please login."
                message.style.textAlign = "center";
                message.style.width = "100%";
                message.style.backgroundColor = "green";
            } else if (success === "Passwords don't match!") {
                matching.innerHTML = 'Passwords don\'t match!';
                matching.style.color = "DarkRed";
            } else {
                matching.innerHTML = success;
            }
        })
    }
});

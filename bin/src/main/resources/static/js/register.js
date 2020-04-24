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
    const submit = document.getElementById('submit');

    $('#password, #confirm-password').on('keyup', function () {
        if ($('#password').val() == $('#confirm-password').val()) {
            $('#message').html('Matching').css('color', 'green');
        } else
            $('#message').html('Not Matching').css('color', 'red');
    });

    studentButton.onclick = studentLogin;
    function studentLogin()
    {
        const postParameters = {
            username: studentUsername.val(),
            password: studentPassword.val()
        };

        $.post("/login-student", postParameters, response => {
            const success = JSON.parse(response).results;

            if (success === "Success!") {
                window.location.href = '/student/main';
            } else {
                studentError.innerHTML = success;
            }
        })
    }

    teacherButton.onclick = teacherLogin;
    function teacherLogin()
    {
        const postParameters = {
            username: teacherUsername.val(),
            password: teacherPassword.val()
        };

        $.post("/login-teacher", postParameters, response => {
            const success = JSON.parse(response).results;

            if (success === "Success!") {
                window.location.href = '/teacher/main';
            } else {
                teacherError.innerHTML = success;
            }
        })
    }
});

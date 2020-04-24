/**
 * Front end logic for the login page.
 */
$(document).ready(() => {
    const studentUsername = $("#student-username");
    const studentPassword = $("#student-password");
    const studentButton = document.getElementById('student-button');
    const studentError = document.getElementById('student-error');

    const teacherUsername = $("#teacher-username");
    const teacherPassword = $("#teacher-password");
    const teacherButton = document.getElementById('teacher-button');
    const teacherError = document.getElementById('teacher-error');

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

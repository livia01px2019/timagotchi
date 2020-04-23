// BEGIN REDACT
/**
 * Front end logic for the login page.
 */

$(document).ready(() => {
    const studentUsername = $("#student-username");
    const studentPassword = $("#student-password");
    const studentButton = document.getElementById('student-button');
    const teacherUsername = $("#teacher-username");
    const teacherPassword = $("#teacher-password");
    const teacherButton = document.getElementById('teacher-button');

    studentButton.onclick = studentLogin;
    function studentLogin()
    {
        const postParameters = {
            username: studentUsername.val(),
            password: studentPassword.val()
        };

        $.post("/login-student", postParameters, response => {
            const valid = JSON.parse(response).results;
            console.log(valid);
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
            const valid = JSON.parse(response).results;
            console.log(valid);
        })
    }
});
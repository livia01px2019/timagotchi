// BEGIN REDACT
/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const classCode = $("#code");
    const submitButton = document.getElementById('submit-button');
    const error = document.getElementById('error-message');

    submitButton.onclick = createNewClass;
    function createNewClass()
    {
        const postParameters = {
            code: classCode.val()
        };

        $.post("submit-new-class", postParameters, response => {
            const success = JSON.parse(response).results;

            if (success === "Success!") {
                window.location.href = '/teacher/main';
            } else {
                error.innerHTML = success;
            }
        })
    }
});
// BEGIN REDACT
/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const className = $("#name");
    const submitButton = document.getElementById('submit-button');
    const error = document.getElementById('error-message');

    submitButton.onclick = createNewClass;
    function createNewClass()
    {
        const postParameters = {
            name: className.val()
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
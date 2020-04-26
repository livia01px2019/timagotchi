$(document).ready(() => {
    //TODO: get the jquery selectors for the list where the suggestions should go and the input box where we're typing
    //HINT: look at the hTML
    const quizList = $("#quizzes");

    window.onload = () => {
        $.post("/student/quiz", postParameters, response => {
            //TODO: using the response object, use JSON to parse it
            //HINT: remember to get the specific field in the JSON you want to use

            let resultsSet = JSON.parse(response).quizzes;
            //TODO: for each element in the set of results, append it to the suggestionList
            for (let result of resultsSet) {
                quizList.append("<tr><td>" + result.name + "</td><td>" + "completed" + "</td></tr>");
            }
        });

        loadTableData(quizList);
    };

    function loadTableData(quizList) {
        const tableBody = document.getElementById('quizTableData');
        let dataHtml = quizList;

        tableBody.innerHTML = dataHtml
    }
});
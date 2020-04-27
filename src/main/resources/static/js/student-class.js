/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const quizTab = document.getElementById('quiz');
    const checkoffTab = document.getElementById('checkoff');
    const quizzes = document.getElementById('quizzes');
    const review = document.getElementById('review');
    const checkoffs = document.getElementById('checkoffs');
    let assignmentNames = [];
    let assignmentIds = [];


    quizTab.onclick = openAssignmentsTab;
    function openAssignmentsTab() {
        // Get all elements with class="tabcontent" and hide them
        const tabcontent = document.getElementsByClassName("tabcontent");
        for (var i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }

        // Get all elements with class="tablinks" and remove the class "active"
        const tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }

        // Show the current tab, and add an "active" class to the button that opened the tab
        quizzes.style.display = "block";
        quizTab.className += " active";

        // Get assignment information.
        const postParameters = {
            type: "quiz"
        };

        $.post("/student-class-get", postParameters, response => {
            assignmentIds = JSON.parse(response).ids;
            assignmentNames = JSON.parse(response).names;

            if (assignmentNames.length === 0) {
                document.getElementById("quiz-list").style.backgroundColor = "Transparent";
            }

            for(let i = 0; i < assignmentNames.length; i++) {
                let name = assignmentNames[i];
                console.log(name);
                quizzes.innerHTML += "<li><button id=" + i + ">name</button></li>";

                $("#" + i).onclick(function() {
                    window.location.href = '/student/view-quiz/' + assignmentIds[i];
                })
            }
        })
    }

    checkoffTab.onclick = openCheckoffTab;
    function openCheckoffTab() {
        // Get all elements with class="tabcontent" and hide them
        const tabcontent = document.getElementsByClassName("tabcontent");
        for (var i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }

        // Get all elements with class="tablinks" and remove the class "active"
        const tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }

        // Show the current tab, and add an "active" class to the button that opened the tab
        checkoffs.style.display = "block";
        checkoffTab.className += " active";

        // Get assignment information.
        const postParameters = {
            type: "checkoff"
        };

        $.post("/student-class-get", postParameters, response => {
            assignmentIds = JSON.parse(response).ids;
            assignmentNames = JSON.parse(response).names;

            if (assignmentNames.length === 0) {
                document.getElementById("checkoff-list").style.backgroundColor = "Transparent";
            }

            for(let i = 0; i < assignmentNames.length; i++) {
                let name = assignmentNames[i];
                console.log(name);
                checkoffs.innerHTML += "<li><button id=" + i + ">name</button></li>";
            }
        })
    }

    review.onclick = reviewQuiz;
    function reviewQuiz() {
        // Get assignment information.
        const postParameters = {
            type: "review"
        };

        $.post("/student-class-get", postParameters, response => {
            assignmentId = JSON.parse(response).id;

            window.location.href = '/student/view-quiz/' + assignmentId;
        })
    }

    // Open the assignments tab to start.
    openAssignmentsTab();
});
/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const quizTab = document.getElementById('quiz');
    const checkoffTab = document.getElementById('checkoff');
	const leaderboardTab = document.getElementById('leaderboard');
    const quizzes = document.getElementById('quizzes');
    const review = document.getElementById('review');
    const checkoffs = document.getElementById('checkoffs');
    let assignmentNames = [];
    let assignmentIds = [];
    let scores = [];
    let totalScores = [];
    let complete = [];


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
            scores = JSON.parse(response).scores;
            totalScores = JSON.parse(response).totalScores;
            complete = JSON.parse(response).completed;


            if (assignmentNames.length === 0) {
                document.getElementById("quizList").style.backgroundColor = "Transparent";
            }

            document.getElementById("quizList").innerHTML = "<li class=\"review\"><button class=\"inner\" id=\"review\">Review</button></li>";
            for(let i = 0; i < assignmentNames.length; i++) {
                let name = assignmentNames[i];
                console.log(complete[i]);
                if (complete[i] === "false") {
                    document.getElementById("quizList").innerHTML +=
                        "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + name +
                        "<p class=\"right\" style=\"color:black\">/" + totalScores[i] +
                        " pts</p><p class=\"right\" style=\"color:red\">INCOMPLETE</p></button></li>";
                } else {
                    document.getElementById("quizList").innerHTML +=
                        "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + name +
                        "<p class=\"right\">" + scores[i] + "/" + totalScores[i] + " pts</p></button></li>";
                }

                $(".outer").click(function() {
                    const id = this.id;
                    window.location.href = '/student/view-quiz/' + assignmentIds[id];
                })
                $(".review").click(function() {
                    // Get assignment information.
                    const postParameters = {
                        type: "review"
                    };

                    $.post("/student-class-get", postParameters, response => {
                        const assignmentId = JSON.parse(response).ids;
                        window.location.href = '/student/view-quiz/' + assignmentId;
                    })
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
            scores = JSON.parse(response).scores;
            totalScores = JSON.parse(response).totalScores;
            complete = JSON.parse(response).completed;

            if (assignmentNames.length === 0) {
                document.getElementById("checkoffList").style.backgroundColor = "Transparent";
            }

            document.getElementById("checkoffList").innerHTML = "";
            for(let i = 0; i < assignmentNames.length; i++) {
                let name = assignmentNames[i];
                if (complete[i] === "false") {
                    document.getElementById("checkoffList").innerHTML +=
                        "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + name +
                        "<p class=\"right\" style=\"color:black\">/" + totalScores[i] +
                        " pts</p><p class=\"right\" style=\"color:red\">INCOMPLETE</p></button></li>";
                } else {
                    document.getElementById("checkoffList").innerHTML +=
                        "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + name +
                        "<p class=\"right\">" + scores[i] + "/" + totalScores[i] + " pts</p></button></li>";
                }
            }
        })
    }

	leaderboardTab.onclick = openLeaderboardTab;
    function openLeaderboardTab() {
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
        leaderboards.style.display = "block";
        leaderboardTab.className += " active";


    }

    // Open the assignments tab to start.
    openAssignmentsTab();
});
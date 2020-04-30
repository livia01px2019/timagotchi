/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const assignmentsTab = document.getElementById('assignments');
    const studentsTab = document.getElementById('students');
    const classId = document.getElementById('class-code');
    const quizzes = document.getElementById('quiz-list');
	const checkoffs = document.getElementById('checkoff-list');
	const assignments = document.getElementById('assignments-list');
    let buttons = null;
    const create = document.getElementById('create-assignment');
    let classNames = [];
    let classIds = [];

    assignmentsTab.onclick = openAssignmentsTab;
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
        document.getElementById("Assignments").style.display = "block";
        assignmentsTab.className += " active";

        // Get assignment information.
        const postParameters = {
            type: "assignments"
        };

        $.post("/teacher-class-get", postParameters, response => {
            assignmentObjects = JSON.parse(JSON.parse(response).assignments);

            if (assignmentObjects.length === 0) {
                quizzes.style.backgroundColor = "Transparent";
				checkoffs.style.backgroundColor = "Transparent";
            }

            quizzes.innerHTML = "";
			checkoffs.innerHTML = "";
            for(let i = 0; i < assignmentObjects.length; i++) {
				const currentAssignment = assignmentObjects[i];
				if (currentAssignment.type == "quiz") {
					quizzes.innerHTML += "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + currentAssignment.name + "</button></li>";
				} else {
					checkoffs.innerHTML += "<li class=\"outer\" id=" + i + "><button class=\"inner\">" + currentAssignment.name + "</button></li>";
				}
            }

            $(".outer").click(function() {
                const i = this.id;
                window.location.href = '/teacher/viewAssignment/' + assignmentObjects[i].id;
            })
        })
    }

    studentsTab.onclick = openStudentsTab;
    function openStudentsTab() {
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
        document.getElementById("Students").style.display = "block";
        studentsTab.className += " active";
    }

    create.onclick = createAssignment;
    function createAssignment() {
        window.location.href = '/teacher/create-assignment';
    }

    // Open the assignments tab to start.
    openAssignmentsTab();
});
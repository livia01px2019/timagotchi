/**
 * Front end logic for the new class page.
 */

$(document).ready(() => {
    const assignmentsTab = document.getElementById('assignments');
    const studentsTab = document.getElementById('students');
    const classId = document.getElementById('class-code');
    const assignments = document.getElementById('assignments-list');

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
            classIds = JSON.parse(response).ids;
            classNames = JSON.parse(response).names;

            if (classNames.length === 0) {
                assignments.style.backgroundColor = "Transparent";
            }

            assignments.innerHTML = "";
            for(let i = 0; i < classNames.length; i++) {
                let name = classNames[i];
                console.log(name);
                assignments.innerHTML += "<li class=\"outer\"><button class=\"inner\" id=" + i + ">" + name + "</button></li>";

                $("#" + i).onclick = assign;
                function assign() {
                    window.location.href = '/teacher/viewAssignment/' + classIds[i];
                }
            }
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
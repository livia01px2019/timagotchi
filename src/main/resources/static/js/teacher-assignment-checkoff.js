(function(){
    // Functions
    function buildTable(){
        // variable to store the HTML output
        const output = [];

        // for each question...
		const studentsList = JSON.parse(students);
		
		for (const currentStudent of studentsList) {
			const checked = "";
			if (currentStudent.score == 1) {
				checked = "checked";
			}
			output.push(
                    `<div class="studentBlock"> <a href="/${currentStudent.id}">
            <div class="name"> ${currentStudent.name} </div> </a>
            <input type="checkbox" name="complete" value="${currentStudent.id}" ${checked}>
          </div>`
                );
			console.log(`${currentStudent.name}`);
		}

        // finally combine our output list into one string of HTML and put it on the page
        assignmentContainer.innerHTML = output.join('');
    }

	function deleteAssignment()
    {
        const postParameters = {
            
        };

        $.post("delete-assignment", postParameters, response => {
            const success = JSON.parse(response).results;
			const classId = JSON.parse(response).classId;
			
			console.log(success);

            if (success === "Success!") {
                window.location.href = '/teacher/'+ classId;
            } else {
                error.innerHTML = success;
            }
        })
    }

	function updateAssignment()
    {

        const checkBoxes = document.getElementsByName('complete');

        let completed = [];
		let notCompleted = [];
        for (let item of checkBoxes) {
			if ($(item).checked == true){
				completed.push($(item).val());
			} else {
				notCompleted.push($(item).val());
			}
		}

        const postParameters = {
            complete: JSON.stringify(completed),
            notComplete: JSON.stringify(notCompleted)
        };
        console.log("complete: "+ complete);
        console.log("not complete: "+ notComplete);

        $.post("/teacher/update-checkoff-submit", postParameters, response => {
            const message = JSON.parse(response).results;
			console.log(message);
			window.alert(message);
			location.reload();
        })
        return false;
    }

    // Variables
    const assignmentContainer = document.getElementById('assignmentBlock');
    

    // Kick things off
    buildTable();
	const deleteButton = document.getElementById('delete-button');
	const updateButton = document.getElementById('update-button');
    const error = document.getElementById('error-message');

	
    deleteButton.onclick = deleteAssignment;
	updateButton.onclick = updateAssignment;

})();
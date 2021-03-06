(function(){
    // Functions
    function buildTable(){
        // variable to store the HTML output
        const output = [];

        // for each question...
		const studentsList = JSON.parse(students);
		
		for (const currentStudent of studentsList) {
			let score = currentStudent.score;
			if(currentStudent.score == null){
				score = "INCOMPLETE";
				output.push(
                    `<div class="studentBlock"> 
            <div class="name"> ${currentStudent.name} </div>
            <div class="score"> ${score} / ${totalScore} pts </div>
          </div>`
                );
			} else {
				output.push(
                    `<div class="studentBlock"> <a href="/teacher/viewAssignmentStudent/${currentStudent.id}">
            <div class="name"> ${currentStudent.name} </div> </a>
            <div class="score"> ${score} / ${totalScore} pts </div>
          </div>`
                );
			}
			
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

    // Variables
    const assignmentContainer = document.getElementById('assignmentBlock');
    

    // Kick things off
    buildTable();
	const deleteButton = document.getElementById('delete-button');
    const error = document.getElementById('error-message');

    deleteButton.onclick = deleteAssignment;

})();
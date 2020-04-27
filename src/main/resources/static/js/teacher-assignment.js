(function(){
    // Functions
    function buildTable(){
        // variable to store the HTML output
        const output = [];

        // for each question...
		const studentsList = JSON.parse(students);
		
		for (const currentStudent of studentsList) {
			output.push(
                    `<div class="studentBlock"> <a href="/${currentStudent.id}">
            <div class="name"> ${currentStudent.name} </div>
            <div class="score"> ${currentStudent.score} / ${totalScore} </div>
          </a></div>`
                );
			console.log(`${currentStudent.name}`);
		}

        // finally combine our output list into one string of HTML and put it on the page
        assignmentContainer.innerHTML = output.join('');
    }

    // Variables
    const assignmentContainer = document.getElementById('assignmentBlock');
    

    // Kick things off
    buildTable();

})();

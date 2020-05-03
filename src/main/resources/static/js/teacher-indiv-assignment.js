(function(){
    // Functions
    function buildTable(){
        // variable to store the HTML output
        const output = [];

        // for each question...
		const recordList = JSON.parse(record);
		
		for (const row of recordList) {
			const correct = row.correct;
			if (correct){
				output.push(
                    `<div class="recordRow"> 
            <div class="question"> ${row.questionPrompt} </div>
            <div class="question"> ${row.questionAnswer} </div>
			<div class="question"> ${row.correctAnswer} </div>
			<div class="correct"> ${row.answer} <img src="../../img/correct-icon.png" alt="correct icon"></div>
          </div>`
                );
			} else {
				output.push(
                    `<div class="recordRow"> 
            <div class="question"> ${row.questionPrompt} </div>
            <div class="question"> ${row.questionAnswer} </div>
			<div class="question"> ${row.correctAnswer} </div>
			<div class="incorrect"> ${row.answer} <img src="../../img/wrong-icon.png" alt="incorrect icon"></div>
          </div>`
                );
			}
					}

        // finally combine our output list into one string of HTML and put it on the page
        recordContainer.innerHTML = output.join('');
    }

    // Variables
    const recordContainer = document.getElementById('recordBlock');
    

    // Kick things off
    buildTable();

})();
(function(){
    // Functions


    // Variables
    const quizContainer = document.getElementById('quiz');
    const resultsContainer = document.getElementById('results');
    const submitButton = document.getElementById('submit');
	const reward = 0;
    let myQuestions = [];

    //new code
    const assignmentID = document.getElementById("hidden").className;
    const address = "/student/load-quiz/" + assignmentID;
    const postParameters = {
        id: assignmentID
    };
    $.post(address, postParameters, response => {
        const convertedAssignment = JSON.parse(response).assignment;
		reward = convertedAssignment.reward;
        const questionSet = convertedAssignment.questions;
        for (let q of questionSet) {
            let a={}
            let correct
            if (q.choices.length == 4) {
                a = {
                    A: q.choices[0],
                    B: q.choices[1],
                    C: q.choices[2],
                    D: q.choices[3]
                }
            } else if (q.choices.length == 3) {
                a = {
                    A: q.choices[0],
                    B: q.choices[1],
                    C: q.choices[2]
                }
            } else {
                a = {
                    A: q.choices[0],
                    B: q.choices[1]
                }
            }
            if (q.answers[0] == 0) {
                correct = "A"
            } else if (q.answers[0] == 1) {
                correct = "B"
            } else if (q.answers[0] == 2) {
                correct = "C"
            } else {
                correct = "D"
            }
            const currQuestion = {
                question: q.prompt,
                answers: a,
                correctAnswer: correct
            }
            myQuestions.push(currQuestion)
        }

        function buildQuiz(){
            // variable to store the HTML output
            const output = [];

            console.log(myQuestions)
            // for each question...
            myQuestions.forEach(
                (currentQuestion, questionNumber) => {

                    // variable to store the list of possible answers
                    const answers = [];

                    // and for each available answer...
                    for(letter in currentQuestion.answers){

                        // ...add an HTML radio button
                        answers.push(
                            `<label>
              <input type="radio" name="question${questionNumber}" value="${letter}">
              ${letter} :
              ${currentQuestion.answers[letter]}
            </label>`
                        );
                    }

                    // add this question and its answers to the output
                    output.push(
                        `<div class="slide">
            <div class="question"> ${currentQuestion.question} </div>
            <div class="answers"> ${answers.join("")} </div>
          </div>`
                    );
                }
            );

            // finally combine our output list into one string of HTML and put it on the page
            quizContainer.innerHTML = output.join('');
        }

        function showResults(){
	
			document.getElementById('congrats-banner').innerHTML = "<img src=\"../../img/congrats-banner.png\""+
			"style=\"width:100%\"><div class=\"congrats-words\"><h1>CONGRATS!</h1><p>+" + reward + "XP</p></div></img>";

            let newHTML = "<table style=\"width:100%;margin-left:auto;margin-right:auto\"><tr><th>Question</th>" +
                "<th>Answer</th></tr>";


            // gather answer containers from our quiz
            const answerContainers = quizContainer.querySelectorAll('.answers');

            // keep track of user's answers
            let numCorrect = 0;

            // for each question...
            myQuestions.forEach( (currentQuestion, questionNumber) => {
                newHTML += "<tr>" +
                    "<td style=\"text-align:left; outline: thin solid; border-collapse: collapse\">" +
                    currentQuestion.question + "</td>";
                // find selected answer
                const answerContainer = answerContainers[questionNumber];
                const selector = `input[name=question${questionNumber}]:checked`;
                const userAnswer = (answerContainer.querySelector(selector) || {}).value;
                let chosenAnswer;
                if (userAnswer === undefined) {
                    chosenAnswer = "Did not answer";
                } else {
                    for (letter in currentQuestion.answers) {
                        if(userAnswer === letter) {
                            chosenAnswer = letter.toString() + " : " + currentQuestion.answers[letter];
                        }
                    }
                }


                // if answer is correct
                if(userAnswer === currentQuestion.correctAnswer){
                    // add to the number of correct answers
                    numCorrect++;

                    newHTML += "<td bgcolor=\"teal\" style=\"outline: thin solid; border-collapse: collapse\">" +
                        chosenAnswer + "</td></tr>"
                }
                // if answer is wrong or blank
                else{
                    newHTML += "<td bgcolor=\"#C31F48\" style=\"outline: thin solid; border-collapse: collapse\">" +
                        chosenAnswer + "</td></tr>"
                }
            });
			document.getElementById('finishButton').innerHTML = "<a href=\"/student/main\"><button style=\"width:100%\">FINISH</button></a>";		
            // show number of correct answers out of total
            document.getElementById('test').innerHTML = newHTML;
            resultsContainer.innerHTML = `${numCorrect} out of ${myQuestions.length}`;
        }

        function showSlide(n) {
            slides[currentSlide].classList.remove('active-slide');
            slides[n].classList.add('active-slide');
            currentSlide = n;
            if(currentSlide === 0){
                previousButton.style.display = 'none';
            }
            else{
                previousButton.style.display = 'inline-block';
            }
            if(currentSlide === slides.length-1){
                nextButton.style.display = 'none';
                submitButton.style.display = 'inline-block';
            }
            else{
                nextButton.style.display = 'inline-block';
                submitButton.style.display = 'none';
            }
        }

        function showNextSlide() {
            showSlide(currentSlide + 1);
        }

        function showPreviousSlide() {
            showSlide(currentSlide - 1);
        }

        // Kick things off
        buildQuiz();

        // Pagination
        const previousButton = document.getElementById("previous");
        const nextButton = document.getElementById("next");
        const slides = document.querySelectorAll(".slide");
        let currentSlide = 0;

        // Show the first slide
        showSlide(currentSlide);

        // Event listeners
        submitButton.addEventListener('click', showResults);
        previousButton.addEventListener("click", showPreviousSlide);
        nextButton.addEventListener("click", showNextSlide);
    })

})();
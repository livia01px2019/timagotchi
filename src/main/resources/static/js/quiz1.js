(function(){
    // Functions


    // Variables
    const quizContainer = document.getElementById('quiz');
    const resultsContainer = document.getElementById('results');
    const submitButton = document.getElementById('submit');
    let myQuestions = [];
    let record = [];
    let studentRecord = [];

    //new code
    const assignmentID = document.getElementById("hidden").className;
    const address = "/student/load-quiz/" + assignmentID;
    const postParameters = {
        id: assignmentID
    };

    $.post(address, postParameters, response => {
        const convertedAssignment = JSON.parse(response).assignment;
		const reward = convertedAssignment.reward;
        const questionSet = convertedAssignment.questions;
		console.log(convertedAssignment);
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
            if (JSON.parse(response).retry === false) {
                document.getElementById('congrats-banner').innerHTML = "<img src=\"../../img/congrats-banner.png\""+
                    "style=\"width:100%\"><div class=\"congrats-words\"><h1>CONGRATS!</h1></div></img>";
				document.getElementById('feed').style.visibility = 'visible';
				document.getElementById('feed-button').style.visibility = 'visible';
				
            } else {
                document.getElementById('congrats-banner').innerHTML = "<img src=\"../../img/congrats-banner.png\""+
                    "style=\"width:100%\"><div class=\"congrats-words\"><h1>CONGRATS!</h1></div></img>";
				document.getElementById('feed').style.height = '0px';

            }

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
				// keep a record of the student's answer
				studentRecord.push(userAnswer);
                // if answer is correct
                if(userAnswer === currentQuestion.correctAnswer){
                    // add to the number of correct answers
                    numCorrect++;
                    record.push("true");
                    newHTML += "<td bgcolor=\"teal\" style=\"outline: thin solid; border-collapse: collapse\">" +
                        chosenAnswer + "</td></tr>"
                }
                // if answer is wrong or blank
                else{
                    record.push("false");
                    newHTML += "<td bgcolor=\"#C31F48\" style=\"outline: thin solid; border-collapse: collapse\">" +
                        chosenAnswer + "</td></tr>"
                }
            });

            const postParams = {
                record: JSON.stringify(record),
                id: assignmentID,
                reward: reward,
                studentRecord: JSON.stringify(studentRecord)
            };

            $.post("/student/quiz-finished", postParams, response => {
                window.location.ref = "/student/main";
            });

			document.getElementById('finishButton').innerHTML = "<a href=\"/student/main\"><button style=\"width:100%; margin-top:20px\">FINISH</button></a>";
            // show number of correct answers out of total
            document.getElementById('test').innerHTML = newHTML;
            resultsContainer.innerHTML = `${numCorrect} out of ${myQuestions.length}`;
            if (convertedAssignment.competitive == true) {
                const retry = JSON.parse(response).retry;
                const ranking = JSON.parse(response).ranking;
                const studentName = JSON.parse(response).name;
                if (retry == false) {
                    let index = 0;
                    for (let entry = 0; entry < ranking.length; entry++) {
                        if (numCorrect.toString > ranking[entry][1]) {
                            break;
                        }
                        index++;
                    }
                    ranking.splice(index, 0, [studentName, numCorrect]);
                }
                let rank = 1;
                let leaderboardHTML = "<div class=\"leaderboard\"><div class=\"leaderboard-header\">" +
                    "<h3>LEADERBOARD</h3></div>";
                for (let i = 0; i < ranking.length; i++) {
					leaderboardHTML += "<div class=\"leaderboard-item\"><div class=\"leaderboard-row\"><h2>"
                    leaderboardHTML += rank + "<h2><p>" + ranking[i][0] + "<p></div><p>";
                    leaderboardHTML += ranking[i][1] + "</p></div>";
                    rank++;
                }
                leaderboardHTML += "</div>";
                console.log(leaderboardHTML);
                let leaderboard = document.getElementById('scoreboard');
                leaderboard.insertAdjacentHTML('beforeend', leaderboardHTML);
                leaderboard.style.display = 'block';
            }
        }

        function showSlide(n) {
            slides[currentSlide].classList.remove('active-slide');
            slides[n].classList.add('active-slide');
            currentSlide = n;
            if (slides.length === 1) {
                previousButton.style.display = 'none';
                nextButton.style.display = 'none';
                submitButton.style.display = 'inline-block';
            } else {
                if (currentSlide === 0) {
                    previousButton.style.display = 'none';
                    nextButton.style.display = 'inline-block';
                    submitButton.style.display = 'none';
                }
                else if (currentSlide === slides.length - 1) {
                    previousButton.style.display = 'inline-block';
                    nextButton.style.display = 'none';
                    submitButton.style.display = 'inline-block';
                } else {
                    previousButton.style.display = 'inline-block';
                    nextButton.style.display = 'inline-block';
                    submitButton.style.display = 'none';
                }
            }
        }

        function showNextSlide() {
            showSlide(currentSlide + 1);
        }

        function showPreviousSlide() {
            showSlide(currentSlide - 1);
        }

		function feed(){
			document.getElementById('feed-button').style.visibility = 'hidden';
			document.getElementById('jar').src='../../img/empty-jar.png';
			document.getElementById('cookie-container').style.visibility = 'visible';
			let pos = 0;
		    let id = setInterval(frame, 10);
		    function frame() {
		      if (pos == 130) {
		        clearInterval(id);
				document.getElementById('cookie-container').style.visibility = 'hidden';
				const audio = new Audio("../../audio/eating.m4a");
                audio.play();
				setTimeout(function(){
					const audio2 = new Audio("../../audio/yay.wav");
                	audio2.play();
				}, 2000);
				const pet= document.getElementById('pet');
				const petContainer= document.getElementById('petContainer');
				setTimeout(function(){petContainer.style.marginBottom = "8px";}, 2000);
		        setTimeout(function(){petContainer.style.marginBottom = "4px";}, 2020);
		        setTimeout(function(){petContainer.style.marginBottom = "8px";}, 2040);
		        setTimeout(function(){petContainer.style.marginBottom = "12px";}, 2060);
		        setTimeout(function(){petContainer.style.marginBottom = "16px";}, 2080);
		        setTimeout(function(){petContainer.style.marginBottom = "20px";}, 2100);
		        setTimeout(function(){petContainer.style.marginBottom = "24px";}, 2120);
		        setTimeout(function(){petContainer.style.marginBottom = "20px";}, 2140);
		        setTimeout(function(){petContainer.style.marginBottom = "12px";}, 2160);
				setTimeout(function(){document.getElementById('add-xp').innerHTML = "+ " + reward + " XP";}, 2000);
				
				if (!(currImage === withRewardImage)) {
					console.log("here");
					setTimeout(function(){document.getElementById('add-xp').innerHTML = "Your Timagotchi is evolving!";}, 2300);
					setTimeout(function(){
						const audio3 = new Audio("../../audio/evolution.mp3");
	                	audio3.play();
					}, 2300);
					setTimeout(function(){pet.src = withRewardImage;}, 3200);
					setTimeout(function(){pet.src = currImage;}, 3500);
					setTimeout(function(){pet.src = withRewardImage;}, 3800);
					setTimeout(function(){pet.src = currImage;}, 4100);
					setTimeout(function(){pet.src = withRewardImage;}, 4400);
					setTimeout(function(){pet.src = currImage;}, 4700);
					setTimeout(function(){pet.src = withRewardImage;}, 5000);
					setTimeout(function(){pet.src = currImage;}, 5300);
					setTimeout(function(){pet.src = withRewardImage;}, 5600);
					setTimeout(function(){pet.src = currImage;}, 5900);
					setTimeout(function(){pet.src = withRewardImage;}, 6200);
					setTimeout(function(){pet.src = currImage;}, 6600);
					setTimeout(function(){pet.src = withRewardImage;}, 7000);
					setTimeout(function(){pet.src = currImage;}, 7400);
					setTimeout(function(){pet.src = withRewardImage;}, 8000);
					setTimeout(function(){document.getElementById('add-xp').innerHTML = "";}, 7800);
					setTimeout(function(){
						const audio4 = new Audio("../../audio/yay2.wav");
	                	audio4.play();
					}, 8900);
					setTimeout(function(){petContainer.style.marginBottom = "8px";}, 8900);
			        setTimeout(function(){petContainer.style.marginBottom = "4px";}, 8920);
			        setTimeout(function(){petContainer.style.marginBottom = "8px";}, 8940);
			        setTimeout(function(){petContainer.style.marginBottom = "12px";}, 8960);
			        setTimeout(function(){petContainer.style.marginBottom = "16px";}, 8980);
			        setTimeout(function(){petContainer.style.marginBottom = "20px";}, 9000);
			        setTimeout(function(){petContainer.style.marginBottom = "24px";}, 9020);
			        setTimeout(function(){petContainer.style.marginBottom = "20px";}, 9040);
			        setTimeout(function(){petContainer.style.marginBottom = "12px";}, 9060);
				}
		      } else {
		        pos++;
		        document.getElementById('cookie-container').style.paddingLeft = pos + 'px';
		        document.getElementById('cookie-container').style.paddingLeft = pos + 'px';
		      }
		    }
			
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
		document.getElementById('feed-button').addEventListener("click", feed);
    })

})();

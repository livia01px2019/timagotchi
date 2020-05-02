<#assign content>
    <script src="../js/jquery-2.1.1.js"></script>
    <script src="../js/create_assignment.js"></script>
    <link href="../css/create_assignment.css" rel="stylesheet">
<#--    <script src="script.js"></script>-->
	<h1>NEW ASSIGNMENT</h1>
	
    <form novalidate='novalidate' class='questionbox'>
        <div class='head' >
            <div class='header-field'>
               	<label style="height: 40px">Title</label>
                <input id="assignmentTitle" style="width: 70%; height: 40px; margin-left: 5px; margin-bottom: 20px; margin-right: 10px"/>
                <div class="tooltip">
                	<label style="height: 40px">Points</label>
                	<span class="tooltiptext">We recommend allocating XP so that students can reach at least 1,500 XP by the end of the year.</span>
                </div>
                <input id="points" style="width: 22%; height: 40px; margin-left: 5px; margin-bottom: 20px"/>
            </div>
            <div class = "radio-buttons">
	            <div class='button'>
	            	<div class="tooltip">
	                	<input id='checkoff' required='required' type='radio' value='checkoff' name='atype'>Checkoff</input>
		                <span class="tooltiptext">Checkoff assignments are assignments that students finish outside of this website but can be given XP for if you check them off.</span>
	                </div>
	                <div class="tooltip">
	                	<input id='quiz' required='required' type='radio' value='quiz' name='atype' checked>Quiz</input>
	                	<span class="tooltiptext">Quiz assignments are created and and completed by students directly on this website.</span>
	                </div>
	            </div>
	            <div id="competitive-button" class='button tooltip' style='padding-left: 7%'>
	                <input id='competitive' required='required' type='checkbox' value='competitive' style="margin-right: 5px">Competitive</input>
	                <span class="tooltiptext">Competitive assignments allow students to compare their score against other students in a leaderboard.</span>
	            </div>
	        </div>

        </div>
        <div class='repeater' id = 'repeater'>
            <section class='repeatable'>
                <h2>Question 1</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question' maxlength='65535' name='question' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_1' maxlength='255' name='answer_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_2' maxlength='255' name='answer_2' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_3' maxlength='255' name='answer_3' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_4' maxlength='255' name='answer_4' type='text'>
                    </label>
                </div>
                <label style="padding-bottom: 0.5rem">
                    Correct Answer:
                    <textarea class='textbox' data-structure='correct' maxlength='255' name='correct' placeholder='Please enter the number representing the correct answer (1-4)' rows='1'></textarea>
                </label>
            </section>
            <section class='repeatable'>
                <h2>Question 2</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question' maxlength='65535' name='question' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_1' maxlength='255' name='answer_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_2' maxlength='255' name='answer_2' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_3' maxlength='255' name='answer_3' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_4' maxlength='255' name='answer_4' type='text'>
                    </label>
                </div>
                <label style="padding-bottom: 0.5rem">
                    Correct Answer:
                    <textarea class='textbox' data-structure='correct' maxlength='255' name='correct' placeholder='Please enter the number representing the correct answer (1-4)' rows='1'></textarea>
                </label>
            </section>
            <section class='repeatable'>
                <h2>Question 3</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question' maxlength='65535' name='question' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_1' maxlength='255' name='answer_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_2' maxlength='255' name='answer_2' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_3' maxlength='255' name='answer_3' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_4' maxlength='255' name='answer_4' type='text'>
                    </label>
                </div>
                <label style="padding-bottom: 0.5rem">
                    Correct Answer:
                    <textarea class='textbox' data-structure='correct' maxlength='255' name='correct' placeholder='Please enter the number representing the correct answer (1-4)' rows='1'></textarea>
                </label>
            </section>
            <input id='count' name='count' type='hidden' value='3'>
        </div>
        <div class="submit">
            <input id='discard' name='discard' type='submit' value='DISCARD'>
            <input id='submit' name='submit' type='submit' value='ADD ASSIGNMENT'>
        </div>
    </form>
</#assign>
<#include "teacher-main.ftl">
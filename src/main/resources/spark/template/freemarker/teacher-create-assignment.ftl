<#assign content>
    <script src="../js/create_assignment.js"></script>
    <link href="../css/create_assignment.css" rel="stylesheet">
<#--    <script src="script.js"></script>-->
    <form novalidate='novalidate' class='questionbox'>
        <div class='head' >
            <div class='header-field'>
                <label style="height: 40px">Title</label>
                <input style="width: 70%; height: 40px; margin-left: 5px; margin-bottom: 20px; margin-right: 10px"/>
                <label style="height: 40px">Points</label>
                <input style="width: 22%; height: 40px; margin-left: 5px; margin-bottom: 20px"/>
            </div>
            <div class='button'>
                <input required='required' type='radio' value='checkoff'>Checkoff</input>
                <input required='required' type='radio' value='quiz'>Quiz</input>
            </div>

        </div>
        <div class='repeater'>
            <section class='repeatable'>
                <h2>Question 1</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question_' id='notes_1' maxlength='65535' name='notes_1' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                </div>
            </section>
            <section class='repeatable'>
                <h2>Question 2</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question_' id='notes_1' maxlength='65535' name='notes_1' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                </div>
            </section>
            <section class='repeatable'>
                <h2>Question 3</h2>
                <label style="padding-bottom: 0.5rem">
                    Question:
                    <textarea class='textbox' data-structure='question_' id='notes_1' maxlength='65535' name='notes_1' placeholder='Enter your question here...' rows='3'></textarea>
                </label>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        First Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Second Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                </div>
                <div class='half-holder clearfix'>
                    <label class='half'>
                        Third Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                    <label class='half'>
                        Fourth Answer:
                        <input class='textbox' data-structure='answer_' id='first_name_1' maxlength='255' name='first_name_1' type='text'>
                    </label>
                </div>
            </section>
            <input id='count' name='count' type='hidden' value='3'>
        </div>
        <div class="submit">
            <input id='discard' name='discard' type='submit' value='Discard' style="background-color: #A5280D">
            <input id='submit' name='submit' type='submit' value='Create Assignment'>
        </div>
    </form>
</#assign>
<#include "teacher-main.ftl">
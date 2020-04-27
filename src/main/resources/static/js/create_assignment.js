// To add javascript functionality - if javascript is enabled
$(document).ready(function () {
    // insert an "add more" button after the last element
    $('#count').before('<input class="add_btn" id="add_btn" type="button" value="ADD QUESTION">');

    // add the "remove" link to each section
    $('.repeatable').prepend('<a class="remove" href="javascript:void(0);">[Remove Question]</a>');

    updateRemoveLinks = function () {
        // if "repeatable" element count is greater than 1...
        if ($('.repeatable').length > 1) {
            // ...show the "remove" link
            $('.repeatable').children('.remove').css({'display':'block'});
            // otherwise...
        } else {
            // don't display the "remove" link
            $('.repeatable').children('.remove').css({'display':'none'});
        }
    }

    updateRemoveLinks();

    // DESTROY!!! >.<
    // this method does all the checking necessary for deleting an element
    destroy = function () {
        // when the user clicks the "remove" link on a section...
        $('.remove').click(function(){
            // ...get that link's "repeatable" parent (container) and remove it
            $(this).parent('.repeatable').remove();
            // now, for each remaining repeatable element...
            $('.repeatable').each(function(){
                var r = this; // store "this", to avoid scope problems
                var num = $(r).index() + 1; // store the index+1 of the current "repeatable" in the collection (zero-based indexes)
                // RE-NUMBER ALL THE THINGS!!! >.<
                // change the heading to reflect the index+1 value
                $(r).children('h2').html('Question ' + num).text();
                // go through all text box elements within...
                $(r).find('input, textarea').each(function(){
                    // ...change their "structure" data attributes to reflect the index+1 value of the "repeatable" element
                    dattr = $(this).data('structure');
                    $(this).attr({
                        'id':dattr + num,
                        'name':dattr
                        // update the "for" attribute on the parent element (label)
                    }).parent('label').attr('for',dattr);
                });
                //adjust the counter
                $('#count').val($('.repeatable').length);
            });
            updateRemoveLinks();
        });
    }

    // now, call the "destroy" method, so that when the page loads, this method is declared and will affect all "repeatable" elements - this has something to do with adding/removing dynamic elements, I wrote this so long ago, that I don't recall exactly how it works...
    destroy();

    // when the user clicks the "add more" button...
    $('.add_btn').click(function(){
        var original = $('.repeatable').last().find(':checked');
        console.log(original);
        // clone the previous element (a "repeatable" element), and insert it before the "add more" button
        $(this).prev('.repeatable').clone().insertBefore(this).html();
        // get the number of repeatable elements on the page
        var num = $('.repeatable').length;
        // again, get the previous element (a "repeatable" element), and change the header to reflect it's new index
        $(this).prev('.repeatable').children('h2').html('Question ' + num);
        // now, go through all text boxes within the last "repeatable" element...
        $('.repeatable').last().find('input, textarea').each(function(){
            // ...change their "structure" data attributes to reflect the index+1 value of the "repeatable" element
            dattr = $(this).data('structure');
            $(this).attr({
                'id':dattr + num,
                'name':dattr
                // update the "for" attribute on the parent element (label)
            }).parent('label').attr('for',dattr);
            // clear the input field contents of the new "repeatable"
            // if the type of the input is "radio"...
            if ($(this).attr('type') == 'radio') {
                // remove the checked attribute
                $(this).removeAttr('checked');
                // for all other inputs...
            } else {
                // clear the value...
                $(this).val('');
            }
            if (original.length == 1) {
                original.prop('checked',true);
            }
            //adjust the counter
            $('#count').val($('.repeatable').length);
        });
        // run the "destroy" method... I forget why... just do it, and don't gimme no lip.
        destroy();
        updateRemoveLinks();
    });
	
	$("input[name='atype']").change(function(){
		document.getElementById('repeater').hidden = !document.getElementById('repeater').hidden;
	});

    const submit = document.getElementById('submit');
    submit.onclick = createAssignment;
    function createAssignment()
    {

        const quizRadio = document.getElementById('quiz');
        const checkoffRadio = document.getElementById('checkoff');
        const competitiveRadio = document.getElementById('competitive');
        const title = document.getElementById('assignmentTitle');
        const points = document.getElementById('points');
        const questions = document.getElementsByName('question');
        const firstAnswers = document.getElementsByName('answer_1');
        const secondAnswers = document.getElementsByName('answer_2');
        const thirdAnswers = document.getElementsByName('answer_3');
        const fourthAnswers = document.getElementsByName('answer_4');
        const correctAnswers = document.getElementsByName('correct');

        let first = [];
        for (let item of firstAnswers) {
            first.push($(item).val());
        }
        let second = [];
        for (let item of secondAnswers) {
            second.push($(item).val());
        }
        let third = [];
        for (let item of thirdAnswers) {
            third.push($(item).val());
        }
        let fourth = [];
        for (let item of fourthAnswers) {
            fourth.push($(item).val());
        }
        let questionsList = [];
        for (let item of questions) {
            questionsList.push($(item).val());
        }
		console.log(first);
		console.log(second);
		console.log(third);
		console.log(fourth);
		console.log(questionsList);
		
        let correct = [];
        for (let item of correctAnswers) {
            correct.push($(item).val());
        }

        console.log("nice");

        console.log("nice");
        console.log("nice");

        console.log("nice");

        console.log("nice");
        console.log("nice");
        console.log("nice");




        const postParameters = {
            quiz: quizRadio.checked.toString(),
            checkoff: checkoffRadio.checked.toString(),
            competitive: competitiveRadio.checked.toString(),
            title: title.value,
            points: points.value,
            questions: JSON.stringify(questionsList),
            firstAnswers: JSON.stringify(first),
            secondAnswers: JSON.stringify(second),
            thirdAnswers: JSON.stringify(third),
            fourthAnswers: JSON.stringify(fourth),
            correctAnswers: JSON.stringify(correct)
        };
        console.log(questionsList);
        console.log(first);

        $.post("/teacher/create-assignment-submit", postParameters, response => {
            const message = JSON.parse(response).results;
			console.log(message);
            console.log(message);
            console.log(message);
            console.log(message);


            if (message === "Assignment successfully created!") {
                const assignment = "/teacher/viewAssignment/" + JSON.parse(response).assignmentid;
                window.alert(message);
                window.location.href = assignment;
            } else {
                window.alert(message);
            }
        })
        return false;
    }

    const discard = document.getElementById('discard');
    discard.onclick = discardAssignment;
    function discardAssignment()
    {
        window.location.href = "/teacher/main";
    }
});
  
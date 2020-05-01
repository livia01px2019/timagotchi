<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link href="https://fonts.googleapis.com/css?family=Source+Code+Pro&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../../css/normalize.css">
    <link rel="stylesheet" href="../../css/html5bp.css">
    <link rel="stylesheet" href="../../css/w3.css">
    <link rel="stylesheet" href="../../css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&family=Roboto:wght@300&display=swap" rel="stylesheet">
</head>
<body>
<div class="sidenav">
    <a href="/student/main">ME</a>
        ${classes}
    <a href="/student/all-classes">ALL CLASSES</a>
    <a href="/student/new-class"><img src="../../img/plus-icon.png"></a>
</div>
<div class="header-student" id="header">
    <div class="header-left">
        <a href="main"><img class="logo" src="../../img/main-skin-right.png" alt="timagotchi logo right">  </a>
        <h1> TIMAGOTCHI </h1>
    </div>
    <a href="/logout"><p>Log out</p></a>
</div>
<div class="after-header-spacing"></div>
<div style="padding-left: 20%">
    <div id="grid"></div>
	<div class="student-quiz-main">
		<div id="congrats-banner" class="congrats-banner"> </div>
	    <h2>${assignmentName}</h2>
	    <link href="../../css/student_quiz.css" rel="stylesheet">
	    <script defer src="../../js/quiz1.js"></script>
	    ${hidden}
	    <div id="test" class="test">
	        <div class="quiz-container">
	            <div id="quiz"></div>
	        </div>
	        <div class="buttons">
		        <button id="previous">PREVIOUS</button>
		        <button id="next">NEXT</button>
		        <button id="submit">SUBMIT</button>
		    </div>
	    </div>

	    <div id="results"></div>
        <div id="scoreboard"></div>
	    <div id="finishButton"></div>
	</div>
</div>
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="../../js/jquery-2.1.1.js"></script>
<script src="../../js/page-logic.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>
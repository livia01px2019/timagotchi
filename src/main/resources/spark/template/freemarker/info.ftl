<#assign content>
<div class="info">
	<div class="info-block" style="background-color:#FCF6BD">
		<img src="img/us.png" alt="who are we?">
		<h1>WHAT IS TIMAGOTCHI?</h1>
		<br>
		<p>
			Timagotchi is a play on words: a combination of everyone’s favorite childhood game “Tamagotchi” and our favorite Software Engineering professor Tim Nelson. It was designed to be a study platform to encourage students to do their work and additional learning outside of the classroom by raising a pet through the work they do. Students can get XP from traditional homework/classwork that they can get checked off for, or quizzes they do directly on the website that are created by their teacher or automatically created by a DocDiff algorithm which finds questions most similar to the ones that they get wrong. We also encourage some friendly competition between students and classes by having a leaderboards that further encourage students to participate!
		</p>
	</div>
	<div class="info-block" style="background-color:#A9DEF9">
		<img src="img/timagotchi-evo.png" alt="timagotchi evolution">
		<h1>HOW DO I USE IT?</h1>
		<br>
		<div class="usage-blocks">
			<div class="usage-block">
				<h2>TEACHERS</h2>
				<p>
					<a href=“/register”>Register</a> a teacher account with your information, then <a href=“/login”>log in</a>. Click on the + button in the sidebar to create a new class, then share the code with your students so they can join. In a specific class page, you can create new assignments and view old assignments in the “Assignments” tab and view the student leaderboard for the class in the “Students” tab. When creating a new assignment, you can either create checkoff work for more traditional assignments or quizzes that students can complete directly on the website. You can also view how your different classes compare in your profile. 
				</p>
			</div>
			<div style="width:200px"></div>
			<div class="usage-block">
				<h2>STUDENTS</h2>
				<p>
					<a href=“/register”>Register</a> a student account with your information, then <a href=“/login”>log in</a>. Once you get a class code from your teacher, click on the + button in the sidebar and enter the code to join the class. You can view all of the assignments for your class and complete any of the quiz assignments directly on the website (you can attempt all quizzes any number of times, but only your first attempt will be recorded!). You can also view how you compare against your classmates and other classes in the Leaderboard pages. Watch your pet grow and change as you finish assignments and gain XP!
				</p>
			</div>
		</div>
	</div>
	<div class="info-block" style="background-color:#4C3B4D; padding-top:20px; padding-bottom:20px;">
		<p style="color:white; background-color:#4C3B4D">This website was made in March-May 2020 by David Lee, Ocean Pak, Huy Pham, and Livia Zhu as the term project of their CS32 Software Engineering class at Brown University.</p>
	</div>
</div>
<link rel="stylesheet" href="css/info.css">
</#assign>
<#include "main.ftl">
<#include "header.ftl">
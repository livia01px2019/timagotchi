<#assign content>
<div class="profile-main">
	<img class="timagotchi" src="../img/timagotchi.png">
	<div id="skin-container" class="skin-container">
		<img id="pet" class="skin" src=${fileNameUsername[0]}>
	</div>
    <button class="pet-button" id="lbutton">&nbsp;</button>
    <button class="pet-button" id="mbutton">&nbsp;</button>
    <button class="pet-button" id="rbutton">&nbsp;</button>
    <script src="../js/jquery-2.1.1.js"></script>
    <script src="../js/main.js"></script>
    <script src="../js/student-me.js"></script>
	<div class="progress-bar">
		<div>
			<p>LVL ${lvlXpProgress[0]}</p>
			<p>${lvlXpProgress[1]} XP</p>
		</div>
		<div class="w3-white" style="width:50%; margin: 20px; outline: solid;">
		  <div style="height:30px; width:${lvlXpProgress[2]}%; background-color: black"></div>
		</div>

	</div>
	<div style="width:300px; height:650px"></div>
	<div class="profile-info" style="width:500px">
		<h1 style="font-family: 'Press Start 2P', cursive; font-size:40px">PROFILE INFO</h1>
		<h3>NAME: ${fileNameUsername[1]}</h3>
		<h3>USERNAME: ${fileNameUsername[2]}</h3>
		<p id="demo"></p>
	</div>
</div>


</#assign>
<#include "student-main.ftl">
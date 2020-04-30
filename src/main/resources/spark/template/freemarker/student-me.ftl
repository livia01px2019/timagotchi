<#assign content>
<div class="profile">
	<img style="height:300px; padding-right: 50px" src=${fileNameUsername[0]}>
	<div class="profile-info">
		<h3 style="font-size:50px">${fileNameUsername[1]}</h3>
		<h3>${fileNameUsername[2]}</h3>
	</div>
</div>
<div class="progress-bar">
	<p>LVL ${lvlXpProgress[0]}</p>
	<div class="w3-white w3-round" style="width:70%; margin: 20px">
	  <div class="w3-container w3-round" style="height:30px; width:${lvlXpProgress[2]}%; background-color: #61C9A8"></div>
	</div>
	<p>${lvlXpProgress[1]} XP</p>
</div>
</#assign>
<#include "student-main.ftl">
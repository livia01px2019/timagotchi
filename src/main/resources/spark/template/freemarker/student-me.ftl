<#assign content>
<div class="profile">
	<img height="100px" src=${skinFile}>
	<div class="profile-info">
		<h3>${name}</h3> <br>
		<h3>${username}</h3>
	</div>
</div>
<div class="progress-bar">
	<p>LVL${lvlXp[0]}</p>
	<div class="w3-white w3-round" style="width:70%">
	  <div class="w3-container w3-round progress" style="width:${lvlXp[1]}%; height:40px">${lvlXp[1]}</div>
	</div>
	<p>${lvlXp[1]}XP</p>
</div>
</#assign>
<#include "student-main.ftl">
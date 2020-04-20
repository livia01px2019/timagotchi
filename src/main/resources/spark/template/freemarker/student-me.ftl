<#assign content>
<#import "sidebar.ftl" as sidebar>
<sidebar>
<div class="profile">
	<img src="img/skin2.png">
	<div class="profile-info">
		<h2>${name}</h2> <br>
		<h2>${username}</h2>
	</div>
</div>
<div class="progress-bar">
	<p>LVL${lvlXp[0]}</p>
	<progress value = "65" max = "100"/></progress>
	<p>${lvlXp[1]}XP</p>
</div>
</#assign>
<#include "main.ftl">
<#include "header.ftl">
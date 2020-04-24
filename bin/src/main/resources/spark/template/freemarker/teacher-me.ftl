<#assign content>
<div class="profile">
	<img style="height:300px; padding-right: 50px" src=${fileNameUsername[0]}>
	<div class="profile-info">
		<h3 style="font-size:50px">${fileNameUsername[1]}</h3>
		<h3>${fileNameUsername[2]}</h3>
	</div>
</div>
<#include "leaderboard.ftl">
</#assign>
<#include "student-main.ftl">
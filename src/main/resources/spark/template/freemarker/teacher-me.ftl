<#assign content>
<div class="profile" style="margin-right: 0px">
		<div>
			<h1 style="font-family: 'Press Start 2P', cursive; font-size:40px">PROFILE INFO</h1>
			<h3>NAME: ${nameUsername[0]}</h3>
			<h3>USERNAME: ${nameUsername[1]}</h3>
		</div>
		<div style="width:60%">
			<#include "leaderboard.ftl">
		</div>
</div>
</#assign>
<#include "teacher-main.ftl">
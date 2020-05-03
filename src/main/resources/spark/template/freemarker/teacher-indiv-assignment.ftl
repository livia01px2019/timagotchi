<#assign content>
<script>
  const record = '${record}';
</script>
<div class="teacher-assignment-main">
	<h2>${assignmentnameStudentname[0]}: ${assignmentnameStudentname[1]}</h2>
	<h3>Score: ${scoreTotalscore[0]}/${scoreTotalscore[1]}</h3>
	<div class="recordRow" style="background-color:#61C9A8; margin-top:20px;">
		<div class="questiontitle">Question</div>
		<div class="questiontitle">Correct Answer</div>
		<div class="correctnesstitle">Correctness</div>
	</div>
    <div id="recordBlock" class="assignmentBlock" style="margin-top:0">
    </div>
</div>
<script defer src="../../js/teacher-indiv-assignment.js"></script>
</#assign>
<#include "teacher-assignment-main.ftl">
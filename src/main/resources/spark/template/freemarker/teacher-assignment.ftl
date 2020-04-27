<#assign content>
<script>
  const students = '${students}';
  const totalScore = ${totalScore};
</script>
<div class="teacher-assignment-main">
	<h2>${assignmentName}</h2>
    <div id="assignmentBlock" class="assignmentBlock">
    </div>
</div>
<script defer src="../../js/teacher-assignment.js"></script>
</#assign>
<#include "teacher-assignment-main.ftl">
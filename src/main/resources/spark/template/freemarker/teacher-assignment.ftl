<#assign content>
<script>
  const students = '${students}';
  const totalScore = ${totalScore};
</script>
<div>
	<h1>${assignmentName}</h1>
	<link href="../../css/teacher-assignment.css" rel="stylesheet">
    <script defer src="../../js/teacher-assignment.js"></script>
    <div id="assignmentBlock">
    </div>
</div>
</#assign>
<#include "teacher-main.ftl">
<#assign content>
<script>
  const students = '${students}';
</script>
<div class="teacher-assignment-main">
	<h2>${assignmentName}</h2>
    <div id="assignmentBlock" class="assignmentBlock">
    </div>
    <b class="error" id="error-message">&nbsp;</b>
    <div class="submit-button-wrapper">
	    <button class="delete-button" id="delete-button" type="submit" type="button">DELETE ASSIGNMENT</button>
	    <button class="update-button" id="update-button" type="submit" type="button">UPDATE STUDENT COMPLETION</button>
	</div>
</div>
<script defer src="../../js/teacher-assignment-checkoff.js"></script>
</#assign>
<#include "teacher-assignment-main.ftl">
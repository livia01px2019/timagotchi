<#assign content>
<div class="login-blocks">
	<div class="login-block">
		<h2> STUDENT LOGIN </h2>
		<form>
			<br>
			<label for="student-username">Username:</label><br>
			<input type="text" id="student-username" name="student-username"><br>
			<br>
			<label for="student-password">Password:</label><br>
			<input type="password" id="student-password" name="student-password"><br>
			<br>
<#--			<div class="submit-button"> <input type="submit" id="student-submit" value="SUBMIT"></div>-->
		</form>
        <b class="error" id="student-error">&nbsp;</b>
        <div class="submit-button-wrapper">
        	<button class="submit-button" id="student-button" type="submit" type="button">SUBMIT</button>
        </div>

	</div>
	<div class="login-block">
		<h2> TEACHER LOGIN </h2>
		<form id="teacher-form" onsubmit="return false">
			<br>
			<label for="teacher-username">Username:</label><br>
			<input type="text" id="teacher-username" name="teacher-username"><br>
			<br>
			<label for="teacher-password">Password:</label><br>
			<input type="password" id="teacher-password" name="teacher-password"><br>
			<br>
<#--			<div class="submit-button"> <input type="submit" id="teacher-submit" value="SUBMIT"></div>-->
		</form>
        <b class="error" id="teacher-error">&nbsp;</b>
        <div class="submit-button-wrapper">
        	<button class="submit-button" id="teacher-button" type="submit" type="button">SUBMIT</button>
        </div>
	</div>
    <script src="js/jquery-2.1.1.js"></script>
    <script src="js/login.js"></script>
</div>
<div class="register-link">
	<p>Don't have an account?</p> <a href = "/register">Register</a>
</div>

</#assign>
<#include "main.ftl">
<#include "header.ftl">
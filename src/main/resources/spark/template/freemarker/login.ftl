<#assign content>
<div class="login-blocks"> 
	<div class="login-block">
		<h2> STUDENT LOGIN </h2>
		<form>
			<br>
			<label for="username">Username:</label><br>
			<input type="text" id="username" name="username"><br>
			<br>
			<label for="Password">Password:</label><br>
			<input type="password" id="password" name="password"><br>
			<br>
			<div class="submit-button"> <input type="submit" value="SUBMIT"></div>
		</form>
	</div>
	<div class="login-block">
		<h2> TEACHER LOGIN </h2>
		<form>
			<br>
			<label for="username">Username:</label><br>
			<input type="text" id="username" name="username"><br>
			<br>
			<label for="Password">Password:</label><br>
			<input type="password" id="password" name="password"><br>
			<br>
			<div class="submit-button"> <input type="submit" value="SUBMIT"></div>
		</form>
	</div>
</div>
<div class="register-link">
	<p>Don't have an account?</p> <a href = "/register">Register</a>
</div>

</#assign>
<#include "main.ftl">
<#include "header.ftl">
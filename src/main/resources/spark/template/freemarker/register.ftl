<#assign content>
    <p id="message"></p>
<div class="register-block">
	<h2> REGISTER </h2> <br>
	<form class="register-form">
		<div class="form-stuff">
			<div class="form-labels">
				<label for="role">I am a...</label><br>

				<label for="name">Full name:</label><br>

			    <label for="username">Username:</label><br>

			    <label for="password">Password:</label><br>

			    <label for="confirm-password">Confirm Password:</label><br>

                <label for="matching">&nbsp;</label><br>
			</div>
			<div class="form-inputs">
				<div class="radio-role">
					<div width="50%">
						<input type="radio" id="student" name="role" value="student">
					    <label for="student">Student</label>
				    </div>
				    <div width="50%">
					    <input type="radio" id="teacher" name="role" value="teacher">
				  		<label for="teacher">Teacher</label><br>
				  	</div>
				  	<div></div>
				</div>
		  		<input type="text" id="name" name="name"><br>
			    <input type="text" id="username" name="username"> <br>
			    <input type="password" id="password" name="password"><br>
			    <input type="password" id="confirm-password" name="confirm-password"><br>
                <b id="matching">&nbsp;</b>
			</div>
		</div>
	</form>
    <div class="submit-button-wrapper">
        <button class="submit-button" id="submit" type="submit" type="button">SUBMIT</button>
    </div>
</div>
<div class="register-link">
	<p>Have an account?</p> <a href = "/login">Login</a>
</div>

</#assign>
<#include "main.ftl">
<#include "header.ftl">
<#assign content>
<div class="new-class">
	<h1 style="font-family: 'Press Start 2P', cursive;">ADD NEW CLASS </h1>
	<div class="new-class-block">
		<form class="register-form">
			<div class="form-stuff">
				<div class="form-labels">
					<label for="name">Class Code: </label><br>
				</div>
				<div class="form-inputs">
			  		<input type="text" id="code" name="code"><br>
				</div>
			</div>			
		</form>
		<b class="error" id="error-message">&nbsp;</b>
		<div class="submit-button-wrapper">
	        <button class="submit-button" id="submit-button" type="submit" type="button">ADD NEW CLASS</button>
	    </div>
	</div>
</div>
<script src="../js/student-new-class.js"></script>

</#assign>
<#include "student-main.ftl">
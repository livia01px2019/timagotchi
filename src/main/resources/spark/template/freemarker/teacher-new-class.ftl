<#assign content>
<div class="new-class">
	<h1 style="font-family: 'Press Start 2P', cursive;">ADD NEW CLASS </h1>
	<div class="new-class-block">
		<form class="register-form">
			<div class="form-stuff">
				<div class="form-labels">
					<label for="name">Class Name</label><br>
				</div>
				<div class="form-inputs">
			  		<input type="text" id="name" name="name"><br>
				</div>
			</div>			
		</form>
		<b class="error" id="error-message">&nbsp;</b>
		<div class="submit-button-wrapper">
	        <button class="submit-button" id="submit-button" type="submit" type="button">ADD NEW CLASS</button>
	    </div>
	</div>
</div>
</#assign>
<#include "teacher-main.ftl">
$(function() {
	$("#addComputerForm").validate({
		rules : {
			computerName : "required"
		},
		messages : {
			computerName : "Please enter a name"
		},
		submitHandler : function(form) {
			form.submit();
		}
	});
});

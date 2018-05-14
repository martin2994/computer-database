$(function() {
	$("#computerForm").validate({
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
	var introducedDate = $("#introduced").val();
	if (introduced != null) {
		$("#discontinued").attr("min", introducedDate);
	}
	$("#introduced").change(function() {
		$("#discontinued").attr("min", $(this).val());
	});
});

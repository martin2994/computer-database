$(function() {
	$('#computerForm').submit(function (e) {
		validateForm(e,'#name',"#nameProblem");
	});
	$('#computerForm').focusout(function (e) {
		validateForm(e,'#name',"#nameProblem");
	});
	var introducedDate = $("#introduced").val();
	if (introduced != null) {
		$("#discontinued").attr("min", introducedDate);
	}
	$("#introduced").change(function() {
		$("#discontinued").attr("min", $(this).val());
	});
});

function validateForm(form, labelVerify, idLabel){
	var name = $.trim($(labelVerify).val());
    if(name === '') {
        $(idLabel).show();
        form.preventDefault(form);
    }
}
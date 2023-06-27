'use strict'
$(function() {
    $.getJSON("http://localhost:8080/ec-202110c/searchItem",
	    function(data) {
	        $('#name').autocomplete({
	            source : data,
	            autoFocus: true,
	            minLength : 1
	        });
	    });
});
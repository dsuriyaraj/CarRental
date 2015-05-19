function Load() {
	
	$.ajax({		
		type:'POST',
		url:'php_ajax.php',
		data: { List_Vehicles: "List_Vehicles"},
		success: function(msg)
		{	
			$('#table').html("");
			$('#table').html(msg);  //Add new content
		}	
	});
}

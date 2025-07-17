document.addEventListener("DOMContentLoaded", () => {
	const userDivs = document.querySelectorAll(".user-info");
 	const hiddenInput = document.getElementById('selectedUserId');
 
	
	userDivs.forEach(div => {
	    div.addEventListener('click', () => {
	      const userId = div.getAttribute('data-user-id');

	      // Speichere die ausgewählte User-ID im versteckten Input
	      hiddenInput.value = userId;

	      // Optional: visuelles Feedback (z.B. Markierung)
	      userDivs.forEach(d => d.classList.remove('selected'));
	      div.classList.add('selected');
	    });
	  });

	
 })
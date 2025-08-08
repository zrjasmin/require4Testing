let container;
let checkboxArray;
let selectedId = new Array(); 
let testId;
let existingInputs = 0;


document.addEventListener('DOMContentLoaded', () => {
	container = document.getElementById('testContainer');
	checkboxArray = document.querySelectorAll(".checkbox");
	
	registerExistingDivs();
	updateInputs();
	
	
	
					
})


function updateInputs() {
	const checkedInputs = document.getElementById("checkedInputs");
	if(selectedId == null || selectedId.length == 0 ) {
			console.log("null")
			checkedInputs.value = '';
		} else {
			checkedInputs.value = JSON.stringify(selectedId);
		}
		
	}


function registerExistingDivs() {
	existingInputs = document.querySelectorAll('#testContainer > div');
	const selectBoxes = document.querySelectorAll(`.testOption input[type=checkbox]`);
	
	let testId;
	
		existingInputs.forEach(div => {
		  testId = div.getAttribute('data-test-id');
		  selectedId.push(testId);
		 
		  //markiert gespeicherte Test als checked
		  selectBoxes.forEach(box => {
			if(box.value == testId) {
				box.checked = true;
				};
			})
		});
		
				 	
			
		
}		
				
function toggleTest(checkbox) {
		testId = checkbox.value;
		
		const testTitel = checkbox.parentNode.textContent.trim();
		container = document.getElementById('testContainer');
		
		
		if(checkbox.checked) {
			const div = document.createElement("div");
			div.setAttribute('data-test-id', testId);
			div.classList.add("test")
			div.innerHTML = `
				<p>${testTitel}</p>
				<button type="button" class="deleteTestBtn" onclick="removeTest(this)"><i class="fa-solid fa-trash"></i></button>
			`;
			
			if(selectedId == null) {
				selectedId = new Array();
			}
			selectedId.push(testId);
				
			container.appendChild(div);
			
			
		} else {
			//löscht durch Unselect
			const selectedBoxes = container.querySelectorAll(`div[data-test-id="${testId}"]`)
			selectedBoxes.forEach(box => container.removeChild(box));
			
			removeFromList(checkbox)

		}
		updateInputs()
		
	}
	
	//löscht durch Button Klick
	function removeTest(button) {
	
		
		const parentDiv = button.closest('div[data-test-id]');
		
		const inputId = parentDiv.getAttribute("data-test-id");
		
		parentDiv.remove()
		
		
		const selectedBoxes = document.querySelectorAll(`.testOption input[type=checkbox]`);
		
		selectedBoxes.forEach(box =>  {
			if (box.value === inputId){
				box.checked =false;
				removeFromList(box)
		}})
		
		updateInputs();
		
		
	}
	
	function removeFromList(check) {
		const newValues = selectedId.map((i) => {
			console.log("aktueller ID: "+i)
			if(i === check.value) {
				console.log("zu löschenden Id: "+ i)
				
				return undefined;
			} else {
				return i;
			}
		}).filter(i => i !==undefined); 
		
		
		selectedId = newValues;
		updateInputs()		
	}
	
	
	
	
	
	
	
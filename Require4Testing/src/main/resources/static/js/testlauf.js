let container;
let checkboxArray;
let selectedId = new Array(); 
let testId;
document.addEventListener('DOMContentLoaded', () => {
	container = document.getElementById('testContainer');
	checkboxArray = document.querySelectorAll(".checkbox");
	console.log(checkboxArray);
	updateInputs();
	
	
					
})


function updateInputs() {
		const checkedInputs = document.getElementById("checkedInputs");
		checkedInputs.value = JSON.stringify(selectedId);
		
	}


				
				
function toggleTest(checkbox) {
		testId = checkbox.value;
		console.log("testid: "+ testId);
		const testTitel = checkbox.parentNode.textContent.trim();
		container = document.getElementById('testContainer');
		
		
		if(checkbox.checked) {
			const div = document.createElement("div");
			div.setAttribute('data-test-id', testId);
			div.innerHTML = `
				<p>${testTitel}</p>
				<button type="button" onclick="removeTest('${testId}', this)">Entfernen</button>
			`;
			selectedId.push(testId);
				
			container.appendChild(div);
			console.log(selectedId)
			
		} else {
			//löscht durch Unselect
			const selectedBoxes = container.querySelectorAll(`div[data-test-id="${testId}"]`)
			selectedBoxes.forEach(box => container.removeChild(box));
			
			removeFromList(checkbox)

		}
		updateInputs()
		
	}
	
	//löscht durch Button Klick
	function removeTest(testId, button) {
	
		
		const divToRemove = button.parentNode;
		divToRemove.remove()
		
		const selectedBoxes = document.querySelectorAll(`.testOption input[type=checkbox]`);
		
		selectedBoxes.forEach(box =>  {
			if (box.value === testId){
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
	
	
	
	
	
	
	
let kriteriumIndex = 0;

function hinzufuegenKriterium() {
    const container = document.getElementById('kriteriencontainer');
	console.log(container);

    // Neues Div für das Kriterium
    const div = document.createElement('div');
	console.log(div);

    // Name-Attribut für die Eingabe
   	const beschreibung = `kriterien[${kriteriumIndex}].beschreibung`;

    div.innerHTML = `
        <input type="hidden" name="kriterien[${kriteriumIndex}].id" />
        <input type="text" name="${beschreibung}" placeholder="Kriterium" />
        <button type="button" >Entfernen</button>
        <br/>
    `;
    container.appendChild(div);
    kriteriumIndex++;
	}
	
	
	
	
	
	
	
	let stepIndex;
	let previousStepIndex;
	let container;
	let existingInputs = 0;
	
	function updateReihenfolge() {
		let schritteValue = Array.from(container.children).map(el => el.querySelector('.schritt').value);
		const reihenfolgeInput = document.getElementById("reihenfolge");		
		reihenfolgeInput.value = JSON.stringify(schritteValue);
		console.log(reihenfolgeInput.value);
		}

		function deleteSchritt(div) {
			div.remove();
			updateReihenfolge();
		}
		


		
		
	document.addEventListener('DOMContentLoaded', () => {
		container = document.getElementById('stepContainer');
		existingInputs = Array.from(container.children).map(el => el.querySelector('.schritt')).length;
		
		registerDragAndDrop();
		updateReihenfolge();
		//let reihenfolgeInput;// Hidden input
		
		if(existingInputs != 0) {
			//es gibt gespeicherte Schritte
			stepIndex = container.children.length+1;
		} else {
			//noch keine gespeicherten Schritte
			stepIndex = container.children.length;
		}
		
		
		
		
		function addNewStep() {
			    // Neues Div für das Kriterium
			    const div = document.createElement('div');
				
				div.classList.add("schritte");
				div.draggable ="true";
			    div.innerHTML = `
						<input type="hidden" name="testschritte[${stepIndex}].id"/>
				        <input type="text" name="testschritte[${stepIndex}].beschreibung" class="schrittvalue" oninput="updateReihenfolge()"/>
						<input type="hidden" name="testschritte[${stepIndex}].stepNumber"  class="schritt" value="${stepIndex}"/>
			    		<button type="button" onclick="deleteSchritt(this.parentElement)">Entfernen</button>
						`;
				
				
				registerDragAndDrop();
				console.log("stepIndex " + stepIndex);
			    container.appendChild(div);
			    stepIndex++;
				
				updateReihenfolge();
				
			
				}
				
			document.getElementById("addStepButton").addEventListener("click", addNewStep);
			
			// Variable für das gezogene Element
			let draggedItem = null;

			// Funktion, um Drag & Drop Events zu registrieren
			function registerDragAndDrop() {
				const schritte = document.querySelectorAll(".schritte");
			
				schritte.forEach(schritt => {
					  schritt.addEventListener('dragstart', dragStart);
					  schritt.addEventListener('dragover', dragOver);
					  schritt.addEventListener('drop', drop);
				})
				
				 
			}

			// Drag & Drop Handler Funktionen
			function dragStart(e) {
			  draggedItem = e.currentTarget;
			  console.log(draggedItem);
			}

			function dragOver(e) {
			  e.preventDefault();
			}

			function drop(e) {
			  e.preventDefault();
			  const target = e.currentTarget;

			  if (target !== draggedItem) {
			    const children = Array.from(container.children);
			    const indexDragged = children.indexOf(draggedItem);
			    const indexTarget = children.indexOf(target);

			    if (indexDragged < indexTarget) {
			      container.insertBefore(draggedItem, target.nextElementSibling);
			    } else {
			      container.insertBefore(draggedItem, target);
			    }

				
			  }
			  
			  console.log('Target:', target);
			  console.log('Next Sibling:', target.nextSibling);
			  console.log('Dragged Item:', draggedItem);
			  updateReihenfolge();
			  
			}
			
				
				
	
	})
	
	
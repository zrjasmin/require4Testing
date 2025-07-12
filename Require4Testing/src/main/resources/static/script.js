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
	
	function updateReihenfolge() {
		const schritteValue = Array.from(container.children).map(el => el.querySelector('.schritt').value);
		const reihenfolgeInput = document.getElementById("reihenfolge");
		console.log(schritteValue);
		reihenfolgeInput.value = JSON.stringify(schritteValue);
		
		for(let o in schritteValue) {
			console.log("schritt: " + o + " wert: " +schritteValue[o]);
		}
		console.log(schritteValue);

		console.log(reihenfolgeInput.value);
		
		}
		
		
	document.addEventListener('DOMContentLoaded', () => {
		container = document.getElementById('stepContainer');
		//let reihenfolgeInput;// Hidden input
		
		
		
		
		function addNewStep() {
				stepIndex = container.children.length;
				console.log(stepIndex);
			    // Neues Div für das Kriterium
			    const div = document.createElement('div');
				
				div.classList.add("schritte");
				div.draggable ="true";
			    div.innerHTML = `
						<input type="hidden" name="testschritte[${stepIndex}].id"/>
				        <input type="text" name="testschritte[${stepIndex}].beschreibung" class="schrittvalue" oninput="updateReihenfolge()"/>
						<input type="hidden" name="testschritte[${stepIndex}].stepNumber"  class="schritt" value="${stepIndex}"/>
			    `;
				
				
				registerDragAndDrop(div);
				
			    container.appendChild(div);
			    stepIndex++;
				updateReihenfolge();
				}
				
				document.getElementById("addStepButton").addEventListener("click", addNewStep);
				// Variable für das gezogene Element
				let draggedItem = null;

				// Funktion, um Drag & Drop Events zu registrieren
				function registerDragAndDrop(element) {
				  element.addEventListener('dragstart', dragStart);
				  element.addEventListener('dragover', dragOver);
				  element.addEventListener('drop', drop);
				}

				// Drag & Drop Handler Funktionen
				function dragStart(e) {
				  draggedItem = e.currentTarget;
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
				      container.insertBefore(draggedItem, target.nextSibling);
				    } else {
				      container.insertBefore(draggedItem, target);
				    }

					
				  }
				  updateReihenfolge();
				  
				}
				
	
	})
	
	
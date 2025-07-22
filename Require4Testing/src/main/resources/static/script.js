let kriteriumIndex = 0;
let container;
let existingInputs = 0;

document.addEventListener('DOMContentLoaded', () => {
	container = document.getElementById('kriteriencontainer');
	existingInputs = Array.from(container.children).map(el => el.querySelector('.kriterium')).length;
	
	if(existingInputs != 0) {
			//es gibt gespeicherte Schritte
			kriteriumIndex = container.children.length;
		}
		
	console.log(existingInputs);
})

function hinzufuegenKriterium() {
    const container = document.getElementById('kriteriencontainer');
	

    // Neues Div für das Kriterium
    const div = document.createElement('div');
	console.log(div);

    // Name-Attribut für die Eingabe
   	const beschreibung = `kriterien[${kriteriumIndex}].beschreibung`;
	div.classList.add("kriterium");
    div.innerHTML = `
        <input type="hidden" name="kriterien[${kriteriumIndex}].id" />
        <input type="text" name="${beschreibung}" placeholder="Kriterium" />
        <button type="button" onclick="loescheKriterium(this.parentElement)" >Entfernen</button>
        <br/>
    `;
    container.appendChild(div);
    kriteriumIndex++;
	}
	

	function loescheKriterium(div) {
		div.remove();
	}

	
	
	
	
	
	
	
	
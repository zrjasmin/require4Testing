let kriteriumIndex = 0;

function hinzufuegenKriterium() {
    const container = document.getElementById('kriteriencontainer');
	console.log(container);

    // Neues Div für das Kriterium
    const div = document.createElement('div');
	console.log(div);

    // Name-Attribut für die Eingabe
   	const beschreibung = `akzeptanzkriterien[${kriteriumIndex}].beschreibung`;

    div.innerHTML = `
        <input type="hidden" name="akzeptanzkriterien[${kriteriumIndex}].id" />
        <input type="text" name="${beschreibung}" placeholder="Kriterium" />
        <button type="button" >Entfernen</button>
        <br/>
    `;
    container.appendChild(div);
    kriteriumIndex++;
	}
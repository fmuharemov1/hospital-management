// Funkcija za kreiranje pacijenta
function kreirajPacijenta() {
    // Prikupljanje podataka iz forme
    const ime = document.getElementById('ime').value;
    const prezime = document.getElementById('prezime').value;
    const datumRodjenja = document.getElementById('datumRodjenja').value;

    // Validacija unetih podataka
    if (ime === '' || prezime === '') {
        alert('Molimo vas da popunite sva obavezna polja.');
        return; // Sada je 'return' unutar funkcije
    }

    // Slanje podataka na server
    fetch('http://localhost:8080/pacijenti', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            ime: ime,
            prezime: prezime,
            datumRodjenja: datumRodjenja,
            // ... ostali podaci
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Greška pri kreiranju pacijenta');
            }
            return response.json();
        })
        .then(data => {
            console.log('Podaci su uspešno poslati:', data);
            // Prikazivanje podataka sa servera
            const rezultatDiv = document.getElementById('rezultat');
            rezultatDiv.innerHTML = `
                <p>Ime: ${data.ime}</p>
                <p>Prezime: ${data.prezime}</p>
                <p>Datum rođenja: ${data.datumRodjenja}</p>
                // ... ostali podaci
            `;
        })
        .catch(error => {
            console.error('Greška pri slanju podataka:', error);
            // Prikazivanje greške
        });
}
function addRow(table, data) {
    const url = `http://localhost:8080/api/${table}`;

    return fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur : ${response.status} - ${response.statusText}`);
            }
            return response.json();
        })
        .catch(error => {
            console.error("Erreur lors de l'ajout des données :", error);
            throw error;
        });
}

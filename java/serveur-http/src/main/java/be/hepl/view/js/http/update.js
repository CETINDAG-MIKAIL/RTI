function updateRow(table, data) {
    const url = `http://localhost:8080/api/${table}?id=${id}`;

    return fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur : ${response.status} - ${response.statusText}`);
            }
            return response.text();
        })
        .catch(error => {
            console.error("Erreur lors de la mise à jour des données :", error);
            throw error;
        });
}
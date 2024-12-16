function deleteRow(table, id) {
    const url = `http://localhost:8080/api/${table}?id=${id}`;

    return fetch(url, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur : ${response.status} - ${response.statusText}`);
            }
            return response.text();
        })
        .catch(error => {
            console.error("Erreur lors de la suppression :", error);
            throw error;
        });
}

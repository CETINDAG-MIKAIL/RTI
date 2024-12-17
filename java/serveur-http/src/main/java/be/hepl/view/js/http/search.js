function searchRows(table, criteria) {
    let url = `http://localhost:8080/api/${table}?`;

    const queryParams = new URLSearchParams(criteria).toString();
    url += queryParams;

    console.log("AVANT-------Recherche des données------- queryParams : ", queryParams);
    console.log("AVANT-------Recherche des données------- url : -", url, "-");

    if (queryParams === "" || queryParams === null) {
        url = url.slice(0, -1);
    }

    console.log("-------Recherche des données------- queryParams : ", queryParams);
    console.log("-------Recherche des données------- url : -", url, "-");

    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur : ${response.status} - ${response.statusText}`);
            }
            console.log("-------Récupération des données réussie-------");

            return response.json();
        })
        .catch(error => {
            console.error("Erreur lors de la récupération des données :", error);
            throw error;
        });
}

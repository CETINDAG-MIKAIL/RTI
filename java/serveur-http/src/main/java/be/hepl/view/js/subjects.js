const idField = document.getElementById("id");
const idError = document.getElementById("id-error");
const nameField = document.getElementById("name");
const nameError = document.getElementById("name-error");

const infoMessage = document.getElementById("info-message");

fillSubjectsTableSearch({});

function addRowListeners(row) {
    row.addEventListener("click", () => {
        const allRows = document.querySelectorAll("tbody tr");
        allRows.forEach(r => r.classList.remove("selected-row"));
        row.classList.add("selected-row");

        const cells = row.querySelectorAll("td");

        idField.value = cells[0].textContent.trim();
        nameField.value = cells[1].textContent.trim();
    });
}

const addButton = document.getElementById("add-button");
const editButton = document.getElementById("edit-button");
const deleteButton = document.getElementById("delete-button");
const searchButton = document.getElementById("search-button");
const emptyButton = document.getElementById("empty-button");

addButton.addEventListener("click", () => {
    deleteFieldsErrors();

    if (nameField.value === "") {
        nameField.classList.add("input-error");
        nameError.textContent = "Ce champ est obligatoire pour l'ajout.";
        return;
    }

    const newRowData = {
        name: nameField.value
    };

    addRow("subjects", newRowData)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillSubjectsTableSearch({});
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message;
        });
});

editButton.addEventListener("click", () => {
    deleteFieldsErrors();
    let error = false;

    if (idField.value === "") {
        idField.classList.add("input-error");
        idError.textContent = "Ce champ est obligatoire pour la modification.";
        error = true;
    }

    if (nameField.value === "") {
        nameField.classList.add("input-error");
        nameError.textContent = "Ce champ est obligatoire pour la modification.";
        return;
    }

    if (error) {
        return;
    }

    const criteria = {
        id: idField.value.trim(),
        name: nameField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    updateRow("subjects", filteredCriteria)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillSubjectsTableSearch({});
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message;
        });
});

deleteButton.addEventListener("click", () => {
    deleteFieldsErrors();

    if (idField.value === "") {
        idField.classList.add("input-error");
        idError.textContent = "Ce champ est obligatoire pour la suppression.";
        return;
    }

    deleteRow("subjects", idField.value)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillSubjectsTableSearch({});
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message;
        });
});

searchButton.addEventListener("click", () => {
    deleteFieldsErrors();

    const criteria = {
        id: idField.value.trim(),
        name: nameField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    console.log("filteredCriteria : ", filteredCriteria);

    fillSubjectsTableSearch(filteredCriteria);
});

emptyButton.addEventListener("click", () => {
    deleteFieldsErrors();
    idField.value = "";
    nameField.value = "";

    infoMessage.textContent = "";
});

function deleteFieldsErrors() {
    idField.classList.remove("input-error");
    idError.textContent = "";
    nameField.classList.remove("input-error");
    nameError.textContent = "";

    infoMessage.textContent = "";
}

function fillSubjectsTableSearch(criteria) {
    searchRows("subjects", criteria)
        .then(data => {
            console.log("Contenu complet de data :", data);
            console.log("Type de data :", typeof data);
            console.log("Est-ce un tableau ?", Array.isArray(data));

            const tableSubjects = document.getElementById("table-subjects");

            tableSubjects.innerHTML = "";

            if (Array.isArray(data)) {
                const tableSubjects = document.getElementById("table-subjects");
                tableSubjects.innerHTML = "";

                data.forEach(subject => {
                    const row = document.createElement("tr");

                    const idCell = document.createElement("td");
                    idCell.textContent = subject.id;

                    const nameCell = document.createElement("td");
                    nameCell.textContent = subject.name;

                    row.appendChild(idCell);
                    row.appendChild(nameCell);

                    tableSubjects.appendChild(row);

                    addRowListeners(row);
                });
            } else {
                console.error("Les données ne sont pas un tableau valide.");
            }
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message || "Erreur lors du chargement des données.";
            console.error("Erreur :", error);
        });
}
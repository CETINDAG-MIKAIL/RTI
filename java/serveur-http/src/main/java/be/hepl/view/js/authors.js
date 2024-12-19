const idField = document.getElementById("id");
const idError = document.getElementById("id-error");
const firstNameField = document.getElementById("firstName");
const firstNameError = document.getElementById("firstName-error");
const lastNameField = document.getElementById("lastName");
const lastNameError = document.getElementById("lastName-error");
const birthDateField = document.getElementById("date-of-birth");
const birthDateError = document.getElementById("date-of-birth-error");

const infoMessage = document.getElementById("info-message");

fillAuthorsTableSearch({});

function addRowListeners(row) {
    row.addEventListener("click", () => {
        const allRows = document.querySelectorAll("tbody tr");
        allRows.forEach(r => r.classList.remove("selected-row"));
        row.classList.add("selected-row");

        const cells = row.querySelectorAll("td");

        idField.value = cells[0].textContent.trim();
        lastNameField.value = cells[1].textContent.trim();
        firstNameField.value = cells[2].textContent.trim();

        let [day, month, year] = cells[3].textContent.split('-');
        birthDateField.value = `${day}-${month}-${year}`.trim();
    });
}

const addButton = document.getElementById("add-button");
const editButton = document.getElementById("edit-button");
const deleteButton = document.getElementById("delete-button");
const searchButton = document.getElementById("search-button");
const emptyButton = document.getElementById("empty-button");

addButton.addEventListener("click", () => {
    deleteFieldsErrors();

    let error = false;

    if (firstNameField.value === "") {
        firstNameField.classList.add("input-error");
        firstNameError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (lastNameField.value === "") {
        lastNameField.classList.add("input-error");
        lastNameError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (birthDateField.value === "") {
        birthDateField.classList.add("input-error");
        birthDateError.textContent = "Ce champ est obligatoire pour l'ajout.";
        return;
    }

    if(error) {
        return;
    }

    const newRowData = {
        firstName: firstNameField.value,
        lastName: lastNameField.value,
        birthDate: birthDateField.value
    };

    addRow("authors", newRowData)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillAuthorsTableSearch({});
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
    let hasInAField = false;

    if (idField.value === "") {
        idField.classList.add("input-error");
        idError.textContent = "Ce champ est obligatoire pour la modification.";
        error = true;
    }

    if (firstNameField.value === "") {
        firstNameField.classList.add("input-error");
        firstNameError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (lastNameField.value === "") {
        lastNameField.classList.add("input-error");
        lastNameError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (birthDateField.value === "") {
        birthDateField.classList.add("input-error");
        birthDateError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (hasInAField) {
        firstNameField.classList.remove("input-error");
        firstNameError.textContent = "";
        lastNameField.classList.remove("input-error");
        lastNameError.textContent = "";
        birthDateField.classList.remove("input-error");
        birthDateError.textContent = "";
    }
    if (error) {
        return;
    }

    const criteria = {
        firstName: firstNameField.value.trim(),
        lastName: lastNameField.value.trim(),
        birthDate: birthDateField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    updateRow("authors", idField.value.trim(), filteredCriteria)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillAuthorsTableSearch({});
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

    deleteRow("authors", idField.value)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillAuthorsTableSearch({});
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
        firstName: firstNameField.value.trim(),
        lastName: lastNameField.value.trim(),
        birthDate: birthDateField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    console.log("filteredCriteria : ", filteredCriteria);

    fillAuthorsTableSearch(filteredCriteria);
});


emptyButton.addEventListener("click", () => {
    deleteFieldsErrors();
    idField.value = "";
    firstNameField.value = "";
    lastNameField.value = "";
    birthDateField.value = "";

    infoMessage.textContent = "";
});

function deleteFieldsErrors() {
    idField.classList.remove("input-error");
    idError.textContent = "";
    firstNameField.classList.remove("input-error");
    firstNameError.textContent = "";
    lastNameField.classList.remove("input-error");
    lastNameError.textContent = "";
    birthDateField.classList.remove("input-error");
    birthDateError.textContent = "";

    infoMessage.textContent = "";
}

const tableAuthors = document.getElementById("table-authors");

function fillAuthorsTableSearch(criteria) {
    searchRows("authors", criteria)
        .then(data => {
            console.log("Contenu complet de data :", data);
            console.log("Type de data :", typeof data);
            console.log("Est-ce un tableau ?", Array.isArray(data));

            tableAuthors.innerHTML = "";

            data.forEach(author => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = author.id;

                const lastNameCell = document.createElement("td");
                lastNameCell.textContent = author.lastName;

                const firstNameCell = document.createElement("td");
                firstNameCell.textContent = author.firstName;

                const birthDateCell = document.createElement("td");
                birthDateCell.textContent = author.birthDate;

                row.appendChild(idCell);
                row.appendChild(lastNameCell);
                row.appendChild(firstNameCell);
                row.appendChild(birthDateCell);

                tableAuthors.appendChild(row);

                addRowListeners(row);
            });
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message || "Erreur lors du chargement des données.";
            console.error("Erreur :", error);
        });
}
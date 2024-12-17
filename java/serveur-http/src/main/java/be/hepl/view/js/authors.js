const idField = document.getElementById("id");
const idError = document.getElementById("id-error");
const firstNameField = document.getElementById("firstName");
const firstNameError = document.getElementById("firstName-error");
const lastNameField = document.getElementById("lastName");
const lastNameError = document.getElementById("lastName-error");
const dateOfBirthField = document.getElementById("date-of-birth");
const dateOfBirthError = document.getElementById("date-of-birth-error");

const infoMessage = document.getElementById("info-message");

function addRowListeners(row) {
    row.addEventListener("click", () => {
        const cells = row.querySelectorAll("td");

        idField.value = cells[0].textContent.trim();
        firstNameField.value = cells[1].textContent.trim();
        lastNameField.value = cells[2].textContent.trim();

        let [day, month, year] = cells[3].textContent.split('-');
        let formattedDate = `${year}-${month}-${day}`;
        dateOfBirthField.value = formattedDate.trim();
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

    if (dateOfBirthField.value === "") {
        dateOfBirthField.classList.add("input-error");
        dateOfBirthError.textContent = "Ce champ est obligatoire pour l'ajout.";
        return;
    }

    if(error) {
        return;
    }

    const newRowData = {
        firstName: firstNameField.value,
        lastName: lastNameField.value,
        dateOfBirth: dateOfBirthField.value
    };

    addRow("authors", newRowData)
        .then(response => {
            infoMessage.style.color = "green";
            infoMessage.textContent = response;
        })
        .catch(error => {
            infoMessage.style.color = "red";
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

    if (dateOfBirthField.value === "") {
        dateOfBirthField.classList.add("input-error");
        dateOfBirthError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (hasInAField) {
        firstNameField.classList.remove("input-error");
        firstNameError.textContent = "";
        lastNameField.classList.remove("input-error");
        lastNameError.textContent = "";
        dateOfBirthField.classList.remove("input-error");
        dateOfBirthError.textContent = "";
    }
    if (error) {
        return;
    }

    const criteria = {
        id: idField.value.trim(),
        firstName: firstNameField.value.trim(),
        lastName: lastNameField.value.trim(),
        dateOfBirth: dateOfBirthField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    updateRow("authors", filteredCriteria)
        .then(response => {
            infoMessage.style.color = "green";
            infoMessage.textContent = response;
        })
        .catch(error => {
            infoMessage.style.color = "red";
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
            infoMessage.style.color = "green";
            infoMessage.textContent = response;
        })
        .catch(error => {
            infoMessage.style.color = "red";
            infoMessage.textContent = error.message;
        });
});

searchButton.addEventListener("click", () => {
    deleteFieldsErrors();

    const criteria = {
        id: idField.value.trim(),
        firstName: firstNameField.value.trim(),
        lastName: lastNameField.value.trim(),
        dateOfBirth: dateOfBirthField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    console.log("filteredCriteria : ", filteredCriteria);

    searchRows("authors", filteredCriteria)
        .then(data => {
            console.log("Contenu complet de data :", data);
            console.log("Type de data :", typeof data);
            console.log("Est-ce un tableau ?", Array.isArray(data));

            const tableAuthors = document.getElementById("table-authors");

            tableAuthors.innerHTML = "";

            data.forEach(author => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = author.id;

                const lastNameCell = document.createElement("td");
                lastNameCell.textContent = author.lastName;

                const firstNameCell = document.createElement("td");
                firstNameCell.textContent = author.firstName;

                const dateOfBirthCell = document.createElement("td");
                dateOfBirthCell.textContent = author.birthDate;

                row.appendChild(idCell);
                row.appendChild(lastNameCell);
                row.appendChild(firstNameCell);
                row.appendChild(dateOfBirthCell);

                tableAuthors.appendChild(row);

                addRowListeners(row);
            });
        })
        .catch(error => {
            infoMessage.style.color = "red";
            infoMessage.textContent = error.message || "Erreur lors du chargement des données.";
            console.error("Erreur :", error);
        });
});


emptyButton.addEventListener("click", () => {
    deleteFieldsErrors();
    idField.value = "";
    firstNameField.value = "";
    lastNameField.value = "";
    dateOfBirthField.value = "";

    infoMessage.textContent = "";
});

function deleteFieldsErrors() {
    idField.classList.remove("input-error");
    idError.textContent = "";
    firstNameField.classList.remove("input-error");
    firstNameError.textContent = "";
    lastNameField.classList.remove("input-error");
    lastNameError.textContent = "";
    dateOfBirthField.classList.remove("input-error");
    dateOfBirthError.textContent = "";

    infoMessage.textContent = "";
}

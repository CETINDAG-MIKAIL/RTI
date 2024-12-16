const idField = document.getElementById("id");
const idError = document.getElementById("id-error");
const firstnameField = document.getElementById("firstname");
const firstnameError = document.getElementById("firstname-error");
const lastnameField = document.getElementById("lastname");
const lastnameError = document.getElementById("lastname-error");
const dateOfBirthField = document.getElementById("date-of-birth");
const dateOfBirthError = document.getElementById("date-of-birth-error");

const infoMessage = document.getElementById("info-message");

document.addEventListener("DOMContentLoaded", () => {
    const rows = document.querySelectorAll("table tr:not(:first-child)");

    rows.forEach(row => {
        row.addEventListener("click", () => {
            const cells = row.querySelectorAll("td");

            idField.value = cells[0].textContent.trim();
            firstnameField.value = cells[1].textContent.trim();
            lastnameField.value = cells[2].textContent.trim();

            let [day, month, year] = cells[3].textContent.split('-');
            let formattedDate = `${year}-${month}-${day}`;
            dateOfBirthField.value = formattedDate.trim();
        });
    });
});

const addButton = document.getElementById("add-button");
const editButton = document.getElementById("edit-button");
const deleteButton = document.getElementById("delete-button");
const searchButton = document.getElementById("search-button");
const emptyButton = document.getElementById("empty-button");

addButton.addEventListener("click", () => {
    deleteFieldsErrors();

    let error = false;

    if (firstnameField.value === "") {
        firstnameField.classList.add("input-error");
        firstnameError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (lastnameField.value === "") {
        lastnameField.classList.add("input-error");
        lastnameError.textContent = "Ce champ est obligatoire pour l'ajout.";
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
        firstname: firstnameField.value,
        lastname: lastnameField.value,
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

    if (firstnameField.value === "") {
        firstnameField.classList.add("input-error");
        firstnameError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (lastnameField.value === "") {
        lastnameField.classList.add("input-error");
        lastnameError.textContent = "Un de ces champs est obligatoire pour la modification.";
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
        firstnameField.classList.remove("input-error");
        firstnameError.textContent = "";
        lastnameField.classList.remove("input-error");
        lastnameError.textContent = "";
        dateOfBirthField.classList.remove("input-error");
        dateOfBirthError.textContent = "";
    }
    if (error) {
        return;
    }

    const criteria = {
        id: idField.value.trim(),
        firstname: firstnameField.value.trim(),
        lastname: lastnameField.value.trim(),
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
        firstname: firstnameField.value.trim(),
        lastname: lastnameField.value.trim(),
        dateOfBirth: dateOfBirthField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    searchRows("authors", filteredCriteria)
        .then(data => {
            const tableAuthors = document.getElementById("table-authors");

            tableAuthors.innerHTML = "";

            data.forEach(author => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = author.id;

                const firstnameCell = document.createElement("td");
                firstnameCell.textContent = author.firstname;

                const lastnameCell = document.createElement("td");
                lastnameCell.textContent = author.lastname;

                const dateOfBirthCell = document.createElement("td");
                dateOfBirthCell.textContent = author.dateOfBirth;

                row.appendChild(idCell);
                row.appendChild(firstnameCell);
                row.appendChild(lastnameCell);
                row.appendChild(dateOfBirthCell);

                tableAuthors.appendChild(row);
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
    firstnameField.value = "";
    lastnameField.value = "";
    dateOfBirthField.value = "";

    infoMessage.textContent = "";
});

function deleteFieldsErrors() {
    idField.classList.remove("input-error");
    idError.textContent = "";
    firstnameField.classList.remove("input-error");
    firstnameError.textContent = "";
    lastnameField.classList.remove("input-error");
    lastnameError.textContent = "";
    dateOfBirthField.classList.remove("input-error");
    dateOfBirthError.textContent = "";

    infoMessage.textContent = "";
}

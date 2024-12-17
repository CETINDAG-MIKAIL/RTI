const idField = document.getElementById("id");
const idError = document.getElementById("id-error");
const titleField = document.getElementById("title");
const titleError = document.getElementById("title-error");
const idAuthorField = document.getElementById("author-id");
const idAuthorError = document.getElementById("author-id-error");
const idSubjectField = document.getElementById("subject-id");
const idSubjectError = document.getElementById("subject-id-error");
const isbnField = document.getElementById("isbn");
const isbnError = document.getElementById("isbn-error");
const pageCountField = document.getElementById("page-count");
const pageCountError = document.getElementById("page-count-error");
const stockQuantityField = document.getElementById("stock-quantity");
const stockQuantityError = document.getElementById("stock-quantity-error");
const priceField = document.getElementById("price");
const priceError = document.getElementById("price-error");
const publishYearField = document.getElementById("publish-year");
const publishYearError = document.getElementById("publish-year-error");

const infoMessage = document.getElementById("info-message");

document.addEventListener("DOMContentLoaded", () => {
    const rows = document.querySelectorAll("table tr:not(:first-child)");

    rows.forEach(row => {
        row.addEventListener("click", () => {
            const cells = row.querySelectorAll("td");

            idField.value = cells[0].textContent.trim();
            titleField.value = cells[1].textContent.trim();
            idAuthorField.value = cells[2].textContent.trim();
            idSubjectField.value = cells[3].textContent.trim();
            isbnField.value = cells[4].textContent.trim();
            pageCountField.value = cells[5].textContent.trim();
            stockQuantityField.value = cells[6].textContent.trim();
            priceField.value = cells[7].textContent.trim();
            publishYearField.value = cells[8].textContent.trim();
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

    if (titleField.value === "") {
        titleField.classList.add("input-error");
        titleError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (idAuthorField.value === "") {
        idAuthorField.classList.add("input-error");
        idAuthorError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (idSubjectField.value === "") {
        idSubjectField.classList.add("input-error");
        idSubjectError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (isbnField.value === "") {
        isbnField.classList.add("input-error");
        isbnError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (pageCountField.value === "") {
        pageCountField.classList.add("input-error");
        pageCountError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (stockQuantityField.value === "") {
        stockQuantityField.classList.add("input-error");
        stockQuantityError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (priceField.value === "") {
        priceField.classList.add("input-error");
        priceError.textContent = "Ce champ est obligatoire pour l'ajout.";
        error = true;
    }

    if (publishYearField.value === "") {
        publishYearField.classList.add("input-error");
        publishYearError.textContent = "Ce champ est obligatoire pour l'ajout.";
        return;
    }

    if (error) {
        return;
    }

    const newRowData = {
        title: titleField.value,
        idAuthor: idAuthorField.value,
        idSubject: idSubjectField.value,
        isbn: isbnField.value,
        pageCount: pageCountField.value,
        stockQuantity: stockQuantityField.value,
        price: priceField.value,
        publishYear: publishYearField.value
    };

    addRow("books", newRowData)
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

    if (titleField.value === "") {
        titleField.classList.add("input-error");
        titleError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (idAuthorField.value === "") {
        idAuthorField.classList.add("input-error");
        idAuthorError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (idSubjectField.value === "") {
        idSubjectField.classList.add("input-error");
        idSubjectError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (isbnField.value === "") {
        isbnField.classList.add("input-error");
        isbnError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (pageCountField.value === "") {
        pageCountField.classList.add("input-error");
        pageCountError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (stockQuantityField.value === "") {
        stockQuantityField.classList.add("input-error");
        stockQuantityError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (priceField.value === "") {
        priceField.classList.add("input-error");
        priceError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (publishYearField.value === "") {
        publishYearField.classList.add("input-error");
        publishYearError.textContent = "Un de ces champs est obligatoire pour la modification.";
        error = true;
    } else {
        hasInAField = true;
    }

    if (hasInAField) {
        titleField.classList.remove("input-error");
        titleError.textContent = "";
        idAuthorField.classList.remove("input-error");
        idAuthorError.textContent = "";
        idSubjectField.classList.remove("input-error");
        idSubjectError.textContent = "";
        isbnField.classList.remove("input-error");
        isbnError.textContent = "";
        pageCountField.classList.remove("input-error");
        pageCountError.textContent = "";
        stockQuantityField.classList.remove("input-error");
        stockQuantityError.textContent = "";
        priceField.classList.remove("input-error");
        priceError.textContent = "";
        publishYearField.classList.remove("input-error");
        publishYearError.textContent = "";
    }
    if (error) {
        return;
    }

    const criteria = {
        id: idField.value.trim(),
        title: titleField.value.trim(),
        idAuthor: idAuthorField.value.trim(),
        idSubject: idSubjectField.value.trim(),
        isbn: isbnField.value.trim(),
        pageCount: pageCountField.value.trim(),
        stockQuantity: stockQuantityField.value.trim(),
        price: priceField.value.trim(),
        publishYear: publishYearField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    updateRow("books", filteredCriteria)
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

    deleteRow("books", idField.value)
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
        title: titleField.value.trim(),
        idAuthor: idAuthorField.value.trim(),
        idSubject: idSubjectField.value.trim(),
        isbn: isbnField.value.trim(),
        pageCount: pageCountField.value.trim(),
        stockQuantity: stockQuantityField.value.trim(),
        price: priceField.value.trim(),
        publishYear: publishYearField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    searchRows("books", filteredCriteria)
        .then(data => {
            const tableBooks = document.getElementById("table-books");

            tableBooks.innerHTML = "";

            data.forEach(book => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = book.id;

                const titleCell = document.createElement("td");
                titleCell.textContent = book.title;

                const idAuthorCell = document.createElement("td");
                idAuthorCell.textContent = book.idAuthor;

                const idSubjectCell = document.createElement("td");
                idSubjectCell.textContent = book.idSubject;

                const isbnCell = document.createElement("td");
                isbnCell.textContent = book.isbn;

                const pageCountCell = document.createElement("td");
                pageCountCell.textContent = book.pageCount;

                const stockQuantityCell = document.createElement("td");
                stockQuantityCell.textContent = book.stockQuantity;

                const priceCell = document.createElement("td");
                priceCell.textContent = book.price;

                const publishYearCell = document.createElement("td");
                publishYearCell.textContent = book.publishYear;

                row.appendChild(idCell);
                row.appendChild(titleCell);
                row.appendChild(idAuthorCell);
                row.appendChild(idSubjectCell);
                row.appendChild(isbnCell);
                row.appendChild(pageCountCell);
                row.appendChild(stockQuantityCell);
                row.appendChild(priceCell);
                row.appendChild(publishYearCell);

                tableBooks.appendChild(row);
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
    titleField.value = "";
    idAuthorField.value = "";
    idSubjectField.value = "";
    isbnField.value = "";
    pageCountField.value = "";
    stockQuantityField.value = "";
    priceField.value = "";
    publishYearField.value = "";

    infoMessage.textContent = "";
});

function deleteFieldsErrors() {
    idField.classList.remove("input-error");
    idError.textContent = "";
    titleField.classList.remove("input-error");
    titleError.textContent = "";
    idAuthorField.classList.remove("input-error");
    idAuthorError.textContent = "";
    idSubjectField.classList.remove("input-error");
    idSubjectError.textContent = "";
    isbnField.classList.remove("input-error");
    isbnError.textContent = "";
    pageCountField.classList.remove("input-error");
    pageCountError.textContent = "";
    stockQuantityField.classList.remove("input-error");
    stockQuantityError.textContent = "";
    priceField.classList.remove("input-error");
    priceError.textContent = "";
    publishYearField.classList.remove("input-error");
    publishYearError.textContent = "";

    infoMessage.textContent = "";
}
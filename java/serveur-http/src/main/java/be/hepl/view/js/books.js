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

fillBooksTableSearch({});

function addRowListeners(row) {
    row.addEventListener("click", () => {
        const allRows = document.querySelectorAll("tbody tr");
        allRows.forEach(r => r.classList.remove("selected-row"));
        row.classList.add("selected-row");

        const cells = row.querySelectorAll("td");

        idField.value = cells[0].textContent.trim();
        titleField.value = cells[1].textContent.trim();
        idAuthorField.value = cells[2].textContent.trim();
        idSubjectField.value = cells[5].textContent.trim();
        isbnField.value = cells[7].textContent.trim();
        pageCountField.value = cells[8].textContent.trim();
        stockQuantityField.value = cells[9].textContent.trim();
        priceField.value = cells[10].textContent.trim();
        publishYearField.value = cells[11].textContent.trim();
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
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillBooksTableSearch({});
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

    updateRow("books", idField.value.trim(), filteredCriteria)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillBooksTableSearch({});
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

    deleteRow("books", idField.value)
        .then(response => {
            infoMessage.classList.remove("error-info-message");
            infoMessage.classList.add("succes-info-message");
            infoMessage.textContent = response;

            fillBooksTableSearch({});
        })
        .catch(error => {
            infoMessage.classList.remove("succes-info-message");
            infoMessage.classList.add("error-info-message");
            infoMessage.textContent = error.message;
        });
});


const pageCountSelect = document.getElementById("page-count-select");
const stockQuantitySelect = document.getElementById("stock-quantity-select");
const priceSelect = document.getElementById("price-select");
const publishYearSelect = document.getElementById("publish-year-select");

searchButton.addEventListener("click", () => {
    deleteFieldsErrors();

    let pageCountKey, stockQuantityKey, priceKey, publishYearKey;

    if (pageCountSelect.value === "minPageCount") {
        pageCountKey = "minPageCount";
    } else {
        pageCountKey = "maxPageCount";
    }

    if (stockQuantitySelect.value === "minStockQuantity") {
        stockQuantityKey = "minStockQuantity";
    } else {
        stockQuantityKey = "maxStockQuantity";
    }

    if (priceSelect.value === "minPrice") {
        priceKey = "minPrice";
    } else {
        priceKey = "maxPrice";
    }

    if (publishYearSelect.value === "minPublishYear") {
        publishYearKey = "minPublishYear";
    } else {
        publishYearKey = "maxPublishYear";
    }


    const criteria = {
        id: idField.value.trim(),
        title: titleField.value.trim(),
        idAuthor: idAuthorField.value.trim(),
        idSubject: idSubjectField.value.trim(),
        isbn: isbnField.value.trim(),
        [pageCountKey]: pageCountField.value.trim(),
        [stockQuantityKey]: stockQuantityField.value.trim(),
        [priceKey]: priceField.value.trim(),
        [publishYearKey]: publishYearField.value.trim()
    };

    const filteredCriteria = Object.fromEntries(
        Object.entries(criteria).filter(([key, value]) => value !== null && value !== "")
    );

    fillBooksTableSearch(filteredCriteria);
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

function fillBooksTableSearch(criteria) {
    searchRows("books", criteria)
        .then(data => {
            console.log("Contenu complet de data :", data);
            console.log("Type de data :", typeof data);
            console.log("Est-ce un tableau ?", Array.isArray(data));

            const tableBooks = document.getElementById("table-books");

            tableBooks.innerHTML = "";

            data.forEach(book => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = book.id;

                const titleCell = document.createElement("td");
                titleCell.textContent = book.title;

                const idAuthorCell = document.createElement("td");
                idAuthorCell.textContent = book.author.id;

                const lastNameAuthorCell = document.createElement("td");
                lastNameAuthorCell.textContent = book.author.lastName;

                const firstNameAuthorCell = document.createElement("td");
                firstNameAuthorCell.textContent = book.author.firstName;

                const idSubjectCell = document.createElement("td");
                idSubjectCell.textContent = book.subject.id;

                const nameSubjectCell = document.createElement("td");
                nameSubjectCell.textContent = book.subject.name;

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
                row.appendChild(lastNameAuthorCell);
                row.appendChild(firstNameAuthorCell);
                row.appendChild(idSubjectCell);
                row.appendChild(nameSubjectCell);
                row.appendChild(isbnCell);
                row.appendChild(pageCountCell);
                row.appendChild(stockQuantityCell);
                row.appendChild(priceCell);
                row.appendChild(publishYearCell);

                tableBooks.appendChild(row);

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
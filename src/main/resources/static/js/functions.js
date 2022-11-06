function hideForeignDiv() {
    let elDiv = document.getElementById("foreignDiv");
    let elSel = document.getElementById("foreignTableId");

    if (document.getElementById("type").value === 'FOREIGN') {
        elDiv.hidden = false;
    } else {
        elDiv.hidden = true;
        elSel.value = "";
    }
}

function onDeleteClick(button) {
    document.getElementById("deleteConfirmForm").action = "/tables/delete/" + button.getAttribute("data-id");
}
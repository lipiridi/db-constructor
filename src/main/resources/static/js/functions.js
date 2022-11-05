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
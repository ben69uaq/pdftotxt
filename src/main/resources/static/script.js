// drag drop functions
function dragOver(e) {
    e.preventDefault();	
}
function dragEnter(e) {
    zone.className = "highlighted";
    zone.innerHTML = "YES HERE";
}
function dragLeave(e) {
    zone.className = "";
    zone.innerHTML = "DROP FILE HERE";
}
function drop(e) {
    e.preventDefault();
    let files = e.dataTransfer.files;
    window.total = files.length;
    window.count = 0;
    ([...files]).forEach(uploadFile);
    zone.innerHTML = "WAIT ...";
}

// file management functions
function uploadFile(file) {
    let formData = new FormData()
    formData.append('file', file)
    fetch('/upload', {
        method: 'POST',
        body: formData
    })
    .then(() => {
        count++;
        zone.innerHTML = count + " / " + total + " UPLOADED";
        if(count == total) {
            setTimeout(() => {
                zone.className = "";
                zone.innerHTML = "DROP FILE HERE";
            }, 2000);
        }
    })
    .then(loadFileList)
    .catch(() => {
        zone.innerHTML = "ERROR";
        setTimeout(() => {
            zone.className = "";
            zone.innerHTML = "DROP FILE HERE";
        }, 2000);
    })
}
function deleteFile(fileName) {
    fetch('/delete/' + fileName)
    .then(loadFileList);
}
function deleteAll() {
    fetch('/delete/all')
    .then(loadFileList);
}
function seeSanitized(fileName) {
    window.open("/get/sanitized/" + fileName, '_blank').focus();
}
function seeOriginal(fileName) {
    window.open("/get/original/" + fileName, '_blank').focus();
}
function seeAll() {
    window.open("/get/all", '_blank').focus();
}

// list management functions
function loadFileList() {
    fetch('/list')
    .then(response => response.json())	
    .then(displayList);
}
function displayList(data) {
    list.innerHTML = "";
    data.forEach(fileName => {
        list.innerHTML += "<div class='item'>" + decodeURI(fileName).replace(/\+/g, ' ')
                        + "<span class='button' onclick='deleteFile(\"" + fileName + "\")' title='delete file'>X</span>"
                        + "<span class='button' onclick='seeOriginal(\"" + fileName + "\")' title='see original file'>O</span>"
                        + "<span class='button' onclick='seeSanitized(\"" + fileName + "\")' title='see sanitized file'>S</span>"
                        + "</div>";
    });
}

// initialisation
loadFileList();
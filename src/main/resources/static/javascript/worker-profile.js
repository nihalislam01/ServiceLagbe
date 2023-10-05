const toggleSwitch = document.getElementById("toggle");
const customAlert = document.getElementById("customAlert");
const okButton = document.getElementById("okButton");
const cancelButton = document.getElementById("cancelButton");
const onAlert = document.getElementById("onAlert");
const offAlert = document.getElementById("offAlert");

toggleSwitch.addEventListener("change", function () {
    if(this.checked) {
        onAlert.style.display="block";
        offAlert.style.display="none";
    } else {
        onAlert.style.display="none";
        offAlert.style.display="block";
    }
    customAlert.style.display="flex";
});
okButton.addEventListener("click", function () {
    customAlert.style.display="none";
});

cancelButton.addEventListener("click", function () {
    customAlert.style.display="none";
    if(toggleSwitch.checked) {
        toggleSwitch.checked = false
    } else {
        toggleSwitch.checked = true
    }
});
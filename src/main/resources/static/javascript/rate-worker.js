const customAlert = document.getElementById("customAlert");
const okButton = document.getElementById("okButton");
const rateButton = document.getElementById("rate");

rateButton.addEventListener("click", function () {
    customAlert.style.display="flex";
});
okButton.addEventListener("click", function () {
    customAlert.style.display="none";
});
window.addEventListener("click", function(event) {
    if (!event.target.matches("#rate")) {
        customAlert.style.display="none";
    }
});


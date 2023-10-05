var button = document.getElementById("dropdownButton");
var dropdownContent = document.querySelector(".dropdown-content");
var selectedItemsBox = document.getElementById("selectedItemsBox");

var selectedItems = [];

function updateSelectedItemsBox() {
    selectedItemsBox.style.display = "block";
    selectedItemsBox.innerHTML = selectedItems.map(item => `
        <div class="selected-item">
            ${item}
            <span class="close" onclick="removeSelectedItem('${item}')">&times;</span>
        </div>
    `).join("");
}

function toggleCheckbox(checkbox) {
    var selectedItem = checkbox.value;
    if (checkbox.checked && !selectedItems.includes(selectedItem)) {
        selectedItems.push(selectedItem);
        updateSelectedItemsBox();
    } else if (!checkbox.checked && selectedItems.includes(selectedItem)) {
        selectedItems.splice(selectedItems.indexOf(selectedItem), 1);
        updateSelectedItemsBox();
    }
}

function removeSelectedItem(item) {
    var index = selectedItems.indexOf(item);
    if (index !== -1) {
        selectedItems.splice(index, 1);
        updateSelectedItemsBox();
        
        var checkbox = Array.from(dropdownContent.querySelectorAll("input[type='checkbox']"))
            .find(input => input.value === item);
        if (checkbox) {
            checkbox.checked = false;
        }
        if (index === 0) {
            selectedItemsBox.style.display = "none";
        }
    }
}

button.addEventListener("click", function() {
    if (dropdownContent.style.display === "block") {
        dropdownContent.style.display = "none";
    } else {
        dropdownContent.style.display = "block";
    }
});

window.addEventListener("click", function(event) {
    if (!event.target.matches("#dropdownButton")) {
        dropdownContent.style.display = "none";
    }
});
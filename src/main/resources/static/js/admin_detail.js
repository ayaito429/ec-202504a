function changeDisplay() {
    const rowElement = document.getElementsByName("completionTime")[0];
    const statusElement = document.getElementsByName("status")[0];

    const value = statusElement.value;

    if (!rowElement) return;
    if (value === "4" || value === 4) {
        rowElement.style = "";
    } else {
        rowElement.style = "display : none;";
    }
}

function changeDeliveryTimestamp() {
    const date = document.getElementById('deliveryDate').value;
    const time = document.querySelector('input[name="deliveryHour"]:checked').value;

    if(date && time){
        const timestamp = `${date} ${time}`;
        document.getElementById('completionTimestamp').value = timestamp;
    }

    console.log(document.getElementById('completionTimestamp').value)
}

const statusSelecter = document.getElementsByName("status")[0]
statusSelecter.addEventListener('change', changeDisplay)
changeDisplay()

const deliveryDate = document.getElementById('deliveryDate')
const deliveryHours = document.getElementsByName('deliveryHour')
deliveryDate.addEventListener('change', changeDeliveryTimestamp);
deliveryHours.forEach((deliveryDate) => {
    deliveryDate.addEventListener('change', changeDeliveryTimestamp);
})

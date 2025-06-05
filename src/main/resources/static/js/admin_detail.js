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

const statusSelecter = document.getElementsByName("status")[0]
statusSelecter.addEventListener('change', changeDisplay)
changeDisplay()


document.querySelector('form').addEventListener('submit', function (e) {
    const date = document.getElementById('deliveryDate').value;
    const time = document.querySelector('input[name="deliveryHour"]:checked').value;

    if (date) {
        const timestamp = `${date} ${time}`;
        document.getElementById('deliveryTimestamp').value = timestamp;
        console.log("送信されるTIMESTAMP:", timestamp);
    }
});

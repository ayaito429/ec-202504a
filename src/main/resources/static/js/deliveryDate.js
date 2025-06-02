document.querySelector('form').addEventListener('submit', function (e) {
    const date = document.getElementById('deliveryDate').value;
    const time = document.querySelector('input[name="deliveryHour"]:checked').value;

    if (date) {
        const timestamp = `${date} ${time}`;
        document.getElementById('deliveryTimestamp').value = timestamp;
        console.log("送信されるTIMESTAMP:", timestamp);
    }
});

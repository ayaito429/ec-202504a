const fieldTypeMap = {
    id: {
        type: "input",
        inputType: "text",
        placeholder: "1"
    },
    name: {
        type: "input",
        inputType: "text",
        placeholder: "山田太郎"
    },
    orderDate: {
        type: "input",
        inputType: "date"
    },
    telephone: {
        type: "input",
        inputType: "text",
        placeholder: "000-0000-0000"
    },
    status: {
        type: "select",
        options: [
            {
                "value": 1,
                "text": "未入金"
            },
            {
                "value": 2,
                "text": "入金済"
            },
            {
                "value": 3,
                "text": "発送済"
            },
            {
                "value": 4,
                "text": "配達完了"
            },
            {
                "value": 9,
                "text": "キャンセル"
            }
        ]
    },
    deliveryTime: {
        type: "input",
        inputType: "date"
    },
    completionTime: {
        type: "input",
        inputType: "date"
    },
    payMethod: {
        type: "select",
        options: [
            {
                "value": 1,
                "text": "代金引換"
            },
            {
                "value": 2,
                "text": "クレジットカード"
            }
        ]
    }


};

function changeInputTypeByMap() {

    const selectedKey = document.getElementById("searchField");
    const config = fieldTypeMap[selectedKey.value];
    const container = document.getElementById("searchInputArea");

    // 入力欄エリアを初期化
    container.innerHTML = "";

    if (!config) return;

    if (config.type === "input") {
        if (config.inputType === "date") {
            ["開始日", "終了日"].forEach((_, index) => {
                const input = document.createElement("input");
                input.type = config.inputType || "text";
                input.name = "searchValue" + (index + 1);
                input.id = "searchValue" + (index + 1);
                if (config.placeholder) input.placeholder = config.placeholder;
                container.appendChild(input);

                if (index === 0) {
                    const span = document.createElement("span")
                    span.innerHTML = "～"
                    container.appendChild(span)
                }
            })
        } else {
            const input = document.createElement("input");
            input.type = config.inputType || "text";
            input.name = "searchValue1";
            input.id = "searchValue1";
            if (config.placeholder) input.placeholder = config.placeholder;
            container.appendChild(input);
        }
    } else if (config.type === "select") {
        const select = document.createElement("select");
        select.name = "searchValue1";
        select.id = "searchValue1";

        config.options.forEach(optionValue => {
            const option = document.createElement("option");
            option.value = optionValue.value;
            option.textContent = optionValue.text;
            select.appendChild(option);
        });

        container.appendChild(select);
    }
}

const searchField = document.getElementById("searchField")
searchField.addEventListener('change', changeInputTypeByMap)

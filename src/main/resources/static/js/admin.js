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
        options: ["未入金", "入金済", "発送済", "配達完了", "キャンセル"]
    },
    deliveryTime: {
        type: "input",
        inputType: "date"
    },
    complationTime: {
        type: "input",
        inputType: "date"
    },
    payMethod: {
        type: "select",
        options: ["代金引換", "クレジットカード"]
    }


};

function changeInputTypeByMap() {
    [1,2].forEach((num) => {
        if(document.getElementById("searchValue" + num)){
            console.log(document.getElementById("searchValue" + num).value)
        }
    });
    const selectedKey = document.getElementById("searchField");
    const config = fieldTypeMap[selectedKey.value];
    const container = document.getElementById("searchInputArea");

    // 入力欄エリアを初期化
    container.innerHTML = "";
    console.log(config)

    if (!config) return;

    console.log(config.type)
    if (config.type === "input") {
        if(config.inputType === "date"){
            ["開始日","終了日"].forEach((name,index) => {
                const input = document.createElement("input");
                input.type = config.inputType || "text";
                input.name = "searchValue" + (index + 1);
                input.id = "searchValue" + (index + 1);
                if (config.placeholder) input.placeholder = config.placeholder;
                container.appendChild(input);
                
                if(index === 0){
                    const span = document.createElement("span")
                    span.innerHTML = "～"
                    container.appendChild(span)
                }
            })
        }else{
            const input = document.createElement("input");
            input.type = config.inputType || "text";
            input.name = "searchValue1";
            input.id = "searchValue1";
            if (config.placeholder) input.placeholder = config.placeholder;
            container.appendChild(input);
        }
    } else if (config.type === "select") {
        const select = document.createElement("select");
        select.name = "searchValue";
        select.id = "searchValue";

        config.options.forEach(optionValue => {
        const option = document.createElement("option");
        option.value = optionValue;
        option.textContent = optionValue;
        select.appendChild(option);
        });

        container.appendChild(select);
    }
}

const searchField = document.getElementById("searchField")
searchField.addEventListener('change', changeInputTypeByMap)

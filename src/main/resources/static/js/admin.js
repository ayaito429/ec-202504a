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
        inputType: "datetime-local"
    },
    complationTime: {
        type: "input",
        inputType: "datetime-local"
    },
    payMethod: {
        type: "select",
        options: ["代金引換", "クレジットカード"]
    }


};

function changeInputTypeByMap() {
     const selectedKey = document.getElementById("searchField").value;
  const config = fieldTypeMap[selectedKey];
  const container = document.getElementById("searchInputArea");

  // 入力欄エリアを初期化
  container.innerHTML = "";

  if (!config) return;

  if (config.type === "input") {
    const input = document.createElement("input");
    input.type = config.inputType || "text";
    input.name = "searchValue";
    input.id = "searchValue";
    if (config.placeholder) input.placeholder = config.placeholder;
    container.appendChild(input);
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

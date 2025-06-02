document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('cartForm');
    if (!form) {
        console.warn('cartForm が見つかりません。JSをスキップします。');
        return;
    }

    const stockElement = document.getElementById('stock');
    const stock = stockElement ? parseInt(stockElement.value, 10) : 0;
    const quantitySelect = document.getElementById('currynum');
    const errorLabel = document.getElementById('errorLabel');

    if (errorLabel) {
        if (stock > 0 && stock <= 10) {
            errorLabel.textContent = '残り ' + stock + ' 個です。';
            errorLabel.style.display = 'inline';
        } else if (stock == 0) {
        errorLabel.textContent = '在庫がありません';
        errorLabel.style.display = 'inline';
        }
    }

    form.addEventListener('submit', function(event) {
        const quantity = parseInt(quantitySelect.value, 10);

        if (errorLabel) {
            errorLabel.textContent = '';
            errorLabel.style.display = 'inline';
        }

        if (stock === 0) {
            if (errorLabel) errorLabel.textContent = '在庫がありません';
            event.preventDefault();
        } else if (quantity > stock) {
            if (errorLabel) {
                errorLabel.innerHTML = '注文数量が在庫を超えています。<br>残りは ' + stock + ' 個です。';
            }
            event.preventDefault();
        }
    });
});

function updateCart() {
    let grandTotal = 0;
    document.querySelectorAll('.lona-cart-item').forEach(item => {
        let price = parseInt(item.querySelector('.price').dataset.price);
        let quantity = parseInt(item.querySelector('.quantity').value);
        let totalPrice = price * quantity;
        item.querySelector('.total-price').innerText = totalPrice.toLocaleString();
        grandTotal += totalPrice;
    });
    document.getElementById('cartTotalAmount').innerText = grandTotal.toLocaleString();
}

updateCart();

function updateCartItemAjax(cartItemId, quantity) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/cart', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify({
            cartItemId: cartItemId,
            quantity: quantity
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.ok) {
            updateCart();
        } else {
            alert('Cập nhật giỏ hàng thất bại!');
        }
    })
    .catch(error => console.error('Lỗi:', error));
}


document.querySelectorAll('.quantity').forEach(input => {
    input.addEventListener('change', function () {
        let cartItemId = this.closest('.lona-cart-item').querySelector('[name=cartItemId]').value;
        let quantity = parseInt(this.value);

        if (quantity < 1) {
            alert('Số lượng không hợp lệ!');
            this.value = 1;
            return;
        }

        updateCartItemAjax(cartItemId, quantity);
    });
});

document.querySelectorAll('.remove-item').forEach(input => {
    input.addEventListener('click', function () {
        let cartItemElement  = this.closest('.lona-cart-item');
        let cartItemId = this.previousElementSibling.value;

        deleteCartItemFromCart(cartItemId, cartItemElement);
    });
});

function deleteCartItemFromCart(cartItemId, cartItemElement) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/cart', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify({
            cartItemId: cartItemId
        })
    })
    .then(response => {
        if (!response.ok) {
            alert('Xóa sản phẩm thất bại! Vui lòng thử lại.');
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.ok) {
            cartItemElement.remove();
            updateCart();
        } else {
            alert('Xóa sản phẩm thất bại! Vui lòng thử lại.');
        }
    })
    .catch(error => console.error('Lỗi:', error));
}

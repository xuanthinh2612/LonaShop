document.querySelector('#add-to-card').addEventListener('click', function () {
        let productId = document.querySelector('#productId').value;
        let quantity = parseInt(document.querySelector('#quantity').value);

        if (quantity < 1) {
            alert('Số lượng không hợp lệ!');
            this.value = 1;
            return;
        }

        addItemToCart(productId, quantity);
});

function addItemToCart(productId, quantity) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify({
            productId: productId,
            quantity: quantity
        })
    })
    .then(response => {
        if (!response.ok) {
            alert('Thêm vào giỏ hàng thất bại!');
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.ok) {
            openModal();
        } else {
            alert(data.errorMessage);
        }
    })
    .catch(error => console.error('Lỗi:', error));
}

function openModal() {
    let myModal = new bootstrap.Modal(document.getElementById('confirmGoToCartModal'));
    myModal.show();
}

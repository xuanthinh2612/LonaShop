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
    if(grandTotal == 0) {
        document.getElementById('myCart').innerHTML =
            `<div class="text-center">
                Không có sản phẩm nào trong giỏ hàng của bạn! quay lại <a href="/">Trang chủ</a>
            </div>`;
    }
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
            alert('Lỗi đường truyền. Cập nhật giỏ hàng thất bại!');
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
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

        if (quantity < 1 || isNaN(quantity)) {
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
        getConfirmModal(cartItemId, cartItemElement);
    });
});
function getConfirmModal(cartItemId, cartItemElement) {
    const modalHtml =
            `<div class="modal fade" id="confirmGoToCartModal" tabindex="-1" aria-labelledby="customModalLabel" aria-hidden="true">
                 <div class="modal-dialog modal-dialog-centered">
                     <div class="modal-content custom-modal">
                         <div class="modal-header">
                             <h5 class="modal-title" id="customModalLabel">Xác nhận</h5>
                             <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                         </div>
                         <div class="modal-body">
                              Bạn có muốn xóa sản phẩm khỏi giỏ hàng
                         </div>
                         <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button id="confirmDeleteBtn" class="btn btn-danger">Xóa</button>
                         </div>
                     </div>
                 </div>
             </div>`
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modalElement = document.getElementById('confirmGoToCartModal');
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();

    // Thêm sự kiện khi nhấn "Xóa"
    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        deleteCartItemFromCart(cartItemId, cartItemElement);
        modalInstance.hide();
    });

    modalElement.addEventListener('hidden.bs.modal', function () {
        modalElement.remove();
    });
}

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
        if (data.success) {
            cartItemElement.remove();
            updateCart();
        } else {
            alert('Xóa sản phẩm thất bại! Vui lòng thử lại.');
        }
    })
    .catch(error => console.error('Lỗi:', error));
}

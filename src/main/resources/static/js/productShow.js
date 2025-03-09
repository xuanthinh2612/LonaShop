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
        if (data.success) {
            updateCartIconInNavbar("added")
            getModalSuccess(data)
        } else {
            getModalError(data);
        }
    })
    .catch(error => console.error('Lỗi:', error));
}

function getModalSuccess(data) {
    const modalHtml =
            `<div class="modal fade" id="confirmGoToCartModal" tabindex="-1" aria-labelledby="customModalLabel" aria-hidden="true">
                 <div class="modal-dialog modal-dialog-centered">
                     <div class="modal-content custom-modal">
                         <div class="modal-header">
                             <h5 class="modal-title" id="customModalLabel">Thông báo</h5>
                             <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                         </div>
                         <div class="modal-body">
                              ${data.successMessage}
                         </div>
                         <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tiếp tục xem</button>
                            <a href="/cart" class="btn btn-danger">Đi đến giỏ hàng</a>
                         </div>
                     </div>
                 </div>
             </div>`

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modalElement = document.getElementById('confirmGoToCartModal');
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();

    modalElement.addEventListener('hidden.bs.modal', function () {
        modalElement.remove();
    });
}

function getModalError(data) {
    const modalHtml =
            `<div class="modal fade" id="confirmGoToCartModal" tabindex="-1" aria-labelledby="customModalLabel" aria-hidden="true">
                 <div class="modal-dialog modal-dialog-centered">
                     <div class="modal-content custom-modal">
                         <div class="modal-header">
                             <h5 class="modal-title" id="customModalLabel">Thông báo</h5>
                             <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                         </div>
                         <div class="modal-body">
                              ${data.errorMessage}
                         </div>
                         <div class="modal-footer">
                            ${data.authError ?
                            `<a href="/register" class="btn btn-outline-dark">Đăng ký</a>
                            <a href="/login" class="btn btn-info">Đăng nhập</a>` : ''}
                            ${data.productError ?
                            `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tiếp tục xem</button>
                            <a href="/cart" class="btn btn-danger">Đi đến giỏ hàng</a>`
                            : ''
                            }
                         </div>
                     </div>
                 </div>
             </div>`
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modalElement = document.getElementById('confirmGoToCartModal');
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();

    modalElement.addEventListener('hidden.bs.modal', function () {
        modalElement.remove();
    });
}
function updateCartIconInNavbar(changeFlg) {
   let itemNumber = parseInt(document.querySelector('#cartNavbar1').innerText)
    if(changeFlg == "deleted") {
        itemNumber = itemNumber - 1;
    } else if(changeFlg == "added") {
        itemNumber = itemNumber + 1
    }
    document.querySelector('#cartNavbar1').innerText = itemNumber
    document.querySelector('#cartNavbar2').innerText = itemNumber
}

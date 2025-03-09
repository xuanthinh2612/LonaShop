(function ($) {
    "use strict";
    
    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Vendor carousel
    $('.vendor-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:2
            },
            576:{
                items:3
            },
            768:{
                items:4
            },
            992:{
                items:5
            },
            1200:{
                items:6
            }
        }
    });


    // Related carousel
    $('.related-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:2
            },
            768:{
                items:3
            },
            992:{
                items:4
            }
        }
    });


    // Product Quantity
    $('.quantity button').on('click', function () {
        var button = $(this);
        var oldValue = button.parent().parent().find('input').val();
        if (button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        button.parent().parent().find('input').val(newVal);
    });
    
})(jQuery);

// toggle khi click ra ngoài vẫn close list
document.addEventListener("click", function (event) {
    var navbarNav = document.getElementById("navbarNav"); // Menu chính
    var categoryNav = document.getElementById("categoryNav"); // Menu danh mục
    var navbarToggler = document.querySelector(".navbar-toggler"); // Nút toggle menu chính
    var categoryToggler = document.querySelector(".navbar-toggler-icon[data-bs-target='#categoryNav']"); // Nút toggle danh mục

    // Kiểm tra nếu click không nằm trong navbar hoặc nút toggle
    if (
        !navbarNav.contains(event.target) &&
        !navbarToggler.contains(event.target) &&
        navbarNav.classList.contains("show")
    ) {
        new bootstrap.Collapse(navbarNav, { toggle: true });
    }

    // Kiểm tra nếu click không nằm trong categoryNav hoặc nút toggle
    if (
        !categoryNav.contains(event.target) &&
        !categoryToggler.contains(event.target) &&
        categoryNav.classList.contains("show")
    ) {
        new bootstrap.Collapse(categoryNav, { toggle: true });
    }
});

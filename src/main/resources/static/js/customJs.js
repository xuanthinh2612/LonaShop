    // Smoothly scroll to the element with the id "scrollTarget" after the page has loaded
    window.onload = function() {
        document.getElementById("scrollTarget").scrollIntoView({ behavior: 'smooth' });
    };

 // close collapse when click outside the collapsibleNav
    $(document).ready(function () {
        $(document).on('click', function (event) {
            // Check if the click is outside the collapsibleNav
            if (!$(event.target).closest('#navbar-vertical').length) {
                // If so, hide the collapsibleNav
                $('#navbar-vertical').collapse('hide');
            }
        });
    });

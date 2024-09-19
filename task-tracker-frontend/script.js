$(function() {
    function validateErrorPlacement(error, element) {
        let errorDiv = $([
            '<div class="form-group mt-3">',
            '    <div class="alert alert-danger mt-3"></div>',
            '</div>'
        ].join(""));
        error.appendTo(errorDiv.find("div.alert"));
        errorDiv.insertAfter(element);
    }

    function validateSuccess(error, element) {
        $(element).next().remove();
    }

    $("#loginForm").validate({
        rules: {
            email: {
                required: true,
                email: true,
            },
            password: {
                required: true,
            },
        },
        messages: {
            email: 'Please enter a valid email address',
            password: 'Please provide a password',
        },
        errorPlacement: validateErrorPlacement,
        success: validateSuccess,
    });

    $("#registerForm").validate({
        rules: {
            email: {
                required: true,
                email: true,
            },
            password: {
                required: true,
                minlength: 5,
            },
            confirmPassword: {
                required: true,
                equalTo: "#registerPassword",
            },
        },
        messages: {
            email: 'Please enter a valid email address',
            password: {
                required: 'Please provide a password',
                minlength: 'Your password must be at least 5 password long',
            },
            confirmPassword: {
                required: 'Please provide a password',
                equalTo: 'Please enter the same password as above',
            },
        },
        errorPlacement: validateErrorPlacement,
        success: validateSuccess,
    });

});

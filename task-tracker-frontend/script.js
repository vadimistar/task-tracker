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

    function setNavbarToAuthorized() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/user',
            xhrFields: {
                withCredentials: true,
            },
            success: function(data) {
                $('#email-text').text(data.email);
            },
            error: function(xhr) {
                console.log('Cannot get user data (status ' + xhr.status + ')');
            },
        });

        $('#login-button').attr('hidden', true);
        $('#register-button').attr('hidden', true);
        $('#user-email').removeAttr('hidden');
        $('#log-out-button').removeAttr('hidden');
    }

    function setNavbarToUnauthorized() {
        $('#login-button').removeAttr('hidden');
        $('#register-button').removeAttr('hidden');
        $('#user-email').attr('hidden', true);
        $('#log-out-button').attr('hidden', true);
    }

    function fetchTasks() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/tasks',
            xhrFields: {
                withCredentials: true,
            },
            success: function(data) {
                function createTaskDiv() {
                    return $([
                        '<a type="button" class="list-group-item list-group-item-action" data-bs-toggle="modal" data-bs-target="#taskModal">',
                        '</a>',
                    ].join(""));
                }

                let doneTasks = $("#done-tasks");
                let toDoTasks = $("#to-do-tasks");

                doneTasks.empty();
                toDoTasks.empty();

                data.forEach(task => {
                    let div = createTaskDiv();
                    div.text(task.title);
                    if (task.isCompleted) {
                        doneTasks.append(div);
                    } else {
                        toDoTasks.append(div);
                    }
                });
            },
            error: function (xhr) {
                console.log('Cannot get user tasks (status ' + xhr.status + ')');
            },
        });

        $('#tasks-container').removeAttr('hidden');
    }

    let loginForm = $("#loginForm");
    let registerForm = $("#registerForm");

    loginForm.validate({
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

    loginForm.submit(function(e) {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/auth/login',
            xhrFields: {
                withCredentials: true,
            },
            data: $(this).serialize(),
            success: function() {
                $('#loginModal').modal('hide');
                setNavbarToAuthorized();
                fetchTasks();
            },
            error: function(xhr) {
                let error = $("#loginError");
                error.removeAttr('hidden');
                let errorText = error.find("div.alert");
                switch (xhr.status) {
                    case 400:
                        errorText.text("Invalid email or password");
                        break;
                    default:
                        let data = JSON.parse(xhr.responseText);
                        errorText.text(data.message);
                        break;
                }
            },
        });
    });

    registerForm.validate({
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

    registerForm.on('submit', function(e) {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/user',
            data: $(this).serialize(),
            xhrFields: {
                withCredentials: true,
            },
            success: function() {
                $('#registerModal').modal('hide');
                setNavbarToAuthorized();
                fetchTasks();
            },
            error: function(xhr) {
                let error = $("#registerError");
                error.removeAttr('hidden');
                let errorText = error.find("div.alert");
                let data = JSON.parse(xhr.responseText);
                errorText.text(data.message);
            }
        });
    });

    $("#log-out-button :button").click(function() {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/auth/logout',
            xhrFields: {
                withCredentials: true,
            },
            success: function() {
                setNavbarToUnauthorized();
                $("#tasks-container").attr("hidden", true);
            }
        })
    });

});

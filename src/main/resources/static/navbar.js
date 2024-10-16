$(document).ready(function() {
    $.get("/user/personal-info",
    function(user) {
        console.log(user);
        $("#profile-name").text(user.nickname);
        $("#profile-img").attr('src', `/img/${user.id}.jpg`);
        $("#personal-link").attr('href', `/user/personal/${user.id}`)
    })
    // 유저 클릭 시 세부메뉴 토글
    $("#user-btn").click(function() {
        console.log("#user-btn 클릭");
        if ($("#user-box").hasClass('d-none')) {
            $("#user-box").removeClass('d-none');
        } else {
            $("#user-box").addClass('d-none');
        }
    });
});
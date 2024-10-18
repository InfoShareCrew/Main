$(document).ready(function() {
    $.get("/blog/personal-info",
    function(user) {
        console.log(user);
        $("#profile-name").text(user.nickname);
        if (user.profileImg == null) {
            $("#profile-img").attr('src', $('#profile-img').attr('src') + 'user_basic.png');
        } else {
            $("#profile-img").attr('src', $('#profile-img').attr('src') + `${user.profileImg}`);
        }
        $("#personal-link").attr('href', `/blog/${user.email}`)
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
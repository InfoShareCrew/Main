$(document).ready(function() {
    $.get("/user/name",
    function(result) {
        console.log(result);
        $("#profile-name").text(result);
        $("#profile-img").attr('src', '../img/e2729fba28172ae8831a761a9c60d35c.jpg');
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
$(document).ready(function() {
    $.get("/blog/personal-info",
    function(user) {
        console.log(user);
        $("#profile-name").text(user.nickname);
        $("#personal-link").attr('href', `/blog/${user.email}`)
        if (user.profileImg != null) {
            $("#profile-img").attr('src', `/file/${user.profileImg}`);
        }
        if (user.redirectUrl) {
            window.location.href = user.redirectUrl;  // 리다이렉트 처리
        }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        console.log("get요청 실패: " + textStatus);
        console.log("오류: " + errorThrown);
    });
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
$(document).ready(function () {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault()
        document.logoutForm.submit();
    });
    customizeDropDownMenu();
    customizeTabs();
});

function customizeDropDownMenu() {
    $(".navbar .dropdown").hover(
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }
    );
    $(".dropdown > a").click(function () {
        location.href = this.href;
    });
}

function customizeTabs() {
    var url = document.location.toString();

    if(url.match('#')){
        $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
    }


/*    hide.bs.tab – перед скрытием текущей активной вкладки, которая через некоторое время будет не активной;
    show.bs.tab – перед отображением вкладки, которая через некоторое время будет активной;
    hidden.bs.tab – после скрытия предыдущей активной вкладки (т.е. той, которая была в событии hide.bs.tab);
    shown.bs.tab – после отображения новой активной вкладки, которая сейчас будет отображаться пользователю.*/
    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        window.location.hash = e.target.hash;
    })


}
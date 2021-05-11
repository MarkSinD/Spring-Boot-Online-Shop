function clearFilter() {
    window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
    elmId = link.attr("elementId");
    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID : " + elmId + "?");
    $("#confirmModel").modal();
}

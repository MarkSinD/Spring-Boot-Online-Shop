dropdownBrands = $("#brand");
dropdownCategories = $("#category")

$(document).ready(function () {
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategories();
    });
    getCategoriesForNewForm();
});
function getCategoriesForNewForm() {
    catIdFiels = $("#categoryId");
    editMode = false;

    if(catIdFiels.length){
        editMode = true;
    }

    if(!editMode)
        getCategories();

}

function getCategories() {
    brandId = dropdownBrands.val()
    url = brandModuleURL +"/" + brandId + "/categories";
    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
    });
}

function checkNameUnique(form) {
    productName = $("#name").val();
    productId = $("#id").val();
    productAlias = $("#alias").val();
    csrfValue = $("input[name='_csrf']").val();

    params = {id: productId, name: productName, alias: productAlias, _csrf: csrfValue};

    $.post(checkUniqueUrl, params, function (response){
        if(response === "OK"){
            form.submit();
        }
        else if(response === "DuplicateName"){
            showWarningModal("There is another product having same name " + productName);
        }
        else if(response === "DuplicateAlias"){
            showWarningModal("There is another product having same alias " + productAlias);
        }
        else{
            showErrorModal("Unknown response from server");
        }
    }).fail(function () {
        showErrorModal("Could not connect to the server")
    });
    return false;
}
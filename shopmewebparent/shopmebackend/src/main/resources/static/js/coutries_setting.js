var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function (){
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");
   buttonAddCountry = $("#buttonAddCountry");
   buttonUpdateCountry = $("#buttonUpdateCountry");
   buttonDeleteCountry = $("#buttonDeleteCountry");
   labelCountryName = $("#labelCountryName");
   fieldCountryName = $("#fieldCountryName");
   fieldCountryCode = $("#fieldCountryCode");

   buttonLoad.click(function () {
       loadCountries();
   });

   dropDownCountry.on("change", function () {
        changeFormStateToSelectedCountry();
   })

    buttonAddCountry.click(function() {
        if (buttonAddCountry.val() === "Add") {
            addCountry();
        } else {
            changeFormStateToNewCountry();
        }
    });

   buttonUpdateCountry.click(function () {
        updateCountry();
   });

   buttonDeleteCountry.click(function () {
        deleteCountry();
   })
});

function deleteCountry() {
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0];
    url = contextPath + "countries/delete/" + countryId;
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    countryId = dropDownCountry.val().split("-")[0];

    $.get(url, function (responseJSON) {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNewCountry();
    }).done(function () {
        buttonLoad.val("Refresh Country List");
        showToastMessage("All counties have been deleted");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}


function updateCountry() {
    if(!validateFormCountry()) return

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    countryId = dropDownCountry.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessage("The state has been updated");
        changeFormStateToNewCountry()
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function validateFormCountry() {
    let formCountry = document.getElementById("formCountry");
    if(!formCountry.checkValidity()){
        formCountry.reportValidity();
        return false;
    }
    return true;
}

function addCountry() {

    if(!validateFormCountry()) return
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        selectedNewlyAddedCountry(countryId, countryCode, countryName);
        showToastMessage("The new state has been added");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });

}


function selectedNewlyAddedCountry(countryId, countryCode, countryName) {
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name :");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry(){
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country:");

    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}


function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountry.empty();
        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function () {
        buttonLoad.val("Refresh Country List");
        showToastMessage("All counties have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}
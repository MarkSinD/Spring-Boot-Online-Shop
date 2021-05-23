var $buttonLoadForStates;
var $dropDownCountryForStates;
var $dropDownStates;
var $buttonAddState;
var $buttonUpdateState;
var $buttonDeleteState;
var $labelStateName;
var $fieldStateName;

$(document).ready(function () {
    $buttonLoadForStates = $("#buttonLoadCountriesForState");
    $dropDownCountryForStates = $("#dropDownCountriesForStates");
    $dropDownStates = $("#dropDownStates");
    $buttonAddState = $("#buttonAddState");
    $buttonUpdateState = $("#buttonUpdateState");
    $buttonDeleteState = $("#buttonDeleteState");
    $labelStateName = $("#labelStateName");
    $fieldStateName = $("#fieldStateName");

    $buttonLoadForStates.click(function () {
        loadCountriesForStates();
    });

    $dropDownCountryForStates.on("change", function () {
        loadStatesForCountry();
    });

    $dropDownStates.on("change", function () {
        changeFormStateToSelectedState();
    });

    $buttonAddState.click(function () {
        if($buttonAddState.val() === "Add"){
            addState();
        }
        else{
            changeFormStateToNew();
        }
    });

    $buttonUpdateState.click(function () {
        updateState();
    });

    $buttonDeleteState.click(function () {
        deleteState();
    })
});

function changeFormStateToSelectedState() {
    $buttonAddState.prop("value", "New");
    $buttonUpdateState.prop("disabled", false);
    $buttonDeleteState.prop("disabled", false);

    $labelStateName.text("Select State/Province: ");

    selectedStateName = $("#dropDownStates option:selected").text();
    $fieldStateName.val(selectedStateName);
}

function loadStatesForCountry() {
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list/" + countryId;
    $.get(url, function(responseJSON) {
        $dropDownStates.empty();
        $.each(responseJSON, function(index, state) {
            $("<option>").val(state.id).text(state.name).appendTo($dropDownStates);
        });

    }).done(function() {
        changeFormStateToNew();
        showToastMessage("All states have been loaded for country " + selectedCountry.text());
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function changeFormStateToNew() {
    $buttonAddState.val("Add");
    $labelStateName.text("State/Province Name");
    $buttonUpdateState.prop("disabled", true);
    $buttonDeleteState.prop("disabled", true);
    $fieldStateName.val("").focus();
}

function loadCountriesForStates() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        $dropDownCountryForStates.empty();
        $.each(responseJSON, function (index, country) {
            optionValue = country.id;
            $("<option>").val(optionValue).text(country.name).appendTo($dropDownCountryForStates);
        });
    }).done(function () {
        loadStatesForCountry();
        showToastMessage("All counties have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}
function deleteState() {
    stateId = $dropDownStates.val();

    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url : url,
        beforeSend: function (xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew();
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function updateState() {
    if(!validateFormState())
        return;

    url = contextPath + "states/save";
    stateId = $dropDownStates.val();
    stateName = $fieldStateName.val();
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();
    jsonData = {id: stateId, name:stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The state has been updated");
        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}



function validateFormState() {
    formState = document.getElementById("formState");
    if(!formState.checkValidity()){
        formState.reportValidity();
        return false;
    }
    return true;
}
/// <reference types="cypress" />
import {Then, When} from "@badeball/cypress-cucumber-preprocessor";
import { BeforeAll } from "@badeball/cypress-cucumber-preprocessor";

BeforeAll(function () {
        const now = new Date();
        const marker = `${now.getFullYear()}${(now.getMonth() + 1).toString().padStart(2, '0')}${now.getDate().toString().padStart(2, '0')}${now.getHours().toString().padStart(2, '0')}${now.getMinutes().toString().padStart(2, '0')}${now.getSeconds().toString().padStart(2, '0')}`;
        Cypress.env('marker', marker);
});


When('I click on {string} link', (linkName: string) => {
        cy.contains('a', linkName).click();
});

Then('I fill {string} with values {string}', (formName: string, formInputValuePairs: string) => {
        const paramsArray = formInputValuePairs.split(';');
        const marker = Cypress.env('marker');
        paramsArray.forEach(param => {
                let [key, value] = param.split('=');
                value += marker;
                if (key && value) {
                        cy.get(`form[id^="${formName}"] [name="dto.${key.trim()}"], form[id^="${formName}"] [name="dto[${key.trim()}]"]`).then(element => {
                                if (element.is('input')) {
                                        cy.wrap(element).clear();
                                        cy.wrap(element).type(value.trim());
                                } else if (element.is('select')) {
                                        // forcing selection because elements are covering each other and because of that they are hard to interact with them
                                        cy.wrap(element).select(value.trim(), {force: true});
                                }
                        });
                }
        });
});

Then('I fill {string} code editor with code snippet {string} and update {string}', (formName: string, codeSnippet: string, tableName: string) => {
        const snippet = codeSnippets[codeSnippet];
        const marker = Cypress.env('marker');
        cy.get(`form[id^="${formName}"] [name="dto.code"], form[id^="${formName}"] [name="dto[code]"]`).then(element => {
                const originalStyle = element.attr('style') || '';
                element.css('display', 'block');
                cy.wrap(element)
                    .clear()
                    // .type(snippet.toString(), {delay: 0})
                    .invoke('val', snippet.toString().replaceAll(tableName, tableName+marker)).trigger('input')
                    .then(() => {
                            element.attr('style', originalStyle);
                    });
        });
});

Then('I submit form {string}', (formName: string) => {
        cy.get(`.${formName}`).within(() => {
                cy.get('.btn-submit').click();
        });
});

Then("I should see updated {string} page", (targetPageUrl: string) => {
        const urlPattern = new RegExp(targetPageUrl.replace(/\*/g, "\\d+"));
        cy.url().should("match", urlPattern);
});

const codeSnippets = {
"uberForm": a => a.text("firstName")
        .text("lastName").validate(v => (v != null && v != '') ? null : "not.valid")
        .text("contactNumber")
        .text("email")
        .text("email")
        .number("commissionRate"),
"gigaUberForm":
    a => a

    // text
    .text("name1")
    .text("name2")
        .validate(v => v != null && v.length() <= 250 ? null : "max.length.250")
    .text("name3")
        .displayWhenSelected("currency1", "PLN")
    .text("name4")
        .additionalPrivileges("manageFinancial", "manageFinancial")
    .text("name5")
        .position(1,6)
    .text("name6")
        .previousRow().position(7,6)
    .text("name7")
        .position(1,6)
        .displayWhenSelected("currency1", "PLN")
        .additionalPrivileges("manageFinancial", "manageFinancial")
        .validate(v => v != null && v.length() <= 50 ? null : "max.length.50")

    // number
    .number("quantity1")
        .validate(v => v != null && v >= 0 ? null : "not.negative")
    .number("quantity2")
        .position(1,6)
    .number("quantity3")
        .additionalPrivileges("manageFinancial", "manageFinancial")
    .number("quantity4")
        .sqlFormula("select ue.quantity1 * quantity2 from uber_entity ue where ue.id=uber_entity_id")
    .number("quantity5")
        .position(1,6)
        .additionalPrivileges("manageFinancial", "manageFinancial")
        .sqlFormula("select d.quantity1 * quantity2 from uber_entity ue where ue.id=uber_entity_id")
        .validate(v => v != null && v >= 0 ? null : "not.negative")

    // file (only one mapping for file is allowed)
    // .file("file1", "image/png,image/jpeg,application/pdf")
    // .file("file2", "image/png,image/jpeg,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    //     .formAndRepositorySupplier((f,r) => f != null ? r.getFileDtos(f.entity) : null)
    // .file("file3", "image/png,image/jpeg,application/pdf")
    //     .validate(f => f != null && f.size() <= 5 * 1024 * 1024 ? null : "max.size.5MB")
    .file("file4", "image/png,image/jpeg,application/pdf")
        .formAndRepositorySupplier((f,r) => f != null ? r.getFileDtos(f.entity) : null)
        .validate(f => f != null && f.size() <= 5 * 1024 * 1024 ? null : "max.size.5MB")

    // dropdown
    .datalist("currencies")
        .repositorySupplier(d => d.toLinkedMap(["PLN", "EUR", "GBP", "USD"]))

    .dropdown("currency1")
        .datalistValues("currencies")
    .dropdown("currency2")
        .datalistValues("currencies")
        .multiselect()
    .dropdown("currency3")
        .datalistValues("currencies")
        .position(1,6)
    .dropdown("currency4")
        .datalistValues("currencies")
        .validate(v => v != null && v != '' ? null : "not.empty")
    .dropdown("currency5")
        .datalistValues("currencies")
        .displayControl()
    .dropdown("currency6")
        .datalistValues("currencies")
        .additionalPrivileges("manageFinancial", "manageFinancial")
    .dropdown("currency7")
        .datalistValues("currencies")
        .multiselect()
        .position(1,6)
        .displayControl()
        .additionalPrivileges("manageFinancial", "manageFinancial")
        .validate(v => v != null && v != '' ? null : "not.empty")

    // many to one
    .manyToOne("reference1", "entity1")
    .manyToOne("reference2", "entity2")
        .descriptionSource("description")
    .manyToOne("reference3", "entity3")
        .descriptionSource("coalesce(first_name,'')||' '||coalesce(last_name,'')")
    .manyToOne("reference4", "entity4")
        .position(1,6)
    .manyToOne("reference5", "entity5")
        .searchable()
    .manyToOne("reference6", "entity6")
        .validateForm(f => f.dto.get("policyId") == '' && f.dto.get("clientId") == '' ?  new Map([['clientId', 'not.empty'],['policyId', 'not.empty']]) : null)
    .manyToOne("reference7", "entity7")
        .displayWhenSelected("currency1", "PLN")
    .manyToOne("reference8", "entity8")
        .descriptionSource("policy_number")
        .position(1,6)
        .searchable()
        .displayWhenSelected("currency1", "PLN")
        .validateForm(f => f.dto.get("policyId") == '' && f.dto.get("clientId") == '' ?  new Map([['clientId', 'not.empty'],['policyId', 'not.empty']]) : null)

    // checkbox
    .checkbox("enabled1")
    .checkbox("enabled2")
        .displayWhenSelected("currency1", "PLN")
    .checkbox("enabled3")
        .displayControl()
    .checkbox("enabled4")
        .sqlFormula("now() + interval '15 days' >= date1 and date2 > now()")
    .checkbox("enabled5")
        .displayWhenSelected("currency1", "PLN")
        .displayControl()
        .sqlFormula("now() + interval '15 days' >= date1 and date2 > now()")

    // radio
    .datalist("payments")
        .repositorySupplier(d => d.toLinkedMap(["Credit Card", "Bank Transfer", "Cash"]))
    .radio("payment1")
    .radio("payment2")
            .datalistValues("payments")
    .radio("payment3")
            .datalistValues("payments")
            .displayWhenSelected("currency1", "PLN")
    .radio("payment4")
            .datalistValues("payments")
            .additionalPrivileges("manageFinancial","manageFinancial")
    .radio("payment5")
            .datalistValues("payments")
            .displayWhenSelected("currency1", "PLN")
            .additionalPrivileges("manageFinancial","manageFinancial")

    // date
    .date("date1")
    .date("date2")
        .position(1,6)
    .date("date3")
        .validate(v => v != null && v != '' ? null : "not.empty")
    .date("date4")
        .displayWhenSelected("currency1", "PLN")
    .date("date5")
        .previousRow().position(7,6)
    .date("date6")
        .position(1,6)
        .displayWhenSelected("currency1", "PLN")
        .validate(v => v != null && v != '' ? null : "not.empty")

    // textarea
    .textarea("description")

    // one to many
    .oneToMany("references","reference","entityId")
};

/// <reference types="cypress" />
import {Then, When} from "@badeball/cypress-cucumber-preprocessor";

import { BeforeAll } from "@badeball/cypress-cucumber-preprocessor";

BeforeAll(function () {
        const now = new Date();
        const marker = `${now.getFullYear()}${(now.getMonth() + 1).toString().padStart(2, '0')}${now.getDate().toString().padStart(2, '0')}${now.getHours().toString().padStart(2, '0')}${now.getMinutes().toString().padStart(2, '0')}${now.getSeconds().toString().padStart(2, '0')}`;
        Cypress.env('marker', marker);
});

When('I fill {string} with values {string}', (formName: string, formInputValuePairs: string) => {
        const paramsArray = formInputValuePairs.split(';');
        const marker = Cypress.env('marker');
        paramsArray.forEach(param => {
                const [key, value] = param.split('=');
                if (key && value) {
                        cy.get(`form[id^="${formName}"] [name="dto.${key.trim()}"], form[id^="${formName}"] [name="dto[${key.trim()}]"]`).then(element => {
                                if (element.is('input')) {
                                        cy.wrap(element).clear();
                                        let targetValue = value.trim();
                                        if (/^(name|eventData)$/.test(key)) {
                                                targetValue = targetValue + marker;
                                        }
                                        cy.wrap(element).type(targetValue);
                                } else if (element.is('select')) {
                                        // forcing selection because elements are covering each other and because of that they are hard to interact with them
                                        cy.wrap(element).select(value.trim(), {force: true});
                                }
                        });
                }
        });
});


Then('I submit form {string}', (formName: string) => {
        cy.get(`.${formName}`).within(() => {
                cy.get('.btn-submit').click();
        });
});

Then('I should see {string} {string} form alert', (formAlertMessage: string, alertType: string) => {
        switch (alertType) {
                case 'danger':
                        cy.xpath("//*[contains(@class, 'form-alert') and contains(@class, 'alert') and contains(@class, 'alert-danger')]//div").as('alertSelector');
                        break;
                case 'success':
                        cy.xpath("//*[contains(@class, 'form-alert') and contains(@class, 'alert') and contains(@class, 'alert-success')]//div").as('alertSelector');
                        break;
                default:
                        throw new Error(`Unknown alert type: ${alertType}`);
        }

        cy.get('@alertSelector').should(($alert) => {
                const alertText = alertType === 'success' ? $alert.text().replace(/\n/g, '') : $alert.text();
                expect(alertText).to.equal(formAlertMessage);
        });
});

Then('I search on the page {string} for {string} via {string} on the screen', (targetPageUrl: string, uniquePhrase: string, searchForm: string) => {
        // UNSTABLE approch
        // cy.get('input[name=' + `${searchForm}` + ']').should('exist').first().type(`${uniquePhrase}{enter}`);
        /*cy.get('input[name=' + `${searchForm}` + ']').should('exist');
        cy.get('input[name=' + `${searchForm}` + ']').first().type(`${uniquePhrase}`);
        // cy.get('input[name=' + `${searchForm}` + '] > button[type="submit"]').click();
        // cy.wait(1000);
        cy.get('input[name=' + `${searchForm}` + ']').first().closest('form')
            .then((form) => {
                    // cy.wrap(form).submit();
                    cy.wrap(form).find('button[type="submit"], input[type="submit"]').click();
            });*/

        // STABLE approch
        cy.visit(Cypress.env("baseUrl") + targetPageUrl + "?" + searchForm + "=" + uniquePhrase);
});

Then('I click delete {string}', (deleteButton: string) => {
        cy.log(`${deleteButton}`);
        // cy.get(`button[title="${deleteButton}"]`).click();
        cy.get(`i[class*="fas fa-trash text-danger"]`).first().closest('button')
            .then((button) => {
                    cy.wrap(button).click();
            });
});





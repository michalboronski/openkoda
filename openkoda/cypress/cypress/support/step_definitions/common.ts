/// <reference types="cypress" />
import {Given, Then, When} from "@badeball/cypress-cucumber-preprocessor";

Cypress.on('uncaught:exception', (err, runnable) => {
    // returning false here prevents Cypress from
    // failing the test
    return false
})

Cypress.on('test:before:run', (test) => {
    if (test.title.includes('@ignore')) {
        test.skip();
    }
});

Given("I am on {string} page", (pageName: string) => {
    cy.visit(Cypress.env("baseUrl") + pageName);
});

Given("I am logged as user {string} with password {string}", (login: string, password: string) => {
    let baseUrl = Cypress.env("baseUrl");

    cy.visit(baseUrl + "/login");
    cy.get('input[name="username"]').type(login);
    cy.get('input[name="password"]').type(password);
    cy.get('button#login-button').click();
});

When("I open {string} page", (pageName: string) => {
    cy.visit(Cypress.env("baseUrl") + pageName);
});

When("I open {string} page with url parameter {string}", (pageName: string, urlParameter: string) => {
    cy.visit(Cypress.env("baseUrl") + pageName + "?" + urlParameter);
});

When("I open {string} page with page size URL parameter equal to number of records in {string} column", (pageName: string, entityName: string) => {
    cy.visit(Cypress.env("baseUrl") + pageName + "?" + entityName + "_size=1000");
});

/*Then("I should see {string} page", (pageName: string) => {
    cy.location("pathname").should('contain', pageName);
});*/

Then("I should see {string} page", (targetPageUrl: string) => {
    cy.url().should("include", targetPageUrl);
});

// #######################

Then("I should find {string} in the visible table", (expectedTableColumnValues: string) => {
    const values = expectedTableColumnValues.split(";");
    cy.get(".table-responsive tbody tr").each((row) => {
        cy.wrap(row).find("td").then((cells) => {
            let found = values.every((value) => {
                return Array.from(cells).some(cell => cell.textContent.includes(value) || value === "?");
            });
            if (found) {
                return false; // breaks the .each loop once found
            }
        });
    });
});


/// <reference types="cypress" />
import {Then, When} from "@badeball/cypress-cucumber-preprocessor";


// When I login as user "jasiu" with password "admin"
When("I login as user {string} with password {string}", (login: string, password: string) => {

    let baseUrl = Cypress.env("baseUrl");

    cy.visit(baseUrl + "/login");
    cy.get('input[name="username"]').type(login);
    cy.get('input[name="password"]').type(password);
    cy.get('button#login-button').click();
});

// Then I should see "Invalid username or password." error message
Then("I should see {string} error message", (errorMessage: string) => {
    cy.location("href").should('contain', 'error');
    cy.get('div[id="errorMessage"]').contains(errorMessage);
});

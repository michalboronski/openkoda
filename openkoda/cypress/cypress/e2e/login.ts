/// <reference types="cypress" />
import {Then, When} from "@badeball/cypress-cucumber-preprocessor";

let contacts = [];

When("I login", () => {

    let baseUrl = Cypress.env("baseUrl");
    let adminLogin = Cypress.env("adminLogin");
    let adminPassword = Cypress.env("adminPassword");

    cy.visit(baseUrl + "/login");
    cy.get('input[name="username"]').type(adminLogin);
    cy.get('input[name="password"]').type(adminPassword);
    cy.get('button#login-button').click();
    cy.url().should('include', '/dashboard')
//    cy.wait(6000);
});


Then("I should see dashboard", () => {
//    cy.location("pathname").should('match', /\/html\/dashboard\/\d/);
});

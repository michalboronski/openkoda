/// <reference types="cypress" />
import {Then, When} from "@badeball/cypress-cucumber-preprocessor";

When('I fill registration form with {string},{string},{string},{string},{string}',
    (firstName: string, lastName: string, login: string, password: string, confirmPassword: string) => {

        const now = new Date();
        const marker = `${now.getFullYear()}${(now.getMonth() + 1).toString().padStart(2, '0')}${now.getDate().toString().padStart(2, '0')}${now.getHours().toString().padStart(2, '0')}${now.getMinutes().toString().padStart(2, '0')}${now.getSeconds().toString().padStart(2, '0')}`;
        if (firstName.length) cy.get('input[name="firstName"]').type(firstName);
        if (lastName.length) cy.get('input[name="lastName"]').type(lastName);
        if (login.length) cy.get('input[name="login"]').type(login ? marker+login : login);
        if (password.length) cy.get('input[name="password"]').type(password);
        if (confirmPassword.length) cy.get('input[id="confirmPassword"]').type(confirmPassword);
    });

Then('I should see {string} button', (buttonState: string) => {
        const button = cy.get('button[type="submit"]');
        switch (buttonState) {
                case 'disabled':
                        button.should('be.disabled');
                        break;

                case 'enabled':
                        button.should('not.be.disabled');
                        break;
        }
});

Then('I submit registration', () => {
        cy.get('button[type="submit"]').click();
});

Then('I should see {string} message', (message: string) => {
        cy.contains(message).should('be.visible');
});

When('I click on {string} link', (linkName: string) => {
        cy.contains('a', linkName).click();
});


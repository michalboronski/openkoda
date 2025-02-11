/// <reference types="cypress" />
import {When} from "@badeball/cypress-cucumber-preprocessor";

// When I open "/html/organization/all" page
// And I click "<adminPageLinkLabel>" in admin menu
// Then I should see "<targetPageUrl>" page


When("I click {string} in admin menu", (adminPageLinkLabel: string) => {
    cy.get('.card-body a') // Znajduje wszystkie linki w elemencie z klasą .card-body
        .contains(adminPageLinkLabel) // Szuka linku zawierającego zadany tekst
        .click(); // Kliknij w ten link

    /*cy.get("body").then(($body) => {

        const link = [...$body.find(".card-body > a")]
            .find(el => el.text === adminPageLinkLabel);

        // Używamy cy.wrap() do owinięcia linku i kliknięcia w niego
        if (link.length) { // Sprawdź, czy znaleziono link
            cy.wrap(link).click();
        } else {
            throw new Error(`Link with label "${adminPageLinkLabel}" not found.`);
        }

        // let link = $body.find(".card-body > a:contains( '" + adminPageLinkLabel + "')");
        // cy.wrap(link).click();
    })*/
});
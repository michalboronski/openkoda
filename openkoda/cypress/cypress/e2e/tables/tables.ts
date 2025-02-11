/// <reference types="cypress" />
import {Given, Then, When} from "@badeball/cypress-cucumber-preprocessor";

/*Then('I should find {string} in the visible table', (rowValues: string) => {
    const values = rowValues.split(';');
    cy.get('.table-responsive tbody').within(() => {
        cy.get('tr').each((row) => {
            cy.wrap(row).find('td').then((cells) => {
                const foundRow = valuesMatch(values, cells);
                if (foundRow) {
                    cy.wrap(row).should('exist');
                }
            });
        });
    });
});

function valuesMatch(values, cells) {
    let cellIndex = 0;
    try {
        for (let i = 0; i < values.length; i++) {
            const value = values[i];
            if (value === "?") {
                cellIndex++;
                continue;
            }
            let cellText = cells.eq(cellIndex).text().trim();
            while (!cellText.includes(value) && cellIndex < cells.length) {
                cellIndex++;
                cellText = cells.eq(cellIndex).text().trim();
            }
            if (!cellText.includes(value)) {
                return false;
            }
        }
    } catch (e) {
        return false;
    }
    return true;
}*/


/*When("I open {string} page", (pageUrl: string) => {
    cy.visit(Cypress.env("baseUrl") + pageUrl);
    cy.wait(2000);
});*/

When('I click {string} column header', (property: string) => {
    const elementId = `sort_${property}`;
    cy.get(`#${elementId}`).click();
});

// Check if data is sorted by column
Then('I should find data sorted {string} by {string} column in {string} direction', (sortType: string, property: string, sortOrder: string) => {
    cy.get('.table-responsive').should('be.visible').within(() => {

        cy.xpath("//table//thead//tr").as('headerRow');
        cy.get('@headerRow').should('exist');
        cy.get('@headerRow').should('have.length', 1);
        cy.xpath("//table//tbody//tr").as('dataRows');
        cy.get('@dataRows').its('length').should('be.gte', 2);

        let propertyIndex = -1;
        cy.get('@headerRow').find('th').then((headers) => {
            propertyIndex = headers.toArray().findIndex(header => header.className === property);
            expect(propertyIndex).gte(0);
            // cy.log(`######################pi: ${propertyIndex}`);

            let rows = cy.get('@dataRows');
            cy.log(`######################rowssssssssssssssss: ${rows}`);
            rows.each((row, index) => {
                cy.log(`r: ${JSON.stringify(row)}`);
            });

            cy.get('@dataRows').then((rows) => {
                const values = Array.from(rows).map(row =>
                    Cypress.$(row).find('td').eq(propertyIndex).text().trim()
                );
                cy.log(`######################values: ${values}`);

                const sortedValues = [...values].slice().sort((a, b) => {
                    const comparison = sortType === 'lexic'
                        ? a.localeCompare(b) // Sortowanie leksykograficzne
                        : parseInt(a) - parseInt(b); // Sortowanie numeryczne

                    return sortOrder === 'ASC' ? comparison : -comparison; // Odwracanie kierunku sortowania
                });

                cy.log(`######################sorted values: ${sortedValues}`);

                expect(values).to.deep.equal(sortedValues);
            });

        });


    });
});

// Function to find property index in header row
function findPropertyIndex(property: string, headerRow: JQuery<HTMLElement>): number {
    return headerRow.find('th').toArray().findIndex(header => header.classList.contains(property));
}

// Ensure there are enough records in a column
Given('I am expecting no less than {string} number of records in {string} column', (numberOfRecords: string, column: string) => {
    // Call the API or function to get the number of records in the specified column
    cy.request('/api/records/count', { column }).then((response) => {
        const numberOfrecordsInColumn = response.body.count; // Adjust based on your API response structure
        cy.log(`Current number of records in ${column}: ${numberOfrecordsInColumn}`);

        // Check if the current count is less than expected
        if (numberOfrecordsInColumn < parseInt(numberOfRecords)) {
            const recordsToCreate = parseInt(numberOfRecords) - numberOfrecordsInColumn;
            // Create the required number of records
            cy.request('POST', '/api/records/create', { column, count: recordsToCreate }); // Adjust endpoint and payload
        }
    });
});

// Check if pagination links with a specific class are visible
Then('I should see pagination links list with class {string}', (paginationId: string) => {
    // Sprawdzenie widoczności linków paginacji o podanej klasie
    cy.get(`.${paginationId}`).should('exist').and('be.visible').then((paginationLinks) => {
        // Jeśli element został znaleziony, sprawdź, czy jest ich więcej niż 0
        expect(paginationLinks.length).to.be.greaterThan(0);
    });
});

// Sprawdzenie, że link paginacji z określonym id nie jest widoczny
Then('I should not see pagination links in list with id {string}', (paginationId: string) => {
    // Używamy cy.get() z asercją .should('not.exist'), aby upewnić się, że element z danym id nie istnieje
    cy.get(`#${paginationId}`).should('not.exist');
});


// Kliknięcie strzałki paginacji na podstawie tekstu linku
Then('I click {string} arrow', (paginationLinkText: string) => {
    // Używamy cy.contains() do znalezienia elementu na podstawie tekstu częściowego
    cy.contains('a', paginationLinkText).should('be.visible').click();
    // Opcjonalnie, można dodać `cy.wait()` jeśli potrzebujemy odczekać na załadowanie nowej strony po kliknięciu
    cy.wait(2000); // 2 sekundy na odświeżenie treści (jeśli konieczne)
});

// Sprawdzenie, czy w URL znajduje się dany parametr
Then('I should see {string} parameter in page URL', (urlParams: string) => {
    // Używamy cy.url() do pobrania bieżącego URL-a i sprawdzamy, czy zawiera oczekiwany parametr
    cy.url().should('include', urlParams);
});

// Sprawdzenie, czy strzałka paginacji jest widoczna na podstawie tekstu linku
Then('I should see {string} arrow', (paginationLinkText: string) => {
    // Używamy cy.contains() do znalezienia elementu na podstawie częściowego tekstu
    cy.contains('a', paginationLinkText).should('be.visible');
});



//#################################### REMOVE
/*Then("I should see {string} page", (targetPageUrl: string) => {
    cy.url().should("include", targetPageUrl);
});*/

/*Then("I should see {string} {string} form alert", (formAlertMessage: string, formAlertType: string) => {
    cy.get(`.alert-${formAlertType}`).should("contain", formAlertMessage);
});

When("I fill {string} with values {string}", (formName: string, setFormValues: string) => {
    const params = new URLSearchParams(setFormValues.split(";").map(val => val.split('=')));
    params.forEach((value, key) => {
        cy.get(`form.${formName} [name="${key}"]`).type(value);
    });
});

When("I submit form {string}", (formName: string) => {
    cy.get(`form.${formName} button.btn-submit`).click();
    cy.wait(2000);
});*/




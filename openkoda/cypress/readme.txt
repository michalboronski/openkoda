Test setup and run


I. Setup
Install NPM (https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
Run:
   npm install cypress --save-dev


II. Run tests

Run tests with default properties
npm run cy:run

Run with overridden properties:
npm run cy:run -- --env baseUrl=https://dev.cloud.openkoda.com/

List od properties is in cypress.config.ts file in e2e.env.
You can extend the list of properties. In order to use it in tests use:
let baseUrl = Cypress.env("baseUrl");
import {defineConfig} from "cypress";
// @ts-ignore
import createBundler from "@bahmutov/cypress-esbuild-preprocessor";
import {addCucumberPreprocessorPlugin} from "@badeball/cypress-cucumber-preprocessor";
// import createEsbuildPlugin from "@badeball/cypress-cucumber-preprocessor/dist/subpath-entrypoints/esbuild";
import createEsbuildPlugin from "@badeball/cypress-cucumber-preprocessor/esbuild";

export default defineConfig({
    e2e: {
        env: {
            "baseUrl": "http://127.0.0.1:8080",
            "adminLogin": "admin",
            "adminPassword": "admin123",
            // TAGS: '@common,@tables'
        },
        video: false,
        // supportFile: false,
        // chromeWebSecurity: false,
        // experimentalRunAllSpecs: true,
        defaultCommandTimeout: 4000,
        viewportHeight: 1000,
        viewportWidth: 1200,
        testIsolation: true,
        watchForFileChanges: false,
        specPattern: "**/*.feature",
        userAgent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        async setupNodeEvents(
            on: Cypress.PluginEvents,
            config: Cypress.PluginConfigOptions
        ): Promise<Cypress.PluginConfigOptions> {
            await addCucumberPreprocessorPlugin(on, config);
            on(
                "file:preprocessor",
                createBundler({
                    plugins: [createEsbuildPlugin(config)],
                })
            );
            return config;
        },
    },
});

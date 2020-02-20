# Kroger Angular(6) Seed

## What's in this seed?
 - Scaffolded Angular 6 project.
 - Angular CLI for quick easy component / class generating. See Code scaffolding below.
 - `kroger-ng-oauth2` installed, you get oAuth2 authentication/login code built in out of the box, for more details on auth methods, checkout [Stash - Kroger-ng-oauth2](https://stash.kroger.com/projects/NPM/repos/kroger-ng-oauth2/browse/README.md)
 - `kroger-notifications` Simple easy to use banner alert library.
 - `primeng` Latest version of [PrimeNG](https://www.primefaces.org/primeng/#/). A free open source component library. You can see the usage of some of the components by checking out the [UX Catalog](https://uxcatalog.kroger.com).
 - Module based file structure. Modules logically broken down by function/page within the web app.
 - Proxy access to `http://localhost:8080` for local development of apis on the server.
 - Navigation structure already set up.
 - LESS CSS preprocessor [LESS(CSS)](http://lesscss.org/).

## How to get started?
- Clone this project: `git clone https://stash.kroger.com/scm/eaf/kroger-ng-seed.git` or generate a project from [KIC Start](https://kic.kroger.com).
- `cd` into the directory containing this client, `npm install` or if coming from a KIC generated project, `mvn clean install` from the parent directory.
- `npm start` from the client directory, after the build has completed in your terminal, you can navigate to `http://localhost:4200` in your browser.

## Further help,  further reading

To get more help on Angular: [Angular.io](https://angular.io/api/)

Recommended video training: [Udemy](https://www.udemy.com/the-complete-guide-to-angular-2/)

For Kroger built, and styled ux components checkout the catalog: [Kroger UX Catalog](https://uxcatalog.kroger.com)

Kroger Design System instructs developers and designers on proper colors, fonts, styles for applications: [Kroger Design System](https://design.kroger.com)

For help with Angular at Kroger, or to discuss front end development, or to keep up to date with what's going on in the UX world at Kroger, or to ask questions and get help, checkout the Angular channel on [MS Teams Angular Channel](https://teams.microsoft.com/l/channel/19%3a71277fd662114c8cbce83f3e4be5ae8e%40thread.skype/Angular?groupId=2ce68710-d72b-47ea-bae9-f328431c0bcd&tenantId=8331e14a-9134-4288-bf5a-5e2c8412f074)

## Development server

Run `npm start` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

Running this command proxies local network through `http://localhost:8080` for local development of both client and server. 

Server needs to be running at `http://localhost:8080` for Authentication to work.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Running Typescript Linting

Run `ng lint` to execute. This will ensure code clarity and legibility across project teams. This is a step in the production build process and will fail TeamCity builds if linting does not pass.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

# furnace-embedded
Run Furnace in a Flat classloader

This project exposes the Furnace APIs as a CDI extension and allows Forge addons to run in simple or CDI containers to be executed in a single classloader.

# Instructions

- Build `furnace-embedded` first by running `mvn clean install`
- Build `wsdemo` by running: `mvn clean wildfly-swarm:run`.
- Open your browser and navigate to http://localhost:8080/hello?class=org.jboss.forge.addon.ui.command.UICommand.
- See the output in your terminal

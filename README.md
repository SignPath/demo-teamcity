Demo repository for the integration of TeamCity with [SignPath](https://signpath.io)

See [.teamcity/settings.kts](.teamcity/settings.kts) for the workflow definition.

You will need to change the URL of the `teamcity-server` repository in the `./teamcity/pom.xml` to point to your TeamCity server instance. 

To test the configuration locally, run `mvn teamcity-configurations:generate` (get it from [Apache Maven](https://maven.apache.org/index.html))

just a comment
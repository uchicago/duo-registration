# Build Process

Maven is used to build the project. To build the war distribution file, clone the project and execute:
`mvn install`

In order for successful compilation:

Copy the `DuoKeys.Template` and include your own integration iKeys, sKeys, hKeys:

`cp ./src/main/resources/DuoKeys.Template ./src/main/resources/DuoKeys.properties`

## Dependencies
The following dependencies are not available through the Maven repositories and must be installed separately:
* [Duo-Java-Client](https://github.com/duosecurity/duo_client_java)


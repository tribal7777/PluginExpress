Thalassa Plugin
===============

RestExpress plugin to support [Thalassa](https://github.com/PearsonEducation/thalassa) Frontend/Backend setup as well as Registrations.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>ThalassaPlugin</artifactId>
			<version>0.1.4</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>ThalassaPlugin</artifactId>
			<version>0.1.5-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from:
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22ThalassaPlugin%22

Note that to use the SNAPSHOT version, you must enable snapshots and a repository in your pom (or settings.xml) file as follows:
```xml
  <profiles>
    <profile>
       <id>allow-snapshots</id>
          <activation><activeByDefault>true</activeByDefault></activation>
       <repositories>
         <repository>
           <id>snapshots-repo</id>
           <url>https://oss.sonatype.org/content/repositories/snapshots</url>
           <releases><enabled>false</enabled></releases>
           <snapshots><enabled>true</enabled></snapshots>
         </repository>
       </repositories>
     </profile>
  </profiles>
```

Usage
=====

Usage of the Thalassa Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

```java
RestExpress server = new RestExpress()...

new CorsHeaderPlugin("*")							// Array of domain strings.
	.exposeHeaders("Location")						// Array of header names (Optional).
	.allowHeaders("Content-Type", "Accept")			// Array of header names (Optional).
	.maxAge(2592000)								// Seconds to cache (Optional).
	.flag("flag value")								// Just like flag() on Routes (Optional).
	.parameter("string", object)					// Just like parameter() on Routes (Optional).
	.register(server);


new ThalassaPlugin()
    .appHost("1.2.3.4")               // The host IP your application is running on
    .appName("Name")                  // The name of your application
    .appPort(80)                      // The port your application is bound to
    .appVersion("1.2.3")              // Your application version
    .aqueductURL("http://aqueduct")   // The http URL to the Aqueduct server (including port)
    .frontendBindAddress("*:80")      // The address and port the HaProxy frontend should bind to
    .registrationRefreshInterval(0)   // The interval in seconds to send registration updates to Aqueduct
    .thalassaURL("http://thalassa")   // The http URL to the Thalassa server (including port)
    .register(server);

```
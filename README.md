# Liquibase extension: InterSystems IRIS Database support

This is a Liquibase extension for InterSystems IRIS support. [InterSystems IRIS](https://www.intersystems.com/data-platform/) is a high-performance database that powers transaction processing applications around the world. Using the Cache database, you can model and store data as tables, objects, or multidimensional arrays (hierarchies). 

## Configuring the extension

These instructions will help you get the extension up and running on your local machine for development and testing purposes. This extension has a prerequisite of Liquibase core in order to use it. Liquibase core can be found at https://www.liquibase.org/download.

### Maven
Specify the Liquibase extension in the `<dependency>` section of your POM file by adding the `com.caretdev:liquibase-iris` dependency for the Liquibase plugin. 
 
```xml
<plugin>
    <!--start with basic information to get Liquibase plugin:
    include <groupId>, <artifactID>, and <version> elements-->
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <version>4.23.1</version>
    <configuration>
    <!--set values for Liquibase properties and settings
    for example, the location of a properties file to use-->
    <propertyFile>liquibase.properties</propertyFile>
    </configuration>
    <dependencies>
    <!--set up any dependencies for Liquibase to function in your
    environment for example, a database-specific plugin-->
        <dependency>
                <groupId>com.caretdev</groupId>
                <artifactId>liquibase-iris</artifactId>
                <version>${liquibase-iris.version}</version>
        </dependency>
        </dependencies>
    </plugin>
``` 
  
## Java call
  
```java
public class Application {
    public static void main(String[] args) {
        IRISDatabase database = (IRISDatabase) DatabaseFactory.getInstance().openDatabase(url, null, null, null, null);
        Liquibase liquibase = new Liquibase("liquibase/ext/changelog.generic.test.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }
}
```
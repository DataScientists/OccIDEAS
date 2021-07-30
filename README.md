# OccIDEAS

## Running in jdk 11
- install jdk 11 via https://www.oracle.com/sg/java/technologies/javase-jdk11-downloads.html
- point your maven compiler to the downloaded jdk 11
- run "mvn clean install"
- set tomcat to use jdk 11
- if you are using intellij, you need to also check the jdk here
  File->Project Structure->Modules ->> Language level to 11 

## Running spring boot jar in command line
- mvn clean install
- java -jar target/occideas-2.0-SNAPSHOT.jar org.occideas.OccideasApplication

## Setting the datasource in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/occideas
spring.datasource.username=
spring.datasource.password=
### Config data files are considered in the following order:
1. application properties packaged inside your jar (application.properties and YAML variants).
2. Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants).
3. Application properties outside of your packaged jar (application.properties and YAML variants).
4. Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants).

## Working in embedded database (mostly for developers or quick deployments)
- profile = embedded
- mvn clean install
- java -jar target/occideas-2.0-SNAPSHOT.jar org.occideas.OccideasApplication --spring.profiles.active=embedded

## Enable Metamodel in IDE
### Eclipse
-> properties / Java Compiler/ Annotation Processing
- Enable project specific settings -> checked
- Enable annotation processing -> checked
- Enable processing in editor -> checked
- Generated source directory = "target/metamodel"
- Click "Apply"
### Intellij
-> preference / Build, Execution,Deployment / Annotation Processors
- Enable annotation processing -> checked
- Obtain processors from project classpath -> checked
  Under “Annotation processors”
- add “org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor”
  Click Apply
- Run “mvn clean install”
- You might also need to
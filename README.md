# OccIDEAS Application Config

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


# OccIDEAS Nectar Server Deployment
- Launch a virtual server, recommended distro is Ubuntu 20.04 LTS.
- apt install default-jdk (recommended version is Java 11)
- apt install mysql-server (recommended version is MySQL 8)
- mysql_secure_installation (secure mysql with root password)
- mysql -u root -p
- CREATE USER 'occideas'@'%' IDENTIFIED BY 'xxxx';
- GRANT ALL PRIVILEGES ON * . * TO 'occideas'@'%';
- FLUSH PRIVILEGES;
- Deploy the database including views
- apt install apache2
- apt-get update
- apt-get install certbot python3-certbot-apache
- run cmd certbot enter domain name (no http...) and choose option 2 to redirect

- a2enmod proxy_http 
- systemctl restart apache2
- nano /etc/apache2/sites-enabled/000-default-le-ssl.conf
add
ProxyPass / http://127.0.0.1:8080/
ProxyPreserveHost On

- upload the jar, e.g. scp -i ./Downloads/occideas.pem /home/heyzeus/git/OccIDEASMaster/target/occideas-2.0-SNAPSHOT.jar ubuntu@203.101.228.216:/home/ubuntu

- apt install screen
- screen, then spacebar

- java -jar occideas-2.0-SNAPSHOT.jar org.occideas.OccideasApplication
- ctrl a d
- systemctl restart apache2

Test at the URL
 - Ensure file permission granted on /opt/data/modules and /opt/data/reports and any other folder in the application.properties config file e.g. chown -R ubuntu /opt/data

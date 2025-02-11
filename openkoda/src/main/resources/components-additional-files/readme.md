* init `mvn spring-boot:run -Dspring-boot.run.profiles=openkoda,custom,drop_and_init_database`
* run `mvn spring-boot:run -Dspring-boot.run.profiles=openkoda,custom`
* create zip for import `mvn package`. Keep in mind that importing package in an Openkoda instance does not import Java code. 
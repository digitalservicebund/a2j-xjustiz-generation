# a2j-xjustiz-generator

creates xjusitz messages out of a2j user data 

## how it works

Java classes corresponding to the XSD schemas will be generated during project build.
This is achieved using the JAXB task registered in the build.gradle.kts.

Generated files provide a Typesafe Interface to Marshal Java Objects to XML or Unmarshal Xml files to Java Objects.

By Generating and applying a Schema to the Marshaller/Unmarshaller validation of the Schema can be enforced.

## Get the Codes from XRepository
The XSD schemata contain codes that are not part of the XJustiz standard, but which can be found in the XRepository (E.g. list with unique court id and the court name). 
To retrieve the newest codes run 

```
sh app/xjustiz_codelists.sh
```

## Generating Java Files
build the project to generate the Java classes based on xjustiz files in src/main/resoruces/xjustiz/
```
./gradlew clean build
```

## Choose one of the following options to run the application: 

### 1. Running the application locally 

```bash
./gradlew bootRun
```

### 2. Running the application locally using the built jar
```
./gradlew bootJar
cd app
java -jar build/libs/app.jar
```


### 3. Running the application using Docker
```
cd app
docker build -t fgr-claim-service .
docker run -p 8080:8080 fgr-claim-service
```

## Generate XJustiz files

Now you can visit [Swagger UI](http://localhost:8080/swagger-ui/index.html#/Flight%20Claims/createClaim) to test the API and download a valid xjusitz message for flight right claims


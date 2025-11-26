# a2-xjustiz-generator

creates xjusitz messages out of a2j user data 

## how it works

Java classes corresponding to the XSD schemas will be generated during project build.
This is achieved using the JAXB task registered in the build.gradle.kts.

Generated files provide a Typesafe Interface to Marshal Java Objects to XML or Unmarshal Xml files to Java Objects.

By Generating and applying a Schema to the Marshaller/Unmarshaller validation of the Schema can be enforced.

## Generating Java Files
build the project to generate the Java classes based on xjustiz files in src/main/resoruces/xjustiz/
```
./gradlew clean build
```

## Get the Codes from XRepository
The XSD schemata contain codes that are not part of the XJustiz standard, but which can be found in the XRepository (E.g. list with unique court id and the court name). 
To retrieve the codes run 

```
sh app/xjustiz_codelists.sh
```

## Running the script
Run the script without validating the example xml and print the output
```
./gradlew bootRun
```

Run the script with enforcing the Schema to let the validation fail
```
./gradlew bootRun --args='--validate'
```

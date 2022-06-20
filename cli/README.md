# Micronaut Guides Tweets CLI


This folder contains a Micronaut CLI application which generates a list of Tweets for the guides exposed at [latest/guides.json](https://guides.micronaut.io/latest/guides.json)

to generate the CLI application run. 

```bash
cd ..
./gradlew :cli:build
```

then you can execute it with: 

```bash
cd cli
java -jar build/libs/cli-0.1-all.jar 
```
# Template Java Maven

- [Description](#description)
- [Prerequisites](#prerequisites)
    - [Eclipse specific](#eclipse-specific)
- [Build](#build)
- [Usage](#usage)

## Description

Write your own description here

### URLS

- Camunda: http://localhost:8081/bpm/camunda/app/welcome/default/#!/welcome
- Actuator: http://localhost:8081/bpm/actuator
- Openapi: http://localhost:8081/bpm/swagger/v3/api-docs
           http://localhost:8081/bpm/swagger/v3/api-docs.yaml
-          http://localhost:8081/bpm/swagger-ui/index.html


## Prerequisites

- JAVA_HOME is set to java 17
- Camunda 7.21
- Maven 3.6.3 (Older versions might cause build problems)
- *_/home/$username/.m2/settings.xml_* is set
  up [Help](https://swp-confluence.atlassian.net/wiki/spaces/SWPIT/pages/411173348/How+to+Install+and+setup+maven#Setting-up-the-maven-settings)


## Build

| Usage         | Action                   |
|---------------|--------------------------|
| Clean project | mvn clean                |
| Build project | mvn package              |
| Test project  | mvn test                 |

## Usage

Write your own usage here
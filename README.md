# Template Java Maven

- [Description](#description)
- [Prerequisites](#prerequisites)
    - [Eclipse specific](#eclipse-specific)
- [Build](#build)
- [Usage](#usage)

## Description

Write your own description here

## Prerequisites

- JAVA_HOME is set to java 17
- Maven 3.6.3 (Older versions might cause build problems)
- *_/home/$username/.m2/settings.xml_* is set
  up [Help](https://swp-confluence.atlassian.net/wiki/spaces/SWPIT/pages/411173348/How+to+Install+and+setup+maven#Setting-up-the-maven-settings)

### Eclipse specific

- Make sure to install the lombok
  plugin. [Help](https://www.cyberithub.com/how-to-install-and-use-lombok-in-java-eclipse-ide/)

## Build

| Usage         | Action                    |
|---------------|---------------------------|
| Clean project | mvn clean                 |
| Build project | mvn package -Partifactory |
| Test project  | mvn test -Partifactory    |

## Usage

Write your own usage here
# Camunda Hello World Template

## Description

Write your own description here

### URLS

- Camunda: 
  - http://localhost:8081/bpm/camunda/app/welcome/default/#!/welcome
  - http://localhost:30081/bpm/camunda/app/welcome/default/#!/welcome
- Actuator: 
  - http://localhost:8081/bpm/actuator
  - http://localhost:30081/bpm/actuator
- H2 Console: 
  - http://localhost:8081/bpm/h2-console (in the connection jdbc url use: jdbc:h2:mem:workflow-hello-world)
  - http://localhost:30081/bpm/h2-console 
- Rest Api:
  - http://localhost:8081/bpm/restapi/camunda
  - http://localhost:8081/bpm/restapi/ping
  - http://localhost:8081/bpm/restapi/workflow
  
  - http://localhost:30081/bpm/restapi/camunda
  - http://localhost:30081/bpm/restapi/ping
  - http://localhost:30081/bpm/restapi/workflow
- Openapi:
  - apidocs
    - http://localhost:8081/bpm/swagger/v3/api-docs
    - http://localhost:8081/bpm/swagger/v3/api-docs.yaml
  
    - http://localhost:30081/bpm/swagger/v3/api-docs
    - http://localhost:30081/bpm/swagger/v3/api-docs.yaml
  - camunda-engine-restapi
    - http://localhost:8081/bpm/swagger/v3/api-docs/camunda-engine-rest-api
    - http://localhost:8081/bpm/swagger/v3/api-docs/camunda-engine-rest-api.yaml
  - actuator  
    - http://localhost:30081/bpm/swagger/v3/api-docs/actuator
    - http://localhost:30081/bpm/swagger/v3/api-docs/actuator.yaml
    
    - http://localhost:30081/bpm/swagger/v3/api-docs/actuator
    - http://localhost:30081/bpm/swagger/v3/api-docs/actuator.yaml
  - restapi
    - http://localhost:8081/bpm/swagger/v3/api-docs/restapi
    - http://localhost:8081/bpm/swagger/v3/api-docs/restapi.yaml
    
    - http://localhost:30081/bpm/swagger/v3/api-docs/restapi
    - http://localhost:30081/bpm/swagger/v3/api-docs/restapi.yaml
  - swagger-ui
    - http://localhost:8081/bpm/swagger-ui/index.html
    - http://localhost:30081/bpm/swagger-ui/index.html

### Servers

- local: localhost, database on h2 locally


## Prerequisites

- Java 21
- Camunda 7.23
- Spring Boot 3.4.6
- Maven 3.6.3 (Older versions might cause build problems)
- *_/home/$username/.m2/settings.xml_* is set
  up [Help](https://swp-confluence.atlassian.net/wiki/spaces/SWPIT/pages/411173348/How+to+Install+and+setup+maven#Setting-up-the-maven-settings)
- there are two run configs which requires passwords (ldap, postgres). therefore you need to edit/rename in the src/main/conf folder the changeme-local.conf and changeme-ci.conf to ci.conf and local.conf.

## Kubernetes

To run maven filtering for destination target/k8s and destination target/helm run:
```bash
mvn clean install -DskipTests 
```

### Deployment with Kubernetes

Deployment goes into the default namespace

To deploy all resources:
```bash
kubectl apply -f target/k8s/
```

To remove all resources:
```bash
kubectl delete -f target/k8s/
```

Check
```bash
kubectl get deployments -o wide
kubectl get pods -o wide
```

You can use the actuator rest call to verify via port 30081

### Deployment with Helm

To run maven filtering for destination target/helm run:
```bash
mvn clean install -DskipTests 
```

Be aware that we are using a different namespace here (not default).

Go to the directory where the tgz file has been created after 'mvn install'
```powershell
cd target/helm/repo
```

unpack
```powershell
$file = Get-ChildItem -Filter workflow-hello-world-v*.tgz | Select-Object -First 1
tar -xvf $file.Name
```

install
```powershell
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name
helm upgrade --install $APPLICATION_NAME ./$APPLICATION_NAME --namespace workflow-hello-world --create-namespace --wait --timeout 5m --debug
```

show logs and show event
```powershell
kubectl get pods -n workflow-hello-world
```
replace $POD with pods from the command above
```powershell
kubectl logs $POD -n workflow-hello-world --all-containers
```

Show Details and Event

$POD_NAME can be: workflow-hello-world-mongodb, workflow-hello-world
```powershell
kubectl describe pod $POD_NAME -n workflow-hello-world
```

Show Endpoints
```powershell
kubectl get endpoints -n workflow-hello-world
```

test
```powershell
helm test $APPLICATION_NAME --namespace workflow-hello-world --logs
```

uninstall
```powershell
helm uninstall $APPLICATION_NAME --namespace workflow-hello-world
```

delete all
```powershell
kubectl delete all --all -n workflow-hello-world
```

create busybox sidecar
```powershell
kubectl run busybox-test --rm -it --image=busybox:1.36 --namespace=workflow-hello-world --command -- sh
```

You can use the actuator rest call to verify via port 30081

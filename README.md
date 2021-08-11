# Overview
TBD

# Deployment Instructions

### Setup Docker Swarm mode
Ensure that Docker is in Swarm mode. If not, run the following command to setup the mode:
```bash
docker swarm init
```

### Setup Docker secrets
Create the necessary secrets. This can be done explicitly using the following form.
```bash
echo "spring" | docker secret create keycloak_user -
echo "spring123" | docker secret create keycloak_password -
echo "password" | docker secret create db_password -
echo "spring-with-test-scope" | docker secret create keycloak_client_id -
echo "TBD" | docker secret create keycloak_client_secret -
```
Or, random secrets may be used.
```bash
openssl rand -base64 20 | docker secret create [keycloak_user -
openssl rand -base64 20 | docker secret create keycloak_password -
openssl rand -base64 20 | docker secret create db_password -
openssl rand -base64 20 | docker secret create keycloak_client_id -
openssl rand -base64 20 | docker secret create keycloak_client_secret -
```
If random secrets are used, then you will have to connect a terminal to the `keycloak` container and retrieve the secrets from the following files in order to log into the keycloak admnistrative console:
```
/run/secrets/keycloak_user
/run/secrets/keycloak_password
```

### Deploy services to Docker Swarm
To start the Docker services, run the following:
```bash
docker stack deploy --compose-file=docker-compose.yml keycloak_test
```
Note that neither the `gateway` nor the `ping` service will come up until the appropriate clients are created within `keycloak`. As a result, each service must be temporarily removed until the Keycloak configuration can be completed.
```bash
docker service rm keycloak_test_ping
docker service rm keycloak_test_gateway
```

### Setup Kaycloak configuration
Open a browser to http://localhost:888, navigate to the **Administraton Console** and login using `keycloak_user` and `keycloak_password` secrets defined above.

By default, the **Master** realm will be displayed. Navigate to the **Client** option in the left panel and click the **Create** button to add a new client with the following settings:

|Parameter|Value|
|---------|-----|
|Client ID|`spring-with-test-scope`|
|Access Type|`confidential`|
|Valid Redirect URIs|`*`|

# Resources
* [Spring Cloud Gateway OAuth2 with Keycloak](https://piotrminkowski.com/2020/10/09/spring-cloud-gateway-oauth2-with-keycloak/)
* [Injecting credentials into a Docker container running a Spring Boot application](https://bmuschko.com/blog/docker-secret-spring-boot/)
* [Using Docker Secrets With Docker Compose](https://www.rockyourcode.com/using-docker-secrets-with-docker-compose/)
* [Manage sensitive data with Docker secrets](https://docs.docker.com/engine/swarm/secrets/)
* [DockerHub - Keycloak](https://hub.docker.com/r/jboss/keycloak/)
* [A Quick Guide to Using Keycloak with Spring Boot](https://www.baeldung.com/spring-boot-keycloak)
* [UAA vs Keycloak comparison](https://github.com/paulojeronimo/keycloak-spring-boot-tutorial/blob/master/uaa-keycloak-comparison.adoc)
* [Using Docker Secrets during Development](https://blog.mikesir87.io/2017/05/using-docker-secrets-during-development/)
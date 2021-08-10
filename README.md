# Overview
TBD

# Deployment Instructions
Ensure that Docker is in Swarm mode. If not, run the following command to setup the mode:
```bash
docker swarm init
```
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
openssl rand -base64 20 | docker secret create keycloak_user -
openssl rand -base64 20 | docker secret create keycloak_password -
openssl rand -base64 20 | docker secret create db_password -
```
If random secrets are used, then you will have to connect a terminal to the `keycloak` container and retrieve the secrets from the following files in order to log into the keycloak admnistrative console:
```
/run/secrets/keycloak_user
/run/secrets/keycloak_password
```
To start the Docker services, run the following:
```bash
docker stack deploy --compose-file=docker-compose.yml keycloak_test
```
> Note that neither the `gateway` nor the `ping` service will come up until the appropriate clients are created within `keycloak`.

# Resources
* [Injecting credentials into a Docker container running a Spring Boot application](https://bmuschko.com/blog/docker-secret-spring-boot/)
* [Using Docker Secrets With Docker Compose](https://www.rockyourcode.com/using-docker-secrets-with-docker-compose/)
# Overview
This 
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
#!/bin/bash

for arg; do
  case $arg in
    --root_password)
    root_password="$2"
    shift 2
    ;;
  esac
done

echo "### Exporting PSQL root password ###"
export PSQL_ROOT_PASSWORD=${root_password}

echo "### Building Spring Boot Application ###"
mvn clean install

echo "### Starting Docker containers ###"
docker-compose up -d

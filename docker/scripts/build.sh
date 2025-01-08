#!/bin/bash
if [[ $# != 1 ]]; then
    echo "Usage: $0 environment"
    echo "* environment: local | cestel | prod"
else
  cd ..

  if [ "$1" == "local" ]; then
    PUBLIC_IP="127.0.0.1"
    ESCAPED_PUBLIC_IP="127\.0\.0\.1"
    API_PUBLIC_PORT="8080"
    API_INTERNAL_PORT="8080"
    BACKOFFICE_PORT="5001"
    MYSQL_VERSION="8.3.0"
    PROXY_HTTP_PORT="80"
    PROXY_HTTPS_PORT="443"
    DOMAIN_NAME="localhost"
    ESCAPED_DOMAIN_NAME="localhost/"
    JDBC_URL="localhost:3306"
    DATABASE_USER="root"
    DATABASE_PWD="root"
    SSL_ALIAS="cestel200"
    OPENVIDU_SERVER="http://localhost:4200/#/"
    SCHEMA="http"

  elif [ "$1" == "prod" ]; then
    PUBLIC_IP="82.223.205.118"
    ESCAPED_PUBLIC_IP="82\.223\.205\.118"
    API_PUBLIC_PORT="8080"
    API_INTERNAL_PORT="8080"
    BACKOFFICE_PORT="5001"
    MYSQL_VERSION="8.3.0"
    PROXY_HTTP_PORT="80"
    PROXY_HTTPS_PORT="443"
    DOMAIN_NAME="familiahuecas.es"
    ESCAPED_DOMAIN_NAME="familiahuecas\.es"
    JDBC_URL="database-familiahuecas"
    DATABASE_USER="root"
    DATABASE_PWD="CalleFalsa123"
    SCHEMA="https"
  else
    echo "Usage: $0 environment"
    echo "* environment: local | prod"
    exit
  fi

  API_NAME_PREFIX="backend"

  API_URL="$SCHEMA://$DOMAIN_NAME/$API_NAME_PREFIX"

  MYSQL_VERSION=$MYSQL_VERSION
  NGINX_VERSION="1.23.0"

  echo "====================Generating Files======================="
  sed "s|API_INTERNAL_PORT|$API_INTERNAL_PORT|g
      s|API_PUBLIC_PORT|$API_PUBLIC_PORT|g
      s|BACKOFFICE_PORT|$BACKOFFICE_PORT|g
      s|PROXY_HTTP_PORT|$PROXY_HTTP_PORT|g
      s|PROXY_HTTPS_PORT|$PROXY_HTTPS_PORT|g" ./scripts/template/docker-compose-template.yml > ./conf/docker-compose.yml

  sed "s|DOMAIN_NAME|$ESCAPED_DOMAIN_NAME|g
     s|BACKOFFICE_IP_URL|http://$ESCAPED_PUBLIC_IP|g
     s|API_IP_URL|http://$ESCAPED_PUBLIC_IP|g
     s|API_PUBLIC_PORT|$API_PUBLIC_PORT|g
     s|API_INTERNAL_PORT|$API_INTERNAL_PORT|g
     s|PROXY_HTTP_PORT|$PROXY_HTTP_PORT|g
     s|PROXY_HTTPS_PORT|$PROXY_HTTPS_PORT|g
     s|BACKOFFICE_PORT|$BACKOFFICE_PORT|g" ./scripts/template/nginx-template.conf > ./conf/nginx.conf

  sed "s|DOMAIN_NAME|$DOMAIN_NAME|g" ./scripts/template/backend-template.env > ./conf/backend.env

  sed "s|DOMAIN_NAME|$DOMAIN_NAME|g
      s|PROXY_HTTPS_PORT|$PROXY_HTTPS_PORT|g" ./scripts/template/backoffice-template.env > ./conf/backoffice.env

  sed "s|DATABASE_USER|$DATABASE_USER|g
      s|DATABASE_PWD|$DATABASE_PWD|g
      s|JDBC_URL|$JDBC_URL|g" ./scripts/template/application-template.properties > ./conf/application.properties

  echo "====================Building Database======================"
#  docker pull mysql:$MYSQL_VERSION
#  docker tag mysql:$MYSQL_VERSION database-familiahuecas
  echo "====================Building Proxy========================="
#  docker pull nginx:$NGINX_VERSION
#  docker tag nginx:$NGINX_VERSION proxy-familiahuecas
  echo "====================Building Backend======================="
  cd ..
#  docker build . --tag backend-familiahuecas

  echo "====================Building Backoffice===================="
  cd ../familiahuecas-frontend
  ./db.sh
  echo "====================Build Done============================="
fi

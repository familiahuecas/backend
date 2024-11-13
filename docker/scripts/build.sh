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

  elif [ "$1" == "cestel" ]; then
    PUBLIC_IP="10.0.3.200"
    ESCAPED_PUBLIC_IP="10\.0\.3\.200"
    API_PUBLIC_PORT="8080"
    API_INTERNAL_PORT="8443"
    BACKOFFICE_PORT="5001"
    MYSQL_VERSION="8.3.0"
    PROXY_HTTP_PORT="8081"
    PROXY_HTTPS_PORT="4438"
    DOMAIN_NAME="visercom.grupocestel.local"
    ESCAPED_DOMAIN_NAME="visercom\.grupocestel\.local"
    JDBC_URL="database-visercomcfg"
    DATABASE_USER="root"
    DATABASE_PWD="CalleFalsa123"
    SSL_ALIAS="cestel200"
    OPENVIDU_SERVER="https://visercom.grupocestel.local/#/"
    SCHEMA="https"

  elif [ "$1" == "prod" ]; then
    PUBLIC_IP="10.0.3.18"
    ESCAPED_PUBLIC_IP="10\.0\.3\.18"
    API_PUBLIC_PORT="8080"
    API_INTERNAL_PORT="8080"
    BACKOFFICE_PORT="5001"
    MYSQL_VERSION="8.3.0"
    PROXY_HTTP_PORT="80"
    PROXY_HTTPS_PORT="443"
    DOMAIN_NAME="visercom.cestel.es"
    ESCAPED_DOMAIN_NAME="visercom\.cestel\.es"
    JDBC_URL="database-visercomcfg"
    DATABASE_USER="root"
    DATABASE_PWD="CalleFalsa123"
    SSL_ALIAS="cestelAWS"
    OPENVIDU_SERVER="http://visercom.cestel.es/#/"
    SCHEMA="https"
  else
    echo "Usage: $0 environment"
    echo "* environment: local | cestel | prod"
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
      s|SSL_ALIAS|$SSL_ALIAS|g
      s|OPENVIDU_SERVER|$OPENVIDU_SERVER|g
      s|JDBC_URL|$JDBC_URL|g" ./scripts/template/application-template.properties > ./conf/application.properties

  echo "====================Building Database======================"
  docker pull mysql:$MYSQL_VERSION
  docker tag mysql:$MYSQL_VERSION database-visercomcfg
  echo "====================Building Proxy========================="
  docker pull nginx:$NGINX_VERSION
  docker tag nginx:$NGINX_VERSION proxy-visercomcfg
  echo "====================Building Backend======================="
  cd ..
  docker build . --tag backend-visercomcfg

  echo "====================Building Backoffice===================="
  cd ../visercomcfg-web
  ./db.sh
  echo "====================Build Done============================="
fi

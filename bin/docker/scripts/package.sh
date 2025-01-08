#!/bin/bash

# Definir la ruta para la carpeta output al mismo nivel que scripts
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
OUTPUT_DIR="$BASE_DIR/output"

echo "====================Creating Deploy Dir==================="
# Eliminar cualquier directorio output existente
rm -rf "$OUTPUT_DIR"
# Crear el directorio output y subdirectorios
mkdir -p "$OUTPUT_DIR/img"

echo "Saving database-familiahuecas"
docker save -o "$OUTPUT_DIR/img/database-familiahuecas.tar" database-familiahuecas
echo "Saving backoffice-familiahuecas"
docker save -o "$OUTPUT_DIR/img/backoffice-familiahuecas.tar" backoffice-familiahuecas
echo "Saving backend-familiahuecas"
docker save -o "$OUTPUT_DIR/img/backend-familiahuecas.tar" backend-familiahuecas
echo "Saving proxy-familiahuecas"
docker save -o "$OUTPUT_DIR/img/proxy-familiahuecas.tar" proxy-familiahuecas

echo "Copying specific scripts to output"
# Copiar solo los archivos específicos al directorio de salida
cp "$SCRIPT_DIR/instalar.sh" "$OUTPUT_DIR/"
cp "$SCRIPT_DIR/iniciar.sh" "$OUTPUT_DIR/"
cp "$SCRIPT_DIR/parar.sh" "$OUTPUT_DIR/"

echo "Copying conf to output"
cp -r "$BASE_DIR/conf/" "$OUTPUT_DIR/conf/"

echo "Copying certs to output"
#cp -r "$BASE_DIR/certs/" "$OUTPUT_DIR/certs/"

echo "====================Compressing Deploy Dir================"
rm -f ./instaladorfamiliahuecas.tar.gz
tar -czvf ./instaladorfamiliahuecas.tar.gz -C "$OUTPUT_DIR" .

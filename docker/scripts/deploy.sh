#!/bin/bash
if [[ $# != 1 ]]; then
    echo "Usage: $0 environment"
    echo "* environment: local | cestel | prod"
else
  ./clean.sh
  ./build.sh $1
  ./package.sh $1
fi

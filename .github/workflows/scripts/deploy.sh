#!/bin/bash

set -o errexit  # abort on nonzero exit status
set -o nounset  # abort on unbound variable
set -o pipefail # don't hide errors within pipes
set -o errtrace # Ensure the error trap handler is inherited

readonly ACTION_RUNNER_DEPLOYMENT_WORKING_DIR="/opt/actions-runner/deployment-$(date +'%Y-%m-%d_%T')"
readonly BACKUP_DIR="backup"

if [ -z "${FORCE_DEPLOY}" ]; then
  FORCE_DEPLOY="false"
fi

readonly RUNTIME_DIR RUNTIME_USER BRANCH_MVN_VERSION BRANCH_NAME DB_SECRET ENVIRONMENT ARTIFACT_ID ARTIFACTORY_URL GROUP_ID

function verifyParameter() {
  echo "Environment Variables are:"
  echo "Variable FORCE_DEPLOY: ${FORCE_DEPLOY}"

  if [[ ${ARTIFACTORY_URL} && ${ARTIFACTORY_URL-x} ]]; then
    echo "[INFO] ARTIFACTORY_URL: ${ARTIFACTORY_URL}"
  else
    echo "[ERROR] Missing mandatory environment variable. ARTIFACTORY_URL was empty"
    exit 1
  fi
  
  if [[ ${ARTIFACT_ID} && ${ARTIFACT_ID-x} ]]; then
    echo "[INFO] ARTIFACTORY_URL: ${ARTIFACT_ID}"
  else
    echo "[ERROR] Missing mandatory environment variable. ARTIFACT_ID was empty"
    exit 1
  fi
  
  if [[ ${GROUP_ID} && ${GROUP_ID-x} ]]; then
    echo "[INFO] ARTIFACTORY_URL: ${GROUP_ID}"
  else
    echo "[ERROR] Missing mandatory environment variable. GROUP_ID was empty"
    exit 1
  fi

  if [[ ${BRANCH_NAME} && ${BRANCH_NAME-x} ]]; then
    echo "[INFO] BRANCH_NAME: ${BRANCH_NAME}"
  else
    echo "[ERROR] Missing mandatory environment variable. BRANCH_NAME was empty"
    exit 1
  fi

  if [[ ${BRANCH_MVN_VERSION} && ${BRANCH_MVN_VERSION-x} ]]; then
    echo "[INFO] BRANCH_MVN_VERSION: ${BRANCH_MVN_VERSION}"
  else
    echo "[ERROR] Missing mandatory environment variable. BRANCH_MVN_VERSION was empty"
    exit 1
  fi

  if [[ ${RUNTIME_DIR} && ${RUNTIME_DIR-x} ]]; then
    echo "[INFO] RUNTIME_DIR: ${RUNTIME_DIR}"
  else
    echo "[ERROR] Missing mandatory environment variable. RUNTIME_DIR was empty"
    exit 1
  fi
  
  if [[ ${RUNTIME_USER} && ${RUNTIME_USER-x} ]]; then
    echo "[INFO] RUNTIME_DIR: ${RUNTIME_USER}"
  else
    echo "[ERROR] Missing mandatory environment variable. RUNTIME_USER was empty"
    exit 1
  fi
  
  if [[ ${DB_SECRET} && ${DB_SECRET-x} ]]; then
    echo "[INFO] DB_SECRET has been set"
  else
    echo "[ERROR] Missing mandatory environment variable. DB_SECRET was empty"
    exit 1
  fi
  
  if [[ ${ENVIRONMENT} && ${ENVIRONMENT-x} ]]; then
    echo "[INFO] TOMCAT_NAME: ${ENVIRONMENT}"
  else
    echo "[ERROR] Missing mandatory environment variable. ENVIRONMENT was empty"
    exit 1
  fi
}

function main() {
  verifyParameter

  echo "[INFO] All good. Starting deployment with user: $(whoami) and version ${BRANCH_MVN_VERSION}. Deployment enforced: ${FORCE_DEPLOY}"
  
  echo "[INFO] not implemented yet"
  #getArtefacts

  #deploy
}

main "$@"



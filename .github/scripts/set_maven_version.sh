#!/bin/bash

set -o pipefail    # don't hide errors within pipes
set -o nounset     # abort on unbound variable
set -o errexit     # abort on nonzero exit status

echo "### Will change the version in pom.xml files..."

branch=$(git rev-parse --abbrev-ref HEAD)
echo "### Current git branch is $branch"

# get current branch name
BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)
echo "### Branch is $BRANCH_NAME"
branch=${BRANCH_NAME//[\/]/_}
echo "### Branch is $branch"
echo "BRANCH_NAME=$branch" >> "$GITHUB_OUTPUT"

# get current version of the top level pom
# TODO: for future we want to use the default artifactory profile. here it is deactivated by intention, needs some research.
mvn help:evaluate -e -P '!artifactory' -Dexpression=project.version
MVN_VERSION=$( mvn help:evaluate -P '!artifactory' -Dexpression=project.version -q -DforceStdout )
echo "MVN_VERSION=$MVN_VERSION" >> "$GITHUB_OUTPUT"
echo "### Current version is: $MVN_VERSION"

# extract version suffix
suffix=$(echo "$MVN_VERSION" | cut -d \- -f 2)
echo "### Suffix is: $suffix"

prefix=$(echo "$MVN_VERSION" | cut -d \- -f 1)
echo "### Prefix is: $prefix"

# build new version
if [[ "$branch" != "master" ]] && [[ "$branch" != "main" ]]
then
    NEW_MAVEN_VERSION=${prefix}_${branch}-${suffix}
else
    NEW_MAVEN_VERSION=$MVN_VERSION
fi

echo "BRANCH_MVN_VERSION=$NEW_MAVEN_VERSION" >> "$GITHUB_OUTPUT"
echo "### Changed version in pom.xml files $NEW_MAVEN_VERSION"



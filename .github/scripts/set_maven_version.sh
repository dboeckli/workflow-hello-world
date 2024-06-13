#!/bin/bash

echo "### Will change the version in pom.xml files..."

branch=$(git rev-parse --abbrev-ref HEAD)
echo "### Current git branch is $branch"

if [ $branch == "main" ]; then
  echo "### Main branch is checked out. Aborting..."
  exit;
fi

branch=${branch//[\/]/_}

MAVEN_VERSION=$( mvn help:evaluate -P '!artifactory' -Dexpression=project.version -q -DforceStdout )
echo "### Current maven project version is: $MAVEN_VERSION"

prefix=$(echo $MAVEN_VERSION | cut -d \- -f 1)
echo "### Version Prefix is: $prefix"

suffix=$(echo $MAVEN_VERSION | cut -d \- -f 2)
echo "### Version suffix is: $suffix"

BRANCH_MAVEN_VERSION=${branch}_$prefix-$suffix

echo "### Branch maven version is: $BRANCH_MAVEN_VERSION"
echo "BRANCH_MAVEN_VERSION=$BRANCH_MAVEN_VERSION" >> "$GITHUB_OUTPUT"
echo "GIT_BRANCH=$GIT_BRANCH" >> "$GITHUB_OUTPUT"
echo "ORIGINAL_MAVEN_VERSION=$ORIGINAL_MAVEN_VERSION" >> "$GITHUB_OUTPUT"



#!/bin/bash

# Increment a version string using Semantic Versioning (SemVer) terminology.
# Source: https://github.com/fmahnke/shell-semver

# Parse command line options.

while getopts ":Mmp" Option
do
  case $Option in
    M ) major=true;;
    m ) minor=true;;
    p ) patch=true;;
  esac
done

shift $(($OPTIND - 1))

version=`cat version.txt`

# Build array from version string.

a=( ${version//./ } )

# Increment version numbers as requested.

if [ ! -z $major ]
then
  ((a[0]++))
  a[1]=0
  a[2]=0
fi

if [ ! -z $minor ]
then
  ((a[1]++))
  a[2]=0
fi

if [ ! -z $patch ] && ! [[ "${a[3]}" =~ ^M.*|^RC.* ]]
then
  ((a[2]++))
fi

NEW_VERSION="${a[0]}.${a[1]}.${a[2]}-SNAPSHOT"

echo "New version is: ${NEW_VERSION}"
echo -n $NEW_VERSION > version.txt

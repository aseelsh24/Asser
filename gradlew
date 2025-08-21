#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# @author: Andres Almiray
#

#
# Used to determine the path to the gradle-wrapper.jar file and the location of the gradle-wrapper.properties file
#
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

#
# The directory that this script is located in
#
APP_HOME=`cd "$(dirname "$0")" && pwd`

#
# The location of the gradle-wrapper.properties file
#
WRAPPER_PROPERTIES_PATH="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"

#
# The location of the gradle-wrapper.jar file, by default in the same folder as the script
#
WRAPPER_JAR_PATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

#
# The location of the gradle-wrapper.jar file, by default in the same folder as the script
#
DOT_GRADLE_USER_HOME_WRAPPER_JAR_PATH="$HOME/.gradle/wrapper/dists"

#
# The version of Gradle to use, by default the one defined in the gradle-wrapper.properties file
#
GRADLE_VERSION=

#
# The URL to download the Gradle distribution from, by default the one defined in the gradle-wrapper.properties file
#
GRADLE_DISTRIBUTION_URL=

#
# The checksum of the Gradle distribution, by default the one defined in the gradle-wrapper.properties file
#
GRADLE_DISTRIBUTION_SHA256_SUM=

#
# The path to the downloaded Gradle distribution
#
GRADLE_DISTRIBUTION_PATH=

#
# The name of the downloaded Gradle distribution
#
GRADLE_DISTRIBUTION_NAME=

#
# The path to the unzipped Gradle distribution
#
GRADLE_HOME=

#
# Download the Gradle distribution
#
download() {
    echo "Downloading $GRADLE_DISTRIBUTION_URL"
    if command -v "curl" > /dev/null 2>&1; then
        curl --progress-bar --location --output "$GRADLE_DISTRIBUTION_PATH" "$GRADLE_DISTRIBUTION_URL"
    elif command -v "wget" > /dev/null 2>&1; then
        wget --progress=bar:force --output-document="$GRADLE_DISTRIBUTION_PATH" "$GRADLE_DISTRIBUTION_URL"
    else
        echo "ERROR: Cannot download Gradle distribution. Please install curl or wget."
        exit 1
    fi
}

#
# Verify the checksum of the downloaded Gradle distribution
#
verify_checksum() {
    echo "Verifying checksum"
    if command -v "sha256sum" > /dev/null 2>&1; then
        LOCAL_CHECKSUM=$(sha256sum "$GRADLE_DISTRIBUTION_PATH" | awk '{print $1}')
        if [ "$LOCAL_CHECKSUM" != "$GRADLE_DISTRIBUTION_SHA256_SUM" ]; then
            echo "ERROR: Checksum mismatch. Expected: $GRADLE_DISTRIBUTION_SHA256_SUM, Got: $LOCAL_CHECKSUM"
            exit 1
        fi
    elif command -v "shasum" > /dev/null 2>&1; then
        LOCAL_CHECKSUM=$(shasum -a 256 "$GRADLE_DISTRIBUTION_PATH" | awk '{print $1}')
        if [ "$LOCAL_CHECKSUM" != "$GRADLE_DISTRIBUTION_SHA256_SUM" ]; then
            echo "ERROR: Checksum mismatch. Expected: $GRADLE_DISTRIBUTION_SHA256_SUM, Got: $LOCAL_CHECKSUM"
            exit 1
        fi
    else
        echo "WARNING: Cannot verify checksum. Please install sha256sum or shasum."
    fi
}

#
# Unzip the downloaded Gradle distribution
#
unzip_distribution() {
    echo "Unzipping $GRADLE_DISTRIBUTION_PATH to $(dirname "$GRADLE_HOME")"
    if command -v "unzip" > /dev/null 2>&1; then
        unzip -q -d "$(dirname "$GRADLE_HOME")" "$GRADLE_DISTRIBUTION_PATH"
    else
        echo "ERROR: Cannot unzip Gradle distribution. Please install unzip."
        exit 1
    fi
}

#
# Check if the gradle-wrapper.properties file exists
#
if [ ! -f "$WRAPPER_PROPERTIES_PATH" ]; then
    echo "ERROR: $WRAPPER_PROPERTIES_PATH not found."
    exit 1
fi

#
# Read the gradle-wrapper.properties file and extract the distributionUrl and distributionSha256Sum
#
while IFS='=' read -r key value
do
    case "$key" in
        "distributionUrl")
            GRADLE_DISTRIBUTION_URL=$(echo "$value" | tr -d '\r')
            ;;
        "distributionSha256Sum")
            GRADLE_DISTRIBUTION_SHA256_SUM=$(echo "$value" | tr -d '\r')
            ;;
    esac
done < "$WRAPPER_PROPERTIES_PATH"

#
# Extract the Gradle version and distribution name from the URL
#
GRADLE_VERSION=$(echo "$GRADLE_DISTRIBUTION_URL" | sed -n 's/.*gradle-\(.*\)-bin.zip/\1/p')
GRADLE_DISTRIBUTION_NAME="gradle-$GRADLE_VERSION-bin.zip"
GRADLE_DISTRIBUTION_PATH="$DOT_GRADLE_USER_HOME_WRAPPER_JAR_PATH/gradle-$GRADLE_VERSION-bin/$GRADLE_DISTRIBUTION_SHA256_SUM/$GRADLE_DISTRIBUTION_NAME"
GRADLE_HOME="$DOT_GRADLE_USER_HOME_WRAPPER_JAR_PATH/gradle-$GRADLE_VERSION-bin/$GRADLE_DISTRIBUTION_SHA256_SUM/gradle-$GRADLE_VERSION"

#
# Check if the Gradle distribution is already downloaded and verified
#
if [ ! -f "$GRADLE_DISTRIBUTION_PATH" ]; then
    mkdir -p "$(dirname "$GRADLE_DISTRIBUTION_PATH")"
    download
    if [ -n "$GRADLE_DISTRIBUTION_SHA256_SUM" ]; then
        verify_checksum
    fi
else
    echo "Gradle distribution already downloaded"
fi

#
# Check if the Gradle distribution is already unzipped
#
if [ ! -d "$GRADLE_HOME" ]; then
    mkdir -p "$(dirname "$GRADLE_HOME")"
    unzip_distribution
else
    echo "Gradle distribution already unzipped"
fi

#
# Set the GRADLE_HOME environment variable
#
export GRADLE_HOME

#
# Execute the Gradle command
#
"$GRADLE_HOME/bin/gradle" "$@"

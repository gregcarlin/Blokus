# Blokus
[![Build Status](https://travis-ci.com/gregthegeek/Blokus.svg?token=GxwRo7a5dp63Gq5XNxwx&branch=master)](https://travis-ci.com/gregthegeek/Blokus)

## Building and Running
1. Build with `mvn package`
2. Run with `./run`

## Command Line Arguments (All optional)
* --port [int] - specifies the port to run spark on
* --dbhost [string] - specifies the host address of the database to connect to
* --dbport [int] - specifies the port the database to connect to is running on
* --db [string] - specifies the name of the database to connect to
* --keystore [string] - specifies a path to a secure keystore for use with ssl
* --keystore-pass [string] - the password to the above specified keystore

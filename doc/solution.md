# Web Card

A simple application in Clojure for managing transactions.

## Installation

This project uses **Leiningen** to build and manage its dependencies. Before continuing, make sure you have [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html) version 1.6 or later installed. You can check your Java version by running the command:
```sh
    $ java -version
```
Then install Leiningen using the instructions on [Leiningen home page](https://leiningen.org/).

The code here presented has been developed and tested on Linux (Mint 19.1 Tessa), with Leiningen 2.9.1 on Java 11.0.3 version.


## Usage

After cloning the repository, first navigate to the root project directory and download all project dependencies.
```sh
    $ lein deps
```
After which you can run the server locally.
```sh
    $ lein run
```
The application runs on port 3003. So any POST requests must be made to *localhost:3003*.


## Routes

All HTTP endpoints accept and return JSON payloads as requests and responses.

Endpoint   | Bank Operation
---------- | ------------------
/transactions | Debit transaction to a given card

## Testing

You can run all project tests with the command:

   Test with the possibilities
```sh
    $ sh resources/request/report-test.sh
```
   Test with a transaction
```sh
    $ sh resources/request/post-request.sh
```
## Requests

   [Here](https://raw.githubusercontent.com/romuloslv/transaction-bank/master/resources/request/transactions.json) you have json example for all requests available.

## Response
```sh
    $ tail -f resources/output.json
```
And a report with all results will be automatically displayed.

## Example

![](https://i.imgur.com/6UAn9Yo.png)

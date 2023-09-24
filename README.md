# Bank Application

Author: Sander Kondratjev

The Bank Application is a Java project built with Spring Boot, using Java 17, Gradle, and PostgreSQL as the database.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Setup Instructions](#setup-instructions)
- [Supported REST Services](#supported-rest-services)
- [What Got Done From Homework](#what-got-done-from-homework)
- [What Got Not Done From Homework](#what-got-not-done-from-homework)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 17 installed on your local machine.
- Gradle build tool installed.
- PostgreSQL database server set up with the necessary configurations.

## Getting Started

To get started with the Bank Application, follow these steps:

### Setup Instructions

Before you begin, ensure you have met the following requirements:

- Java 17 installed on your local machine. You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or use [OpenJDK](https://openjdk.java.net/).
- Gradle build tool installed. You can download it from the [Gradle website](https://gradle.org/).
- PostgreSQL database server set up with the necessary configurations. You can download PostgreSQL from the [official website](https://www.postgresql.org/download/).

### Installation

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/SanderKondratjev/bank
   
2. Open the Project: Open the cloned project in your preferred Integrated Development Environment (IDE), such as IntelliJ IDEA, Eclipse, or Visual Studio Code.
3. Database configuration is in src/main/resources/application.properties file
4. Ensure PostgreSQL is Running:
Make sure you have PostgreSQL installed and running on your machine.
Verify that the specified database in the configuration exists.
5. Build the Project: Build the project using Gradle by running the following command in your terminal or IDE:
   ```bash
   ./gradlew build
   
6. To start the Bank Application, use the following Gradle command:
   ```bash
   ./gradlew bootRun
   
7. Usage:
   Access the Bank Application through your web browser at http://localhost:8080.
   Utilize the application to manage accounts, transactions, and balances.

### Supported REST Services
- Open Account: Create a new account with a custom name.
- Perform Transaction: Perform credit and debit transactions on existing accounts.
- Retrieve Account Balance: Retrieve the current balance for an account.
- Retrieve Account Statements: Get a history of transactions for an account.

### What Got Done From Homework

**1. Accounts**
- The system should support the opening of accounts.
- The system should allow users to provide a custom name when creating an account.
- The system should generate a valid IBAN for the account (ISO 13616) during the account creation process. (Author is not 100% sure about the IBAN generation and its validity)

**2. Transactions**
- The system should support transactions on any created accounts.
- The system should allow any credit transaction.
- Debit transactions should only be allowed on the account when the transaction amount does not exceed the account balance.
- The system should support transactions in EUR, GBP, and USD currencies; other currencies are not permitted.

**3. Balance**
- The system should provide a service for retrieving account balances.
- All balance changes should be logged. The log entries should include information about the balance state before and after the transaction, as well as identifying information about the transaction that modified the balance.
- The balance should never be allowed to go into negative values.

**4. Balance**
- The system should offer a service for retrieving account statements
  (transaction history).
- The account statement service should include the following data: ~~transaction identifier~~, ~~account~~, date (date-time with offset), amount, currency, and description.

### What Got Not Done From Homework
**1. Bonus Points**
- Those got not done because author was told not to invest too much time into the project (this project was done in 15H). Good chunk of time went on setting up, because author usually does not create new projects.

**2. Not All Code Is "Clean Code"**
- There is always improvements for making code more readable. From some method it would be better to extract methods to make more "pieces".

**3. Transaction Integration Test Fully Operating**
- Assertion is not working correctly, as balance is not updated. This is commented out currently, but the file is in place.


   

# Backend Coding Challenge

Please load this project in your favourite IDE and check if everything works executing:

```shell
./gradlew bootRun
```

# Coding Challenge Java/Kotlin

Welcome to our coding challenge, hope you enjoy it!

We need your help migrating a legacy social network service.

This service have users that can friend other users and offers an HTTP API to do so.

## Requirements

The solution can be implemented either in **Java 11** or **Kotlin**.

The use cases that need to be implemented are:

* Sign up
    * A new user requests to register to our service, providing its username and password.
    * Username must be unique, from 5 to 10 alphanumeric characters.
    * Password from 8 to 12 alphanumeric characters.
* Request friendship
    * A registered user requests friendship to another registered user.
    * A user cannot request friendship to himself or to a user that already has a pending request from him.
* Accept friendship
    * A registered user accepts a requested friendship.
    * Once accepted both users become friends forever and cannot request friendship again.
* Decline friendship
    * A registered user declines a requested friendship.
    * Once declined friendship can be requested again.
* Friends
    * List friends of a registered user.

Make sure [docker](https://www.docker.com/products/docker-desktop) is started and execute:

No database needed, you can persist everything in memory.

We expect from you to apply good practices and be proud of what you do. Good luck!

## Build & Run

```shell
./gradlew bootRun
```
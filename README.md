# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

My sequence diagram: [Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfJCIK-P8gK0eh8KVEB-rgeWKkwes+h-DsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspXI3r5d5LsKYoSm6MpymW7xKpgKrBhqbrarq+pBUYEBqDAaAQMwVpoiFvJhTZdk9n227eTZ-rMtMl7QEgABeKAcFGMZxoUoGYcgqYwOmACMBE5qoebzNBRYlvUPj1XqjUtbsdFNsODpJr6zpdhu7pbl5gorfSh5yCgz7xOel7XvtArhSua4BudO0be2emli54oZKoAGYM9IHVPphGGfMJGod8FFUfWQNad1a3wL12EwLh+GjP98XEaRIOXmDyEQ2h9GMd4fj+F4KDoDEcSJETJMub4WCiYKoH1A00gRvxEbtBG3Q9HJqgKQRoOIeDaNXFDulWX9fPUdjDbfWVLr2UJ1POfLp5uWoHlVXtt78v5YAnWd8H82gc6hatlTLpFT73fIsryuL6CJaqKVajqerGhjBtZTleUFdaOTFQuV0y5tFX9rt0P05Ks3xPNrXtSgsYKdpPUlGAaZOENSMjWNBZjJN0DTZH0eLY2DGXT9nb1Cdr6PT5JVaxwKDcMel561e2hG7Xi6m8KGQzBANB3S+D2dk9osvUrYDvZ90s1WBMBC798LJrDKfw3hWZz8XePMf4KLrv42Dihq-FojAADiSoaLTM8M6frMc-YSq827EuC4nH6jy8Yy21jr-S+t5f2TROfHMzkgEXxViSdWNd-ba11t-du-t7wRXFBbQeVtYrf3tslLaaUXbfw9gKL2MBCq+1LmHDa3Zewh2rtfCOFFC6x3jvGKGS9k6p3TtmfkWcJrFjzgqAuUBmqtQbLjMhgcK6W2AFAqkmt6SMh1n2YBagMQIP3Eg+o5sz5KirsPaGz16jHxyEo1Qk8ECAVHmXGoLxlgPxzAWBo4xbEoAAJLSALANcIwRAhrBcL4ueMBOjzwkovSookcJr1GDYi+9jHFKlce4zx3iYC+JcP4wJm9PD4wCBwAA7G4JwKAnAxAjMEOAXEABs8AJyGCUTAIoy86YLwZq0Do99H5I2-jRS4BEnEADlIKSyCYmEWsIxbPwFsDC4PSlT9NRpMr6FjxEwEOuiJRGI4DVKURAtWocZHG3pDAeRcDxmGz9mo66mjK7aGtq7fWSEsHqhwc7fU+CODZUIflYhPssBiP-rZWWwcoG0LqvQwRC1GGdTfqwvqg1hpcPzDwqa-DQVCKLqI2RAc-l2SufIaRMgMVjk2UqDEfSlSqLCl3Cut0lE6P+dVEZX56gbKPCgYxpjzGjJCR2KxMBYlzHiUDDxXihkYWhmE1eiM+UuLcYKxJS0GKZO3pYBuDlNikyQAkMAyq+wQDVQAKQgOKLRcwYjJFAGqepydGnBOacyGSPQnFPzuRM2iBFsAIGAMqqAcAIAOSgF074TiBWv2Fg8Tln9OmDLdR6r1Pq-UBpBEGmVv9FlYtlgAK0NWgNZBrxRbJQISVWnlq57I7qOQ5TJjnOtOWQyllzJE3MwUlR5qVnm3Mou7N5ntPkkJ+Riyx5UqFAr+bVARqKIUJxYaE5e7C4W5gRYWXhpYZoooWiI5a-byEAJxVI3Z+L9mjnkWspN5KTZCg0Sg41KAPTNo1LUttSaCG5R7d8s5pU01ByHaHWhSjen5RFL4TgE7mELyTjCiJnD52A0RXwpxz7mAADNANoqbLemA2zWowHdZ6ygcboCVjNDWcMhRfncrsjSoedLLH+kDIRsMSFgNdVA2Kmd-UMxztGgunOS76gmkIzAWs9Z5VvtWksijuK92XVqH4LQqziVJrWEFNY2HY2+ugKezu57JTYFkzU7RlHBzv3DTAXN2bwH-jMQszl1HZ4iq5eKhG69hOKoJl4T16rNVuflIgYMsBgDYHdYQPIBQ6lXxHaWRmzNWbs16MYN+DK4TWMstZpZIBuB4HWelqA2zi26NLTA5ZWXAwIBUSJzTy4e590MDusrV1KWVf7tFLctbz0Nb03MD0pGvKUMqt+hL-ofN4HZVZr8NmktQunWwiVTnN5AA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

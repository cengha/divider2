# DIVIDER GAME APPLICATION


### How to play the game?

1. Application uses "local" profile as default
2. Download project to your local computer, open terminal in app's root directory
3. The build command #"mvn clean package"
4. To run application you can type #"mvn spring-boot:run"
5. Now open "localhost:7091/index" from your browser
6. When you enter an username and click "start gaming" button you will open a web socket and start waiting for an opponent to join
7. Open an another browser. (i.e chrome, chrome incognito etc). When you join the game, game will be started
with a random number which is lower than 1000 and oppononents will be  notified.
8.You can simulate game anytime by clicking simulate checkbox

### TODO List

* Increase test coverage and fix web socket controller tests (At the moment client can connect but can't subscribe and receive messages)
* Spring Cloud application properties implementation
* Web Security and WebSocket Security implementations
* Spring actuator endpoints implementation

### Personal Notes

> There is also first version of this [application](https://github.com/cengha/divider) . It also includes Web Security and Websocket Security implementations.
> The main difference between two versions is architectural. The first version mix Rest and Websocket endpoints while the second version focuses only Websocket endpoints.
> Between them i prefer second version.

### Technology Stack

##### Backend : Java, Spring Boot, In Memory H2 db, Lombok, Thymeleaf, JODA, JUnit, Mockito
##### Frontend: Html, Jquery, Bootstrap Stomp client, SockJs

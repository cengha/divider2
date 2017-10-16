# DIVIDER GAME APPLICATION


### How to play the game?

1. Application uses **local** profile as default and runs on port **7091**
2. Download project to your local computer, open terminal in root directory.
3. To build project type **mvn clean package** in terminal.
4. To run application you can type **mvn spring-boot:run**.
5. Now open **localhost:7091/index** from your browser.
6. When you enter an username and click **start gaming** button you will open a web socket connection and start waiting for an opponent to join.
7. Open another browser. (i.e chrome, chrome incognito etc). When you join the game as second user, game will be started
with a random starter number which is lower than **1000** (configurable in properties file) and oppononents will be notified both.
8. You can simulate game anytime by checking/unchecking **simulate** checkbox.

### TODO List

* Issue1: WebSocketController test can open web socket connection, send messages through channel but can't receive any.
* Issue2: While running controller and non controller tests together at once, WebSocketControllerTest can not open ws connection due to port conflict.
* Increase test coverage and fix web socket controller tests.
* Spring Cloud application properties implementation.
* Web Security and WebSocket Security implementations.
* Spring actuator endpoints implementation.
* ActiveMQ or RabbitMQ implementation.

### Personal Notes

> There is also first version of this [game](https://github.com/cengha/divider). It also includes Web Security and Websocket Security implementations.
> The main difference between two versions is architectural. The first version mix Rest and Websocket endpoints while the second version focuses only Websocket endpoints.
> Between them I prefer second version.

### Technology Stack

* **Backend :** Java, Spring Boot, In Memory H2 db, Lombok, Thymeleaf, JODA, JUnit, Mockito
* **Frontend:** Html, Jquery, Bootstrap, Stomp client, SockJs

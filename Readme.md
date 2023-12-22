Getting Started:
This repository contains a Java backend application for a Chess Matchmaking Microservice, designed as a proof of concept (POC). The microservice enables players to register for a chess game, experience a waiting screen during opponent search, and receive match results within 30 seconds.
Components
	Player Registration Endpoint:
●	REST endpoint for player registration.
	Player Entity:
●	Represents player details (playerId, skillLevel).
	Player Service:
●	Registers players and notifies the matchmaking system.
	Matchmaking Consumer:
●	Listens to the matchmaking queue and triggers matchmaking.
	Matchmaking Service:
●	Implements the matchmaking algorithm and notifies players.
	Result Consumer:
●	Listens to the result queue and processes match results.
	RabbitMQ Configuration:
●	Configuration for RabbitMQ queues.
Installation
1.	Clone the Repository:
git clone https://github.com/your-username/chess-matchmaking-microservice.git
2.	Build and Run:
mvn clean install
java -jar target/chess-matchmaking-microservice.jar
Usage
	Player Registration:
●	Use the /players/register endpoint to register players.
	Waiting Screen:
●	Players will experience a waiting screen while the system searches for opponents.
	Matchmaking Algorithm:
●	Matchmaking is triggered automatically based on player registrations. System will search for the opponent based on the skill level of players.
	Result Notification:
●	Players receive match results through the result queue.
Configuration
Adjust the application configuration in application.properties.







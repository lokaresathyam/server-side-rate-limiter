# Server-Side IP Address Based Rate Limiter
This project is an implementation of a server-side rate limiter for a RESTful API. It provides a flexible and configurable rate limiting mechanism that can be used to prevent API abuse and maintain application stability.

## Technologies Used
- Java
- Spring Boot
- Redis

## Installation
To run this project, you need to have Redis installed on your system. You can run Redis using the following command:

```sh
docker run -d --name redis-container -p 6379:6379 redis
```

Then, you can clone this repository and run the following command to start the application:

```sh
./mvnw spring-boot:run
```


## Configuration
The rate limiter can be configured by modifying the RateLimiterConfig class. It provides the following parameters:

- type: The type of rate limiter to use. The supported types are LEAKY_BUCKET and TOKEN_BUCKET.
- capacity: The maximum number of tokens that can be stored in the bucket.
- refillRate: The rate at which the bucket is refilled with new tokens.
- timeInterval: The time interval over which the tokens are refilled.


## Authors
- Sathyam Lokare

## References
- Redis documentation: https://redis.io/documentation

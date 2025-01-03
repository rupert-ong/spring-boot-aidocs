# Spring Boot References Docs with Spring AI and GPT-4

## Overview

`sbdocs` is a Spring Boot Shell application that integrates with OpenAI models using Spring AI. It also utilizes a
PostgreSQL database with the pgvector extension for vector storage.

## Prerequisites

- GraalVM Java 17 or higher
- Maven 3.3.9 or higher
- Docker and Docker Compose (Docker Desktop)
- [OpenAI API key](https://openai.com/api/)

## Setup

### Environment Variables

Ensure the following environment variables are set:

- `OPENAI_API_KEY`: Your OpenAI API key.

If running this in the IDE, you can set the environment variables in the run configuration.

### Docker Compose

The application uses Docker Compose to manage the PostgreSQL database with pgvector extension. The configuration is
defined in `compose.yaml`.

To start the services, run:

```sh
docker-compose -f compose.yaml up -d
```

Alternatively, you can start the pg vector service using Docker Desktop.

### Spring Boot Reference PDF

Ensure you download the Spring Boot Reference PDF and place it in the `src/main/resources/docs` directory as
`spring-boot-reference.pdf`.

### GraalVM Native Image

You must point your `%JAVA_HOME%` to GraalVM 17 or higher. You must also update your PATH to include the GraalVM bin
directory.

For Windows Platform, you must install [Visual Studio 2022 Build Tools](https://visualstudio.microsoft.com/downloads/)
and follow
the [setup instructions](https://medium.com/graalvm/using-graalvm-and-native-image-on-windows-10-9954dc071311)

## Compiling and Running the Native Image

To compile the application, use the following command:

```sh
./mvnw -Pnative native:compile -DskipTests
```

Be sure to include the `OPENAI_API_KEY` environment variable.

### Using the Native Image

Once the native image is built, you can run it directly:

```sh
target/aidocs
```

When running the shell application, you can use the following command format to query Spring augmented content:

```shell
q "What is Spring Boot?"
```


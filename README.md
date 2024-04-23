## Spring io 2023 Hands-on (Bootiful)

    -[x] Demo fast approach to create CRUD app
    -[x] Postgres database connection with test containers
    -[x] Docker-compose integration
    -[x] Init spring project with spring CLI 

### Build production images

**Using Maven**

    mvn spring-boot:build-image

**Using Gradle**
    
    gradle client:bootBuildImage # build client image
    gradle service:bootBuildImage # build service image

**Using native compile**

     gradle client:nativeCompile # client
     gradle service:nativeCompile # service

### Environment variables

    POSTGRES_USER=bp
    POSTGRES_DB=bp
    POSTGRES_PASSWORD=bp

    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bp
    SPRING_DATASOURCE_USERNAME=bp
    SPRING_DATASOURCE_PASSWORD=bp

### links

1. [Spring I/O 2023 - Keynote](https://www.youtube.com/watch?v=IgmeFeTU1a4)
2. [Use CLI to generate project template](https://dev.to/akojimsg/use-cli-to-generate-your-spring-template-32fh)
3. [micrometer.io/](https://micrometer.io/)
4. [buildpacks.io/](https://buildpacks.io/)
5. [www.graalvm.org](https://www.graalvm.org/)
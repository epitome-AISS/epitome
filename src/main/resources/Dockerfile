# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM hub.docker.com/debian

# Copy local code to the container image.
WORKDIR /app
COPY ailab-1.0.0.jar service.jar

# default dev
ENV SPRING_PROFILES_ACTIVE="local"
ENV JVM_OPTS=""

RUN echo $JVM_OPTS
ENTRYPOINT exec java $JVM_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
-jar service.jar
FROM openjdk:17

COPY . ./app

# Remove the client folder
RUN rm -rf ./app/client/node_modules && rm -rf ·/app/client/build

# React variables need to be exported before build time
ARG REACT_APP_REDUX_PERSIST_KEY
RUN export REACT_APP_REDUX_PERSIST_KEY=${REACT_APP_REDUX_PERSIST_KEY}
ARG REACT_APP_CURRENCY
RUN export REACT_APP_CURRENCY=${REACT_APP_CURRENCY}

# Run mvn package to install fresh dependencies
RUN cd ./app && bash ./mvnw clean package -DskipTests || true

# Copy the jar
RUN cp ./app/target/app-0.0.0.jar app.jar

# Remove the ./app dir with the source code
RUN rm -rf ./app

# Specify the run command 
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "app.jar"]


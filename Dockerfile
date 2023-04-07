FROM openjdk:20
COPY . /app
WORKDIR /app
RUN javac src/Main.java
CMD ["java", "src/Main"]
# Car Rental API

### <a id="docker-setup"></a>Docker Setup
Below are the steps to run the project with Docker:
1. Install Docker
2. Every time you run this project
   1. From the root of the repository run: `git pull`
   2. From the root of the repository run: `docker-compose down && ./gradlew clean bootJar && docker-compose up --build -d`
3. After finish working for the day run: `docker-compose down`

#### Front-End Developers
If you are a Front-End developer and is just consuming the API, step 2 of the [Docker Setup](#docker-setup) will make sure you always have the latest code available in the remote.
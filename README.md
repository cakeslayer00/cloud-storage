# cloud-storage

Pet-project that is a replica of cloud data storage such as Google Drive etc. Developed with the ultimate goal of understanding Spring Boot, Spring Security, working with NoSQL (Redis) and use of Testcontainers for testing.

## Table of Contents
- [Technologies](#technologies)
- [How to Install and Run](#how-to-install-and-run)  
- [Credits](#credits)

## Technologies
- Spring Boot
- Spring Security
- Spring Data JPA
- Liquibase
- Redis
- PostgreSQL
- Testcontainers

## How to Install and Run
1. Clone the repository using any method convenient for you
2. Enter the directory: `cd cloud-storage`
3. Create a `.env` file:
   - You can create one by using .env.example as guide
4. Start the application: `docker compose up --build`
   - Assumes you have Docker installed
5. For the complete experience, use the frontend from: https://github.com/zhukovsd/cloud-storage-frontend/
   - Check that repository for frontend setup instructions
6. Enjoy!

## Credits
This project was implemented based on requirements provided by Sergey Zhukov's roadmap:
https://zhukovsd.github.io/java-backend-learning-course/projects/

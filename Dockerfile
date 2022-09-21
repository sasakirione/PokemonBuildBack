FROM amazoncorretto:17

ARG _DB_PATH
ARG _DB_USER
ARG _DB_PASS
ARG _AUTH0_ISSUER
ARG _AUTH0_AUDIENCE
ARG _AUTH0_SECRET

ENV PORT 8080
ENV DB_PATH ${_DB_PATH}
ENV DB_USER ${_DB_USER}
ENV DB_PASS ${_DB_PASS}

ENV AUTH0_ISSUER ${_AUTH0_ISSUER}
ENV AUTH0_AUDIENCE ${_AUTH0_AUDIENCE}
ENV AUTH0_SECRET ${_AUTH0_SECRET}

COPY build/libs/PokemonBuild-0.2.0-all.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
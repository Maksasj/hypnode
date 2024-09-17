# build
FROM ubuntu:latest

WORKDIR /source
COPY ./ ./

RUN apt update
RUN apt install -y python3 gcc golang-go
RUN python3 build.py --no-run

EXPOSE 8170

# run, todo ubunut is to heavy
FROM ubuntu:latest
WORKDIR /app
COPY --from=0 ./source/target ./

ENTRYPOINT ["./hne"]
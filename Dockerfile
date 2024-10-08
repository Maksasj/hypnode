# build
FROM ubuntu:latest

WORKDIR /source
COPY ./ ./

RUN apt update
RUN apt install -y python3 gcc golang-go
RUN python3 build.py --no-run

EXPOSE 8170

# run with alpine + glibc
FROM frolvlad/alpine-glibc
WORKDIR /app
COPY --from=0 ./source/target ./

CMD ./hne
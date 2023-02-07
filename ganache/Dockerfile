FROM ubuntu:22.10

#Install curl
RUN apt-get update
RUN apt-get install sudo -y

RUN apt-get upgrade -y

RUN apt-get update 
RUN apt-get install curl -y

RUN curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -

#Install node
RUN apt-get install -y nodejs

RUN apt-get upgrade -y

RUN apt-get update
RUN apt-get install npm -y
RUN npm install -g ganache-cli 
#RUN npm audit fix
RUN export NODE_OPTIONS=--openssl-legacy-provider
#RUN ganache-cli -p 8546 chainId 777
#RUN npm update
#RUN mkdir /nftvehicle
#RUN mkdir /nftvehicle/api-gateway
#EXPOSE 80
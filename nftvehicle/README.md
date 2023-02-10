# nftvehicle Microservice
## General description
  This will contain all services offered by nftvehicle. Initially, it is a microservice containing all service of the nftvehicle. The main idea is to share a solution using a microservice architecture.

## Pre-requirements
  It is required create a network in docker as follows:

    $ docker network create --gateway 172.18.1.1 --subnet 172.18.1.0/24 nftvehicleNetwork
 
## Install process
  Using the Dockerfile located in this repository, execute the following steps:

    * Download this repository in a path in your computer, so-called PATHL
    * Download ubuntu image:
      $ docker pull ubuntu
    
    * Build the ubuntu image in a repository:
      $ docker build -t nftvehicle  <PATHL>

    * Run ubuntu: 
      $ docker run -it --network nftvehicleNetwork -p 6000:6000 -v <PATHL>:/nftvehicle  nftvehicle

    * You must intro to the ubuntu instance and install npm:
      $ sudo apt install nodejs npm
    
    * Then, go to the instance path:
      $ cd /nftvehicle

    * Then, Update npm:
      $ npm update
    
    * Execute web server:
      $ ./startApp


  ----
   -api-gateway/
   -ganache/
   -manufacturer

# Blockchain instance for API NFTVehicleM
## General description
  This will install the blockchain instance, which will store the transactions of NFT-Vehicle. We have used Ganache application (part of the Truffle Suite). We have not modified anything of Ganache.

## Pre-requirements
  It is required to have created a network in docker as follows:

    $ docker network create --gateway 172.18.1.1 --subnet 172.18.1.0/24 nftvehicleNetwork
 
## Install process
  Using the Dockerfile located in this repository, execute the following steps:

    * Download this repository in a path in your computer, so-called PATHL
    * Download ubuntu image:
      $ docker pull ubuntu
    
    * Build the ubuntu image in a repository:
      $ docker build -t ganacheimage  <PATHL>

    * Run ubuntu: 
      $ docker run -it --network nftvehicleNetwork -p 8546:8546 -v <PATHL>:/nftvehicle/ganache  ganacheimage

    * You must intro to the ubuntu instance and install npm:
      $ sudo apt install nodejs npm
    
    * Then, go to the ubuntu instance path:
      $ cd /nftvehicle/ganache

    * Then, Update npm:
      $ npm update
    
    * Execute the following command:
      $ ./startApp

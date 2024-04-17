# Blockchain instance for API NFTVehicleM
## General description
  This will install the blockchain instance, which will store the transactions of NFT-Vehicle. We have used Ganache application (part of the Truffle Suite). We have not modified anything of Ganache.

## Pre-requirements
  It is required to have created a network in docker as follows:

    $ docker network create --gateway 172.18.1.1 --subnet 172.18.1.0/24 nftvehicleNetwork
  
  To see the created network:    

    $ docker network inspect nftvehicleNetwork
 
## Install process
  Using the Dockerfile located in this repository, execute the following steps:

    * Download this repository in a path in your computer, so-called PATHLBLOCKCHAIN
    * Download ubuntu image:
      $ docker pull ubuntu:23.10
    
    * If you are running docker in MacOS, disable line 22 and enable line 25 in the Dockerfile in PATHLBLOCKCHAIN

    * Build the ubuntu image in a repository:
      $ docker build -t ganacheimage  <PATHLBLOCKCHAIN>

    * Run ubuntu: 
      $ docker run -it --network nftvehicleNetwork -p 8546:8546 -v <PATHLBLOCKCHAIN>:/nftvehicle/ganache  ganacheimage

    * Then, go to the ubuntu instance path:
      $ cd /nftvehicle/ganache

    * Update npm:
      $ npm update

    * Change permissions:
      $ chmod 544 startApp

    * Execute the following command:
      $ ./startApp

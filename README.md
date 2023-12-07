# NFTVehicleM: Architecture for accessing the NFT-Vehicle
## General description
  This is the source code of an architecture that will contain all services for accessing the NFT-Vehicle. This is a project containing mainly two parts:  
  - An API-Gateway service, we have called NFTVehicleM; and 
  - A blockchain server access. This uses Ganache program (application part of Truffle Suite). 

## Folders
  It is composed by the following folders:

  - ganache/
  - nftvehicle/

## Install
  To install each part execute the following instructions: 
  
  1. Firstly install docker, a guide for that visit https://docs.docker.com/engine/install/
  2. Install Ganache: follow the instructions explained in file README.md within folder ganache/.
  3. Install NFTVehicleM, for that follow the instructions explained in file README.md within folder nftvehicle/.

## Example
  To consume the services provided by NFTVehicleM you can use Postman application (https://www.postman.com/). The following figure illustrate an example of sending the request for tokenizing a vehilce consuming the create service:
  ![](create.png)
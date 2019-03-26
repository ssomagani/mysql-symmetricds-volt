# Setting up replication from MySQL to VoltDB using SymmetricDS

# Worked best when starting from the provided sample. Follow the Getting Started instructions on SymmetricDS's pages.

## Setup MySQL
* 

## Setup MySQL source 
* Didn't work with port 33060 (what I read online as default) so removed the port from the jdbc.url
* Didn't work with root with no password so created a new user with password

## Setup VoltDB source
* Didn't work When trying to setup a target to VoltDB, SymmetricDS fails on schema creation (specifically the table - SYM_INCOMING_ERROR).


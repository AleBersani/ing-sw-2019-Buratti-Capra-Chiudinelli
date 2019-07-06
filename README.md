# Prova Finale Ingegneria del Software 2019
## Gruppo AM25

- ###   10577247    Roberto Buratti ([@Furcanzo](https://github.com/Furcanzo))<br>roberto.buratti@mail.polimi.it
- ###   10490184    Paolo Capra ([@PaoloC96](https://github.com/PaoloC96))<br>paolo2.capra@mail.polimi.it
- ###   10534129    Andrea Chiudinelli ([@Kiudi](https://github.com/Kiudi))<br>andrea.chiudinelli@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![RED](https://placehold.it/15/f03c15/f03c15)](#)|
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

# How to use the jar (Java 8)

Below are reported the procedures step by step to launch the jar.
Remember to launch the Server before the Client.

Server:
1) Go on artifacts folder and open the Server_jar directory. Now you can open the command shell in this directory.
2) When it's open print "java -jar ./Server.jar".
3) Provide the number of the port. ex.5000
4) Add the configuration file of the game by printing "./config.json".

ex. C:...\artifacts\Server_jar>java -jar ./Server_jar 5000 ./config.json

We also provide a .bat file located in the Server_jar folder that launch automatically the Server on port 5000.

Client:
1) Go on artifacts folder and open the Client_jar directory. Now you can oper the command shell in this directory.
2) When it's open print "java -jar ./Client.jar".
3) Provide the IP address of the server followed by the port, separate them with ":". ex 192.168.0.3:5000 or 127.0.0.1:5000

ex. C:...\artifacts\Client_jar>java -jar ./Client.jar 192.168.0.3:5000

We also provide a .bat file located in the Client_jar folder that launch automatically the Client with the IP local address 127.0.0.1 and the port number 5000.

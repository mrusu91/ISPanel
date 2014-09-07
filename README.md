ISPanel
=======

An UNIX firewall / router manager written in Java. 

This project is composed of two apps. One is the server app which should run on server,
and the other one is an Android app which gives you the ability to manage the server.

This is just a microscopic chunk of a project started during TechAbility event.

// Features:
   - Gives you server status (hostname, cpu, mem, etc)
   - Gives you LAN clients status (using arp-scan)
   - Save logs in ISPanel.log file in app root directory

To give you status of clients you should create a clients.all file that will contain a client per line

Line format(\t means a TAB space):

IP\tMAC\tName

EX:

192.168.0.100  FF:FF:FF:FF:FF:FF Barack Obama

192.168.0.105  AA:AA:AA:AA:AA:AA George Bush

// TODO: a lot :)  couple of ideas:
   - Implement Java wrappers to networking tools like iptables, tc, iproute, dhcpd, etc
   - Define a layer of supported features on top of those tools
   - Implement an API for controlling those features
   - Implement an User Interface on server that use the API
   - Find a secure communication protocol between server and Android app (HTTPS maybe?)
   - Add the ability to manage server features on Android app usng the API layer





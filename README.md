#### Operating Systems Project 2016

### Project Overview
Multi-threaded TCP Server Application which allows multiple	customers to update	their bank accounts	and	send payments.	
The module is taught to undergraduate students at [GMIT](http://www.gmit.ie) in the Department of Computer Science and Applied Physics.
The lecturer is Martin Hynes.

The project was guided by the following excerpt from the project instructions:
>Your	project	is	to	write	a	Multi-threaded	TCP	Server	Application	which	allows	multiple	customers	to	update	their	bank	accounts	and	send	payments	to	other	bank	accounts.	The	service	should	allow	the	users	to:	
1. Register	with	the	system		• Name	• Address	• Bank	A/C	Number	• Username		• Password	2. Log-in	to	the	banking	system	from	the	client	application	to	the	server	application.	3. Change	customer	details.	4. Make	Lodgements	to	their	Bank	Account.	5. Make	Withdrawal	from	their	Bank	Account	(Note:	Each	User	has	a	credit	limit	of	€1000).	6. View	the	last	ten	transactions	on	their	bank	account


### How to run the application
The application can be run from two instances of eclipse using provider and requester java classes or by connecting requester to provider running on windows server IP 35.167.134.104.
When starting Requester, user asked to what IP to connect.

To what IP would you like to connect? 
Press (1) 127.0.0.1 
Press (2) 35.167.134.104

If connection successful, the Login menu appears:

Connected to host in port 2004
Welcome to login menu.
Please choose: 
(1) Register 
(2) Login
(q) Exit

If login successful user menu appears:

Welcome to user menu:
Please choice: 
(1) New account 
(2) Transaction
(3) View Account Information
(q) Exit


### Architecture
The	code written in Java.


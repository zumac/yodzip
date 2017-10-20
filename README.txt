* Overview

This is simple web application (Servlet, HTML/CSS/JS) to demonstrate use of Yodlee Aggregation APIs.
The primary use cases and APIs used are

1. Cobrand Login - (YodleeSampleApp.java - getCobrandSession)
2. User Login  - (YodleeSampleApp.java - userLogin)
3. Link Account - using FastLink (how to launch FastLink in iframe) - YodleeSampleApp.java - getFastLinkToken
4. Get list of user's linked accounts to show account summary information - YodleeSampleApp.java - getUserAccounts
5. Get list of transactions for specific selected account - YodleeSampleApp.java - getTransactions

* Pre-requisites for running Sample Application

Sample application is built with minimal dependencies. Required for running sample application are

1. Tomcat 7 Application Server
2. Java 7
3. Eclipse or Similar IDE - optional, but recommended for understanding use of Yodlee APIs

* Third Party Dependencies for Sample Application


1. JQuery - in HTML files (index.html, accounts.html)
2. Bootstrap - in HTML files (index.html, accounts.html)

(*These are dependencies for demo sample application, and not for use of Yodlee APIs)


* Sample App Structure 

Sample App is designed to be minimal and simple to read. Primary objective of this application is to demonstrate use of Yodlee APIs
Sample App is structured as standard Web Application Structure (expanded WAR file)

YodleeSampleApp
	- src
		- YodleeSampleApp.java - Java Servlet interacting with Yodlee API. All Yodlee API call usage are inside this source file.
		- config.properties - resource file to configure Yodlee API URL and Cobrand Credentials.
		- beans - supporting POJO objects
		- parser - supporting JSON parsers
		- util
			- HTTP.java - helper methods to make HTTP calls.
	- WebContent
		- index.html - initializes with Cobrand Login check.
		- accounts.html - lists user accounts, details and transactions.
	- pom.xml
	- README.txt
	

	

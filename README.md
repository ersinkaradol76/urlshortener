![](./src/main/resources/icon/favicon.ico) Assignment: RocketML URL Shortener Project

This project is to build a simple URL shortener service that will accept a URL as an argument over a REST API and is to
return a shortened URL as a result.


## How to Start Project
```bash
install maven from <link> : https://maven.apache.org/install.html
open urlshortener.zip file
cd urlshortener
mvn spring-boot:run

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8081

OR

open urlshortener.zip file
in Eclipse IDE, use menu item "File->Open Projects from File System", and open project
wait Maven dependencies being downloaded 
run StartWebApplication.java

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8081
<link> : http://localhost:8081/file

```


`<link>` : <http://localhost:8081>

## Problem Description

1. Build a simple URL shortener service that will accept a URL as an argument over a REST API and
return a shortened URL as a result.
2. The URL and shortened URL should be stored in memory by application. As a bonus instead of in memory, store these things in a text file.
3. If I again ask for the same URL, it should give me the same URL as it gave before instead of
generating a new one.
4. As a bonus put this application in a Docker image by writing a Dockerfile and provide the docker
image link along with the source code link.

## Solution Description

Project is developed in Java Spring Boot, and for front-end used Thymeleaf.

1- In Memory Part

<link> : http://localhost:8081

	After entering the url, system first validates the url. If it is a valid url, then clears the url so that the similar urls can be stored only once.
	The url and the shortened url are stored in two Hash Map such as MapA<url, shorturl>, MapB<shorturl, url>. MapA is used to control that if the url 
	is stored before, second one is to used to get the url for the shorturl. First MapA is checked if the url was stored before. 
	If it was, the shorturl is returned. If it is not, a key is generated and the shorturl and url are srored in MapA and MapB.
	The key is generated by using a alphanumeric character array and randomly taking the character from this set for the specified times. 
	A hashing library such as Google MurmurHash could be used for generating unique key and use only one Hash Map, but I prefered two develop it by myself.
	
1- In File Part

<link> : http://localhost:8081/file

	The process is the same as the memory part. The only difference is the url and the shorturl are stored in two files similar to the maps.
	While writing to the files teh url and shorturl are separated by "=====" string.	


## Usage

1- Go to the site <link> : http://localhost:8081

2- Enter a url to be shortened and click on the **Shorten** button

3- The system will validate the url, if it valid controls if it was added into the map before. If it not, generates the short url and stores in the maps. 
If it stored before, returns a message explaning this situation.

4- Go to the site <link> : http://localhost:8081/file

5- Enter a url to be shortened and click on the **Shorten** button

6- The system will validate the url, if it valid controls if it was added into the file before. If it not, generates the short url and stores in the files. 
If it stored before, returns a message explaning this situation.


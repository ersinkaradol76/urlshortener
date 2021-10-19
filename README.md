# ![](./src/main/resources/icon/favicon.ico) Assignment: Tutuka Reconciliation Project

This project compares 2 csv files containing reconciliation data, makes the matches and lists the results.

## How to Start Project
```bash
install maven from <link> : https://maven.apache.org/install.html
open reconciliation.zip file
cd reconciliation
mvn spring-boot:run

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8080

OR

open reconciliation.zip file
in Eclipse IDE, use menu item "File->Open Projects from File System", and open project
wait Maven dependencies being downloaded 
run StartWebApplication.java

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8080

```


`<link>` : <http://localhost:8080>

## Problem Description

Given sets of data as two sample CSV files, compare the two files, and report on how many transactions match perfectly, versus transactions which cannot be matched

And those transactions which cannot be matched will need to be reported on, so that a third party could refer to the report and investigate the exceptions

If a transaction cannot be matched perfectly, you should attempt to look for any close matches and suggest them as possibilities

## Solution Description

Project is developed in Java Spring Boot, and for front-end used Thymeleaf.

After uploading the csv files to the /upload folder, **compareCsvFiles** method is called that is implemented in TransactionService class.
This method returns **ComparisonResult** object that holds all the data created after all process.

In this method, **formTransactionDataMap** method is called for the two CSV files. This method takes the file as a parameter, 
reads it line by line and forms a list of **FileLineInfo** that holds the fields of the line, 
for all the **FileLineInfo** object in the list creates a **Transaction** object by calling the **createTransaction** method.
This method calls **validateFields** method to validate the fields of the line. If the data is valid,
stores the **Transaction** object in a HashMap that the key is the combination of the **Transaction ID** and the **Transaction Description** fields
and the value is the **Transaction** Object. If the line has a field that is invalid, stores the line
number in the error list. If the line is a repeat of another line by checking the key in the HashMap, stores the line number in the repeated list. 
In the HashMap only the first one of the repeated lines is stored.

After forming the two HashMap for the CSV files, **findPerfectMatchs** method is called. This method compares the to HashMaps.
If all the fields of the lines equal, it is a perfect match and the **Transaction** object is stored in the perfectMatch list and
this data removed from the second HashMap to prevent repeated data and gain performance.
If the data is not matched, that is stored in the nonMatched list to search for the candidates.
When the search for the first HashMap ends, search for the second HashMap is done with the remaining data.

Lastly **findMatchingCandidates** method is called. This method finds the matching candidates from the nonMatched data lists,
If the data meets the candidate conditions, stores in the candidate list, and removes from the nonMatched List
At last only the nonMatched data remains in the nonMatched list. The candidate conditions are, first find matching for **Transaction ID**,
second for **Wallet Reference** and lastly for **Transaction Date**, **Transaction Amount** and **Transaction Narrative** triple. While controlling
Transaction Narrative, the punctuations, blanks and numbers are eliminated and the remaining is converted to lowercase.

All the data prepared is stored in **ComparisonResult**, and used for the reporting.



## Usage

1- Click on the **Browse** buttons and Select the csv files from the file system.

![](./src/main/resources/image/image1.png)

2- Click on the **Compare** button

3- The system will upload the files, read the contents, do the required validations, creates the data structure for matching and displays the general info as counts.

![](./src/main/resources/image/image2.png)

4- Click on the **Detail Report** button

5- The candidates and nonmatched data will be listed. Also the lines that have error and the repeated lines will be displayed as line numbers.

![](./src/main/resources/image/image3.png)



##  Assumptions

1- Since the sample files have limited lines, pagination is not done.

2- In the CSV file, there can be 50.000 lines of data. Most of the lines are assumed to be perfect match.

2- If the line has a data that doesn’t suit the specs, the line will be ignored and will be reported as line number. The field constraints are;

- A line must have 8 fields.

- First line of the file is the header line and will be ignored.

- **Profile Name** field can not be null and must have maximum 30 characters in length.

- **Transaction Date** field can not be null and must be in date format.

- **Transaction Amount** field can not be null and must be in numeric format. 

- **Transaction Narrative** field can not be null and must have maximum 200 characters in length.

- **Transaction Description** field can not be null and must be **DEDUCT** or **REVERSAL** in value.

- **Transaction ID** field can not be null and must have 16 characters in length and be in numeric format. 

- **Transaction Type** field can not be null and must have 1 character in length and be in numeric format. 

- **Wallet Reference** field can not be null and must have 34 characters in length. 



3- In a file, if there are repeated lines, the first one will be stored, others will be reported as line number. The **Transaction Description** and **Transaction ID** fields together forms the unique key for a record.

4- If all the fields match, it is a perfect match. 

5- If not the data is checked if it is a matching candidate. For this purpose, these candidate conditions are used. First find matching for **Transaction ID**,
second for **Wallet Reference** and lastly for **Transaction Date**, **Transaction Amount** and **Transaction Narrative** triple. While controlling
Transaction Narrative, the punctuations, blanks and numbers are eliminated and the remaining is converted to lowercase.

6- Project will be opened in Chrome or Microsoft Edge with the address http://localhost:8080.

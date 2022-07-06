This project contains a csv standalone parser implemented with Spring Boot.
The entrypoint is the class under /src/main/java/de/kremer/parser/ParserApplication.
As input arguments the parser accepts csv files with a full path reference for example --f=/path/to/the/your/file.csv
After the file processing the parser transfer the data to a webservice for a median calculation. 
The endpoint is available under http://localhost:8083/median.
The response of the webservice is storen in the same directory as the origin file with the suffix "_median.csv".

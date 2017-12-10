# Similarity Finder

Web application that allows to compare your personal document (i.e. a thesis) against a number of resources (literature) and highlights similarities.

## Author

* Lukas Lebovitz

## Technology Stack used

* Java Spring Boot
* H2 & JPA
* Javascript React & Redux
* Bootstrap
* SockJS & Stomp
* Apache PDFBox

## Similarity Algorithm

The detection is based on pure string comparison. The Sørensen–Dice coefficient is the fundamental algorithm used in the comparison. The implementation is closely following the implementation from this repository: [tdebatty/java-string-similarity](https://github.com/tdebatty/java-string-similarity)

## Documentation

This is a similarity detection tool. You can use it to compare one document against one or more resources.

The documents will be uploaded to the server. Only the parsed content of your documents will be temporarily stored. After 3 hours of inactivity your session is timed out and all your content will be ultimately removed from the server.

The tool is able to pick up similar text based on pure text comparison. Paraphrasing will not be detected.

## Todo

* Improve Performance of DetailComparator
* File Upload Loading Bar
* Support multiple users running the app => Queue-System and show queue-position in frontend
* A lot of code refactoring to achieve clean code / improve quality
* Deploy

## Contact

Mail: lukas.lebo@hotmail.com

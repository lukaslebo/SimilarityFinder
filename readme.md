# Similarity Detection Tool

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

To be created...

## Todo

* File Upload Loading Bar
* Similarity Detection Logic => sending back progress info
* Similarity Fetching when progress at 100%
* Similarity highlighting at frontend

* Support multiple users running the app => Queue-System and show queue-position in frontend

## Contact

Mail: lukas.lebo@hotmail.com

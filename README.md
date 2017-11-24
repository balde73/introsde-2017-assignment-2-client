# introsde-2017-assignment-2-client

Federico Baldessari | federico.baldesssari@studenti.unitn.it

[Heroku Server](https://example-balde.herokuapp.com/sdelab/person)

## About the code

The project is divided into 3 packages
- `rest.client`: contains _MyClient.java_ which performs requests to the server deployed on Heroku
- `rest.model`: contains the model
- `rest.utils`: contains _Postman.java_ to make calls and _PrettyPrinter.java_ to manage the writing into file

The configuration file are: `build.xml` and `ivy.xml`

## Setup

Clone the repository
```
git clone https://github.com/balde73/introsde-2017-assignment-2-client.git
```
open the directory and run
```
ant execute.client
```
two files will be created with the response from the Heroku server. One for xml and the other for json

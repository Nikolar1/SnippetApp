# Snippet App

This app reads provided books from www.gutenberg.org, and then parses them into snippets, provides easy search through them, and predicts authors of snippets using machine learning. 

For indexing and searching through snippets lucene library is used.

Machine learning is done using the weka library. The arff files created after parsing are filter throug StringToWordVector filter twice seperatly to get bag-of-words representation of snippets.

The snippets are represented through 5-2 word n-grams and 3-1 character n-grams.

Two sepreate SMO classifiers are trained on these representations.

In the end a new soft voting classifier is made using these two which is then evaluated on the test set, and used in the future for predicting authors of snippets.

## Starting the app
The app is dockerized so to easily run it you need docker. In the project root folder run the following command:

	docker-compose up --build
	
The app will start and be accessible at `http://localhost:3000`.

> **Note:** Make sure that docker engine is running before executing the command.

> **Note 2:** The app needs some time to check data integrity, index snippets and load classifier some functionality will be unavailable during that process.

> **Note 3:** During initial start up a lot of time needs to pass for all snippets to be sent into the database (around 10 minutes)
## Screenshots

### Home page

![Home page](front-page.png)

### Search page

![Search page found](search-page-found.png)

### Author prediction page
![Author prediction page](author-prediction-page.png)

### Prediction aided search page
![Prediction aided search page](prediction-aided-page.png)

### Status page
![Microservice status page](microservice-status-page.png)
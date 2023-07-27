# snippetBackend

This app reads provided books from www.gutenberg.org, and then parses them into snippets, provides easy search through them, and predicts authors of snippets using machine learning. 

For indexing and searching through snippets lucene library is used.

Machine learning is done using the weka library. The arff files created after parsing are filter throug StringToWordVector filter twice seperatly to get bag-of-words representation of snippets.

The snippets are represented through 5-1 word n-grams and 3-1 character n-grams and then joined into one by adding all unique attributes together.

Because of large number of attributes, attribute selection is done based on info gain.

In the end SMO classifier is used to build a model which is then evaluated on the test set, and used in the future for predicting authors of snippets.

## Starting the app
The app is dockerized so to easily run it you need docker. In the project root folder run the following command:

	docker-compose up --build
	
The app will start and be accessible at `http://localhost:3000`.

> **Note:** Make sure that docker engine is running before executing the command.

> **Note 2:** The app needs some time to parse the books, index snippets and load classifier some functionality will be unavailable during that process.

## Screenshots

### Home page

![Home page](front%20page.png)

### Search page

![Search page found](Search%20page%20found.png)

### Author prediction page

### Prediction aided search page

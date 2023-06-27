# snippetBackend

This app reads provided books from www.gutenberg.org, and then parses them into snippets, provides easy search through them, and predicts authors of snippets using machine learning. 

## Usage
The app is dockerized so to easily run it you need docker. In the project root folder run the following command:

	docker-compose up --build
	
The app will start and be accessible at `http://localhost:3000`.

> **Note:** Make sure that docker engine is running before executing the command.

> **Note 2:** The app needs some time to parse the books and index snippets some functionality will be unavailable during that process.

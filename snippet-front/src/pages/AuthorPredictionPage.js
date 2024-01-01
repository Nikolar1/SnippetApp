import React, { useState } from 'react';
import Header from '../components/Header';

const AuthorPredictionPage = () => {
  const [snippet, setSnippet] = useState('');
  const [author, setAuthor] = useState('');

  const handleSnippetChange = (e) => {
    setSnippet(e.target.value);
  };

  const handleGetAuthor = () => {
    if(snippet){
        const url = "http://localhost:8085/predict?" + (snippet ? `snippet=${encodeURIComponent(snippet)}` : '');
        fetch(url, {
          method: 'GET',
    
        })
          .then(response => response.json())
          .then(data => {
            setAuthor(data.author);
          })
          .catch(error => {
            console.error('Error:', error);
          });
        }
  };

  return (
    <div>
        <Header />
    <div className="container mt-5">
        
    <h1>Snippet Page</h1>
    <div className="input-group mb-3">
      <input
        type="text"
        className="form-control"
        value={snippet}
        onChange={handleSnippetChange}
        placeholder="Enter a snippet"
      />
      <button
        className="btn btn-primary"
        onClick={handleGetAuthor}
      >
        Get Author
      </button>
    </div>
    <div className="mt-3">
      <h2>Author:</h2>
      <p>{author}</p>
    </div>
  </div>
  </div>
  );
};

export default AuthorPredictionPage;
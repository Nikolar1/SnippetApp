import React, { useState } from 'react';
import Header from '../components/Header';

const PredictionAidedSearchPage = () => {
  const [searchResults, setSearchResults] = useState([]);


  const [snippet, setSnippet] = useState('');

  const handleSearch = () => {
    if(snippet){
    const url = "http://localhost:8080/aidedSearch?" + (snippet ? `snippet=${encodeURIComponent(snippet)}` : '');
    fetch(url, {
      method: 'GET',

    })
      .then(response => response.json())
      .then(data => {
          setSearchResults(data);
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
    
      <h1>Prediction Aided Search Page</h1>

      <div className="input-group mb-3">
      <input
        type="text"
        className="form-control"
        value={snippet}
        onChange={e => setSnippet(e.target.value)}
        placeholder="Enter a snippet"
      />
      <button
        className="btn btn-primary"
        onClick={handleSearch}
      >
        Search
      </button>
    </div>


      <table className="table">
        <thead>
          <tr>
            <th>Author</th>
            <th>Book</th>
            <th>Snippet</th>
          </tr>
        </thead>
        <tbody>
          {searchResults.map((item, index) => (
            <tr key={index}>
              <td>{item.author}</td>
              <td>{item.book}</td>
              <td>{item.snippet}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
};

export default PredictionAidedSearchPage;
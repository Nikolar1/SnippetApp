import React, { useState } from 'react';
import Header from '../components/Header';

const SearchPage = () => {
  const [searchResults, setSearchResults] = useState([]);


  const [author, setAuthor] = useState('');
  const [book, setBook] = useState('');
  const [snippet, setSnippet] = useState('');

  const handleSearch = () => {
    if(author || book || snippet){
    const url = "http://localhost:8080/search" + 
                    (author || book || snippet ? '?' : '') +
                    (author ? `author=${encodeURIComponent(author)}` : '') +
                    (author && book ? '&' : '') +
                    (book ? `book=${encodeURIComponent(book)}` : '') +
                    ((author || book) && snippet ? '&' : '') +
                    (snippet ? `snippet=${encodeURIComponent(snippet)}` : '');
    fetch(url, {
      method: 'GET',

    })
      .then(response => response.json())
      .then(data => {
        if (data !== null || data !== undefined) {
          setSearchResults(data);
        }
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
    
      <h1>Search Page</h1>
      <div className="row">
        <div className="col-md-4">
          <input
            type="text"
            className="form-control"
            value={author}
            onChange={e => setAuthor(e.target.value)}
            placeholder="Author"
          />
        </div>
        <div className="col-md-4">
          <input
            type="text"
            className="form-control"
            value={book}
            onChange={e => setBook(e.target.value)}
            placeholder="Book"
          />
        </div>
        <div className="col-md-4">
          <input
            type="text"
            className="form-control"
            value={snippet}
            onChange={e => setSnippet(e.target.value)}
            placeholder="Snippet"
          />
        </div>
      </div>
      <div className="row mt-3">
        <div className="col-md-12">
          <button className="btn btn-primary" onClick={handleSearch}>
            Search
          </button>
        </div>
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

export default SearchPage;
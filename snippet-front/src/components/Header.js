import React from 'react';

const Header = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <a className="navbar-brand" href="/">Snippet</a>
      <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav ml-auto">
          <li className="nav-item active">
            <a className="nav-link" href="/">Home</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/SearchPage">Search snippets</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="#">Predict snippet author</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="#">Prediction aided snippet search</a>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Header;
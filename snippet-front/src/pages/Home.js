import React from 'react';
import './Home.css';
import { Link } from 'react-router-dom';
import Header from '../components/Header';

const Home = () => {
  return (
  <div>
    <Header />
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="row">
        <div className="col-md-4">
        <Link to="/SearchPage">
          <button className="btn btn-dark btn-lg btn-square">
            Search snippets
          </button>
          </Link>
        </div>
        <div className="col-md-4">
          <button className="btn btn-dark btn-lg btn-square" disabled>
            Predict snippet author
          </button>
        </div>
        <div className="col-md-4">
          <button className="btn btn-dark btn-lg btn-square" disabled>
            Prediction aided snippet search
          </button>
        </div>
      </div>
    </div>
    </div>
  );
};

export default Home;
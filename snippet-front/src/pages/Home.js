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
        <Link to="/PredictPage">
          <button className="btn btn-dark btn-lg btn-square">
            Predict snippet author
          </button>
          </Link>
        </div>
        <div className="col-md-4">
        <Link to="/AidedSearchPage">
          <button className="btn btn-dark btn-lg btn-square">
            Prediction aided snippet search
          </button>
          </Link>
        </div>
      </div>
    </div>
    </div>
  );
};

export default Home;
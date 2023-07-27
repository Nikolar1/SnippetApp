import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import SearchPage from './pages/SearchPage';
import NotFound from './pages/NotFound';
import AuthorPredictionPage from './pages/AuthorPredictionPage';
import PredictionAidedSearchPage from './pages/PredictionAidedSearchPage';

function App() {
  return (
    <Router>
      <div className="app-container">
     <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/SearchPage" element={<SearchPage />} />
      <Route path="/PredictPage" element={<AuthorPredictionPage />} />
      <Route path="/AidedSearchPage" element={<PredictionAidedSearchPage />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
    </div>
  </Router>
  );
}

export default App;

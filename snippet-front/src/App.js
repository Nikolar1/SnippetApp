import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import SearchPage from './pages/SearchPage';
import NotFound from './pages/NotFound';

function App() {
  return (
    <Router>
      <div className="app-container">
     <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/SearchPage" element={<SearchPage />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
    </div>
  </Router>
  );
}

export default App;

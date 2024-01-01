import React, { useState } from 'react';
import Header from '../components/Header';
import './StatusPage.css';

const StatusPage = () => {
    const [isParserServiceRunning, setParserServiceRunning] = useState(false);
    const [isSearchServiceRunning, setSearchServiceRunning] = useState(false);
    const [isClassificationServiceRunning, setClassificationServiceRunning] = useState(false);

    const handleSearch = () => {
            const url = "http://localhost:8085/serviceStatus";
            fetch(url, {
                method: 'GET',

            })
                .then(response => response.json())
                .then(data => {
                    if (data !== null || data !== undefined) {
                        setParserServiceRunning(data.parserService == "RUNNING");
                        setSearchServiceRunning(data.searchService == "RUNNING");
                        setClassificationServiceRunning(data.classificationService == "RUNNING")
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
    };

    const handlePing = (service) => {
        switch (service) {
            case 'Parser':
                handleSearch();
                break;
            case 'Search':
                handleSearch();
                break;
            case 'Classification':
                handleSearch();
                break;
            default:
                break;
        }
    };

    const renderServiceBox = (serviceName, isServiceRunning) => (
        <div className="service-box">
            <div className="service-header">
                <div className="service-name">{serviceName}</div>
                <div className={`status-circle ${isServiceRunning ? 'green' : 'red'}`}></div>
            </div>
            <div className="service-content">
                <p>Service is {isServiceRunning ? 'running' : 'stopped'}.</p>
                <button onClick={() => handlePing(serviceName)}>Ping</button>
            </div>
        </div>
    );

    return (
        <div>
            <Header />
            <div className="app">
                <div className="service-container">
                    {renderServiceBox('Parser', isParserServiceRunning)}
                    {renderServiceBox('Search', isSearchServiceRunning)}
                    {renderServiceBox('Classification', isClassificationServiceRunning)}
                </div>
            </div>
        </div>
    );
};

export default StatusPage;
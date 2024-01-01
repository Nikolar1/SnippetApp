import React, {useEffect, useState} from 'react';
import Header from '../components/Header';
import {
  FacebookShareButton,
  TwitterShareButton,
  LinkedinShareButton,
  RedditShareButton,
  FacebookIcon, LinkedinIcon, RedditIcon, XIcon
} from 'react-share';
import {Stomp} from '@stomp/stompjs';

const PredictionAidedSearchPage = () => {
  const [searchResults, setSearchResults] = useState([]);

  const [translations, setTranslations] = useState({});

  const [snippet, setSnippet] = useState('');
  const [stompClient, setStompClient] = useState(null);
  const [suggestionWasClicked, setSuggestionWasClicked] = useState(false);
  const [summary, setSummary] = useState({});
  const [suggestion, setSuggestion] = useState("");

  useEffect(() => {
    // Connect to the WebSocket server
    const connectWebSocket = () => {
      const socket = new WebSocket('ws://localhost:8085/webSocketSearch'); // Replace with your WebSocket endpoint

      const stomp = Stomp.over(socket);

      stomp.connect({}, () => {
        console.log('Connected to WebSocket');
        setStompClient(stomp);
      });
    };

    connectWebSocket();

    // Cleanup on unmount
    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, []);

  useEffect(() => {
    // Subscribe to the topic to receive messages
    if (stompClient) {
      const subscription = stompClient.subscribe('/topic/aidedAutocomplete', (response) => {
        const receivedData = response.body;
        console.log(receivedData)
        setSuggestion(receivedData);
      });

      // Cleanup subscription on component unmount
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [stompClient]);

  useEffect(() => {
    if (stompClient) {
      if (!suggestionWasClicked) {
        stompClient.send('/app/aidedAutocomplete', {}, JSON.stringify(snippet));
      }else {
        setSuggestionWasClicked(false);
      }
    }
  },[snippet])

  const handleSearch = () => {
    if(snippet){
    const url = "http://localhost:8085/aidedSearch?" + (snippet ? `snippet=${encodeURIComponent(snippet)}` : '');
    fetch(url, {
      method: 'GET',

    })
      .then(response => response.json())
      .then(data => {
          setSearchResults(data);
          getSummary(data);
        //translateSnippets(data)
      })
      .catch(error => {
        console.error('Error:', error);
      });
    }
  };

  const translateSnippets = (data) => {
    //Didn't want to leave payment data so I don't have an api key
    const apiKey = 'API_KEY';
    const snippetsToTranslate = data.map((item) => item.snippet);
    fetch(
        `https://translation.googleapis.com/language/translate/v2?key=${apiKey}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            q: snippetsToTranslate,
            source: 'en',
            target: 'sr',
          }),
        }
    )
        .then((response) => response.json())
        .then((translationData) => {
          const translatedSnippets = translationData.data.translations.map(
              (t) => t.translatedText
          );
          const translationMap = {};
          data.forEach((item, index) => {
            translationMap[item.snippet] = translatedSnippets[index];
          });
          setTranslations(translationMap);
        })
        .catch((error) => {
          console.error('Translation Error:', error);
        });
  };

  const getSummary = (data) => {
    const snippetsToSumarize = data.map((item) => item.snippet.replace(/"/g, "'"));
    const body = JSON.stringify({
      text: snippetsToSumarize
    })
    fetch(
        `http://localhost:8085/summarize`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: body,
        }
    )
        .then((response) => response.json())
        .then((summaryData) => {
          const sumarizedSnippets = summaryData.summary_text;
          const summaryMap = {};
          data.forEach((item, index) => {
            summaryMap[item.snippet] = sumarizedSnippets[index];
          });
          setSummary(summaryMap);
        })
        .catch((error) => {
          console.error('Summary Error:', error);
        });
  };

  const suggestionClicked = (clickedSuggestion) => {
    setSuggestionWasClicked(true)
    setSuggestion("")
    setSnippet(suggestion)
  }

  return (
    <div>
      <Header />
    <div className="container mt-5">

      <h1>Prediction Aided Search Page</h1>

      <div className="input-group mb-3">
        <div style={{width: "90%"}}>
        <input
            type="text"
            className="form-control"
            value={snippet}
            onChange={e => setSnippet(e.target.value)}
            placeholder="Snippet"
        />
        </div>
        <button
            className="btn btn-primary"
            onClick={handleSearch}
        >
          Search
        </button>
        {suggestion != "" && (
            <div style={{ backgroundColor: 'white', clear: "both" }} onClick={() => suggestionClicked(suggestion)}>
              {suggestion}
            </div>
        )}

    </div>
      <table className="table">
        <thead>
          <tr>
            <th>Author</th>
            <th>Book</th>
            <th>Snippet</th>
            <th>Summary</th>
            <th>Share</th>
          </tr>
        </thead>
        <tbody>
          {searchResults.map((item, index) => (
            <tr key={index}>
              <td>{item.author}</td>
              <td>{item.book}</td>
              <td>{item.snippet}</td>
              <td>{summary[item.snippet]}</td>
              <td style={{  width: '8%' }}>
                <div className="mt-2 d-flex flex-wrap">
                  <div className="mr-2">
                    <FacebookShareButton url={window.location.href} quote={item.snippet}>
                      <FacebookIcon size={32} round={true} />
                    </FacebookShareButton>
                  </div>
                  <div className="mr-2">
                    <TwitterShareButton url={window.location.href} title={item.snippet}>
                      <XIcon size={32} round={true} />
                    </TwitterShareButton>
                  </div>
                  <div className="mr-2">
                    <LinkedinShareButton url={window.location.href} title={item.snippet}>
                      <LinkedinIcon size={32} round={true} />
                    </LinkedinShareButton>
                  </div>
                  <div className="mr-2">
                    <RedditShareButton url={window.location.href} title={item.snippet}>
                      <RedditIcon size={32} round={true} />
                    </RedditShareButton>
                  </div>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
};

export default PredictionAidedSearchPage;
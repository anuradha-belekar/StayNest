import React from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter} from 'react-router-dom';
import App from './App';
import './index.css';
//ReactDOM.createRoot is the new way to initialize your app in React 18+
//document.getElementById('root'):-This gets the HTML element with ID root from index.html
//.render(...):-renders the JSX inside the root
//<React.StrictMode>:-find potential problems in app
//<BrowserRouter>:-enable client-side routing features like <Routes>, <Route>, useNavigate(), and useParams()
//<App />:-main application component
ReactDOM.createRoot(document.getElementById('root')).render(
      <React.StrictMode>
            <BrowserRouter>
                  <App/>
            </BrowserRouter>
      </React.StrictMode>

);

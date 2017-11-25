import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import { Provider } from 'react-redux';
import store from './store';
import WebApp from './containers/WebApp'

ReactDOM.render(
<Provider store = { store }>
  <WebApp/>
</Provider>
, document.getElementById('root'));

registerServiceWorker();

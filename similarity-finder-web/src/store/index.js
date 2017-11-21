import { createStore, applyMiddleware } from 'redux';
import reduce from './reducers';
import thunk from 'redux-thunk';

const store = createStore(reduce, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(), applyMiddleware(thunk));

export default store;
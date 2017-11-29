import { combineReducers } from 'redux';
import webApiReducer from './webApiReducer';
import displayReducer from './displayReducer';

export default combineReducers({ webApiReducer, displayReducer });
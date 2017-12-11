import { SET_NEW_USER, REFRESH_USER, SET_DOCUMENT, SET_RESOURCES, REMOVE_DOCUMENT, 
  REMOVE_RESOURCE, SELECT_RESOURCE, SET_PROGRESS, SET_SIMILARITIES, REMOVE_SIMILARITIES } from '../actions/types';
import Moment from 'moment';

const API_BASE_URL = 'http://192.168.1.200:8080'; //'http://localhost:8080';

const initialState = {
  apiBaseUrl: API_BASE_URL,
  isLoggedin: false,
  userId: null,
  expiresAt: null,
  document: null,
  resources: [],
  resourceIndex: null,
  similarities: [],
  isProcessed: false,
  processingInitiated: false,
  progress: null,
}

const webApiReducer = (state = initialState, action) => {
  let newState = { ...state };

  if (newState.isLoggedin && newState.expiresAt.isBefore(new Moment())) {
    console.log('Account is expired.');
    return initialState;
  }

  switch (action.type) {

    case SET_NEW_USER:
      newState.isLoggedin = true;
      newState.userId = action.payload.id;
      newState.expiresAt = toMoment(action.payload.expiresAt);
      return newState;

    case REFRESH_USER:
      if (action.payload.user_exists) {
        newState.expiresAt = toMoment(action.payload.expiresAt);
      }
      return newState;

    case SET_DOCUMENT:
      newState.document = action.payload.document;
      newState.isProcessed = false;
      return newState;

    case SET_RESOURCES:
      let currentId = null;
      if (newState.resources.length > 0) {
        currentId = newState.resources[newState.resourceIndex].id;
      }
      newState.resources = action.payload.resources;
      if (newState.resourceIndex === null && newState.resources.length > 0) {
        newState.resourceIndex = 0;
      }
      else if (currentId !== null) {
        newState.resources.forEach((el, index) => {
          if (el.id === currentId) {
            newState.resourceIndex = index;
          }
        })
      }
      newState.isProcessed = false;
      return newState;

    case REMOVE_DOCUMENT:
      newState.document = null;
      newState.isProcessed = false;
      newState.similarities = [];
      return newState;

    case REMOVE_RESOURCE:
      currentId = newState.resources[newState.resourceIndex].id;
      if (newState.similarities.length > 0) {
        newState.similarities = newState.similarities.filter(el => el.resourceId !== currentId);
      }
      newState.resources = newState.resources.filter( (el, index) => index !== newState.resourceIndex);
      if (newState.resourceIndex >= newState.resources.length) {
        newState.resourceIndex = newState.resources.length-1;
      }
      if (newState.resources.length === 0) {
        newState.resourceIndex = null;
      }
      return newState;

    case SELECT_RESOURCE:
      newState.resourceIndex = action.payload.resourceIndex;
      return newState;

    case SET_PROGRESS:
      if (action.payload.progress >= 0) {
        newState.progress = action.payload.progress;
      }
      return newState;

    case SET_SIMILARITIES:
      newState.progress = null;
      newState.similarities = action.payload.similarities;
      if (newState.similarities.length > 0) {
        newState.isProcessed = true;
      }
      return newState;

    case REMOVE_SIMILARITIES:
      newState.isProcessed = false;
      newState.similarities = [];
      return newState;

    default:
      return state;
  }
}

export default webApiReducer;

function toMoment(inputArr) {
  --inputArr[1];
  inputArr[6] = Number((inputArr[5]+'').substr(0,3));
  return Moment.utc(inputArr);
} 
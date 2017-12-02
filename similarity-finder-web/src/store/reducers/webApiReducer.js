import { SET_NEW_USER, REFRESH_USER, SET_DOCUMENT, SET_RESOURCES, REMOVE_DOCUMENT, REMOVE_RESOURCE } from '../actions/types';
import Moment from 'moment';

const API_BASE_URL = 'http://localhost:8080';

const initialState = {
  apiBaseUrl: API_BASE_URL,
  isLoggedin: false,
  userId: null,
  expiresAt: null,
  document: null,
  resources: [],
  resourceIndex: null,
  similarities: [],
}

const webApiReducer = (state = initialState, action) => {
  let newState = { ...state };

  if (newState.isLoggedin && newState.expiresAt.isBefore(new Moment())) {
    console.log('logout...');
    return initialState;
  }

  switch (action.type) {

    case SET_NEW_USER:
      newState.isLoggedin = true;
      newState.userId = action.payload.id;
      newState.expiresAt = toMoment(action.payload.expiresAt);
      return newState;

    case REFRESH_USER:
      if (action.payload.exists) {
        newState.expiresAt = toMoment(action.payload.expiresAt);
      }
      return newState;

    case SET_DOCUMENT:
      newState.document = action.payload.document;
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
      return newState;

    case REMOVE_DOCUMENT:
      newState.document = null;
      return newState;

    case REMOVE_RESOURCE:
      newState.resources = newState.resources.filter( (el, index) => index !== newState.resourceIndex);
      if (newState.resourceIndex > newState.resources.length) {
        newState.resourceIndex = newState.SET_RESOURCES.length-1;
      }
      if (newState.resources.length === 0) {
        newState.resourceIndex = null;
      }
      return newState;
      
    default:
      return state;
  }
}

export default webApiReducer;

function toMoment(inputArr) {
  let dateString = inputArr.slice(0, 3).join("-").concat(" ",inputArr.slice(3, 6).join(":")).concat(".",(inputArr[6]+"").slice(0,3));
  let date = new Date(dateString);
  return new Moment(date.toISOString());
} 
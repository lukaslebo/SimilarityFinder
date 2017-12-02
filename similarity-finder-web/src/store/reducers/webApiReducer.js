import { SET_NEW_USER, REFRESH_USER, SET_DOCUMENT, SET_RESOURCES, REMOVE_DOCUMENT, REMOVE_RESOURCE } from '../actions/types';

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
  switch (action.type) {
    case SET_NEW_USER:
      newState.isLoggedin = true;
      newState.userId = action.payload.id;
      newState.expiresAt = action.payload.expiresAt;
      return newState;
    case REFRESH_USER:
      if (action.payload.exists) {
        newState.expiresAt = action.payload.expiresAt;
      }
      return newState;
    case SET_DOCUMENT:
      newState.document = action.payload.document;
      return newState;
    case SET_RESOURCES:
      newState.resources = action.payload.resources;
      if (newState.resourceIndex === null && newState.resources.length > 0) {
        newState.resourceIndex = 0;
      }
      return newState;
    case REMOVE_DOCUMENT:
      newState.document = null;
      return newState;
    case REMOVE_RESOURCE:
      newState.resources = newState.resources.filter(el => el.id !== newState.resourceIndex);
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
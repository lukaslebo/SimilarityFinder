import { SET_NEW_USER, REFRESH_USER } from '../actions/types';

const API_BASE_URL = 'http://localhost:8080';

const initialState = {
  apiBaseUrl: API_BASE_URL,
  isLoggedin: false,
  userId: null,
  expiresAt: null,
  document: null,
  resources: [],
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
    default:
      return state;
  }
}

export default webApiReducer;
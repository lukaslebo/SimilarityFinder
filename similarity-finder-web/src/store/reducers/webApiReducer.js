import { } from '../actions/types';

const API_BASE_URL = 'https://localhost:8080';

const initialState = {
  user: {},
  token: {},
  isLoggedin: false,
  apiBaseUrl: API_BASE_URL,
  blitzList: [],
}

export const webApiReducer = (state =  initialState, action) => {
  // let newState = { ...state };
  switch (action.type) {

    default:
      return state;
  }
}
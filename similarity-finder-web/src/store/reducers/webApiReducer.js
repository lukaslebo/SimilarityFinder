import { } from '../actions/types';

const API_BASE_URL = 'https://propulsion-blitz.herokuapp.com/api';

const initialState = {
  user: {},
  token: {},
  isLoggedin: false,
  apiBaseUrl: API_BASE_URL,
  blitzList: [],
}

export const blitzReducer = (state =  initialState, action) => {
  let newState = { ...state };
  switch (action.type) {

    default:
      return state;
  }
}
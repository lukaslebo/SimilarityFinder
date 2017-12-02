import { ADD_FILE, CANCEL_UPLOAD, SET_NEW_USER, REFRESH_USER } from './types';

export const addButtonPressed = (frame) => ({
  type: ADD_FILE,
  payload: { frame },
})

export const closeUpload = () => ({
  type: CANCEL_UPLOAD,
})

export const setNewUser = (res) => ({
  type: SET_NEW_USER,
  payload: {
    ...res,
  }
})

export const setRefresh = (res) => ({
  type: REFRESH_USER,
  payload: {
    ...res,
  }
})

export const getNewUser = () => {
  return async (dispatch, getState) => {
    const URL = getState().webApiReducer.apiBaseUrl + '/api/user';
    const config = {
      method: 'GET',
    };
    const response = await fetch(URL, config);
    const res = await response.json();
    dispatch(setNewUser(res));
  }
}

export const refreshUser = () => {
  return async (dispatch, getState) => {
    const URL = getState().webApiReducer.apiBaseUrl + '/api/user/'+getState().webApiReducer.userId;
    const config = {
      method: 'PUT',
    };
    const response = await fetch(URL, config);
    const res = await response.json();
    dispatch(setRefresh(res));
  }
}

export const fileUpload = (files, suffix = '/setDoc') => {
  return async (dispatch, getState) => {
    const baseURL = getState().webApiReducer.apiBaseUrl;
    const userId = getState().webApiReducer.userId;
    const URL =  baseURL + '/api/document/' + userId + suffix;
    let formData = new FormData();
    for (let file of files) {
      formData.append('file', file);
    }
    const config = {
      method: 'POST',
      body: formData,
    };
    const response = await fetch(URL, config);
    const res = await response.json();
    console.log(res);
  }
}

import { ADD_FILE, CLOSE_UPLOAD, SET_NEW_USER, REFRESH_USER, SET_DOCUMENT, SET_RESOURCES, REMOVE_DOCUMENT, REMOVE_RESOURCE } from './types';

export const addButtonPressed = (frame) => ({
  type: ADD_FILE,
  payload: { frame },
})

export const closeUpload = () => ({
  type: CLOSE_UPLOAD,
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
    switch (suffix) {
      case '/setDoc':
        dispatch(setDocument(res));
        break;
      case '/addResource':
        dispatch(setResources(res));
        break;
      default:
    }
  }
}

export const docRemove = (suffix = '/removeDocument') => {
  return async (dispatch, getState) => {
    const state = getState().webApiReducer;
    const resourceId = state.resources[state.resourceIndex].id;
    const baseURL = state.apiBaseUrl;
    const userId = state.userId;
    let URL = baseURL + '/api/document/' + userId + suffix;
    if (suffix === '/removeResource') {
      URL += '/' + resourceId;
    }
    const config = {
      method: 'DELETE',
    }
    const response = await fetch(URL, config);
    const res = await response.json();
    if (res.status !== 'ok') {
      return;
    }
    switch (suffix) {
      case '/removeDocument':
        dispatch(removeDocument());
        break;
      case '/removeResource':
        dispatch(removeResource());
        break;
      default:
    }
  }
}

export const setDocument = (res) => ({
  type: SET_DOCUMENT,
  payload: {
    ...res,
  },
})

export const setResources = (res) => ({
  type: SET_RESOURCES,
  payload: {
    ...res,
  },
})

export const removeDocument = () => ({
  type: REMOVE_DOCUMENT,
})

export const removeResource = () => ({
  type: REMOVE_RESOURCE,
})

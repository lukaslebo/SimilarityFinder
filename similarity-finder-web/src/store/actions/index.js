import { ADD_FILE, CLOSE_CARD, PROGRESS_INDICATOR, SET_NEW_USER, REFRESH_USER, SET_DOCUMENT, SET_RESOURCES, 
  REMOVE_DOCUMENT, REMOVE_RESOURCE, SELECT_RESOURCE, CONTACT_CARD, DESCRIPTION_CARD, AUTHOR_CARD, SET_PROGRESS, SET_SIMILARITIES } from './types';

export const addButtonPressed = (frame) => ({
  type: ADD_FILE,
  payload: { frame },
})

export const showInfoCard = (card) => {
  switch (card) {
    case 'contact':
      return cardAction(CONTACT_CARD);
    case 'description':
      return cardAction(DESCRIPTION_CARD);
    case 'author':
      return cardAction(AUTHOR_CARD);
    default:
  }
}

export const cardAction = (type) => ({
  type,
})

export const closeCard = () => ({
  type: CLOSE_CARD,
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
    if (res.status !== 'ok') {
      return;
    }
    switch (suffix) {
      case '/setDoc':
        dispatch(setDocument(res));
        break;
      case '/addResource':
        dispatch(setResources(res));
        break;
      default:
    }
    dispatch(closeCard());
  }
}

export const textUpload = (title, text, suffix = '/setDocText') => {
  return async (dispatch, getState) => {
    const baseURL = getState().webApiReducer.apiBaseUrl;
    const userId = getState().webApiReducer.userId;
    const URL =  baseURL + '/api/document/' + userId + suffix;
    const headers = new Headers({'Content-Type':'application/json'});
    const config = {
      method: 'POST',
      headers: headers,
      body: JSON.stringify({
        title,
        text,
      }),
    };
    const response = await fetch(URL, config);
    const res = await response.json();
    if (res.status !== 'ok') {
      return;
    }
    switch (suffix) {
      case '/setDocText':
        dispatch(setDocument(res));
        break;
      case '/addResourceText':
        dispatch(setResources(res));
        break;
      default:
    }
    dispatch(closeCard()); 
  }
}

export const docRemove = (suffix = '/removeDocument') => {
  return async (dispatch, getState) => {
    const state = getState().webApiReducer;
    if (state.resources.length > 0) {
      var resourceId = state.resources[state.resourceIndex].id;
    }
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

export const selectResource = (resourceIndex) => ({
  type: SELECT_RESOURCE,
  payload: {
    resourceIndex,
  },
})

export const showProgress = () => ({
  type: PROGRESS_INDICATOR,
})

export const setProgress = (progress) => ({
  type: SET_PROGRESS,
  payload: {
    progress,
  },
})


export const getSimilarities = () => {
  return async (dispatch, getState) => {
    const state = getState().webApiReducer;
    const baseURL = state.apiBaseUrl;
    const userId = state.userId;
    let URL = baseURL + '/api/document/' + userId + '/similarities';
    const config = {
      method: 'GET',
    }
    const response = await fetch(URL, config);
    const res = await response.json();
    if (res.status !== 'ok') {
      return;
    }
    dispatch(setSimilarities(res));
  }
}

export const setSimilarities = (res) => ({
  type: SET_SIMILARITIES,
  payload: {
    similarities: res.similarities,
  },
})

export const loadSpecificUser = (userId) => {
  return async (dispatch, getState) => {
    const state = getState().webApiReducer;
    const baseURL = state.apiBaseUrl;
    let URL = baseURL + '/api/document/' + userId;
    const config = {
      method: 'GET',
    }
    const response = await fetch(URL, config);
    const res = await response.json();
    if (res.status !== 'ok') {
      dispatch(getNewUser());
      return;
    }
    dispatch(setNewUser(res));
    dispatch(setDocument(res));
    dispatch(setResources(res));
    dispatch(setSimilarities(res));
    // dispatch(refreshUser());
  }
}
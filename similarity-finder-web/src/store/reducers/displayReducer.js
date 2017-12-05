import { ADD_FILE, CONTACT_CARD, DESCRIPTION_CARD, AUTHOR_CARD, CLOSE_CARD, PROGRESS_INDICATOR } from '../actions/types';

const initialState = {
  showUploadCard: false,
  showContactCard: false,
  showDescriptionCard: false,
  showAuthorCard: false,
  showProgress: false,
  frame: null,
}

const displayReducer = (state = initialState, action) => {
  let newState = { ...state };
  switch (action.type) {

    case ADD_FILE:
      newState.frame = action.payload.frame;
      newState.showUploadCard = true;
      return newState;
    
    case CONTACT_CARD:
      newState.showContactCard = true;
      return newState;
    
    case DESCRIPTION_CARD:
      newState.showDescriptionCard = true;
      return newState;

    case AUTHOR_CARD:
      newState.showAuthorCard = true;
      return newState;

    case PROGRESS_INDICATOR:
      newState.showProgress = true;
      return newState;

    case CLOSE_CARD:
      return initialState;

    default:
      return state;
  }
}

export default displayReducer;
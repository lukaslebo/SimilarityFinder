import { ADD_FILE, CANCEL_UPLOAD } from '../actions/types';

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
    case CANCEL_UPLOAD:
      return initialState;
    default:
      return state;
  }
}

export default displayReducer;
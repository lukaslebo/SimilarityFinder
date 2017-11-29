import { ADD_FILE, CANCEL_UPLOAD } from './types';

export const addButtonPressed = (frame) => ({
  type: ADD_FILE,
  payload: { frame },
})

export const cancelUpload = () => ({
  type: CANCEL_UPLOAD,
})


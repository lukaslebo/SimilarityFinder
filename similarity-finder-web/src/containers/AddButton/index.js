import React from 'react';
import { connect } from 'react-redux';

import { addButtonPressed } from '../../store/actions';

class AddButton extends React.Component {

  addFile = (e) => {
    let frame = e.target.parentNode.parentNode.getAttribute('id');
    if (frame === "leftframe") {
      frame = "left";
    }
    else if (frame === "rightframe") {
      frame = "right";
    }
    else {
      frame = null;
    }
    this.props.dispatch(addButtonPressed(frame));
  }

  render() {
    return (
      <div id={ this.props.id }>
        <button className="btn btn-outline-primary float-right clickable" onClick={ this.addFile }>Add</button>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(AddButton);
import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { docRemove } from '../../store/actions';

class RemoveButton extends React.Component {

  remove = (e) => {
    let frame = e.target.parentNode.parentNode.getAttribute('id');
    let apiSuffix;
    if (frame === 'leftframe') {
      if (this.props.document === null) {
        return;
      }
      apiSuffix = '/removeDocument';
    }
    else if (frame === 'rightframe') {
      if (this.props.resources.length === 0) {
        return;
      }
      apiSuffix = '/removeResource';
    }
    this.props.dispatch(docRemove(apiSuffix));
  }

  render() {
    return (
      <div id={ this.props.id }>
        <button className="btn btn-outline-primary remove-button float-right" onClick={ this.remove }>Remove</button>
      </div>
    );
  }

}

const mapStateToProps = (state) => ({
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(RemoveButton);
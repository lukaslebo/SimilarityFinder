import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import Moment from 'moment';

import { refreshUser } from '../../store/actions';

class RunButton extends React.Component {

  run = () => {
    var expiresAt = new Moment(this.props.expiresAt.slice(0, 3).join("-").concat(" ",this.props.expiresAt.slice(3, 6).join(":")).concat(".",this.props.expiresAt[6]));
    console.log(this.props.isLoggedin);
    console.log(this.props.userId);
    console.log("expires in " + expiresAt.toNow(true));
    // this.props.dispatch(refreshUser());
  }

  render() {
    return (
      <div id={ this.props.id }>
        <p className="btn-description">Find <span>Similarities</span></p>
        <button type="button" id="run-button" className="btn btn-outline-primary" onClick={ this.run }>Run</button>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(RunButton);
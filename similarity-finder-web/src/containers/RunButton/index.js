import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import LoadingIndicator from '../LoadingIndicator';

import WebSocket from '../../websocketUtils';

import { setProgress, showProgress, closeCard, getSimilarities } from '../../store/actions';

class RunButton extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      socket: null,
      requestInProcess: false,
    };
  }

  componentDidUpdate = () => {
    if (this.state.socket === null && this.props.isLoggedin) {
      const socket =  new WebSocket(this.startDetection);
      this.setState({
        socket: socket,
      });
    }
  }

  startDetection = (isConnected) => {
    if (isConnected && !this.state.requestInProcess) {
      const startURL = `/app/start-detection/${ this.props.userId }`;
      // const startURL = `/app/start-dummy-detection/${ this.props.userId }`;
      this.state.socket.send(startURL);
      this.setState({
        requestInProcess: true,
      });
      this.props.dispatch(removeSimilarities());
    }
  }

  run = () => {
    if (this.props.document == null || this.props.resources.length === 0) {
      return;
    }
    if (!this.state.socket.isConnected) {
      const URL = `/user-progress/${ this.props.userId }`;
      this.state.socket.connect(URL, this.updateProgress);
    }
  }

  updateProgress = (msg) => {
    if (msg.status === 'ok' && !this.props.showProgress) {
      this.props.dispatch(showProgress());
    }
    if (msg.progress >= 0) {
      this.props.dispatch(setProgress(msg.progress));
    }
    if (msg.isDone) {
      this.props.dispatch(getSimilarities());
      setTimeout(() => { 
        this.props.dispatch(closeCard());
        this.props.dispatch(setProgress(null)); }
      , 500);
      this.state.socket.disconnect();
      this.setState({
        requestInProcess: false,
      });
    }
  }

  hover = () => {
    window.$('.btn-description>span').css('color','red');
  }

  unhover = () => {
    window.$('.btn-description>span').css('color','');
  }

  render() {
    let overlay;
    if (this.props.showProgress) {
      overlay = <LoadingIndicator val={ `${ this.props.progress }` } adaptive-color/>
    }
    return (
      <div id={ this.props.id }>
        <div className="constrain">
          <p className="btn-description">Find <span>Similarities</span></p>
          <button id="run-button" className="btn btn-outline-primary clickable" onClick={ this.run } onMouseEnter={ this.hover } onMouseLeave={ this.unhover }>Run</button>
        </div>
        { overlay }
      </div>
    );
  }
  
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(RunButton);
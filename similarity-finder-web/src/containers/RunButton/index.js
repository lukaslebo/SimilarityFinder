import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import LoadingIndicator from '../LoadingIndicator';

import WebSocket from '../../websocketUtils';

import { setProgress, showProgress, closeCard, getSimilarities, removeSimilarities } from '../../store/actions';

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
      // this.props.dispatch(showProgress());
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
      window.$('#detection-info').modal('show')
    }
  }

  hover = () => {
    window.$('.btn-description>span').css('color','red');
  }

  unhover = () => {
    window.$('.btn-description>span').css('color','');
  }

  infoModal = () => {
    let sims = this.props.similarities;
    let simObj = {};
    for (let res of this.props.resources) {
      simObj[res.id] = { name: res.fileName, simCount: 0 };
    }
    for (let sim of sims) {
      simObj[sim.resourceId].simCount++;
    }
    let simArr = window.$.map(simObj, (val, index) => { return val; });
    let header = 'No Similarities detected.';
    if (this.props.similarities.length > 0) {
      header = `${ this.props.similarities.length } Similarities detected.`
    }
    return (
      <div className="modal fade" id="detection-info" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
        <div className="modal-dialog" role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLongTitle">Similarity Detection Complete</h5>
              <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <h4>{ header }</h4>
              { simArr.map( (val, index) => {
                return (<p key={ index }>{ val.name } has { val.simCount } similarit{ val.simCount !== 1 ? 'ies' : 'y' }.</p>);
              }) }
            </div>
          </div>
        </div>
      </div>
    );
  }

  render() {
    let overlay;
    if (this.props.showProgress) {
      overlay = <LoadingIndicator val={ `${ this.props.progress }` } adaptive-color/>
    }
    return (
      <div id={ this.props.id }>
        <div className="constrain">
          <p className="btn-description nonselectable">Find <span>Similarities</span></p>
          <button id="run-button" className="btn btn-outline-primary clickable" onClick={ this.run } onMouseEnter={ this.hover } onMouseLeave={ this.unhover }>Run</button>
        </div>
        { this.infoModal() }
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
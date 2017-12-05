import React from 'react';
import './index.css';
import { connect } from 'react-redux';

class LoadingIndicator extends React.Component {
  render() {
    let value = '';
    if (this.props.val >= 0) {
      value = this.props.val + '%';
    }
    return (
      <div className="dimmer">
        <div className="l-wrapper">
          
          <div className="nonselectable spinner-progress">{ value }%</div>
          <svg viewBox="-60 -60 240 240" version="1.1" xmlns="http://www.w3.org/2000/svg" xlink="http://www.w3.org/1999/xlink" xmlnsXlink="http://www.w3.org/1999/xlink">
            
            <symbol id="s--circle">
              <circle r="10" cx="20" cy="20"></circle>
            </symbol>
            
            <g className="g-circles g-circles--v1">
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>  
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
              <g className="g--circle">
                <use xlinkHref="#s--circle" className="u--circle"/>
              </g>
            </g>
        </svg>
      </div>
    </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state,
});

export default connect(mapStateToProps)(LoadingIndicator);
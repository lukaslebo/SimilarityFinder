import React from 'react';
import './index.css';
import { connect } from 'react-redux';

class LoadingIndicator extends React.Component {

  getColor = (val) => {
    var hue=((val)*120).toString(10);
    return ["hsl(",hue,",100%,50%)"].join("");
  }

  percentage = () => {
    let value = '';
    if (this.props.val >= 0) {
      value = this.props.val + '%';
    }
    return value;
  }

  colorSetter = () => {
    const domElement = window.$('div.spinner-progress');
    if (!this.props['adaptive-color'] || this.percentage().length === 0) {
      domElement.css('color', 'rgba(0, 0, 0, 0.6)');
      return;
    }
    const val = Number(this.props.val)/100;
    const color = this.getColor(val);
    domElement.css('color', color);
  }

  componentDidMount = () => {
    this.colorSetter();
  }

  componentDidUpdate = () => {
    this.colorSetter();
  }

  render() {
    return (
      <div className="dimmer">
        <div className="l-wrapper">
          
          <div className="nonselectable spinner-progress">{ this.percentage() }</div>
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

export default connect()(LoadingIndicator);
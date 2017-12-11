import React from 'react';
import './index.css';
import { connect } from 'react-redux';

class ProgressBar extends React.Component {

  setProgress = (val) => {
    if (val < 0 || val > 1 ) {
      return;
    }
    const width = Math.round(val*100) + '%';
    window.$('#bar').css('width', width);  
  }

  componentDidMount = () => {
    this.setProgress(this.props.value);
  }

  componentDidUpdate = () => {
    this.setProgress(this.props.value);
  }

  render() {
    return (
      <div>
        <div id="seconds" class="seconds">
          <div id="bar" class="bar"></div>
        </div>
      </div>
    );
  }

}

export default connect()(ProgressBar);
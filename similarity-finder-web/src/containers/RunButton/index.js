import React from 'react';
import './index.css';

class RunButton extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id }>
        <p className="btn-description">Find <span>Similarities</span></p>
        <button type="button" id="run-button" className="btn btn-outline-primary">Run</button>
      </div>
    );
  }
}


export default RunButton;
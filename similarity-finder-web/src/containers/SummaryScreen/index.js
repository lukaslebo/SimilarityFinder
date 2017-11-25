import React from 'react';
import './index.css';

class SummaryScreen extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id } className="summaryscreen">
        Summary
      </div>
    );
  }
}


export default SummaryScreen;
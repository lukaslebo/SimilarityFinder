import React from 'react';
import './index.css';
import { connect } from 'react-redux';

class SummaryScreen extends React.Component {

  summaryText = () => {
    const n = this.props.similarities.length;
    if (n === 0) {
      return 'No similarities detected.';
    }
    else {
      return `${ n } similarities detected.`;
    }
  }

  render() {
    return (
      <div id={ this.props.id } className="summaryscreen primary-outline nonselectable">
        { this.summaryText() }
      </div>
    );
  }

}

const mapStateToProps = (state) => ({
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(SummaryScreen);
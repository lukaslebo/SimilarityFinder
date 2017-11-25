import React from 'react';
import './index.css';

class RemoveButton extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id }>
        <button className="btn btn-outline-primary remove-button float-right">Remove</button>
      </div>
    );
  }
}


export default RemoveButton;
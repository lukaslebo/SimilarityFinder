import React from 'react';
import './index.css';

class AddButton extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id }>
        <button className="btn btn-outline-primary add-button float-right">Add</button>
      </div>
    );
  }
}


export default AddButton;
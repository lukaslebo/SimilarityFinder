import React from 'react';
import './index.css';

class Header extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id }>
        <h3><span>Similarity</span> Detection Tool</h3>
      </div>
    );
  }
}


export default Header;
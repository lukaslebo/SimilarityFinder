import React from 'react';
import './index.css';
import Logo from './logo.js';

class Header extends React.Component {

  render() {
    return (
      <div id={ this.props.id }>
        <h3 className="blue-hover"><Logo/><span>Similarity</span> Detection Tool</h3>
      </div>
    );
  }

}

export default Header;
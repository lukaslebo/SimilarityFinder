import React from 'react';
import './index.css';
import Logo from './logo.js';

class Header extends React.Component {

  render() {
    return (
      <div id={ this.props.id }>
        <h3><div className="title-container nonselectable"><Logo/><div className="left-title">Similarity</div><div className="right-title">Finder</div></div></h3>
      </div>
    );
  }

}

export default Header;
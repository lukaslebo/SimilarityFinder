import React from 'react';
import './index.css';

class Footer extends React.Component {

  pd = (e) => {
    e.preventDefault();
  }

  render() {
    return (
      <div id={ this.props.id }>
        <ul className="footer">
          <li><a href="" onClick={ this.pd }>Author</a></li>
          <li>|</li>
          <li><a href="" onClick={ this.pd }>Description</a></li>
          <li>|</li>
          <li><a href="" onClick={ this.pd }>Contact</a></li>
        </ul>
      </div>
    );
  }

}


export default Footer;
import React from 'react';
import './index.css';

class Footer extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div id={ this.props.id }>
        <ul className="footer">
          <li><a href="">Author</a></li>
          <li>|</li>
          <li><a href="">Description</a></li>
          <li>|</li>
          <li><a href="">Contact</a></li>
        </ul>
      </div>
    );
  }
}


export default Footer;
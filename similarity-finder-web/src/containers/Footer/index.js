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
          <li>Author</li>
          <li>|</li>
          <li>Description</li>
          <li>|</li>
          <li>Contact</li>
        </ul>
      </div>
    );
  }
}


export default Footer;
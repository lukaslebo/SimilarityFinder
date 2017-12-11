import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { showInfoCard } from '../../store/actions';

class Footer extends React.Component {

  showItem = (e) => {
    const itemName = e.target.getAttribute('data-name');
    this.props.dispatch(showInfoCard(itemName));
  }

  render() {
    return (
      <div id={ this.props.id }>
        <ul className="footer">
          <li className="blue-hover clickable nonselectable" data-name="author" onClick={ this.showItem }>Author</li>
          <li className="nonselectable">|</li>
          <li className="blue-hover clickable nonselectable" data-name="description" onClick={ this.showItem }>Description</li>
          <li className="nonselectable">|</li>
          <li className="blue-hover clickable nonselectable" data-name="contact" onClick={ this.showItem }>Contact</li>
        </ul>
      </div>
    );
  }

}

export default connect()(Footer);
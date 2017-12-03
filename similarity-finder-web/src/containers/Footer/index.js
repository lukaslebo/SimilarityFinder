import React from 'react';
import './index.css';
import { connect } from 'react-redux';

class Footer extends React.Component {

  showItem = (e) => {
    console.log(e.target.getAttribute('data-name'));
  }

  render() {
    return (
      <div id={ this.props.id }>
        <ul className="footer">
          <li className="blue-hover clickable" data-name="author" onClick={ this.showItem }>Author</li>
          <li>|</li>
          <li className="blue-hover clickable" data-name="description" onClick={ this.showItem }>Description</li>
          <li>|</li>
          <li className="blue-hover clickable" data-name="contact" onClick={ this.showItem }>Contact</li>
        </ul>
      </div>
    );
  }

}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(Footer);
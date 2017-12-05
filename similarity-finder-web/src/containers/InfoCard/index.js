import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { closeCard } from '../../store/actions';

class InfoCard extends React.Component {

  infoContent = () => {
    if (this.props.showContactCard) {
      return this.contactHtml();
    }
    else if (this.props.showDescriptionCard) {
      return this.descriptionHtml();
    }
    else if (this.props.showAuthorCard) {
      return this.authorHtml();
    }
  }

  preventClickThrough = (e) => {
    e.stopPropagation();
  }

  cardClass = () => {
    if (this.props.showContactCard) {
       return "contact";
    } else if (this.props.showDescriptionCard) {
      return "description";
    } else if (this.props.showAuthorCard) {
      return "author";
    }
  }

  contactHtml = () => {
    return (
      <div>
        <p>You found a bug or want to give feedback?</p> 
        <a href="mailto:similaritydetector@gmail.com">similaritydetector@gmail.com</a>
        <p>I'm happy to hear from you!</p>
      </div>
    );
  }

  descriptionHtml = () => {
    return (
      <div>
        <p>
          This is a similarity detection tool. 
          You can use it to compare one document against one or more resources.<br/><br/>
          The documents will be uploaded to the server. 
          Only the parsed content of your documents will be temporarily stored. 
          After 3 hours of inactivity your session is timed out and all your content 
          will be ultimately removed from the server.<br/><br/>
          The tool is able to pick up similar text based on pure text comparison. Paraphrasing will not be detected.
        </p>
      </div>
    );
  }

  authorHtml = () => {
    return (
      <div>
        <p>
          Author and Core Contributor:<br/><br/>
          Lukas Lebovitz
        </p>
      </div>
    );
  }

  closeCard = () => {
    this.props.dispatch(closeCard());
  }

  render() {
    return (
      <div className="dimmer" onClick={ this.closeCard }>
        <div className={`info-card ${ this.cardClass() }`} onClick={ this.preventClickThrough }>
          { this.infoContent() }
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(InfoCard);
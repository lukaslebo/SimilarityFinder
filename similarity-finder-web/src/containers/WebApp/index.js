import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import Moment from 'moment';

import Header from '../Header';
import TextFrame from '../TextFrame';
import RemoveButton from '../RemoveButton';
import AddButton from '../AddButton';
import RunButton from '../RunButton';
import SummaryScreen from '../SummaryScreen';
import Footer from '../Footer';
import UploadCard from '../UploadCard';
import InfoCard from '../InfoCard';

import { getNewUser, loadSpecificUser, refreshUser } from '../../store/actions';


class WebApp extends React.Component {

  setCookie = (cname, cvalue, exmins) => {
    var d = new Date();
    d.setTime(d.getTime() + (exmins * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
  }

  getCookie = (cname) => {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
  }

  setupUserId = () => {
    if (!this.props.isLoggedin) {
      const userId = this.getCookie('userId');
      if (userId !== '') {
        this.props.dispatch(loadSpecificUser(userId));
      }
      else {
        this.props.dispatch(getNewUser());
      }
    }
    else {
      if (this.props.expiresAt.diff(Moment.utc(), 'minutes', true) <= 175) {
        this.props.dispatch(refreshUser());
      }
      this.setCookie('userId', this.props.userId, this.props.expiresAt.diff(new Moment(), 'minutes'));
    }
  }

  componentDidMount = () => {
    this.setupUserId();
  }

  componentDidUpdate = () => {
    this.setupUserId();
  }

  render() {
    let overlay;
    if (this.props.showUploadCard) {
      overlay = <UploadCard/>;
    }
    else if (this.props.showContactCard || this.props.showDescriptionCard || this.props.showAuthorCard) {
      overlay = <InfoCard/>;
    }
    return (
      <div className="grid-wrapper" >
        <Header id="header"/>
        <SummaryScreen id="topleft"/>
        <RunButton id="topright"/>
        <div id="leftframe">
          <RemoveButton/><AddButton/>
          <TextFrame id="left"/>
        </div>
        <div id="rightframe">
          <RemoveButton/><AddButton/>
          <TextFrame id="right"/>
        </div>
        <Footer id="footer"/>
        { overlay }
      </div>
    );
  }
  
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(WebApp);
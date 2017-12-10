import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import Header from '../Header';
import TextFrame from '../TextFrame';
import RemoveButton from '../RemoveButton';
import AddButton from '../AddButton';
import RunButton from '../RunButton';
import SummaryScreen from '../SummaryScreen';
import Footer from '../Footer';
import UploadCard from '../UploadCard';
import InfoCard from '../InfoCard';

import { getNewUser, loadSpecificUser } from '../../store/actions';


class WebApp extends React.Component {

  componentDidMount = () => {
    if (!this.props.isLoggedin) {
      //this.props.dispatch(getNewUser());
      const userId = '2419373b-8f09-4183-8c61-7d9bfb597543';
      this.props.dispatch(loadSpecificUser(userId));
    }
  }

  componentDidUpdate = () => {
    if (!this.props.isLoggedin) {
      this.props.dispatch(getNewUser());
    }
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
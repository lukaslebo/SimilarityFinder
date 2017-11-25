import React from 'react';
import './index.css';

import Header from '../../containers/Header';
import TextFrame from '../../containers/TextFrame';
import RemoveButton from '../../containers/RemoveButton';
import AddButton from '../../containers/AddButton';
import RunButton from '../../containers/RunButton';
import SummaryScreen from '../../containers/SummaryScreen';
import Footer from '../../containers/Footer';


class WebApp extends React.Component {

  // constructor(props) {
  //   super(props);
  // }

  render() {
    return (
      <div className="grid-wrapper" >
        <Header id="header"/>
        <SummaryScreen id="topleft"/>
        <RunButton id="topright"/>
        <div id="leftframe">
          <RemoveButton/><AddButton/>
          <TextFrame/>
        </div>
        <div id="rightframe">
          <RemoveButton/><AddButton/>
          <TextFrame/>
        </div>
        <Footer id="footer"/>
      </div>
    );
  }
}


export default WebApp;
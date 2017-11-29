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


class WebApp extends React.Component {

  render() {
    let overlay = null;
    if (this.props.showUploadCard) {
      overlay = <UploadCard/>;
    }
    return (
      <div className="grid-wrapper" >
        <Header id="header"/>
        <SummaryScreen id="topleft"/>
        <RunButton id="topright"/>
        <div id="leftframe">
          <RemoveButton frame="left"/><AddButton frame="left"/>
          <TextFrame/>
        </div>
        <div id="rightframe">
          <RemoveButton frame="right"/><AddButton frame="right"/>
          <TextFrame/>
        </div>
        <Footer id="footer"/>
        { overlay }
      </div>
    );
  }
  
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(WebApp);
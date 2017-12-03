import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { truncateByWidth } from '../../stringUtils';

import { selectResource } from '../../store/actions';

class TextFrame extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      windowsWidth: window.innerWidth,
    };
  }

  resize = () => {
    if (window.innerWidth === this.state.windowsWidth) {
      return;
    }
    this.setState({
      windowsWidth: window.innerWidth,
    });
  }

  componentDidMount() {
    window.addEventListener('resize', this.resize)
  }
  
  componentWillUnmount() {
    window.removeEventListener('resize', this.resize)
  }

  documentSelector = () => {
    if (this.props.id === 'left') {
      return this.props.document;
    }
    else if (this.props.id === 'right') {
      if (this.props.resources.length === 0) {
        return null;
      }
      return this.props.resources[this.props.resourceIndex];
    }
  }

  defaultMessage = () => {
    const leftMsg = <div className="default-msg">Add a document</div>;
    const rightMsg = <div className="default-msg">Add one or more resources</div>;
    if (this.props.id === 'left') {
      return leftMsg;
    }
    else if (this.props.id === 'right') {
      return rightMsg;
    }
  }

  textFormatter = (doc) => {
    if (this.props.similarities.length === 0) {
      return doc.content;
    }
  }

  displayText = () => {
    const doc = this.documentSelector();
    if (doc === null) {
      return this.defaultMessage();
    }
    return this.textFormatter(doc);
  }

  getFilename = () => {
    const doc = this.documentSelector();
    if (doc === null) {
      return "No file selected.";
    }
    let fileName;
    if (this.props.id === 'left') {
      fileName = truncateByWidth(doc.fileName, window.$('.leftside-wide').width()+35.59);
    }
    else if (this.props.id === 'right') {
      fileName = truncateByWidth(doc.fileName, window.$('.leftside-wide').width());
    }
    return fileName;
  }

  resourceSelector = (e) => {
    const resourceIndex = Number(e.target.getAttribute('data-resource-index'));
    this.props.dispatch(selectResource(resourceIndex));
  }

  dropdownMenu = () => {
    if (this.props.resources.length === 0) {
      return <div className="dropdown-item">No documents.</div>;
    }
    else if (this.props.resources.length === 1) {
      return <div className="dropdown-item">No other documents.</div>;
    }
    return this.props.resources.map( (doc, index) => {
      let fileName = truncateByWidth(doc.fileName, window.$('.leftside-wide').width());
      return <div key={ index } className="dropdown-item clickable" data-resource-index={ index } onClick={ this.resourceSelector }>{ fileName }</div>;
    }).filter( (doc, index) => index !== this.props.resourceIndex);
  }

  fileDisplay = () => {
    const fileName = this.getFilename();
    if (this.props.id === 'left') {
      return <div className="btn primary-outline file-display">{ fileName }</div>;
    }
    else if (this.props.id === 'right') {
      return (
        <div className="btn-group file-display">
          <div className="btn primary-outline leftside-wide">{ fileName }</div>
          <button className="btn primary-outline clickable dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <span className="sr-only">Toggle Dropdown</span>
          </button>
          <div className="dropdown-menu leftside-wide">
            {
              this.dropdownMenu()
            }
            
          </div>
        </div>
      );
    }
    return fileName;
  }

  render() {
    return (
      <div className="frame-container">
        { this.fileDisplay() }
        <div className="primary-outline textframe">
          { this.displayText() }
        </div>
      </div>
    );
  }

}

const mapStateToProps = (state) => ({
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(TextFrame);
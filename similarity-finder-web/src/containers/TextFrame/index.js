import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { selectResource } from '../../store/actions';

class TextFrame extends React.Component {

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
    return doc.fileName;
  }

  resourceSelector = (e) => {
    const resourceIndex = Number(e.target.getAttribute('data-resource-index'));
    this.props.dispatch(selectResource(resourceIndex));
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
              this.props.resources.map( (doc, index) => {
                return <div key={ index } className="dropdown-item clickable" data-resource-index={ index } onClick={ this.resourceSelector }>{ doc.fileName }</div>;
              })
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
import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { closeUpload, fileUpload } from '../../store/actions';

class UploadCard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      fileList: [],
    }
  }

  selectFiles = (e) => {
    let files = e.target.files;
    files = window.$.map(files, (value, index) => value);
    this.setState({
      fileList: files,
    });
  }

  fileList = () => {
    switch(this.state.fileList.length) {
      case 0:
        return "No Files Selected";
      case 1:
        return this.state.fileList[0].name;
      default:
        return `${ this.state.fileList.length } Files selected`;
    }
  }

  tooltip = () => {
    if (this.state.fileList.length < 2) {
      return '';
    }
    let filenames = this.state.fileList.map(file => file.name);
    return filenames.join("<br/>");
  }

  upload = () => {
    if (this.state.fileList.length === 0) {
      return;
    }
    let apiSuffix;
    switch(this.props.frame) {
      case "left":
        apiSuffix = "/setDoc";
        break;
      case "right":
       apiSuffix = "/addResource";
       break;
      default:
       apiSuffix = undefined;
    }
    this.props.dispatch(fileUpload(this.state.fileList, apiSuffix));
  }

  cancleUpload = () => {
    this.props.dispatch(closeUpload());
  }

  componentDidMount = () => {
    window.$('[data-toggle="tooltip"]').tooltip();
  }

  componentDidUpdate() {
    window.$('[data-toggle="tooltip"]').tooltip();
  }

  render() {
    return (
      <div className="dimmer">
        <div className="uploadcard">
          <label className="btn btn-outline-primary clickable">
            <input 
              id="file-upload" type="file" accept=".txt,.pdf" 
              multiple={ this.props.frame === "right" ? true:false}
              hidden onChange={ this.selectFiles }
            />
            Browse
          </label>
          <div className="file-list">
            <p 
              className="auto-width" data-toggle="tooltip" data-placement="bottom" 
              data-original-title={ this.tooltip() } data-html
            >
              { this.fileList() }
            </p>
          </div>
          <button className="btn btn-outline-primary upload clickable" onClick={ this.cancleUpload }>Cancel</button>
          <button className="btn btn-outline-primary cancel clickable" onClick={ this.upload }>Upload</button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(UploadCard);
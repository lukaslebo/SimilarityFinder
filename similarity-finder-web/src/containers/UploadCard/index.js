import React from 'react';
import './index.css';
import { connect } from 'react-redux';

import { truncateByWidth } from '../../stringUtils';

import { closeCard, fileUpload, textUpload } from '../../store/actions';

class UploadCard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      fileList: [],
      ulType: 'file',
      ulTitle: '',
      ulText: '',
      isUploading: false,
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
        return truncateByWidth(this.state.fileList[0].name, 378);
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

  upload = (e) => {
    if (e) {
      e.preventDefault();
    }
    if (this.state.ulType === 'file') {
      this.uploadFile();
    }
    else if (this.state.ulType === 'text') {
      this.uploadText();
    }
  }

  uploadFile = () => {
    if (this.state.fileList.length === 0 || this.state.isUploading) {
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
    }
    this.setState({
      isUploading: true,
    });
    this.props.dispatch(fileUpload(this.state.fileList, apiSuffix));
  }

  uploadText = () => {
    if (this.state.ulTitle.length === 0  || this.state.ulText.length === 0 || this.state.isUploading) {
      return;
    }
    let apiSuffix;
    switch(this.props.frame) {
      case "left":
        apiSuffix = "/setDocText";
        break;
      case "right":
       apiSuffix = "/addResourceText";
       break;
      default:
    }
    this.setState({
      isUploading: true,
    });
    this.props.dispatch(textUpload(this.state.ulTitle, this.state.ulText, apiSuffix));
  }

  changeHandler = (e) => {
    const val = e.currentTarget.value;
    const type = e.currentTarget.getAttribute('data-type');
    if (type === 'title') {
      this.setState({
        ulTitle: val,
      });
    }
    else if (type === 'text') {
      this.setState({
        ulText: val,
      });
    }
  }

  cancelUpload = (e) => {
    this.props.dispatch(closeCard());
  }

  componentDidMount = () => {
    window.$('[data-toggle="tooltip"]').tooltip();
  }

  componentDidUpdate() {
    window.$('[data-toggle="tooltip"]').tooltip();
  }

  typeSelector = (e) => {
    const type = e.target.getAttribute('data-ul-type');
    if (type === 'file') {
      this.setState({
        ulType: 'file',
      });
    }
    else if (type === 'text') {
      this.setState({
        ulType: 'text',
      })
    }
  }

  uploadCard = () => {
    if (this.state.ulType === 'file') {
      return this.uploadFileHtml();
    }
    else if (this.state.ulType === 'text') {
      return this.uploadTextHtml();
    }
  }

  uploadFileHtml = () => {
    return (
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
        <button className="btn btn-outline-primary upload clickable" onClick={ this.upload }>Upload</button>
        <button className="btn btn-outline-primary cancel clickable" onClick={ this.cancelUpload }>Cancel</button>
      </div>
    );
  }

  uploadTextHtml = () => {
    return (
      <form className="uploadcard text" onSubmit={ this.upload }>
        <label className="input-label">Title</label>
        <input className="input-title" type="text" placeholder="Enter a title here" data-type="title" value={ this.state.ulTitle } onChange={ this.changeHandler }/>
        <label className="input-label">Your text</label>
        <textarea className="input-text" type="text" placeholder="Enter your text here" data-type="text" value={ this.state.ulText } onChange={ this.changeHandler }/>
        <button type="submit" className="btn btn-outline-primary upload clickable" onClick={ this.upload }>Add</button>
        <button className="btn btn-outline-primary cancel-text clickable" onClick={ this.cancelUpload }>Cancel</button>
      </form>
    );
  }

  render() {
    return (
      <div className="dimmer">
        <div className="type-selector">
          <div className={`ul-type ul-file clickable nonselectable${ this.state.ulType === 'file' ? ' ul-active' : ''}`} data-ul-type="file" onClick={ this.typeSelector }>File</div>
          <div className={`ul-type ul-text clickable nonselectable${ this.state.ulType === 'text' ? ' ul-active' : ''}`} data-ul-type="text" onClick={ this.typeSelector }>Text</div>
        </div>
        { this.uploadCard() }
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(UploadCard);
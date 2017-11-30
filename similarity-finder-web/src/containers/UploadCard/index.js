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
    let filenames = this.state.fileList.map(el => el.name);
    return filenames.join("<br/>");
  }

  upload = () => {
    this.props.dispatch(fileUpload(this.state.fileList));
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
          <label className="btn btn-outline-primary">
            <input 
              id="file-upload" 
              type="file" accept=".txt,.pdf" 
              multiple={ this.props.frame === "right" ? true:false}
              hidden
              onChange={ this.selectFiles }
            />
            Browse
          </label>
          <div className="file-list">
            <a 
              data-toggle="tooltip" 
              data-placement="bottom"
              data-original-title={ this.tooltip() }
              data-html
            >
              { this.fileList() }
            </a>
          </div>
          <button className="btn btn-outline-primary upload" onClick={ this.cancleUpload }>Cancel</button>
          <button className="btn btn-outline-primary cancel" onClick={ this.upload }>Upload</button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(UploadCard);
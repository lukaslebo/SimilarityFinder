import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import { cancelUpload } from '../../store/actions';

class UploadCard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      fileList: [],
    }
  }

  selectFiles = (e) => {
    console.log(e.target.files);
    this.setState({
      fileList: e.target.files,
    });
  }

  fileList = () => {
    switch(this.state.fileList.length) {
      case 0:
        return (
          <div>
            No Files Selected
          </div>
        );
      case 1:
        return (
          <div>
            { this.state.fileList[0].name }
          </div>
        );
      default:
        let tooltip = "";
        for (let file of this.state.fileList) {
          if (tooltip.length > 0) {
            tooltip += "\n";
          }
          tooltip += file.name
        }
        return (
          <div title={ tooltip }>
            { this.state.fileList.length } Files selected
          </div>
        );
    }
  }

  upload = () => {
    console.log('upload files ...');
  }

  cancleUpload = () => {
    this.props.dispatch(cancelUpload());
  }

  render() {
    return (
      <div className="dimmer">
        <div className="uploadcard">
          <label htmlFor="file-upload" className="btn btn-outline-primary">
            <input 
              id="file-upload" 
              type="file" accept=".txt,.pdf" 
              multiple={ this.props.frame === "right" ? true:false}
              hidden
              onChange={ this.selectFiles }
            />
            Browse
          </label>
          { this.fileList() }
          <button className="btn btn-outline-primary" onClick={ this.upload }>Upload</button>
          <button className="btn btn-outline-primary" onClick={ this.cancleUpload }>Cancel</button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  ...state.displayReducer,
});

export default connect(mapStateToProps)(UploadCard);
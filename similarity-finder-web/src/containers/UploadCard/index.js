import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import { cancelUpload } from '../../store/actions';
import $ from 'jquery';
import { tooltip } from 'bootstrap';

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
        return "No Files Selected";
      case 1:
        return this.state.fileList[0].name;
      default:
        return `${ this.state.fileList.length } Files selected`;
    }
  }

  upload = () => {
    console.log('upload files ...');
  }

  cancleUpload = () => {
    this.props.dispatch(cancelUpload());
  }

  componentDidMount = () => {
    $(() => {
      $('[data-toggle="tooltip"]').tooltip();
    });
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
          <a 
            data-toggle="tooltip" 
            data-placement="bottom"
            title="dada"
            data-original-title="Tooltip on bottom" 
            className="file-list"
          >
            { this.fileList() }
          </a>
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
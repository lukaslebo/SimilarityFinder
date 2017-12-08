import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import Rangy from 'rangy';

class HighlightedText extends React.Component {

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

  similaritySelector = () => {
    if (this.props.id === 'left') {
      return this.props.similarities.map(el => el).sort( (a, b) => {
        return a.startIndex < b.startIndex ? -1 : 1;
      });
    }
    else if (this.props.id === 'right') {
      const resourceId = this.props.resources[this.props.resourceIndex].id
      return this.props.similarities.filter( el => {
        return el.resourceId === resourceId;
      }).sort( (a, b) => {
        return a.resourceStartIndex < b.resourceStartIndex ? -1 : 1;
      });
    }
  }

  adjustIndexes = (similarities) => {
    const doc = this.documentSelector();
    const words = doc.parsedDocument_punctuated;
    for (let sim of similarities) {
      if (this.props.id === 'left') {
        sim.startIndex = words.slice(0, sim.startIndex).join(" ").length;
        sim.startIndex = sim.startIndex === 0 ? sim.startIndex : sim.startIndex + 1;
        sim.endIndex = words.slice(0, sim.endIndex+1).join(" ").length;
      } else if (this.props.id === 'right') {
        sim.resourceStartIndex = words.slice(0, sim.resourceStartIndex).join(" ").length;
        sim.resourceStartIndex = sim.resourceStartIndex === 0 ? sim.resourceStartIndex : sim.resourceStartIndex + 1;
        sim.resourceEndIndex = words.slice(0, sim.resourceEndIndex+1).join(" ").length;
      }
    }
  }

  setHighlighting = () => {
    let similarities = this.similaritySelector();
    this.adjustIndexes(similarities);
    const html = window.$(`div.text-container-${ this.props.id }`)[0];
    for (let sim of similarities) {
      if (this.props.id === 'left'){
        this.highlightRange(html, sim.startIndex, sim.endIndex, sim);
      } else if (this.props.id === 'right') {
        this.highlightRange(html, sim.resourceStartIndex, sim.resourceEndIndex, sim);
      }
    }
  }

  highlightRange = (html, start, end, sim) => {
    var range = Rangy.createRange();
    range.moveToBookmark({
        containerNode: html,
        start: start,
        end: end
    });
    range.splitBoundaries();
    var textNodes = range.getNodes([3]);
    for (let textNode of textNodes) {
        let span = document.createElement("span");
        span.className = "highlight";
        span.setAttribute('data-sim-id', sim.id);
        textNode.parentNode.insertBefore(span, textNode);
        span.appendChild(textNode);
    }
  }

  docText = () => {
    const doc = this.documentSelector();
    return doc.content;
  }

  clickSimilarity = (e) => {
    console.log(e.target.getAttribute('data-sim-id'));
  }

  componentDidMount = () => {
    this.setHighlighting();
  }

  componentDidUpdate = () => {
    this.setHighlighting();
  }

  render() {
    return (
      <div className={ `text-container-${ this.props.id }` } onClick={ this.clickSimilarity }>
        { this.docText() }
      </div>
    );
  }

}

const mapStateToProps = (state) => ({
  ...state.webApiReducer,
});

export default connect(mapStateToProps)(HighlightedText);
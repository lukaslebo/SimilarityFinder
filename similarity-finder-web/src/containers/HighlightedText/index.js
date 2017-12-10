import React from 'react';
import './index.css';
import { connect } from 'react-redux';
import Rangy from 'rangy';

import { selectResource } from '../../store/actions';

class HighlightedText extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      simcount: 0,
    };
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

  similaritySelector = () => {
    const sim = JSON.parse(JSON.stringify(this.props.similarities));
    if (this.props.id === 'left') {
      return sim.sort( (a, b) => {
        return a.startIndex <= b.startIndex ? -1 : 1;
      });
    }
    else if (this.props.id === 'right') {
      const resourceId = this.props.resources[this.props.resourceIndex].id
      return sim.filter( el => {
        return el.resourceId === resourceId;
      }).sort( (a, b) => {
        return a.resourceStartIndex <= b.resourceStartIndex ? -1 : 1;
      });
    }
  }

  adjustIndexes = (similarities) => {
    let corr = 0;
    const doc = this.documentSelector();
    const words = doc.parsedDocument_punctuated;
    for (let sim of similarities) {
      if (this.props.id === 'left') {
        sim.startIndex = words.slice(0, sim.startIndex).join(" ").length;
        while (true) {
          let string = doc.content.substr(0, sim.startIndex+corr);
          let matches = this.occurrences(string, "\n\n", true);
          if (matches === corr) {
            break;
          }
          corr = matches;
        }
        sim.startIndex = sim.startIndex === 0 ? sim.startIndex : sim.startIndex + 1 + corr;
        sim.endIndex = words.slice(0, sim.endIndex+1).join(" ").length + corr;
      } else if (this.props.id === 'right') {
        sim.resourceStartIndex = words.slice(0, sim.resourceStartIndex).join(" ").length;
        while (true) {
          let string = doc.content.substr(0, sim.resourceStartIndex+corr);
          let matches = this.occurrences(string, "\n\n", true);
          if (matches === corr) {
            break;
          }
          corr = matches;
        }
        sim.resourceStartIndex = sim.resourceStartIndex === 0 ? sim.resourceStartIndex + corr : sim.resourceStartIndex + 1 + corr;
        sim.resourceEndIndex = words.slice(0, sim.resourceEndIndex+1).join(" ").length + corr;
      }
    }
  }

  occurrences = (string, subString, allowOverlapping) => {
        string += "";
        subString += "";
        if (subString.length <= 0) return (string.length + 1);
    
        let n = 0,
            pos = 0,
            step = allowOverlapping ? 1 : subString.length;
    
        while (true) {
            pos = string.indexOf(subString, pos);
            if (pos >= 0) {
                ++n;
                pos += step;
            } else break;
        }
        return n;
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
    let trigger = '';
    const doc = this.documentSelector();
    if (this.similaritySelector().length === 0) {
      trigger = ' ';
    }
    return doc.content+trigger;
  }

  clickSimilarity = (e) => {
    const simId = e.target.getAttribute('data-sim-id');
    if (simId && this.props.id === 'left') {
      const sim = this.similaritySelector().filter(el => el.id === simId)[0];
      let resourceIndex;
      this.props.resources.forEach( (el, index) => {
        if (el.id === sim.resourceId) {
          resourceIndex = index;
        }}
      )
      setTimeout( () => {
        const correspondingSpan = window.$(`div.text-container-right [data-sim-id=${simId}]`)[0];
        const scrollbox = window.$('div.text-container-right')[0].parentElement;
        this.scrollTo(scrollbox, correspondingSpan.offsetTop - 250, 500);
      } , 0);
      this.props.dispatch(selectResource(resourceIndex));
    }
    else if (simId && this.props.id === 'right') {
      setTimeout( () => {
        const correspondingSpan = window.$(`div.text-container-left [data-sim-id=${simId}]`)[0];
        const scrollbox = window.$('div.text-container-left')[0].parentElement;
        this.scrollTo(scrollbox, correspondingSpan.offsetTop - 250, 500);
      } , 0);
    }
  }

  scrollTo = (element, to, duration) => {
    var start = element.scrollTop,
        change = to - start,
        currentTime = 0,
        increment = 20;

    const animateScroll = function(){
        const easeInOutQuad = (t, s, c, d) => {
            t /= d/2;
            if (t < 1) {
              return c/2*t*t + s;
            }
            t--;
            return -c/2 * (t*(t-2) - 1) + s;
        }     
        currentTime += increment;
        var val = easeInOutQuad(currentTime, start, change, duration);
        element.scrollTop = val;
        if(currentTime < duration) {
            setTimeout(animateScroll, increment);
        }
    };
    animateScroll();
  }

  componentDidMount = () => {
    setupMouseHover();
    this.setHighlighting();
  }

  componentDidUpdate = () => {
    if (this.props.id === 'left') {
      const count = window.$('div.text-container-left').children().length;
      if (count > 0) {
        return;
      }
    }
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

const setupMouseHover = () => {
  setInterval( () => {
    window.$('.sim-active').removeClass('sim-active');
    const hovered = window.$('.highlight:hover');
    if (hovered.length > 0) {
      const simId = hovered[hovered.length-1].getAttribute('data-sim-id');
      const simPair = window.$(`[data-sim-id=${simId}]`);
      simPair.addClass('sim-active');
      simPair.find('*').addClass('sim-active');
    }
  }, 100);
}
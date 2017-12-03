const width = (text, font = '16px Roboto') => {
  const o = window.$('<div>' + text + '</div>')
        .css({'position': 'absolute', 'float': 'left', 'white-space': 'nowrap', 'visibility': 'hidden', 'font': font})
        .appendTo(window.$('body'));
  const w = o.width();
  o.remove();
  return w;
}

const truncate = (text, maxLength, seperator = '...') => {
  if (text.length <= maxLength) {
    return text;
  }
  const pad = Math.round((maxLength-seperator.length)/2);
  const left = text.substr(0, pad);
  const right = text.substr(text.length-pad);
  return [left,seperator,right].join('');
}

const binarySearchText = (text, maxWidth, lower = 0, upper = text.length, bestIndex = 0) => {
  let mid = Math.floor( (upper + lower) / 2);
  const truncatedText = truncate(text, mid);
  const currentWidth = width(truncatedText);
  if (currentWidth < maxWidth && mid > bestIndex) {
    bestIndex = mid;
  }
  if (!(lower < upper)) {
    return bestIndex;
  }
  else if (currentWidth < maxWidth) {
    return binarySearchText(text, maxWidth, mid+1, upper, bestIndex);
  }
  else if (currentWidth > maxWidth) {
    return binarySearchText(text, maxWidth, lower, mid-1, bestIndex);
  }
}

export const truncateToWidth = (text, width) => {
  const bestLength = binarySearchText(text, width);
  return truncate(text, bestLength);
}

export const truncateByWidth = (text, maxWidth, seperator = '...') => {
  if (width(text) <= maxWidth) {
    return text;
  }
  const pad = (maxWidth-width(seperator))/2;
  let left;
  let right;
  for (let i = 1; i < text.length; i++) {
    if (width(text.substr(0,i)) > pad) {
      left=text.substr(0,i-1);
      break;
    }
  }
  for (let i = 1; i < text.length; i++) {
    if (width(text.substr(text.length-i)) > pad) {
      right=text.substr(text.length-i+1);
      break;
    }
  }
  return [left,seperator,right].join('');
}
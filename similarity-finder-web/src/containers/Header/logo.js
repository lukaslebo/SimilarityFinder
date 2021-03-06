import React from 'react';
import './index.css';

class Logo extends React.Component {
	render = () => { 
		const innerColor = '#b16b6a';
		const outerColor = '#885252'
		return (
			<div className="logo-container">
				<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlnsXlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
					viewBox="0 0 1000 1000" xmlSpace="preserve">
					<g>
						<g>
							<polygon fill={ innerColor } points="384.7,239 391.5,251.1 569,150.9 562.2,138.8 		"/>
						</g>
						<path fill={ outerColor } d="M821.1,872.6c15.3,25.6,6.9,58.8-18.8,74l0,0c-25.6,15.3-58.8,6.9-74-18.8L588.1,692.5
							c-15.3-25.6-6.8-58.8,18.8-74l0,0c25.6-15.3,58.8-6.8,74,18.8L821.1,872.6z"/>
						<g>
							<path fill={ outerColor } d="M503.7,63c4.8,25.6,9.2,53.1,12.7,82.2c45.6,16.1,85.8,48.1,111.4,93.5c56.3,99.7,21.1,226.2-78.7,282.5
								c-18.7,10.5-38.3,17.8-58.1,22.1c-7.2,26.6-15.6,53.3-25.5,80.1c41.3-2.6,82.6-14.4,121-36.1c136.3-76.9,184.4-249.7,107.5-386
								C652,126.9,581.4,78.8,503.7,63z"/>
							<path fill={ innerColor } d="M491,543.3c-86.4,18.7-178.6-19.8-224.3-100.8c-56.3-99.7-21.1-226.2,78.7-282.4
								c54.3-30.6,116.5-34.1,171-14.8c-3.5-29.1-7.8-56.6-12.7-82.2C439.2,49.8,369.9,58.9,308,93.8c-136.3,76.9-184.4,249.7-107.5,386
								c55.2,97.9,159.9,150.3,265,143.6C475.4,596.6,483.8,569.8,491,543.3z"/>
						</g>
						<g>
							<path fill={ innerColor } d="M367.4,334.8L313.8,365c0,0,15,26.6,19.7,34.8c-10.5,5.9-82.7,46.6-82.7,46.6l6.8,12l94.7-53.4
								c0,0-15-26.6-19.7-34.8c9.2-5.2,133.6-75.4,133.6-75.4l-6.8-12L367.4,334.8z"/>
						</g>
						<g>
							<path fill={ innerColor } d="M472.2,235.2c-17.3,9.8-23.4,31.8-13.7,49.1c9.8,17.3,31.8,23.4,49.1,13.7c11.7-6.6,18.3-18.8,18.3-31.4
								c0-6-1.5-12.1-4.6-17.6c-4.7-8.4-12.4-14.4-21.7-17C490.3,229.3,480.6,230.5,472.2,235.2z M470.6,277.5c-6-10.7-2.2-24.2,8.4-30.2
								c5.2-2.9,11.1-3.6,16.9-2c5.7,1.6,10.5,5.3,13.4,10.5v0c6,10.6,2.2,24.2-8.4,30.2C490.2,291.9,476.7,288.1,470.6,277.5z
								M521.3,248.9L521.3,248.9L521.3,248.9L521.3,248.9z"/>
						</g>
						<g>
							<path fill={ innerColor } d="M347.5,225.9c-8.4,4.7-14.4,12.4-17,21.7c-2.6,9.3-1.4,19,3.3,27.4c9.8,17.3,31.8,23.4,49.1,13.7
								c8.4-4.7,14.4-12.4,17-21.7c2.6-9.3,1.4-19-3.3-27.4l0,0c-4.7-8.4-12.4-14.4-21.7-17C365.6,220,355.9,221.2,347.5,225.9z
								M345.9,268.2c-2.9-5.2-3.6-11.2-2.1-16.9c1.6-5.7,5.3-10.5,10.5-13.4c5.2-2.9,11.1-3.6,16.8-2.1c5.7,1.6,10.5,5.3,13.4,10.5
								c1.9,3.4,2.9,7.1,2.9,10.9c0,2-0.3,4-0.8,6c-1.6,5.7-5.3,10.5-10.5,13.4C365.5,282.6,351.9,278.8,345.9,268.2z"/>
						</g>
						<g>
							<path fill={ innerColor } d="M562.9,386.1c-8.4,4.7-14.4,12.4-17,21.7c-2.6,9.3-1.4,19,3.3,27.4c4.7,8.4,12.4,14.4,21.7,17
								c9.3,2.6,19,1.4,27.4-3.3c17.3-9.8,23.4-31.8,13.7-49.1l0,0C602.2,382.4,580.2,376.3,562.9,386.1z M574.6,438.8
								c-5.7-1.6-10.5-5.3-13.4-10.5c-2.9-5.2-3.6-11.2-2.1-16.9c1.6-5.7,5.3-10.4,10.5-13.4c10.7-6,24.2-2.2,30.2,8.4l0,0
								c6,10.7,2.2,24.2-8.4,30.2C586.3,439.7,580.3,440.4,574.6,438.8z"/>
						</g>
						<g>
							<path fill={ innerColor } d="M486.5,328c-8.4,4.7-14.4,12.4-17,21.7c-2.6,9.3-1.4,19,3.3,27.4c9.8,17.3,31.8,23.4,49.1,13.7
								c8.4-4.7,14.4-12.4,17-21.7c2.6-9.3,1.4-19-3.3-27.4l0,0C525.8,324.4,503.8,318.3,486.5,328z M484.9,370.3
								c-2.9-5.2-3.6-11.1-2-16.9c1.6-5.7,5.3-10.5,10.5-13.4c10.7-6,24.2-2.2,30.2,8.4c1.9,3.4,2.9,7.1,2.9,10.9c0,2-0.3,4-0.8,6
								c-1.6,5.7-5.3,10.5-10.5,13.4C504.4,384.7,490.9,380.9,484.9,370.3z"/>
						</g>
						<g>
							<polygon fill={ innerColor } points="282.9,477 289.7,489 482.3,380.3 475.5,368.3 		"/>
						</g>
						<g>
							<path fill={ innerColor } d="M397.2,426.4l42.5,75.2L560,433.7l-6.8-12c0,0-97.5,55-108.3,61.1c-5.7-10-35.7-63.2-35.7-63.2
								L397.2,426.4z"/>
						</g>
					</g>
				</svg>
			</div>
		);
	} 

}

export default Logo;
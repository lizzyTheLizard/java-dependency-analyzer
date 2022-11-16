import jquery from 'jquery';
import FileSaver from 'file-saver';

export function save(){
	const fileContend = jquery('svg').prop('outerHTML');
	const file = new File([fileContend], 'image.svg', {type: 'image/svg+xml'});
	FileSaver.saveAs(file);
}

import jquery from 'jquery';
import * as nomnoml from 'nomnoml';
import {data} from './dummyData';
import './styles.scss';
import { Offcanvas } from 'bootstrap';
import {jDepsReader} from './input/JDepsReader';
import {nomnomlWriter} from './output/NomnomlWriter';
import {basePackage} from './transform/BasePackage';
import type {RemoveClassesSelection} from './transform/RemoveClasses';
import {removeClasses} from './transform/RemoveClasses';
import type {Filter} from './transform/Graph';
import FileSaver from 'file-saver';
import ClickEvent = JQuery.ClickEvent;
import {removePackages} from './transform/RemovePackages';

const jDepsInput = jDepsReader(data);

function update(): void {
	const filters = generateFilters();
	const graph = jDepsInput.filter(filters);
	const output = nomnomlWriter(graph);
	jquery('main').html(nomnoml.renderSvg(output));
	jquery('svg').addClass('img-fluid');
	jquery('g').on('click', (e) => selectElement(e));
}

function generateFilters(): Filter[] {
	const filters: Filter[] = [];

	const removeClassesSelection = jquery('#showClasses').val() as RemoveClassesSelection;
	if (removeClassesSelection) {
		filters.push(removeClasses(removeClassesSelection));
	}

	const basePackageSelection = jquery('#basePackage').val() as string;
	if (basePackageSelection) {
		filters.push(basePackage(basePackageSelection));
	}

	const removePackagesSelection = jquery('#ignorePackages').val() as string;
	if (removePackagesSelection) {
		filters.push(...removePackages(removePackagesSelection.split(',')));
	}

	return filters;
}

function selectElement(e: ClickEvent<HTMLElement, undefined, HTMLElement, HTMLElement>){
	const name = getFullName(e.target);
	const node = jDepsInput.findNode(name);

	jquery('#sidebarTitle').text(node.name);
	jquery('#sidebarBody').text(name);
	jquery('#sidebar')
		.map((i,e) => new Offcanvas(e))
		.each((i,e) => e.show());
	return false;
}

function getFullName(e: HTMLElement, lastName?: string) : string{
	const overallParent = jquery('svg').get(0);
	if(e === overallParent) {
		return '';
	}
	const name = e.getAttribute('data-name');
	if(!name || name === lastName) {
		return getFullName(e.parentElement, lastName);
	}
	const parentName = getFullName(e.parentElement, name);
	if(!parentName) {
		return name;
	} else {
		return parentName + '.' + name;
	}
}

function save(){
	const fileContend = jquery('svg').prop('outerHTML');
	const file = new File([fileContend], 'image.svg', {type: 'image/svg+xml'});
	FileSaver.saveAs(file);
}

update();
jquery('#updateButton').on('click', () => update());
jquery('#saveButton').on('click', () => save());



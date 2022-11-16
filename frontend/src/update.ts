import {nomnomlWriter} from './output/NomnomlWriter';
import jquery from 'jquery';
import * as nomnoml from 'nomnoml';
import {Filter, Graph, GraphNode} from './transform/Graph';
import {removeClasses, RemoveClassesSelection} from './transform/RemoveClasses';
import {basePackage} from './transform/BasePackage';
import {removePackages} from './transform/RemovePackages';
import {Offcanvas} from 'bootstrap';

export function update(jDepsInput: Graph): void {
	const filters = generateFilters();
	const graph = jDepsInput.filter(filters);
	const output = nomnomlWriter(graph);
	jquery('#img').html(nomnoml.renderSvg(output));
	jquery('svg').addClass('img-fluid');
	jquery('g').on('click', (e) => selectElement(e.target, jDepsInput));
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

function selectElement(target: HTMLElement, jDepsInput: Graph){
	const name = getFullName(target);
	const node = jDepsInput.findNode(name);
	const htmlSidebarElement = jquery('#sidebar').get(0);
	if(!htmlSidebarElement) {
		throw new Error('Sidebar not defined?');
	}
	const sidebar = new Offcanvas(htmlSidebarElement);
	updateSidebar(name, node, () => {
		update(jDepsInput);
		sidebar.hide();
	});
	sidebar.show();
	return false;
}

function updateSidebar(fullName: string, node: GraphNode, closeAndUpdate: () => void ){
	jquery('#sidebarTitle').text(node.name);
	jquery('#sidebarName').text(fullName);
	jquery('#sidebarType').text(node.type);

	jquery('#sidebarSetBase').off('click');
	jquery('#sidebarSetBase').on('click',() => {
		jquery('#basePackage').val(fullName);
		closeAndUpdate();
		return false;
	});
	jquery('#sidebarIgnore').off('click');
	jquery('#sidebarIgnore').on('click',() => {
		const oldIgnore = jquery('#ignorePackages').val();
		const newIgnore = oldIgnore ? oldIgnore + ',' +fullName : fullName;
		jquery('#ignorePackages').val(newIgnore);
		closeAndUpdate();
		return false;
	});
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

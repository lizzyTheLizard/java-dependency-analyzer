import jquery from 'jquery';
import * as nomnoml from 'nomnoml';
import {data} from './dummyData';
import './styles.scss';
import {jDepsReader} from './input/JDepsReader';
import {nomnomlWriter} from './output/NomnomlWriter';
import {basePackage} from './transform/BasePackage';
import type {RemoveClassesSelection} from './transform/RemoveClasses';
import {removeClasses} from './transform/RemoveClasses';
import type {Filter} from './transform/Graph';

function update(): void {
	const filters: Filter[] = [];

	const removeClassesSelection = jquery('#showClasses').val() as RemoveClassesSelection;
	if (removeClassesSelection) {
		filters.push(removeClasses(removeClassesSelection));
	}

	const basePackageSelection = jquery('#basePackage').val() as string;
	if (basePackageSelection) {
		filters.push(basePackage(basePackageSelection));
	}

	let graph = jDepsReader(data);
	filters.forEach(f => {
		graph = f(graph);
	});
	const output = nomnomlWriter(graph);

	console.log('DRAW');
	jquery('main').children().remove();
	jquery('main').append(nomnoml.renderSvg(output));
	jquery('svg').addClass('img-fluid');
}

update();
jquery('#updateButton').on('click', () => {
	update();
});

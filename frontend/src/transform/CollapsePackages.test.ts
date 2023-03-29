import { GraphNodeImpl } from '../transform/GraphImpl';
import { collapsePackages } from './CollapsePackages';
import { simpleTestGraph } from './TestData';

test('Collapse none', () => {
	const result = simpleTestGraph.filter(collapsePackages([]));
	expect(result).toEqual(simpleTestGraph);
});

test('Collapse other', () => {
	const result = simpleTestGraph.filter(collapsePackages(['package2']));
	expect(result).toEqual(simpleTestGraph);
});

test('Collapse package1', () => {
	const result = simpleTestGraph.filter(collapsePackages(['package1']));
	expect(result).toEqual({
		nodes: [
			new GraphNodeImpl([], [], 'package1', 'package1', 'PACKAGE'),
			new GraphNodeImpl([], [], 'package2', 'package2', 'PACKAGE'),
		],
		dependencies: simpleTestGraph.dependencies
	});
});

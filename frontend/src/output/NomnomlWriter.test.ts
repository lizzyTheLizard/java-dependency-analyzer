import {nomnomlWriter} from './NomnomlWriter';
import {Graph } from '../transform/Graph';
import {GraphImpl, GraphNodeImpl} from '../transform/GraphImpl';


test('Invalid Line', () => {
	const graph: Graph = new GraphImpl([
		new GraphNodeImpl( [
			new GraphNodeImpl([], [], 'test1', 'CLASS', ),
			new GraphNodeImpl([], [], 'test2', 'CLASS'),
		], [
			{ from: 'test1', to: 'test2', dependencies: [
				{from: 'package.test1', to: 'package.test2'},
			]},
		], 'package', 'PACKAGE')
	],[]);
	const output = nomnomlWriter(graph);
	expect(output).toEqual('[<package> package | [test1]\n[test2]\n[test1] --> [test2]]');
});

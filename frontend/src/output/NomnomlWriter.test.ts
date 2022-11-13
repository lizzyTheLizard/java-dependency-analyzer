import type {Type} from '../transform/Graph';
import {nomnomlWriter} from './NomnomlWriter';

test('Invalid Line', () => {
	const graph = {
		dependencies: [],
		nodes: [
			{
				name: 'package',
				type: 'PACKAGE' as Type,
				dependencies: [
					{
						from: 'test1',
						to: 'test2',
						dependencies: [
							{from: 'package.test1', to: 'package.test2'},
						],
					},
				],
				nodes: [
					{
						name: 'test1',
						type: 'CLASS' as Type,
						dependencies: [],
						nodes: [],
					}, {
						name: 'test2',
						type: 'CLASS' as Type,
						dependencies: [],
						nodes: [],
					},
				],
			},
		],
	};
	const output = nomnomlWriter(graph);
	expect(output).toEqual('[<package> package | [test1]\n[test2]\n[test1] --> [test2]]');
});

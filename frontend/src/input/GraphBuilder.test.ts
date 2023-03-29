import {GraphBuilder} from './GraphBuilder';

test('Constructor', () => {
	const target = new GraphBuilder();
	expect(target).toEqual({
		dependencies: [],
		nodes: [],
	});
});

test('Add Class', () => {
	const target = new GraphBuilder();
	target.addNode('test');
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'test',
				fullName: 'test',
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			},
		],
	});
});

test('Add Package', () => {
	const target = new GraphBuilder();
	target.addNode('package.test');
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'package',
				fullName: 'package',
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test',
						fullName: 'package.test',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			},
		],
	});
});

test('Add Dependency', () => {
	const target = new GraphBuilder();
	target.addNode('test1');
	target.addNode('test2');
	target.addDependency({from: 'test1', to: 'test2'});
	expect(target).toEqual({
		dependencies: [
			{
				from: 'test1',
				to: 'test2',
				dependencies: [
					{from: 'test1', to: 'test2'},
				],
			},
		],
		nodes: [
			{
				name: 'test1',
				fullName: 'test1',
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			}, {
				name: 'test2',
				fullName: 'test2',
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			},
		],
	});
});

test('Add Dependency In Packet', () => {
	const target = new GraphBuilder();
	target.addNode('package.test1');
	target.addNode('package.test2');
	target.addDependency({from: 'package.test1', to: 'package.test2'});
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'package',
				fullName: 'package',
				type: 'PACKAGE',
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
						fullName: 'package.test1',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					}, {
						name: 'test2',
						fullName: 'package.test2',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			},
		],
	});
});

test('Add Dependency Over Packet', () => {
	const target = new GraphBuilder();
	target.addNode('package1.test1');
	target.addNode('package2.test2');
	target.addDependency({from: 'package1.test1', to: 'package2.test2'});
	expect(target).toEqual({
		dependencies: [
			{
				from: 'package1',
				to: 'package2',
				dependencies: [
					{from: 'package1.test1', to: 'package2.test2'},
				],
			},
		],
		nodes: [
			{
				name: 'package1',
				fullName: 'package1',
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test1',
						fullName: 'package1.test1',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			}, {
				name: 'package2',
				fullName: 'package2',
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test2',
						fullName: 'package2.test2',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			},
		],
	});
});

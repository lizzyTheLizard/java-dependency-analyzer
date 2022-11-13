import {GraphImpl} from './GraphImpl';

test('Constructor', () => {
	const target = new GraphImpl();
	expect(target).toEqual({
		dependencies: [],
		nodes: [],
	});
});

test('Add Class', () => {
	const target = new GraphImpl();
	target.addNode('test');
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'test',
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			},
		],
	});
});

test('Add Package', () => {
	const target = new GraphImpl();
	target.addNode('package.test');
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'package',
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test',
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
	const target = new GraphImpl();
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
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			}, {
				name: 'test2',
				type: 'CLASS',
				dependencies: [],
				nodes: [],
			},
		],
	});
});

test('Add Dependency In Packet', () => {
	const target = new GraphImpl();
	target.addNode('package.test1');
	target.addNode('package.test2');
	target.addDependency({from: 'package.test1', to: 'package.test2'});
	expect(target).toEqual({
		dependencies: [],
		nodes: [
			{
				name: 'package',
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
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					}, {
						name: 'test2',
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
	const target = new GraphImpl();
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
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test1',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			}, {
				name: 'package2',
				type: 'PACKAGE',
				dependencies: [],
				nodes: [
					{
						name: 'test2',
						type: 'CLASS',
						dependencies: [],
						nodes: [],
					},
				],
			},
		],
	});
});

import type {Filter, Graph} from './Graph';
import {GraphImpl} from './GraphImpl';

export function basePackage(basePackage: string): Filter {

	return graph => {
		const node = findNode(basePackage, graph);
		return new GraphImpl([{
			...node,
			name: basePackage,
			fullName: basePackage,
			type: 'PACKAGE',
		}],[]);
	};
}

function findNode(name: string, input: Graph): Graph {
	const parts = name.split('.');
	parts.forEach(p => {
		const existingNode = input.nodes.find(n => n.name === p);
		if (existingNode) {
			input = existingNode;
		} else {
			throw new Error('Node ' + name + ' not found');
		}
	});
	return input;
}

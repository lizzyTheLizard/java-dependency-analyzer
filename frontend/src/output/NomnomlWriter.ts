import type {Graph, GraphNode} from '../transform/Graph';

export function nomnomlWriter(input: Graph): string {
	return getChildren(input);
}

function nomnomlWriterNode(input: GraphNode): string {
	switch (input.type) {
	case 'PACKAGE':
		return `[<package> ${input.name} | ${getChildren(input)}]`;
	default:
		return `[${input.name}]`;
	}
}

function getChildren(input: GraphNode | Graph): string {
	return input.nodes
		.map(n => nomnomlWriterNode(n))
		.join('\n') + getDependencies(input);
}

function getDependencies(input: GraphNode | Graph): string {
	if (input.dependencies.length === 0) {
		return '';
	}

	return '\n' + input.dependencies
		.map(d => `[${d.from}] --> [${d.to}]`)
		.join('\n');
}

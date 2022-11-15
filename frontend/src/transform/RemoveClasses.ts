import type {Filter, Graph, GraphNode} from './Graph';
import {GraphNodeImpl} from './GraphImpl';

export type RemoveClassesSelection = 'HIDE_INNER' | 'HIDE_ALL' | 'SHOW_ALL';

export function removeClasses(selection: RemoveClassesSelection): Filter {
	switch (selection) {
	case 'HIDE_INNER':
		return graph => removeInternalClassesRecusive(graph);
	case 'HIDE_ALL':
		return graph => removeClassesRecusive(graph);
	default:
		return graph => graph;
	}
}

function removeClassesRecusive(graph: Graph): GraphNode {
	const classes = graph.nodes.filter(n => n.type === 'CLASS').map(n => n.name);
	return new GraphNodeImpl(
		graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeClassesRecusive(n)),
		graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
		(graph as GraphNode)?.name ?? '', (graph as GraphNode)?.type ?? 'PACKAGE'
	);
}

function removeInternalClassesRecusive(graph: Graph): GraphNode {
	const classes = graph.nodes.filter(n => n.type === 'CLASS' && n.name.indexOf('$') > 0).map(n => n.name);
	return new GraphNodeImpl(
		graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeInternalClassesRecusive(n)),
		graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
		(graph as GraphNode)?.name ?? '', (graph as GraphNode)?.type ?? 'PACKAGE'
	);
}

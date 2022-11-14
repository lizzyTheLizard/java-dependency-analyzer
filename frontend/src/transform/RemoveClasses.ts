import type {Filter, Graph} from './Graph';

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

function removeClassesRecusive<T extends Graph>(graph: T): T {
	const classes = graph.nodes.filter(n => n.type === 'CLASS').map(n => n.name);
	return {
		...graph,
		nodes: graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeClassesRecusive(n)),
		dependencies: graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
	};
}

function removeInternalClassesRecusive<T extends Graph>(graph: T): T {
	const classes = graph.nodes.filter(n => n.type === 'CLASS' && n.name.indexOf('$') > 0).map(n => n.name);
	return {
		...graph,
		nodes: graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeInternalClassesRecusive(n)),
		dependencies: graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
	};
}

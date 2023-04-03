import type {Filter, Graph, GraphNode} from './Graph';
import {GraphNodeImpl} from './GraphImpl';

export type RemoveClassesSelection = 'HIDE_INNER' | 'HIDE_ALL' | 'SHOW_ALL';

export function removeClasses(selection: RemoveClassesSelection): Filter {
    switch (selection) {
    case 'HIDE_INNER':
        return graph => removeInternalClassesRecursive(graph);
    case 'HIDE_ALL':
        return graph => removeClassesRecursive(graph);
    default:
        return graph => graph;
    }
}

function removeClassesRecursive(graph: Graph): GraphNode {
    const classes = graph.nodes.filter(n => n.type === 'CLASS').map(n => n.name);
    return new GraphNodeImpl(
        graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeClassesRecursive(n)),
        graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
        graph.attributes,
        (graph as GraphNode)?.name ?? '',
        (graph as GraphNode)?.fullName ?? '',
        (graph as GraphNode)?.type ?? 'PACKAGE'
    );
}

function removeInternalClassesRecursive(graph: Graph): GraphNode {
    const classes = graph.nodes.filter(n => n.type === 'CLASS' && n.name.indexOf('$') > 0).map(n => n.name);
    return new GraphNodeImpl(
        graph.nodes.filter(n => !classes.includes(n.name)).map(n => removeInternalClassesRecursive(n)),
        graph.dependencies.filter(d => !classes.includes(d.from) && !classes.includes(d.to)),
        graph.attributes,
        (graph as GraphNode)?.name ?? '',
        (graph as GraphNode)?.fullName ?? '',
        (graph as GraphNode)?.type ?? 'PACKAGE'
    );
}

import type {Filter, Graph, GraphNode} from './Graph';
import {GraphNodeImpl} from './GraphImpl';


export function removePackages(packages: string[]): Filter[] {
    return packages.map(p => (graph => removePackagesRecursive(graph, p)));
}

function removePackagesRecursive(graph: Graph, remainingName: string): GraphNode {
    return new GraphNodeImpl(
        graph.nodes.filter(n => n.name != remainingName).map(n => remainingName.startsWith(n.name) ? removePackagesRecursive(n, remainingName.substring(n.name.length + 1)) : n),
        graph.dependencies.filter(d => d.from !== remainingName && d.to !== remainingName),
        (graph as GraphNode)?.name ?? '',
        (graph as GraphNode)?.fullName ?? '',
        (graph as GraphNode)?.type ?? 'PACKAGE'
    );
}
